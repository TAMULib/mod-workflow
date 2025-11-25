package org.folio.rest.workflow.controller.advice;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.TransactionSystemException;

@ExtendWith(MockitoExtension.class)
class GlobalAdviceTest {

  private static final RuntimeException runtimeException = new RuntimeException("A runtime failure.");

  @Mock
  private TransactionSystemException transactionSystemException;

  private GlobalAdvice globalExceptionHandler;

  @BeforeEach
  void beforeEach() {
    globalExceptionHandler = new GlobalAdvice();
  }

  @Test
  void handleConstraintViolationWorksTest() {
    when(transactionSystemException.getRootCause()).thenReturn(runtimeException);
    ResponseEntity<String> response = globalExceptionHandler.handleConstraintViolation(transactionSystemException);

    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
  }

}
