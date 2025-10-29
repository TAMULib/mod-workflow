package org.folio.rest.workflow.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import jakarta.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.FileSystemException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import org.folio.rest.workflow.dto.TriggerDto;
import org.folio.rest.workflow.enums.HttpMethod;
import org.folio.rest.workflow.exception.EventPublishException;
import org.folio.rest.workflow.kafka.EventProducer;
import org.folio.rest.workflow.model.repo.TriggerRepo;
import org.folio.spring.messaging.model.Event;
import org.folio.spring.tenant.annotation.TenantHeader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.util.PathMatcher;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.HandlerMapping;

@RestController
@RequestMapping("/events")
public class EventController {

  private static final Logger logger = LoggerFactory.getLogger(EventController.class);
  private static final Pattern TENANT_PATTERN = Pattern.compile("^[a-z0-9_-]+$");

  @Value("${tenant.headerName:X-Okapi-Tenant}")
  private String tenantHeaderName;

  @Value("${event.uploads.path}")
  private String eventUploadsDirectory;

  private EventProducer eventProducer;

  private TriggerRepo triggerRepo;

  private PathMatcher pathMatcher;

  private ObjectMapper objectMapper;

  public EventController(EventProducer eventProducer, TriggerRepo triggerRepo, PathMatcher pathMatcher, ObjectMapper objectMapper) {
    this.eventProducer = eventProducer;
    this.triggerRepo = triggerRepo;
    this.pathMatcher = pathMatcher;
    this.objectMapper = objectMapper;
  }

  @PostMapping(value = "/**", consumes = "application/json", produces = { MediaType.APPLICATION_JSON_VALUE })
  public JsonNode postHandleEvents(
    @RequestBody(required = false) JsonNode body,
    HttpServletRequest request
  ) throws EventPublishException {
    return processRequest(request, body);
  }

  @PostMapping(value = "/**", consumes = "multipart/form-data", produces = { MediaType.APPLICATION_JSON_VALUE })
  public JsonNode postHandleEventsWithFile(
    @RequestParam("file") MultipartFile multipartFile,
    @RequestParam("path") String directoryPath,
    @TenantHeader String tenant,
    HttpServletRequest request
  ) throws EventPublishException, IOException {

    if (!TENANT_PATTERN.matcher(tenant).matches()) {
      throw new FileSystemException("Invalid tenant directory name");
    }

    ObjectNode body = objectMapper.createObjectNode();

    Path tenantPath = Path.of(eventUploadsDirectory)
      .resolve(tenant)
      .normalize();

    Path filePath = tenantPath.resolve(directoryPath)
      .resolve(multipartFile.getOriginalFilename())
      .normalize();

    if (!filePath.startsWith(tenantPath)) {
      throw new FileSystemException("Path/directory traversal attack");
    }

    File file = filePath.toFile();

    file.mkdirs();

    body.put("inputFilePath", filePath.toString());

    Collections.list(request.getParameterNames())
      .stream()
      .filter(name -> !name.equals("file"))
      .filter(name -> !name.equals("path"))
      .forEach(name -> {
        body.put(name, request.getParameter(name));
      });

    try (InputStream is = multipartFile.getInputStream()) {
      Files.copy(is, filePath, StandardCopyOption.REPLACE_EXISTING);
    }

    return processRequest(request, body);
  }

  private JsonNode processRequest(
    HttpServletRequest request,
    JsonNode body
  ) throws EventPublishException {
    String tenant = request.getHeader(tenantHeaderName);

    String requestPath = (String) request.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE);
    HttpMethod method = HttpMethod.valueOf(request.getMethod());

    Map<String, String> headers = Collections.list(request.getHeaderNames())
      .stream()
      .collect(Collectors.toMap(h -> h, request::getHeader));

    logger.debug("Tenant: {}", tenant);
    logger.debug("Request path: {}", requestPath);
    logger.debug("Request method: {}", method);
    logger.debug("Request headers: {}", headers);
    logger.debug("Request body: {}", body);

    Optional<TriggerDto> trigger = checkTrigger(method, requestPath);
    if (trigger.isPresent()) {
      processEvent(
        trigger.get(),
        new Event(
          trigger.get().getId(),
          trigger.get().getPathPattern(),
          trigger.get().getMethod().toString(),
          tenant,
          requestPath,
          headers,
          body
        )
      );
    }

    return body;
  }

  private Optional<TriggerDto> checkTrigger(HttpMethod method, String path) {
    return triggerRepo.findViewAllBy(TriggerDto.class).stream().filter(t ->
      method.equals(t.getMethod()) && pathMatcher.match(t.getPathPattern(), path)
    ).findAny();
  }

  private void processEvent(TriggerDto trigger, Event event) throws EventPublishException {
    logger.debug("Publishing event: {}: {}", trigger.getName(), trigger.getDescription());

    try {
      eventProducer.send(event);
    } catch (Exception e) {
      throw new EventPublishException("Unable to publish event!", e);
    }
  }

}
