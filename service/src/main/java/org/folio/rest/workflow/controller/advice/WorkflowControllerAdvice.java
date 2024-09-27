package org.folio.rest.workflow.controller.advice;

import jakarta.persistence.EntityNotFoundException;
import org.folio.rest.workflow.exception.SecureOperationException;
import org.folio.rest.workflow.exception.WorkflowAlreadyActiveException;
import org.folio.rest.workflow.exception.WorkflowDeploymentException;
import org.folio.rest.workflow.exception.WorkflowEngineServiceException;
import org.folio.rest.workflow.exception.WorkflowImportException;
import org.folio.rest.workflow.exception.WorkflowNotFoundException;
import org.folio.spring.web.model.response.ResponseErrors;
import org.folio.spring.web.utility.ErrorUtility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class WorkflowControllerAdvice {

  private static final Logger logger = LoggerFactory.getLogger(WorkflowControllerAdvice.class);

  @ResponseStatus(HttpStatus.NOT_FOUND)
  @ExceptionHandler(WorkflowNotFoundException.class)
  public ResponseErrors handleWorkflowNotFoundException(WorkflowNotFoundException exception) {
    logger.debug(exception.getMessage(), exception);
    return ErrorUtility.buildError(exception, HttpStatus.NOT_FOUND);
  }

  @ResponseStatus(HttpStatus.NOT_FOUND)
  @ExceptionHandler(EntityNotFoundException.class)
  public ResponseErrors handleEntityNotFoundException(EntityNotFoundException exception) {
    logger.debug(exception.getMessage(), exception);
    return ErrorUtility.buildError(exception, HttpStatus.NOT_FOUND);
  }

  @ResponseStatus(HttpStatus.FORBIDDEN)
  @ExceptionHandler(WorkflowAlreadyActiveException.class)
  public ResponseErrors handleWorkflowAlreadyActivrException(WorkflowAlreadyActiveException exception) {
    logger.debug(exception.getMessage(), exception);
    return ErrorUtility.buildError(exception, HttpStatus.FORBIDDEN);
  }

  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  @ExceptionHandler(WorkflowDeploymentException.class)
  public ResponseErrors handleWorkflowDeploymentException(WorkflowDeploymentException exception) {
    logger.debug(exception.getMessage(), exception);
    return ErrorUtility.buildError(exception, HttpStatus.INTERNAL_SERVER_ERROR);
  }

  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  @ExceptionHandler(WorkflowEngineServiceException.class)
  public ResponseErrors handleWorkflowEngineServiceException(WorkflowEngineServiceException exception) {
    logger.debug(exception.getMessage(), exception);
    return ErrorUtility.buildError(exception, HttpStatus.INTERNAL_SERVER_ERROR);
  }

  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  @ExceptionHandler(SecureOperationException.class)
  public ResponseErrors handleSecureOperationException(SecureOperationException exception) {
    logger.error(exception.getMessage(), exception);
    return ErrorUtility.buildError(exception, HttpStatus.INTERNAL_SERVER_ERROR);
  }

  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler(WorkflowImportException.class)
  public ResponseErrors handleWorkflowImportExceptionException(WorkflowImportException exception) {
    logger.debug(exception.getMessage(), exception);
    return ErrorUtility.buildError(exception, HttpStatus.BAD_REQUEST);
  }

}
