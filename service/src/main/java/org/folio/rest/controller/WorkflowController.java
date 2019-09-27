package org.folio.rest.controller;

import org.folio.rest.annotation.TokenHeader;
import org.folio.rest.exception.WorkflowEngineServiceException;
import org.folio.rest.model.Workflow;
import org.folio.rest.service.WorkflowEngineService;
import org.folio.rest.tenant.annotation.TenantHeader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/workflows")
public class WorkflowController {

  private static final Logger logger = LoggerFactory.getLogger(WorkflowEngineService.class);

  @Autowired
  private WorkflowEngineService workflowEngineService;

  @PutMapping("/{id}/activate")
  public Workflow activateWorkflow(
    @PathVariable String id,
      @TenantHeader String tenant,
    @TokenHeader String token
  ) throws WorkflowEngineServiceException {
    logger.info("Activating: " + id);
    return workflowEngineService.activate(id, tenant, token);
  }

  @PutMapping("/{id}/deactivate")
  public Workflow deactivateWorkflow(
    @PathVariable String id,
    @TenantHeader String tenant,
    @TokenHeader String token
  ) throws WorkflowEngineServiceException {
    logger.info("Deactivating: " + id);
    return workflowEngineService.deactivate(id, tenant, token);
  }

}
