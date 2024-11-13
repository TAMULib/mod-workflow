package org.folio.rest.workflow.model;

import static org.folio.spring.test.mock.MockMvcConstant.NULL_STR;
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

@ExtendWith(MockitoExtension.class)
class MoveToNodeTest {

  @Mock
  private Node node;

  private List<Node> nodes;

  private MoveToNode moveToNode;

  @BeforeEach
  void beforeEach() {
    moveToNode = new MoveToNode();
    nodes = new ArrayList<>();
    nodes.add(node);
  }

  @Test
  void getIdWorksTest() {
    setField(moveToNode, "id", VALUE);

    assertEquals(VALUE, moveToNode.getId());
  }

  @Test
  void setIdWorksTest() {
    setField(moveToNode, "id", null);

    moveToNode.setId(VALUE);
    assertEquals(VALUE, getField(moveToNode, "id"));
  }

  @Test
  void getNameWorksTest() {
    setField(moveToNode, "name", VALUE);

    assertEquals(VALUE, moveToNode.getName());
  }

  @Test
  void setNameWorksTest() {
    setField(moveToNode, "name", null);

    moveToNode.setName(VALUE);
    assertEquals(VALUE, getField(moveToNode, "name"));
  }

  @Test
  void getDescriptionWorksTest() {
    setField(moveToNode, "description", VALUE);

    assertEquals(VALUE, moveToNode.getDescription());
  }

  @Test
  void setDescriptionWorksTest() {
    setField(moveToNode, "description", null);

    moveToNode.setDescription(VALUE);
    assertEquals(VALUE, getField(moveToNode, "description"));
  }

  @Test
  void getDeserializeAsWorksTest() {
    setField(moveToNode, "deserializeAs", VALUE);

    assertEquals(VALUE, moveToNode.getDeserializeAs());
  }

  @Test
  void setDeserializeAsWorksTest() {
    setField(moveToNode, "deserializeAs", null);

    moveToNode.setDeserializeAs(VALUE);
    assertEquals(VALUE, getField(moveToNode, "deserializeAs"));
  }

  @Test
  void getGatewayIdWorksTest() {
    setField(moveToNode, "gatewayId", VALUE);

    assertEquals(VALUE, moveToNode.getGatewayId());
  }

  @Test
  void setGatewayIdWorksTest() {
    setField(moveToNode, "gatewayId", null);

    moveToNode.setGatewayId(VALUE);
    assertEquals(VALUE, getField(moveToNode, "gatewayId"));
  }

  @Test
  void getNodesWorksTest() {
    setField(moveToNode, "nodes", nodes);

    assertEquals(nodes, moveToNode.getNodes());
  }

  @Test
  void setNodesWorksTest() {
    setField(moveToNode, "nodes", null);

    moveToNode.setNodes(nodes);
    assertEquals(nodes, getField(moveToNode, "nodes"));
  }

  @ParameterizedTest
  @MethodSource("providePrePersistFor")
  void prePersistWorksTest(Map<String, Object> initial, Map<String, Object> expected) {
    initial.forEach((String attribute, Object value) -> {
      setField(moveToNode, attribute, value);
    });

    moveToNode.prePersist();

    expected.forEach((String attribute, Object value) -> {
      assertEquals(value, getField(moveToNode, attribute));
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
    final List<Node> nodeList = new ArrayList<>();

    return Stream.of(
      Arguments.of(
        helperFieldMap(NULL_STR, null),
        helperFieldMap("",       nodeList)
      ),
      Arguments.of(
        helperFieldMap(VALUE,    null),
        helperFieldMap(VALUE,    nodeList)
      ),
      Arguments.of(
        helperFieldMap(NULL_STR, nodeList),
        helperFieldMap("",       nodeList)
      )
    );
  }

  /**
   * Helper for reducing inline code repititon for assignments.
   *
   * @param gatewayId The gatewayId value.
   * @param nodes The nodes value.
   *
   * @return The built arguments map.
   */
  private static Map<String, Object> helperFieldMap(String gatewayId, List<Node> nodes ) {
    final Map<String, Object> map = new HashMap<>();

    map.put("gatewayId", gatewayId);
    map.put("nodes", nodes);

    return map;
  }

}
