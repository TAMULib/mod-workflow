package org.folio.rest.workflow.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.util.Iterator;
import lombok.extern.slf4j.Slf4j;
import org.folio.rest.workflow.dto.WorkflowDto;
import org.folio.rest.workflow.dto.WorkflowOperationalDto;
import org.folio.rest.workflow.exception.WorkflowEngineServiceException;
import org.folio.rest.workflow.exception.WorkflowNotFoundException;
import org.folio.rest.workflow.model.Workflow;
import org.folio.rest.workflow.model.repo.WorkflowRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service
public class WorkflowEngineService {

  private static final String WORKFLOW_ENGINE_ACTIVATE_URL_TEMPLATE = "%s%s/workflow-engine/workflows/activate";
  private static final String WORKFLOW_ENGINE_DEACTIVATE_URL_TEMPLATE = "%s%s/workflow-engine/workflows/deactivate";

  private static final String PROCESS_DEFINITION_START_URL_TEMPLATE = "%s%s/process-definition/%s/start";
  private static final String PROCESS_DEFINITION_GET_URL_TEMPLATE = "%s%s/process-definition%s";

  private static final String HISTORY_PROCESS_INSTANCE_URL_TEMPLATE = "%s%s/history/process-instance%s";
  private static final String HISTORY_INCIDENT_URL_TEMPLATE = "%s%s/history/incident%s";

  @Value("${tenant.headerName:X-Okapi-Tenant}")
  private String tenantHeaderName;

  @Value("${okapi.auth.tokenHeaderName:X-Okapi-Token}")
  private String tokenHeaderName;

  @Value("${okapi.url}")
  private String okapiUrl;

  @Value("${okapi.camunda.base-path:/}")
  private String basePath;

  @Value("${okapi.camunda.rest-path:/camunda}")
  private String restPath;

  @Autowired
  private WorkflowRepo workflowRepo;

  @Autowired
  private ObjectMapper mapper;

  private RestTemplate restTemplate;

  public WorkflowEngineService(RestTemplateBuilder restTemplateBuilder) {
    this.restTemplate = restTemplateBuilder.build();
  }

  public Workflow activate(String workflowId, String tenant, String token)
      throws WorkflowEngineServiceException {

    WorkflowDto workflow = workflowRepo.getViewById(workflowId, WorkflowDto.class);
    return sendWorkflowRequest(workflow, WORKFLOW_ENGINE_ACTIVATE_URL_TEMPLATE, tenant, token);
  }

  public Workflow deactivate(String workflowId, String tenant, String token)
      throws WorkflowEngineServiceException {

    WorkflowDto workflow = workflowRepo.getViewById(workflowId, WorkflowDto.class);
    return sendWorkflowRequest(workflow, WORKFLOW_ENGINE_DEACTIVATE_URL_TEMPLATE, tenant, token);
  }

  /**
   * Delete the Workflow from the given Workflow ID.
   *
   * To be removed. This is just an experiment.
   *
   * @param workflowId ID of the Workflow to delete.
   * @param tenant The tenant to use.
   * @param token The token to use.
   *
   * @throws WorkflowEngineServiceException When the request fails in some way preventing the return of an HttpEntity.
   */
  public void delete(String workflowId, String tenant, String token)
      throws WorkflowEngineServiceException {

    try {
      deactivate(workflowId, tenant, token);
    } catch (WorkflowEngineServiceException e) {
      throw new WorkflowEngineServiceException(String.format("Failed to delete Workflow '%s' due to deactivation failure: %s!", workflowId, e.getMessage()), e);
    }

    workflowRepo.deleteById(workflowId);
  }

  /**
   * Check that the Workflow exists or throw an exception.
   *
   * @param workflowId The Workflow to check.
   *
   * @throws WorkflowNotFoundException The exception when the Workflow is not found.
   */
  public void exists(String workflowId) throws WorkflowNotFoundException {
    if (!workflowRepo.existsById(workflowId)) {
      throw new WorkflowNotFoundException(workflowId);
    }
  }

  public JsonNode start(String workflowId, String tenant, String token, JsonNode context)
      throws WorkflowEngineServiceException {

    WorkflowOperationalDto workflow = workflowRepo.getViewById(workflowId, WorkflowOperationalDto.class);
    String id = workflow.getDeploymentId();
    String version = workflow.getVersionTag();

    JsonNode definition = fetchDeploymentDefinition(id, version, tenant, token);
    String definitionId = definition.get("id").asText();

    HttpEntity<JsonNode> contextHttpEntity = new HttpEntity<>(context, headers(tenant, token));

    String url = String.format(PROCESS_DEFINITION_START_URL_TEMPLATE, okapiUrl, restPath, definitionId);
    try {
      ResponseEntity<JsonNode> response = exchange(url, HttpMethod.POST, contextHttpEntity, JsonNode.class);

      return response.getBody();
    } catch (Exception e) {
      throw new WorkflowEngineServiceException(String.format("Failed to start workflow: %s!", e.getMessage()), e);
    }
  }

  public JsonNode history(String workflowId, String tenant, String token) throws WorkflowEngineServiceException {
    WorkflowOperationalDto workflow = workflowRepo.getViewById(workflowId, WorkflowOperationalDto.class);
    String deploymentId = workflow.getDeploymentId();
    String version = workflow.getVersionTag();

    JsonNode processDefinition = fetchDeploymentDefinition(deploymentId, version, tenant, token);
    String processDefinitionId = processDefinition.get("id").asText();

    ArrayNode instances = fetchProcessInstanceHistory(processDefinitionId, tenant, token);

    Iterator<JsonNode> iter = instances.iterator();

    while (iter.hasNext()) {
      JsonNode instance = iter.next();
      String processInstanceId = instance.get("id").asText();

      ((ObjectNode) instance).withArray("incidents")
        .addAll(fetchIncidentsHistory(processInstanceId, tenant, token));
    }

    return instances;
  }

