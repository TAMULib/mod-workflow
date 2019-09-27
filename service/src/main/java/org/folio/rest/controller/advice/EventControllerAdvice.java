package org.folio.rest.controller.advice;

import org.folio.rest.exception.EventPublishException;
import org.folio.rest.model.response.Errors;
import org.folio.rest.utility.ErrorUtility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class EventControllerAdvice {

  private static final Logger logger = LoggerFactory.getLogger(EventControllerAdvice.class);

  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  @ExceptionHandler(EventPublishException.class)
  public Errors handleEventPublishException(EventPublishException exception) {
    logger.debug(exception.getMessage(), exception);
    return ErrorUtility.buildError(exception, HttpStatus.INTERNAL_SERVER_ERROR);
  }

}
