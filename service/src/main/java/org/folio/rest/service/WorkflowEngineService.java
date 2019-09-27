package org.folio.rest.service;

import org.folio.rest.exception.WorkflowEngineServiceException;
import org.folio.rest.model.Workflow;
import org.folio.rest.model.repo.WorkflowRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class WorkflowEngineService {

  private static final Logger logger = LoggerFactory.getLogger(WorkflowEngineService.class);

  private static final String WORKFLOW_ENGINE_ACTIVATE_URL_TEMPLATE = "%s/workflow-engine/workflows/activate";
  private static final String WORKFLOW_ENGINE_DEACTIVATE_URL_TEMPLATE = "%s/workflow-engine/workflows/deactivate";

  @Value("${tenant.headerName:X-Okapi-Tenant}")
  private String tenantHeaderName;

  @Value("${okapi.auth.tokenHeaderName:X-Okapi-Token}")
  private String tokenHeaderName;

  @Value("${okapi.location}")
  private String okapiLocation;

  @Autowired
  private HttpService httpService;

  @Autowired
  private WorkflowRepo workflowRepo;

  public Workflow activate(String workflowId, String tenant, String token) throws WorkflowEngineServiceException {
    Workflow workflow = workflowRepo.getOne(workflowId);
    return sendRequest(workflow, WORKFLOW_ENGINE_ACTIVATE_URL_TEMPLATE, tenant, token);
  }

  public Workflow deactivate(String workflowId, String tenant, String token) throws WorkflowEngineServiceException {
    Workflow workflow = workflowRepo.getOne(workflowId);
    return sendRequest(workflow, WORKFLOW_ENGINE_DEACTIVATE_URL_TEMPLATE, tenant, token);
  }

  private Workflow sendRequest(Workflow workflow, String requestPath, String tenant, String token)
      throws WorkflowEngineServiceException {

    HttpHeaders requestHeaders = new HttpHeaders();
    requestHeaders.add(tenantHeaderName, tenant);
    requestHeaders.add(tokenHeaderName, token);

    HttpEntity<Workflow> workflowHttpEntity = new HttpEntity<>(workflow, requestHeaders);

    ResponseEntity<Workflow> response = this.httpService.exchange(
      String.format(requestPath, okapiLocation),
      HttpMethod.POST,
      workflowHttpEntity,
      Workflow.class
    );

    if (response.getStatusCode() == HttpStatus.OK) {
      logger.debug("Response body: {}", response.getBody());

      try {
        Workflow responseWorkflow = response.getBody();
        logger.info("Workflow is actvie = {}, deploymentID = {}", responseWorkflow.isActive(), responseWorkflow.getDeploymentId());
        return workflowRepo.save(responseWorkflow);
      } catch (Exception e) {
        throw new WorkflowEngineServiceException("Unable to get updated workflow from workflow engine!");
      }
    }
    throw new WorkflowEngineServiceException(response.getStatusCodeValue());
  }

}