  private JsonNode fetchDeploymentDefinition(String deploymentId, String version, String tenant, String token)
      throws WorkflowEngineServiceException {

    HttpEntity<Void> httpEntity = new HttpEntity<>(headers(tenant, token));

    String arguments = String.format("?deploymentId=%s&versionTag=%s&maxResults=1", deploymentId, version);
    String url = String.format(PROCESS_DEFINITION_GET_URL_TEMPLATE, okapiUrl, restPath, arguments);

    try {
      ResponseEntity<ArrayNode> response = exchange(url, HttpMethod.GET, httpEntity, ArrayNode.class);

      ArrayNode definitions = response.getBody();
      if (response.getStatusCode() == HttpStatus.OK && definitions != null && !definitions.isEmpty()) {
        return definitions.get(0);
      }

      throw new WorkflowEngineServiceException("Unable to get workflow process definition from workflow engine!");
    } catch (Exception e) {
      throw new WorkflowEngineServiceException(String.format("Failed to deployment definition: %s!", e.getMessage()), e);
    }
  }

  private ArrayNode fetchProcessInstanceHistory(String processDefinitionId, String tenant, String token)
      throws WorkflowEngineServiceException {

    HttpEntity<Void> httpEntity = new HttpEntity<>(headers(tenant, token));

    String arguments = String.format("?processDefinitionId=%s&sortBy=startTime&sortOrder=asc", processDefinitionId);
    String url = String.format(HISTORY_PROCESS_INSTANCE_URL_TEMPLATE, okapiUrl, restPath, arguments);

    try {
      ResponseEntity<ArrayNode> response = exchange(url, HttpMethod.GET, httpEntity, ArrayNode.class);

      ArrayNode definitions = response.getBody();
      if (response.getStatusCode() == HttpStatus.OK && definitions != null) {
        return definitions;
      }

      throw new WorkflowEngineServiceException("Unable to get workflow process instance history from workflow engine!");
    } catch (Exception e) {
      throw new WorkflowEngineServiceException(String.format("Failed to fetch process instance history: %s!", e.getMessage()), e);
    }
  }

  private ArrayNode fetchIncidentsHistory(String processInstanceId, String tenant, String token) throws WorkflowEngineServiceException {

    HttpEntity<Void> httpEntity = new HttpEntity<>(headers(tenant, token));

    String arguments = String.format("?processInstanceId=%s&sortBy=createTime&sortOrder=asc", processInstanceId);
    String url = String.format(HISTORY_INCIDENT_URL_TEMPLATE, okapiUrl, restPath, arguments);

    try {
      ResponseEntity<ArrayNode> response = exchange(url, HttpMethod.GET, httpEntity, ArrayNode.class);

      ArrayNode incidents = response.getBody();
      if (response.getStatusCode() != HttpStatus.OK || incidents == null) {
        log.debug("Unable to get workflow incidents history from workflow engine!");

        incidents = mapper.createArrayNode();
      }

      return incidents;
    } catch (Exception e) {
      throw new WorkflowEngineServiceException(String.format("Failed to fetch incident history: %s!", e.getMessage()), e);
    }
  }

  /**
   * Send a HTTP request to perform an action to the Workflow Engine end point and update the workflow locally.
   *
   * @param workflow The workflow associated with the action.
   * @param requestPath The end point being used.
   * @param tenant The tenant to use.
   * @param token The token to use.
   *
   * @return The updated Workflow.
   *
   * @throws WorkflowEngineServiceException On certain request failures or when failed to update the Workflow.
   */
  private Workflow sendWorkflowRequest(WorkflowDto workflow, String requestPath, String tenant, String token)
      throws WorkflowEngineServiceException {

    HttpEntity<WorkflowDto> entity = new HttpEntity<>(workflow, headers(tenant, token));
    String url = String.format(requestPath, okapiUrl, basePath);
    log.debug("Send Okapi workflow engine request {} {}", HttpMethod.POST, url);

    try {
      ResponseEntity<Workflow> response = exchange(url, HttpMethod.POST, entity, Workflow.class);

      if (response.getStatusCode() == HttpStatus.OK) {
        Workflow responseWorkflow = response.getBody();

        if (responseWorkflow != null) {
          String deploymentId = responseWorkflow.getDeploymentId();
          log.info("Workflow is active = {}, deploymentID = {}", Boolean.TRUE.equals(responseWorkflow.getActive()), deploymentId);
          return workflowRepo.save(responseWorkflow);
        }
      }
    } catch (Exception e) {
      throw new WorkflowEngineServiceException(String.format("Failed to send workflow request: %s!", e.getMessage()), e);
    }

    throw new WorkflowEngineServiceException("Unable to get updated workflow from workflow engine!");
  }

  private HttpHeaders headers(String tenant, String token) {
    HttpHeaders requestHeaders = new HttpHeaders();
    log.debug("Request Headers: tenant '{}' and token '{}'.", tenant, token);

    if (tenant != null) {
      requestHeaders.add(tenantHeaderName, tenant);
    }

    if (token != null) {
      requestHeaders.add(tokenHeaderName, token);
    }

    requestHeaders.add("Content-Type", "application/json");
    return requestHeaders;
  }

  private <T> ResponseEntity<T> exchange(String url, HttpMethod method, HttpEntity<?> request, Class<T> responseType) {
    log.debug("Exchange for {} {} {}", responseType.getSimpleName(), method, url);
    return this.restTemplate.exchange(url, method, request, responseType, (Object[]) new String[0]);
  }

}
