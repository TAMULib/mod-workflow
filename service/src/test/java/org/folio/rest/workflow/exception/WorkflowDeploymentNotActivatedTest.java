package org.folio.rest.workflow.exception;

import static org.folio.spring.test.mock.MockMvcConstant.UUID;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class WorkflowDeploymentNotActivatedTest {

  @Test
  void workflowDeploymentNotActivatedWorksTest() {
    WorkflowDeploymentNotActivated exception = Assertions.assertThrows(WorkflowDeploymentNotActivated.class, () -> {
      throw new WorkflowDeploymentNotActivated(UUID);
    });

    assertNotNull(exception);
    assertTrue(exception.getMessage().contains(UUID));
  }

  @Test
  void workflowDeploymentNotActivatedWorksWithChildExceptionTest() {
    WorkflowDeploymentNotActivated exception = Assertions.assertThrows(WorkflowDeploymentNotActivated.class, () -> {
      throw new WorkflowDeploymentNotActivated(UUID, new RuntimeException("Additional Exception"));
    });

    assertNotNull(exception);
    assertTrue(exception.getMessage().contains(UUID));
  }

}
