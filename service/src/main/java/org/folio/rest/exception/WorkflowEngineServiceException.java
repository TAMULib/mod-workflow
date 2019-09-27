package org.folio.rest.exception;

public class WorkflowEngineServiceException extends Exception {

  private static final long serialVersionUID = -2815351244979297723L;

  public WorkflowEngineServiceException(String message) {
    super(message);
  }

  public WorkflowEngineServiceException(int code) {
    super(Integer.toString(code));
  }

}
