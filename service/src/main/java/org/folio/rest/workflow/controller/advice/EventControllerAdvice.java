package org.folio.rest.workflow.controller.advice;

import java.nio.file.FileSystemException;
import org.folio.rest.workflow.exception.EventPublishException;
import org.folio.spring.web.model.response.ResponseErrors;
import org.folio.spring.web.utility.ErrorUtility;
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
  public ResponseErrors handleEventPublishException(EventPublishException exception) {
    return buildError(exception, HttpStatus.INTERNAL_SERVER_ERROR);
  }

  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler(FileSystemException.class)
  public ResponseErrors handleFileSystemException(FileSystemException exception) {
    return buildError(exception, HttpStatus.BAD_REQUEST);
  }

  /**
   * Build the error message.
   *
   * @param ex The exception.
   * @param code The HTTP Status Code.
   *
   * @return The built error response entity.
   */
  private ResponseErrors buildError(Exception ex, HttpStatus code) {
    logger.error(ex.getMessage());

    if (logger.isDebugEnabled()) {
      ex.printStackTrace();
    }

    return ErrorUtility.buildError(ex, code);
  }

}
