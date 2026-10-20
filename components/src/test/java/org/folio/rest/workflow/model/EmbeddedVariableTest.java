package org.folio.rest.workflow.model;

import static org.folio.rest.workflow.enums.VariableType.LOCAL;
import static org.folio.rest.workflow.enums.VariableType.PROCESS;
import static org.folio.spring.test.mock.MockMvcConstant.VALUE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.util.ReflectionTestUtils.getField;
import static org.springframework.test.util.ReflectionTestUtils.setField;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;
import org.folio.rest.workflow.enums.VariableType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class EmbeddedVariableTest {

  private static final String ASARRAY     = "asArray";
  private static final String ASJSON      = "asJson";
  private static final String ASTRANSIENT = "asTransient";
  private static final String KEY         = "key";
  private static final String SPIN        = "spin";
  private static final String TYPE        = "type";

  private EmbeddedVariable embeddedVariable;

  @BeforeEach
  void beforeEach() {
    embeddedVariable = new EmbeddedVariable();
  }

  @Test
  void getKeyWorksTest() {
    setField(embeddedVariable, KEY, VALUE);

    assertEquals(VALUE, embeddedVariable.getKey());
  }

  @Test
  void setKeyWorksTest() {
    setField(embeddedVariable, KEY, null);

    embeddedVariable.setKey(VALUE);
    assertEquals(VALUE, getField(embeddedVariable, KEY));
  }

  @Test
  void getTypeWorksTest() {
    setField(embeddedVariable, TYPE, PROCESS);

    assertEquals(PROCESS, embeddedVariable.getType());
  }

  @Test
  void setTypeWorksTest() {
    setField(embeddedVariable, TYPE, null);

    embeddedVariable.setType(PROCESS);
    assertEquals(PROCESS, getField(embeddedVariable, TYPE));
  }

  @Test
  void getSpinWorksTest() {
    setField(embeddedVariable, SPIN, true);

    assertEquals(true, embeddedVariable.getSpin());
  }

  @Test
  void setSpinWorksTest() {
    setField(embeddedVariable, SPIN, false);

    embeddedVariable.setSpin(true);
    assertEquals(true, getField(embeddedVariable, SPIN));
  }

  @Test
  void getAsArrayWorksTest() {
    setField(embeddedVariable, ASARRAY, true);

    assertEquals(true, embeddedVariable.getAsArray());
  }

  @Test
  void setAsArrayWorksTest() {
    setField(embeddedVariable, ASARRAY, false);

    embeddedVariable.setAsArray(true);
    assertEquals(true, getField(embeddedVariable, ASARRAY));
  }

  @Test
  void getAsJsonWorksTest() {
    setField(embeddedVariable, ASJSON, true);

    assertEquals(true, embeddedVariable.getAsJson());
  }

  @Test
  void setAsJsonWorksTest() {
    setField(embeddedVariable, ASJSON, false);

    embeddedVariable.setAsJson(true);
    assertEquals(true, getField(embeddedVariable, ASJSON));
  }

  @Test
  void getAsTransientWorksTest() {
    setField(embeddedVariable, ASTRANSIENT, true);

    assertEquals(true, embeddedVariable.getAsTransient());
  }

  @Test
  void setAsTransientWorksTest() {
    setField(embeddedVariable, ASTRANSIENT, false);

    embeddedVariable.setAsTransient(true);
    assertEquals(true, getField(embeddedVariable, ASTRANSIENT));
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

    final List<Arguments> arguments = new ArrayList<>();

    arguments.add(Arguments.of(
      helperFieldMap(null, null,  null,  null,  null,  null),
      helperFieldMap(null, false, false, false, false, PROCESS)
    ));

    arguments.add(Arguments.of(
      helperFieldMap(KEY,  null,  null,  null,  null,  null),
      helperFieldMap(KEY,  false, false, false, false, PROCESS)
    ));

    arguments.add(Arguments.of(
      helperFieldMap(null, true,  null,  null,  null,  null),
      helperFieldMap(null, true,  false, false, false, PROCESS)
    ));

    arguments.add(Arguments.of(
      helperFieldMap(null, null,  true,  null,  null,  null),
      helperFieldMap(null, false, true,  false, false, PROCESS)
    ));

    arguments.add(Arguments.of(
      helperFieldMap(null, null,  null,  true,  null,  null),
      helperFieldMap(null, false, false, true,  false, PROCESS)
    ));

    arguments.add(Arguments.of(
      helperFieldMap(null, null,  null,  null,  true,  null),
      helperFieldMap(null, false, true, false, true,  PROCESS)
    ));

    arguments.add(Arguments.of(
      helperFieldMap(null, null,  null,  null,  null,  LOCAL),
      helperFieldMap(null, false, false, false, false, LOCAL)
    ));

    return arguments.stream();
  }

  /**
   * Helper for reducing in line code repetition for assignments.
   *
   * @param key         The key value.
   * @param asArray     The asArray value.
   * @param asJson      The asJson value.
   * @param asTransient The asTransient value.
   * @param spin        The spin value.
   * @param type        The type value.
   *
   * @return The built arguments map.
   */
  private static Map<String, Object> helperFieldMap(
    String key, Boolean asArray, Boolean asJson, Boolean asTransient, Boolean spin, VariableType type
  ) {

    final Map<String, Object> map = new HashMap<>();

    map.put(KEY, key);
    map.put(ASARRAY, asArray);
    map.put(ASJSON, asJson);
    map.put(ASTRANSIENT, asTransient);
    map.put(SPIN, spin);
    map.put(TYPE, type);

    return map;
  }

}
