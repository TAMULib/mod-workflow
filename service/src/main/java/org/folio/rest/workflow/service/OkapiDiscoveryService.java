package org.folio.rest.workflow.service;

import static org.springframework.http.HttpMethod.GET;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.folio.rest.workflow.model.Action;
import org.folio.rest.workflow.model.Handler;
import org.folio.spring.web.service.HttpService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class OkapiDiscoveryService {

  private static final Logger logger = LoggerFactory.getLogger(OkapiDiscoveryService.class);

  private static final String ID = "id";
  private static final String HANDLERS = "handlers";
  private static final String PROVIDES = "provides";

  private static final String PROXY_TENANT_BASE_PATH = "/_/proxy/tenants/";
  private static final String PROXY_MODULE_BASE_PATH = "/_/proxy/modules/";
  private static final String MODULES_SUB_PATH = "/modules";

  @Value("${tenant.headerName:X-Okapi-Tenant}")
  private String tenantHeaderName;

  @Value("${okapi.url}")
  private String okapiUrl;

  @Value("${okapi.camunda.base-path:/}")
  private String basePath;

  private HttpService httpService;

  private ObjectMapper objectMapper;

  public OkapiDiscoveryService(HttpService httpService, ObjectMapper objectMapper) {
    this.httpService = httpService;
    this.objectMapper = objectMapper;
  }

  public List<Action> getActionsByTenant(String tenant) throws IOException {
    List<Action> actions = new ArrayList<>();
    JsonNode modulesNode = getModules(tenant);
    for (JsonNode moduleNode : modulesNode) {
      String id = moduleNode.get("id").asText();
      actions.addAll(getActionsByTenantAndModuleId(tenant, id));
    }
    return actions;
  }

  public List<Action> getActionsByTenantAndModuleId(String tenant, String id) throws IOException {
    Map<String, List<Handler>> handlerMap = getHandlers(tenant, id);
    List<Action> actions = new ArrayList<>();
    for (Map.Entry<String, List<Handler>> entry : handlerMap.entrySet()) {
      String interfaceName = entry.getKey();
      List<Handler> handlers = entry.getValue();
      handlers.forEach(handler -> actions.addAll(handler.getActionByInterface(interfaceName)));
    }
    return actions;
  }

  public Map<String, List<Handler>> getHandlers(String tenant, String id) throws IOException {
    JsonNode moduleDescriptorNode = getModuleDescriptor(tenant, id);

    Map<String, List<Handler>> handlerMap = new HashMap<>();

    if (moduleDescriptorNode.get(PROVIDES) != null) {
      for (JsonNode interfaceNode : moduleDescriptorNode.get(PROVIDES)) {
        List<Handler> handlers = new ArrayList<>();
        String interfaceName = interfaceNode.get(ID).asText();
        for (JsonNode handlersNode : interfaceNode.get(HANDLERS)) {
          handlers.add(objectMapper.readValue(handlersNode.toString(), Handler.class));
        }
        handlerMap.put(interfaceName, handlers);
      }
    }

    return handlerMap;
  }

  public JsonNode getModules(String tenant) {
    String url = okapiUrl + basePath + PROXY_TENANT_BASE_PATH + tenant + MODULES_SUB_PATH;
    ResponseEntity<JsonNode> response = request(url, tenant);
    if (logger.isDebugEnabled()) {
      logger.debug("Response status code {}", response.getStatusCode());
    }
    return response.getBody();
  }

  public JsonNode getModuleDescriptor(String tenant, String id) {
    String url = okapiUrl + basePath + PROXY_MODULE_BASE_PATH + id;
    ResponseEntity<JsonNode> response = request(url, tenant);
    if (logger.isDebugEnabled()) {
      logger.debug("Response status code {}", response.getStatusCode());
    }
    return response.getBody();
  }

  private ResponseEntity<JsonNode> request(String url, String tenant) {
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    if (tenantHeaderName != null) {
      headers.add(tenantHeaderName, tenant);
    }

    HttpEntity<?> request = new HttpEntity<>(headers);
    if (logger.isDebugEnabled()) {
      logger.debug("Proxy request for {} to {}", tenant.replaceAll("[\n\r]", " "), url.replaceAll("[\n\r]", " "));
    }

    return this.httpService.exchange(url, GET, request, JsonNode.class);
  }

}
