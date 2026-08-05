package org.folio.rest.workflow.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.time.Instant;
import java.util.Iterator;
import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.folio.rest.workflow.dto.WorkflowDto;
import org.folio.rest.workflow.dto.WorkflowOperationalDto;
import org.folio.rest.workflow.dto.WorkflowOperationalNodeDto;
import org.folio.rest.workflow.exception.WorkflowDeploymentNotFound;
import org.folio.rest.workflow.exception.WorkflowEngineServiceException;
import org.folio.rest.workflow.exception.WorkflowNotFoundException;
import org.folio.rest.workflow.model.Workflow;
import org.folio.rest.workflow.model.repo.WorkflowRepo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Service
public class WorkflowEngineService {

  private static final Log LOG = LogFactory.getLog(WorkflowEngineService.class);

  private static final String WORKFLOW_ENGINE_ACTIVATE_URL_TEMPLATE = "%s%s/workflow-engine/workflows/activate";
  private static final String WORKFLOW_ENGINE_DEACTIVATE_URL_TEMPLATE = "%s%s/workflow-engine/workflows/deactivate";

  private static final String PROCESS_DEFINITION_START_URL_TEMPLATE = "%s%s/process-definition/{arg1}/start";
  private static final String PROCESS_DEFINITION_GET_URL_TEMPLATE = "%s%s/process-definition?deploymentId={arg1}&versionTag={arg2}&maxResults=1";

  private static final String HISTORY_PROCESS_INSTANCE_URL_TEMPLATE = "%s%s/history/process-instance?processDefinitionId={arg1}&sortBy=startTime&sortOrder=asc";
  private static final String HISTORY_INCIDENT_URL_TEMPLATE = "%s%s/history/incident?processInstanceId={arg1}&sortBy=createTime&sortOrder=asc";

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

  private DeleteService deleteService;

  private WorkflowRepo workflowRepo;

  private JsonMapper mapper;

  private RestTemplate restTemplate;

