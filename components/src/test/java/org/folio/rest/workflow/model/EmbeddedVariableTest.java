package org.folio.rest.workflow.model;

import static org.folio.spring.test.mock.MockMvcConstant.NULL_STR;
import static org.folio.spring.test.mock.MockMvcConstant.VALUE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.util.ReflectionTestUtils.getField;
import static org.springframework.test.util.ReflectionTestUtils.setField;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import org.folio.rest.workflow.enums.DirectoryAction;
import org.folio.rest.workflow.enums.VariableType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class EmbeddedVariableTest {

  private EmbeddedVariable embeddedVariable;

  @BeforeEach
  void beforeEach() {
    embeddedVariable = new EmbeddedVariable();
  }

  @Test
  void getKeyWorksTest() {
    setField(embeddedVariable, "key", VALUE);

    assertEquals(VALUE, embeddedVariable.getKey());
  }

  @Test
  void setKeyWorksTest() {
    setField(embeddedVariable, "key", null);

    embeddedVariable.setKey(VALUE);
    assertEquals(VALUE, getField(embeddedVariable, "key"));
  }

  @Test
  void getTypeWorksTest() {
    setField(embeddedVariable, "type", VariableType.PROCESS);

    assertEquals(VariableType.PROCESS, embeddedVariable.getType());
  }

  @Test
  void setTypeWorksTest() {
    setField(embeddedVariable, "type", null);

    embeddedVariable.setType(VariableType.PROCESS);
    assertEquals(VariableType.PROCESS, getField(embeddedVariable, "type"));
  }

  @Test
  void getSpinWorksTest() {
    setField(embeddedVariable, "spin", true);

    assertEquals(true, embeddedVariable.getSpin());
  }

  @Test
  void setSpinWorksTest() {
    setField(embeddedVariable, "spin", false);

    embeddedVariable.setSpin(true);
    assertEquals(true, getField(embeddedVariable, "spin"));
  }

  @Test
  void getAsJsonWorksTest() {
    setField(embeddedVariable, "asJson", true);

    assertEquals(true, embeddedVariable.getAsJson());
  }

  @Test
  void setAsJsonWorksTest() {
    setField(embeddedVariable, "asJson", false);

    embeddedVariable.setAsJson(true);
    assertEquals(true, getField(embeddedVariable, "asJson"));
  }

  @Test
  void getAsTransientWorksTest() {
    setField(embeddedVariable, "asTransient", true);

    assertEquals(true, embeddedVariable.getAsTransient());
  }

  @Test
  void setAsTransientWorksTest() {
    setField(embeddedVariable, "asTransient", false);

    embeddedVariable.setAsTransient(true);
    assertEquals(true, getField(embeddedVariable, "asTransient"));
  }

  @ParameterizedTest
  @MethodSource("providePrePersistFor")
  void prePersistWorksTest(Map<String, Object> initial, Map<String, Object> expected) {
    initial.forEach((String attribute, Object value) -> {
      setField(embeddedVariable, attribute, value);
    });

    embeddedVariable.prePersist();

    expected.forEach((String attribute, Object value) -> {
      assertEquals(value, getField(embeddedVariable, attribute));
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
        helperFieldMap(null,  null,  null,  null),
        helperFieldMap(false, false, false, VariableType.PROCESS)
      ),
      Arguments.of(
        helperFieldMap(false, false, false, VariableType.PROCESS),
        helperFieldMap(false, false, false, VariableType.PROCESS)
      ),
      Arguments.of(
        helperFieldMap(true,  true,  true,  null),
        helperFieldMap(true,  true,  true, VariableType.PROCESS)
      ),
      Arguments.of(
        helperFieldMap(true,  true,  true,  VariableType.LOCAL),
        helperFieldMap(true,  true, true,   VariableType.LOCAL)
      ),
      Arguments.of(
        helperFieldMap(null,  null,  null,  VariableType.PROCESS),
        helperFieldMap(false, false, false, VariableType.PROCESS)
      ),
      Arguments.of(
        helperFieldMap(null,  true,  true,  null),
        helperFieldMap(false, true,  true,  VariableType.PROCESS)
      )
    );
  }

  /**
   * Helper for reducing inline code repititon for assignments.
   *
   * @param asJson The asJson value.
   * @param asTransient The asTransient value.
   * @param spin The spin value.
   * @param type The type value.
   *
   * @return The built arguments map.
   */
  private static Map<String, Object> helperFieldMap(Boolean asJson, Boolean asTransient, Boolean spin, VariableType type ) {
    final Map<String, Object> map = new HashMap<>();

    map.put("asJson", asJson);
    map.put("asTransient", asTransient);
    map.put("spin", spin);
    map.put("type", type);

    return map;
  }

}
