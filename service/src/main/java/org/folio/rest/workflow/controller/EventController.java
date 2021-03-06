package org.folio.rest.workflow.controller;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.jms.JMSException;
import javax.servlet.http.HttpServletRequest;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import org.apache.commons.lang3.StringUtils;
import org.folio.rest.workflow.exception.EventPublishException;
import org.folio.rest.workflow.jms.EventProducer;
import org.folio.rest.workflow.jms.model.Event;
import org.folio.rest.workflow.model.Trigger;
import org.folio.rest.workflow.model.repo.TriggerRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
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

  @Autowired
  private EventProducer eventProducer;

  @Autowired
  private TriggerRepo triggerRepo;

  @Autowired
  private PathMatcher pathMatcher;

  @Autowired
  private ObjectMapper objectMapper;

  // @formatter:off
  @PostMapping(value = "/**", consumes = "application/json")
  public JsonNode postHandleEvents(
    @RequestBody(required = false) JsonNode body,
    HttpServletRequest request
  ) throws EventPublishException {
  // @formatter:on
    return processRequest(request, body);
  }

  // @formatter:off
  @PostMapping(value = "/**", consumes = "multipart/form-data")
  public JsonNode postHandleEventsWithFile(
    @RequestParam("file") MultipartFile multipartFile,
    @RequestParam("path") String directoryPath,
    HttpServletRequest request
  ) throws EventPublishException, IOException {
  // @formatter:on

    ObjectNode body = objectMapper.createObjectNode();
    String filePath = StringUtils.appendIfMissing(directoryPath, File.separator) + multipartFile.getOriginalFilename();
    body.put("inputFilePath", filePath);

    Collections.list(request.getParameterNames())
      .stream()
      .filter(name -> !name.equals("file"))
      .filter(name -> !name.equals("path"))
      .forEach(name -> {
        body.put(name, request.getParameter(name));
      });

    File file = new File(filePath);

    file.mkdirs();

    try (InputStream is = multipartFile.getInputStream()) {
      Files.copy(is, file.toPath(), StandardCopyOption.REPLACE_EXISTING);
    }

    return processRequest(request, body);
  }

  private JsonNode processRequest(
    HttpServletRequest request,
    JsonNode body
  ) throws EventPublishException {
    String tenant = request.getHeader("X-Okapi-Tenant");

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

    Optional<Trigger> trigger = checkTrigger(method, requestPath);
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

  private Optional<Trigger> checkTrigger(HttpMethod method, String path) {
    return triggerRepo.findAll().stream().filter(t -> {
      return method.equals(t.getMethod()) && pathMatcher.match(t.getPathPattern(), path);
    }).findAny();
  }

  private void processEvent(Trigger trigger, Event event) throws EventPublishException {
    logger.debug("Publishing event: {}: {}", trigger.getName(), trigger.getDescription());
    try {
      eventProducer.send(event);
    } catch (JMSException | IOException e) {
      throw new EventPublishException("Unable to publish event!", e);
    }
  }

}
