package org.folio.rest.workflow.exception;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class WorkflowDeploymentExceptionTest {

  @Test
  void workflowDeploymentExceptionWorksTest() {
    WorkflowDeploymentException exception = Assertions.assertThrows(WorkflowDeploymentException.class, () -> {
      throw new WorkflowDeploymentException();
    });

    assertNotNull(exception);
  }

}
