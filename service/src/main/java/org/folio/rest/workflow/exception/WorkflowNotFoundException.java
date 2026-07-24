package org.folio.rest.workflow.exception;

public class WorkflowNotFoundException extends Exception {

  private static final long serialVersionUID = -7896387728725860219L;

  private final static String WORKFLOW_NOT_FOUND_MESSAGE = "The workflow: %s cannot be found.";

  public WorkflowNotFoundException(String id) {
    super(String.format(WORKFLOW_NOT_FOUND_MESSAGE, id));
  }

  public WorkflowNotFoundException(String id, Exception e) {
    super(String.format(WORKFLOW_NOT_FOUND_MESSAGE, id), e);
  }

}
