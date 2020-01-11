package org.folio.rest.workflow.exception.handler;

import org.folio.spring.model.response.Errors;
import org.folio.spring.utility.ErrorUtility;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

@RestController
@ControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler({ TransactionSystemException.class })
  public ResponseEntity<Errors> handleConstraintViolation(TransactionSystemException ex) {
    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
        .body(ErrorUtility.buildError((Exception) ex.getRootCause(), HttpStatus.BAD_REQUEST));
  }

}
