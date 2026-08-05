package org.folio.rest.workflow.dto;

import static org.folio.spring.test.mock.MockMvcConstant.VALUE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.util.ReflectionTestUtils.getField;
import static org.springframework.test.util.ReflectionTestUtils.setField;

import org.folio.rest.workflow.model.Workflow;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class WorkflowOperationalDtoTest {

  private WorkflowOperationalDto workflowDto;

  @BeforeEach
  void beforeEach() {
    workflowDto = new Impl();
  }

  @Test
  void getVersionTagWorksTest() {
    setField(workflowDto, "versionTag", VALUE);

    assertEquals(VALUE, workflowDto.getVersionTag());
  }

  @Test
  void setVersionTagWorksTest() {
    setField(workflowDto, "versionTag", null);

    workflowDto.setVersionTag(VALUE);
    assertEquals(VALUE, getField(workflowDto, "versionTag"));
  }

  @Test
  void getDeploymentIdWorksTest() {
    setField(workflowDto, "deploymentId", VALUE);

    assertEquals(VALUE, workflowDto.getDeploymentId());
  }

  @Test
  void setDeploymentIdWorksTest() {
    setField(workflowDto, "deploymentId", null);

    workflowDto.setDeploymentId(VALUE);
    assertEquals(VALUE, getField(workflowDto, "deploymentId"));
  }

  private static class Impl extends Workflow implements WorkflowOperationalDto { }

}
