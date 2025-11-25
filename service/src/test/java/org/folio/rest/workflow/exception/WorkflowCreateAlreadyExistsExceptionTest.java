package org.folio.rest.workflow.exception;

import static org.folio.spring.test.mock.MockMvcConstant.UUID;
import static org.folio.spring.test.mock.MockMvcConstant.VALUE;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class WorkflowCreateAlreadyExistsExceptionTest {

  @Test
  void workflowCreateAlreadyExistsExceptionTest() {
    WorkflowCreateAlreadyExistsException exception = Assertions.assertThrows(WorkflowCreateAlreadyExistsException.class, () -> {
      throw new WorkflowCreateAlreadyExistsException(UUID, VALUE);
    });

    assertNotNull(exception);
    assertTrue(exception.getMessage().contains(UUID));
    assertTrue(exception.getMessage().contains(VALUE));
  }

}
