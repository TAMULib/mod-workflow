package org.folio.rest.workflow.dto;

import static org.folio.spring.test.mock.MockMvcConstant.VALUE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.util.ReflectionTestUtils.getField;
import static org.springframework.test.util.ReflectionTestUtils.setField;

import java.util.ArrayList;
import java.util.List;
import org.folio.rest.workflow.model.Node;
import org.folio.rest.workflow.model.Setup;
import org.folio.rest.workflow.model.Workflow;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class WorkflowDtoTest {

  @Mock
  private Setup setup;

  @Mock
  private Node node;

  private List<Node> nodes;

  private WorkflowDto workflowDto;

  @BeforeEach
  void beforeEach() {
    workflowDto = new Impl();
    nodes = new ArrayList<>();
    nodes.add(node);
  }

  @Test
  void getIdWorksTest() {
    setField(workflowDto, "id", VALUE);

    assertEquals(VALUE, workflowDto.getId());
  }

  @Test
  void setIdWorksTest() {
    setField(workflowDto, "id", null);

    workflowDto.setId(VALUE);
    assertEquals(VALUE, getField(workflowDto, "id"));
  }

  @Test
  void getNameWorksTest() {
    setField(workflowDto, "name", VALUE);

    assertEquals(VALUE, workflowDto.getName());
  }

  @Test
  void setNameWorksTest() {
    setField(workflowDto, "name", null);

    workflowDto.setName(VALUE);
    assertEquals(VALUE, getField(workflowDto, "name"));
  }

  @Test
  void getDescriptionWorksTest() {
    setField(workflowDto, "description", VALUE);

    assertEquals(VALUE, workflowDto.getDescription());
  }

  @Test
  void setDescriptionWorksTest() {
    setField(workflowDto, "description", null);

    workflowDto.setDescription(VALUE);
    assertEquals(VALUE, getField(workflowDto, "description"));
  }

  @Test
  void getNodesWorksTest() {
    setField(workflowDto, "nodes", nodes);

    assertEquals(nodes, workflowDto.getNodes());
  }

  @Test
  void setNodesWorksTest() {
    setField(workflowDto, "nodes", null);

    workflowDto.setNodes(nodes);
    assertEquals(nodes, getField(workflowDto, "nodes"));
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
  void getHistoryTimeToLiveWorksTest() {
    setField(workflowDto, "historyTimeToLive", 1);

    assertEquals(1, workflowDto.getHistoryTimeToLive());
  }

  @Test
  void setHistoryTimeToLiveWorksTest() {
    setField(workflowDto, "historyTimeToLive", null);

    workflowDto.setHistoryTimeToLive(1);
    assertEquals(1, getField(workflowDto, "historyTimeToLive"));
  }

  @Test
  void getActiveWorksTest() {
    setField(workflowDto, "active", true);

    assertEquals(true, workflowDto.getActive());
  }

  @Test
  void setActiveWorksTest() {
    setField(workflowDto, "active", false);

    workflowDto.setActive(true);
    assertEquals(true, getField(workflowDto, "active"));
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

  @Test
  void getSetupWorksTest() {
    setField(workflowDto, "setup", setup);

    assertEquals(setup, workflowDto.getSetup());
  }

  @Test
  void setSetupWorksTest() {
    setField(workflowDto, "setup", null);

    workflowDto.setSetup(setup);
    assertEquals(setup, getField(workflowDto, "setup"));
  }

  private static class Impl extends Workflow implements WorkflowDto { };

}
