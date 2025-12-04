package org.folio.rest.workflow.events;

import org.folio.rest.workflow.exception.WorkflowCreateAlreadyExistsException;
import org.folio.rest.workflow.model.Workflow;
import org.folio.rest.workflow.model.repo.WorkflowRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.core.annotation.HandleBeforeCreate;
import org.springframework.data.rest.core.annotation.RepositoryEventHandler;

@RepositoryEventHandler
public class WorkflowEventHandler {

  @Autowired
  private WorkflowRepo workflowRepo;

  @HandleBeforeCreate
  public void handleWorkflowBeforeCreate(Workflow workflow) throws WorkflowCreateAlreadyExistsException {
    if (workflowRepo.existsByIdAndVersionTag(workflow.getId(), workflow.getVersionTag())) {
      throw new WorkflowCreateAlreadyExistsException(workflow.getId(), workflow.getVersionTag());
    }
  }

}
