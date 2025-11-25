package org.folio.rest.workflow.exception.handler;

import org.folio.spring.web.model.response.ResponseErrors;
import org.folio.spring.web.utility.ErrorUtility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

@RestController
@ControllerAdvice
public class GlobalExceptionHandler {

  private final Logger logger = LoggerFactory.getLogger(this.getClass());

  @SuppressWarnings("java:S4507") // SonarQube false positive. The stacktrace is protected by debug enabled check.
  @ExceptionHandler({ TransactionSystemException.class })
  public ResponseEntity<ResponseErrors> handleConstraintViolation(TransactionSystemException ex) {
    logger.error(ex.getMessage());

    if (logger.isDebugEnabled()) {
      ex.printStackTrace();
    }

    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
        .body(ErrorUtility.buildError((Exception) ex.getRootCause(), HttpStatus.BAD_REQUEST));
  }

}
