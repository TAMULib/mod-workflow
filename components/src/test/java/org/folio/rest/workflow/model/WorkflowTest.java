package org.folio.rest.workflow.model;

import static org.folio.spring.test.mock.MockMvcConstant.VALUE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.util.ReflectionTestUtils.getField;
import static org.springframework.test.util.ReflectionTestUtils.setField;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.fasterxml.jackson.databind.JsonNode;

@ExtendWith(MockitoExtension.class)
class WorkflowTest {

  @Mock
  private Setup setup;

  @Mock
  private Node node;

  private List<Node> nodes;

  private Workflow workflow;

  @BeforeEach
  void beforeEach() {
    workflow = new Workflow();
    nodes = new ArrayList<>();
    nodes.add(node);
  }

  @Test
  void getIdWorksTest() {
    setField(workflow, "id", VALUE);

    assertEquals(VALUE, workflow.getId());
  }

  @Test
  void setIdWorksTest() {
    setField(workflow, "id", null);

    workflow.setId(VALUE);
    assertEquals(VALUE, getField(workflow, "id"));
  }

  @Test
  void getNameWorksTest() {
    setField(workflow, "name", VALUE);

    assertEquals(VALUE, workflow.getName());
  }

  @Test
  void setNameWorksTest() {
    setField(workflow, "name", null);

    workflow.setName(VALUE);
    assertEquals(VALUE, getField(workflow, "name"));
  }

  @Test
  void getDescriptionWorksTest() {
    setField(workflow, "description", VALUE);

    assertEquals(VALUE, workflow.getDescription());
  }

  @Test
  void setDescriptionWorksTest() {
    setField(workflow, "description", null);

    workflow.setDescription(VALUE);
    assertEquals(VALUE, getField(workflow, "description"));
  }

  @Test
  void getNodesWorksTest() {
    setField(workflow, "nodes", nodes);

    assertEquals(nodes, workflow.getNodes());
  }

  @Test
  void setNodesWorksTest() {
    setField(workflow, "nodes", null);

    workflow.setNodes(nodes);
    assertEquals(nodes, getField(workflow, "nodes"));
  }

  @Test
  void getVersionTagWorksTest() {
    setField(workflow, "versionTag", VALUE);

    assertEquals(VALUE, workflow.getVersionTag());
  }

  @Test
  void setVersionTagWorksTest() {
    setField(workflow, "versionTag", null);

    workflow.setVersionTag(VALUE);
    assertEquals(VALUE, getField(workflow, "versionTag"));
  }

  @Test
  void getHistoryTimeToLiveWorksTest() {
    setField(workflow, "historyTimeToLive", 1);

    assertEquals(1, workflow.getHistoryTimeToLive());
  }

  @Test
  void setHistoryTimeToLiveWorksTest() {
    setField(workflow, "historyTimeToLive", null);

    workflow.setHistoryTimeToLive(1);
    assertEquals(1, getField(workflow, "historyTimeToLive"));
  }

  @Test
  void getActiveWorksTest() {
    setField(workflow, "active", true);

    assertEquals(true, workflow.getActive());
  }

  @Test
  void setActiveWorksTest() {
    setField(workflow, "active", false);

    workflow.setActive(true);
    assertEquals(true, getField(workflow, "active"));
  }

  @Test
  void getDeploymentIdWorksTest() {
    setField(workflow, "deploymentId", VALUE);

    assertEquals(VALUE, workflow.getDeploymentId());
  }

  @Test
  void setDeploymentIdWorksTest() {
    setField(workflow, "deploymentId", null);

    workflow.setDeploymentId(VALUE);
    assertEquals(VALUE, getField(workflow, "deploymentId"));
  }

  @Test
  void getSetupWorksTest() {
    setField(workflow, "setup", setup);

    assertEquals(setup, workflow.getSetup());
  }

  @Test
  void setSetupWorksTest() {
    setField(workflow, "setup", null);

    workflow.setSetup(setup);
    assertEquals(setup, getField(workflow, "setup"));
  }

  @ParameterizedTest
  @MethodSource("providePrePersistFor")
  void prePersistWorksTest(Map<String, Object> initial, Map<String, Object> expected) {
    initial.forEach((String attribute, Object value) -> {
      setField(workflow, attribute, value);
    });

    workflow.prePersist();

    expected.forEach((String attribute, Object value) -> {
      assertEquals(value, getField(workflow, attribute));
    });
  }

  /**
   * Helper function for parameterized tests for the prePersist function.
   *
   * @return
   *   The arguments array stream with the stream columns as:
   *     - Arguments initial The initial values.
   *     - Arguments expect The expected values.
   */
  private static Stream<Arguments> providePrePersistFor() {

    return Stream.of(
      Arguments.of(
        helperFieldMap(null,  null, null, null,            null,              null),
        helperFieldMap(false, 0,    "",   new HashMap<>(), new ArrayList<>(), "")
      ),
      Arguments.of(
        helperFieldMap(true,  null, VALUE, null,            null,              VALUE),
        helperFieldMap(true,  0,    VALUE, new HashMap<>(), new ArrayList<>(), VALUE)
      )
    );
  }

  /**
   * Helper for reducing inline code repititon for assignments.
   *
   * @param active The active value.
   * @param historyTimeToLive The historyTimeToLive value.
   * @param name The name value.
   * @param initialContext The initialContext value.
   * @param nodes The nodes value.
   * @param versionTag The versionTag value.
   *
   * @return The built arguments map.
   */
  private static Map<String, Object> helperFieldMap(Boolean active, Integer historyTimeToLive, String name, Map<String, JsonNode> initialContext, List<Node> nodes, String versionTag) {
    final Map<String, Object> map = new HashMap<>();

    map.put("active", active);
    map.put("historyTimeToLive", historyTimeToLive);
    map.put("name", name);
    map.put("initialContext", initialContext);
    map.put("nodes", nodes);
    map.put("versionTag", versionTag);

    return map;
  }

}
