package org.folio.rest.workflow.controller.advice;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalAdvice extends AbstractAdvice {

  ObjectMapper objectMapper;

  public GlobalAdvice() {
    this.objectMapper = new ObjectMapper();
  }

  @Override
  protected ObjectMapper getObjectMapper() {
    return objectMapper;
  }

  @ExceptionHandler({ TransactionSystemException.class })
  public ResponseEntity<String> handleConstraintViolation(TransactionSystemException ex) {
    return buildError((Exception) ex.getRootCause(), HttpStatus.BAD_REQUEST);
  }

}
