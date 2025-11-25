package org.folio.rest.workflow.controller.advice;

import jakarta.persistence.EntityNotFoundException;
import org.folio.rest.workflow.exception.WorkflowAlreadyActiveException;
import org.folio.rest.workflow.exception.WorkflowCreateAlreadyExistsException;
import org.folio.rest.workflow.exception.WorkflowDeploymentException;
import org.folio.rest.workflow.exception.WorkflowEngineServiceException;
import org.folio.rest.workflow.exception.WorkflowImportException;
import org.folio.rest.workflow.exception.WorkflowNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class WorkflowControllerAdvice extends AbstractAdvice {

  @ResponseStatus(HttpStatus.NOT_FOUND)
  @ExceptionHandler(EntityNotFoundException.class)
  public ResponseEntity<String> handleEntityNotFoundException(EntityNotFoundException exception) {
    return buildError(exception, HttpStatus.NOT_FOUND);
  }

  @ResponseStatus(HttpStatus.CONFLICT)
  @ExceptionHandler({ WorkflowCreateAlreadyExistsException.class })
  public ResponseEntity<String> handleWorkflowCreateAlreadyExistsException(WorkflowCreateAlreadyExistsException ex) {
    return buildError(ex, HttpStatus.CONFLICT);
  }

  @ResponseStatus(HttpStatus.NOT_FOUND)
  @ExceptionHandler(WorkflowNotFoundException.class)
  public ResponseEntity<String> handleWorkflowNotFoundException(WorkflowNotFoundException exception) {
    return buildError(exception, HttpStatus.NOT_FOUND);
  }

  @ResponseStatus(HttpStatus.FORBIDDEN)
  @ExceptionHandler(WorkflowAlreadyActiveException.class)
  public ResponseEntity<String> handleWorkflowAlreadyActivrException(WorkflowAlreadyActiveException exception) {
    return buildError(exception, HttpStatus.FORBIDDEN);
  }

  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  @ExceptionHandler(WorkflowDeploymentException.class)
  public ResponseEntity<String> handleWorkflowDeploymentException(WorkflowDeploymentException exception) {
    return buildError(exception, HttpStatus.INTERNAL_SERVER_ERROR);
  }

  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  @ExceptionHandler(WorkflowEngineServiceException.class)
  public ResponseEntity<String> handleWorkflowEngineServiceException(WorkflowEngineServiceException exception) {
    return buildError(exception, HttpStatus.INTERNAL_SERVER_ERROR);
  }

  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler(WorkflowImportException.class)
  public ResponseEntity<String> handleWorkflowImportExceptionException(WorkflowImportException exception) {
    return buildError(exception, HttpStatus.BAD_REQUEST);
  }

}
