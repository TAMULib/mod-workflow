package org.folio.rest.workflow.exception;

/**
 * For providing a 404 when any Workflow deployment is not activated in the Workflow Engine.
 */
public class WorkflowDeploymentNotActivated extends Exception {

  private static final long serialVersionUID = 529029857472170077L;

  public WorkflowDeploymentNotActivated(String message) {
    super(message);
  }

  public WorkflowDeploymentNotActivated(String message, Exception e) {
    super(message, e);
  }

  public WorkflowDeploymentNotActivated(int code) {
    super(Integer.toString(code));
  }

  public WorkflowDeploymentNotActivated(int code, Exception e) {
    super(Integer.toString(code), e);
  }

}
