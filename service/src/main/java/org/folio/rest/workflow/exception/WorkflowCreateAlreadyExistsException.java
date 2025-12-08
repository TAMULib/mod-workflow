package org.folio.rest.workflow.exception;

public class WorkflowCreateAlreadyExistsException extends Exception {

  private static final long serialVersionUID = 11120352756118677L;

  private static final String MESSAGE = "The workflow cannot be created because a workflow with UUID '%s' and Version Tag '%s' already exists.";

  public WorkflowCreateAlreadyExistsException(String id, String versionTag) {
    super(String.format(MESSAGE, id, versionTag));
  }

  public WorkflowCreateAlreadyExistsException(String id, String versionTag, Exception e) {
    super(String.format(MESSAGE, id, versionTag), e);
  }

}
