package org.folio.rest.workflow.model;

import static org.folio.spring.test.mock.MockMvcConstant.NULL_STR;
import static org.folio.spring.test.mock.MockMvcConstant.VALUE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.util.ReflectionTestUtils.getField;
import static org.springframework.test.util.ReflectionTestUtils.setField;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class NodeTest {

  private Node node;

  @BeforeEach
  void beforeEach() {
    node = new Impl();
  }

  @Test
  void getIdWorksTest() {
    setField(node, "id", VALUE);

    assertEquals(VALUE, node.getId());
  }

  @Test
  void setIdWorksTest() {
    setField(node, "id", null);

    node.setId(VALUE);
    assertEquals(VALUE, getField(node, "id"));
  }

  @Test
  void getNameWorksTest() {
    setField(node, "name", VALUE);

    assertEquals(VALUE, node.getName());
  }

  @Test
  void setNameWorksTest() {
    setField(node, "name", null);

    node.setName(VALUE);
    assertEquals(VALUE, getField(node, "name"));
  }

  @Test
  void getDescriptionWorksTest() {
    setField(node, "description", VALUE);

    assertEquals(VALUE, node.getDescription());
  }

  @Test
  void setDescriptionWorksTest() {
    setField(node, "description", null);

    node.setDescription(VALUE);
    assertEquals(VALUE, getField(node, "description"));
  }

  @Test
  void getDeserializeAsWorksTest() {
    setField(node, "deserializeAs", VALUE);

    assertEquals(VALUE, node.getDeserializeAs());
  }

  @Test
  void setDeserializeAsWorksTest() {
    setField(node, "deserializeAs", null);

    node.setDeserializeAs(VALUE);
    assertEquals(VALUE, getField(node, "deserializeAs"));
  }

  @ParameterizedTest
  @MethodSource("providePrePersistFor")
  void prePersistWorksTest(Map<String, Object> initial, Map<String, Object> expected) {
    initial.forEach((String attribute, Object value) -> {
      setField(node, attribute, value);
    });

    node.prePersist();

    expected.forEach((String attribute, Object value) -> {
      assertEquals(value, getField(node, attribute));
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
    final Set<EmbeddedVariable> ivList = new HashSet<>();
    ivList.add(new EmbeddedVariable());

    return Stream.of(
      Arguments.of(
        helperFieldMap(NULL_STR),
        helperFieldMap("")
      ),
      Arguments.of(
        helperFieldMap(VALUE),
        helperFieldMap(VALUE)
      )
    );
  }

  /**
   * Helper for reducing inline code repititon for assignments.
   *
   * @param name The name value.
   *
   * @return The built arguments map.
   */
  private static Map<String, Object> helperFieldMap(String name) {
    final Map<String, Object> map = new HashMap<>();

    map.put("name", name);

    return map;
  }

  private static class Impl extends Node { }

}
