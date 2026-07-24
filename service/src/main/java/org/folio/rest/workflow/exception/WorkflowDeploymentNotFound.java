package org.folio.rest.workflow.exception;

/**
 * For providing a 404 when any Workflow deployment is not found in the Workflow Engine.
 */
public class WorkflowDeploymentNotFound extends Exception {

  private static final long serialVersionUID = 424162623670077L;

  public WorkflowDeploymentNotFound(String message) {
    super(message);
  }

  public WorkflowDeploymentNotFound(String message, Exception e) {
    super(message, e);
  }

  public WorkflowDeploymentNotFound(int code) {
    super(Integer.toString(code));
  }

  public WorkflowDeploymentNotFound(int code, Exception e) {
    super(Integer.toString(code), e);
  }

}
