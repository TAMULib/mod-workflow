package org.folio.rest.workflow.controller.advice;
import java.nio.file.FileSystemException;
import org.folio.rest.workflow.exception.EventPublishException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class EventControllerAdvice extends AbstractAdvice {

  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  @ExceptionHandler(EventPublishException.class)
  public ResponseEntity<String>  handleEventPublishException(EventPublishException exception) {
    return buildError(exception, HttpStatus.INTERNAL_SERVER_ERROR);
  }

  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler(FileSystemException.class)
  public ResponseEntity<String>  handleFileSystemException(FileSystemException exception) {
    return buildError(exception, HttpStatus.BAD_REQUEST);
  }

}