  public WorkflowEngineService(DeleteService deleteService, WorkflowRepo workflowRepo, JsonMapper mapper, RestTemplateBuilder restTemplateBuilder) {
    this.deleteService = deleteService;
    this.workflowRepo = workflowRepo;
    this.mapper = mapper;
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
   * @param workflowId ID of the Workflow to delete.
   * @param tenant The tenant to use.
   * @param token The token to use.
   *
   * @throws WorkflowEngineServiceException When the request fails in some way preventing the return of an HttpEntity.
   */
  @Transactional(rollbackFor = Exception.class)
  public void delete(String workflowId, String tenant, String token)
      throws WorkflowEngineServiceException {

    final WorkflowOperationalNodeDto workflow = workflowRepo.getViewById(workflowId, WorkflowOperationalNodeDto.class);

    // Deployment ID will not exist if it has not been activated.
    if (workflow.getDeploymentId() != null) {
      deleteDeployment(workflow, tenant, token);
    }

    deleteService.deleteNodes(workflow);

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
      throws WorkflowDeploymentNotFound, WorkflowEngineServiceException, WorkflowNotFoundException {

    WorkflowOperationalDto workflow = workflowRepo.getViewById(workflowId, WorkflowOperationalDto.class);

    if (workflow == null) {
      throw new WorkflowNotFoundException(String.format("Workflow ID '%s'", workflowId));
    }

    String id = workflow.getDeploymentId();
    String version = workflow.getVersionTag();

    JsonNode definition = fetchFirstDeploymentDefinition(workflowId, id, version, tenant, token);

    if (!definition.has("id") ) {
      throw new WorkflowEngineServiceException(String.format("Workflow ID '%s' with Deployment ID '%s' has no definition ID!", workflowId, id));
    }

    String definitionId = definition.get("id").asText();

    HttpEntity<JsonNode> contextHttpEntity = new HttpEntity<>(context, headers(tenant, token));

    String url = String.format(PROCESS_DEFINITION_START_URL_TEMPLATE, okapiUrl, restPath);
    Map<String, Object> params = Map.of("arg1", definitionId);

    try {
      final ResponseEntity<JsonNode> response = exchange(url, HttpMethod.POST, contextHttpEntity, JsonNode.class, params);

      if (response.getStatusCode() == HttpStatus.NOT_FOUND) {
        throw new WorkflowNotFoundException(String.format("Workflow ID '%s'", workflowId));
      }

      return response.getBody();
    } catch (RestClientException e) {
      throw new WorkflowEngineServiceException(String.format("Failed to start workflow: %s!", e.getMessage()), e);
    }
  }

  public JsonNode history(String workflowId, String tenant, String token)
      throws WorkflowDeploymentNotFound, WorkflowEngineServiceException {

    WorkflowOperationalDto workflow = workflowRepo.getViewById(workflowId, WorkflowOperationalDto.class);
    String id = workflow.getDeploymentId();
    String version = workflow.getVersionTag();

    JsonNode processDefinition = fetchFirstDeploymentDefinition(workflowId, id, version, tenant, token);
    String processDefinitionId = processDefinition.get("id").asText();

    ArrayNode instances = fetchProcessInstanceHistory(processDefinitionId, tenant, token);

    Iterator<JsonNode> iter = instances.iterator();

    while (iter.hasNext()) {
      JsonNode instance = iter.next();

      if (instance.has("id")) {
        String processInstanceId = instance.get("id").asText();

        ((ObjectNode) instance).withArray("incidents")
          .addAll(fetchIncidentsHistory(processInstanceId, tenant, token));
      }
    }

    return instances;
  }

  /**
   * Delete the deployed workflow.
   *
   * @param workflow The workflow to delete the deployment of.
   * @param tenant   The tenant to use.
   * @param token    The token to use.
   *
   * @throws WorkflowEngineServiceException When the request fails in some way preventing the return of an HttpEntity.
   */
  private void deleteDeployment(WorkflowOperationalNodeDto workflow, String tenant, String token) throws WorkflowEngineServiceException {

    final String deploymentId = workflow.getDeploymentId();
    final String id = workflow.getId();
    final String version = workflow.getVersionTag();

    final ResponseEntity<ArrayNode> response = fetchDeploymentDefinitions(deploymentId, version, tenant, token);

    if (response.getStatusCode() == HttpStatus.OK || response.getStatusCode() == HttpStatus.NOT_FOUND) {

      final ArrayNode definitions = response.hasBody()
        ? response.getBody()
        : null;

      if (definitions != null && response.getStatusCode() != HttpStatus.NOT_FOUND && !definitions.isEmpty()) {
        try {
          deactivate(id, tenant, token);
        } catch (WorkflowEngineServiceException e) {
          final String message = String.format(
            "Failed to delete Workflow ID '%s' for Deployment ID '%s' and Version Tag '%s' due to deactivation failure: %s!",
            id,
            deploymentId,
            version,
            e.getMessage()
          );

          throw new WorkflowEngineServiceException(message, e);
        }
      }
    }
  }

  /**
   * Fetch the first matching deployment for the given workflow.
   *
   * @param workflowId   The Workflow ID, for use in exception logs.
   * @param deploymentId The Deployment ID to find.
   * @param version      The version tag to use.
   * @param tenant       The FOLIO tenant.
   * @param token        The session token.
   *
   * @return The first matching response.
   *
   * @throws WorkflowDeploymentNotFound     On not found.
   * @throws WorkflowEngineServiceException On error.
   */
  private JsonNode fetchFirstDeploymentDefinition(String workflowId, String deploymentId, String version, String tenant, String token)
      throws WorkflowDeploymentNotFound, WorkflowEngineServiceException {

    final ResponseEntity<ArrayNode> response = fetchDeploymentDefinitions(deploymentId, version, tenant, token);

    if (response.getStatusCode() == HttpStatus.OK || response.getStatusCode() == HttpStatus.NOT_FOUND) {
      final ArrayNode definitions = response.hasBody()
        ? response.getBody()
        : null;

      if (definitions == null || response.getStatusCode() == HttpStatus.NOT_FOUND || definitions.isEmpty() || definitions.get(0) == null) {
        throw new WorkflowDeploymentNotFound(String.format("Workflow with Deployment ID '%s' for Workflow ID '%s' is not found.", deploymentId, workflowId));
      }

      return definitions.get(0);
    }

    throw new WorkflowEngineServiceException("Unable to get workflow process definition from workflow engine!");
  }

  /**
   * Fetch all deployments, but return the response entity to allow caller to handle.
   *
   * @param deploymentId The activated Workflow deployment ID.
   * @param version      The Workflow version number.
   * @param tenant       The tenant.
   * @param token        The token.
   *
   * @return The response entity.
   *
   * @throws WorkflowEngineServiceException On error.
   */
  private ResponseEntity<ArrayNode> fetchDeploymentDefinitions(String deploymentId, String version, String tenant, String token)
      throws WorkflowEngineServiceException {

    if (deploymentId == null) {
      throw new WorkflowEngineServiceException("Failed to deployment definition: Deployment ID is missing!");
    }

    final HttpEntity<Void> httpEntity = new HttpEntity<>(headers(tenant, token));
    final String url = String.format(PROCESS_DEFINITION_GET_URL_TEMPLATE, okapiUrl, restPath);
    final Map<String, Object> params = Map.of("arg1", deploymentId, "arg2", version);

    try {
      return exchange(url, HttpMethod.GET, httpEntity, ArrayNode.class, params);
    } catch (RestClientException e) {
      throw new WorkflowEngineServiceException(String.format("Failed to deployment definition: %s!", e.getMessage()), e);
    }
  }

  private ArrayNode fetchProcessInstanceHistory(String processDefinitionId, String tenant, String token)
      throws WorkflowEngineServiceException {

    HttpEntity<Void> httpEntity = new HttpEntity<>(headers(tenant, token));

    String url = String.format(HISTORY_PROCESS_INSTANCE_URL_TEMPLATE, okapiUrl, restPath);
    Map<String, Object> params = Map.of("arg1", processDefinitionId);

    try {
      ResponseEntity<ArrayNode> response = exchange(url, HttpMethod.GET, httpEntity, ArrayNode.class, params);

      final ArrayNode definitions = response.hasBody()
        ? response.getBody()
        : null;

      if (definitions != null && response.getStatusCode() == HttpStatus.OK) {
        return definitions;
      }

      throw new WorkflowEngineServiceException("Unable to get workflow process instance history from workflow engine!");
    } catch (RestClientException e) {
      throw new WorkflowEngineServiceException(String.format("Failed to fetch process instance history: %s!", e.getMessage()), e);
    }
  }

  private ArrayNode fetchIncidentsHistory(String processInstanceId, String tenant, String token) throws WorkflowEngineServiceException {

    HttpEntity<Void> httpEntity = new HttpEntity<>(headers(tenant, token));

    String url = String.format(HISTORY_INCIDENT_URL_TEMPLATE, okapiUrl, restPath);
    Map<String, Object> params = Map.of("arg1", processInstanceId);

    try {
      ResponseEntity<ArrayNode> response = exchange(url, HttpMethod.GET, httpEntity, ArrayNode.class, params);

      ArrayNode incidents = response.hasBody()
        ? response.getBody()
        : null;

      if (incidents == null || response.getStatusCode() != HttpStatus.OK) {
        LOG.debug("Unable to get workflow incidents history from workflow engine!");

        incidents = mapper.createArrayNode();
      }

      return incidents;
    } catch (RestClientException e) {
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

    LOG.debug(String.format("Send Okapi workflow engine request %s %s", HttpMethod.POST, url));

    try {
      ResponseEntity<Workflow> response = exchange(url, HttpMethod.POST, entity, Workflow.class, Map.of());

      if (response.getStatusCode() == HttpStatus.OK) {
        Workflow responseWorkflow = response.getBody();

        if (responseWorkflow != null) {
          String deploymentId = responseWorkflow.getDeploymentId();
          Boolean active = responseWorkflow.getActive();

          responseWorkflow.setChecksum(workflow.getChecksum());
          responseWorkflow.setCreatedOn(workflow.getCreatedOn());
          responseWorkflow.setUpdatedOn(Instant.now());

          LOG.info(String.format("Workflow is active = %s, deploymentID = %s", Boolean.TRUE.equals(active), deploymentId));
          return workflowRepo.save(responseWorkflow);
        }
      }
    } catch (RestClientException e) {
      throw new WorkflowEngineServiceException(String.format("Failed to send workflow request: %s!", e.getMessage()), e);
    }

    throw new WorkflowEngineServiceException("Unable to get updated workflow from workflow engine!");
  }

  private HttpHeaders headers(String tenant, String token) {
    HttpHeaders requestHeaders = new HttpHeaders();
    LOG.debug(String.format("Request Headers: tenant '%s' and token '%s'.", tenant, token));

    if (tenant != null) {
      requestHeaders.add(tenantHeaderName, tenant);
    }

    if (token != null) {
      requestHeaders.add(tokenHeaderName, token);
    }

    requestHeaders.add("Content-Type", "application/json");
    return requestHeaders;
  }

  private <T> ResponseEntity<T> exchange(String url, HttpMethod method, HttpEntity<?> request, Class<T> responseType, Map<String, ? extends Object> params) {
    LOG.debug(String.format("Exchange for %s %s %s %s", responseType.getSimpleName(), method, url, params));
    return this.restTemplate.exchange(url, method, request, responseType, params);
  }

}
