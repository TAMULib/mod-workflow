package org.folio.rest.workflow.model;

import static org.folio.spring.test.mock.MockMvcConstant.NULL_STR;
import static org.folio.spring.test.mock.MockMvcConstant.VALUE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.util.ReflectionTestUtils.getField;
import static org.springframework.test.util.ReflectionTestUtils.setField;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;

class ScriptTaskTest {

  private static final String ASYNCAFTER     = "asyncAfter";
  private static final String ASYNCBEFORE    = "asyncBefore";
  private static final String CODE           = "code";
  private static final String DESCRIPTION    = "description";
  private static final String DESERIALIZEAS  = "deserializeAs";
  private static final String ID             = "id";
  private static final String INPUTVARIABLES = "inputVariables";
  private static final String NAME           = "name";
  private static final String OUTPUTVARIABLE = "outputVariable";
  private static final String RESULTVARIABLE = "resultVariable";
  private static final String SCRIPTFORMAT   = "scriptFormat";

  @Mock
  private EmbeddedVariable embeddedVariable;

  private Set<EmbeddedVariable> inputVariables;

  private ScriptTask scriptTask;

  @BeforeEach
  void beforeEach() {
    scriptTask = new ScriptTask();
  }

  @Test
  void getIdWorksTest() {
    setField(scriptTask, ID, VALUE);

    assertEquals(VALUE, scriptTask.getId());
  }

  @Test
  void setIdWorksTest() {
    setField(scriptTask, ID, null);

    scriptTask.setId(VALUE);
    assertEquals(VALUE, getField(scriptTask, ID));
  }

  @Test
  void getNameWorksTest() {
    setField(scriptTask, NAME, VALUE);

    assertEquals(VALUE, scriptTask.getName());
  }

  @Test
  void setNameWorksTest() {
    setField(scriptTask, NAME, null);

    scriptTask.setName(VALUE);
    assertEquals(VALUE, getField(scriptTask, NAME));
  }

  @Test
  void getDescriptionWorksTest() {
    setField(scriptTask, DESCRIPTION, VALUE);

    assertEquals(VALUE, scriptTask.getDescription());
  }

  @Test
  void setDescriptionWorksTest() {
    setField(scriptTask, DESCRIPTION, null);

    scriptTask.setDescription(VALUE);
    assertEquals(VALUE, getField(scriptTask, DESCRIPTION));
  }

  @Test
  void getDeserializeAsWorksTest() {
    setField(scriptTask, DESERIALIZEAS, VALUE);

    assertEquals(VALUE, scriptTask.getDeserializeAs());
  }

  @Test
  void setDeserializeAsWorksTest() {
    setField(scriptTask, DESERIALIZEAS, null);

    scriptTask.setDeserializeAs(VALUE);
    assertEquals(VALUE, getField(scriptTask, DESERIALIZEAS));
  }

  @Test
  void getInputVariablesWorksTest() {
    setField(scriptTask, INPUTVARIABLES, inputVariables);

    assertEquals(inputVariables, scriptTask.getInputVariables());
  }

  @Test
  void setInputVariablesWorksTest() {
    setField(scriptTask, INPUTVARIABLES, null);

    scriptTask.setInputVariables(inputVariables);
    assertEquals(inputVariables, getField(scriptTask, INPUTVARIABLES));
  }

  @Test
  void getOutputVariableWorksTest() {
    setField(scriptTask, OUTPUTVARIABLE, embeddedVariable);

    assertEquals(embeddedVariable, scriptTask.getOutputVariable());
  }

  @Test
  void setOutputVariableWorksTest() {
    setField(scriptTask, OUTPUTVARIABLE, null);

    scriptTask.setOutputVariable(embeddedVariable);
    assertEquals(embeddedVariable, getField(scriptTask, OUTPUTVARIABLE));
  }

  @Test
  void getAsyncBeforeWorksTest() {
    setField(scriptTask, ASYNCBEFORE, true);

    assertEquals(true, scriptTask.getAsyncBefore());
  }

  @Test
  void setAsyncBeforeWorksTest() {
    setField(scriptTask, ASYNCBEFORE, false);

    scriptTask.setAsyncBefore(true);
    assertEquals(true, getField(scriptTask, ASYNCBEFORE));
  }

  @Test
  void getAsyncAfterWorksTest() {
    setField(scriptTask, ASYNCAFTER, true);

    assertEquals(true, scriptTask.getAsyncAfter());
  }

  @Test
  void setAsyncAfterWorksTest() {
    setField(scriptTask, ASYNCAFTER, false);

    scriptTask.setAsyncAfter(true);
    assertEquals(true, getField(scriptTask, ASYNCAFTER));
  }

  @Test
  void getScriptFormatWorksTest() {
    setField(scriptTask, SCRIPTFORMAT, VALUE);

    assertEquals(VALUE, scriptTask.getScriptFormat());
  }

  @Test
  void setScriptFormatWorksTest() {
    setField(scriptTask, SCRIPTFORMAT, null);

    scriptTask.setScriptFormat(VALUE);
    assertEquals(VALUE, getField(scriptTask, SCRIPTFORMAT));
  }

  @Test
  void getCodeWorksTest() {
    setField(scriptTask, CODE, VALUE);

    assertEquals(VALUE, scriptTask.getCode());
  }

  @Test
  void setCodeWorksTest() {
    setField(scriptTask, CODE, null);

    scriptTask.setCode(VALUE);
    assertEquals(VALUE, getField(scriptTask, CODE));
  }

  @Test
  void hasResultVariableReturnsTrueTest() {
    setField(scriptTask, RESULTVARIABLE, VALUE);

    assertTrue(scriptTask.hasResultVariable());
  }

  @Test
  void hasResultVariableReturnsFalseTest() {
    setField(scriptTask, RESULTVARIABLE, null);

    assertFalse(scriptTask.hasResultVariable());
  }

  @Test
  void getResultVariableWorksTest() {
    setField(scriptTask, RESULTVARIABLE, VALUE);

    assertEquals(VALUE, scriptTask.getResultVariable());
  }

  @Test
  void setResultVariableWorksTest() {
    setField(scriptTask, RESULTVARIABLE, null);

    scriptTask.setResultVariable(VALUE);
    assertEquals(VALUE, getField(scriptTask, RESULTVARIABLE));
  }

  @ParameterizedTest
  @MethodSource("providePrePersistFor")
  void prePersistWorksTest(Map<String, Object> initial, Map<String, Object> expected) {
    initial.forEach((String attribute, Object value) -> {
      setField(scriptTask, attribute, value);
    });

    scriptTask.prePersist();

    expected.forEach((String attribute, Object value) -> {
      assertEquals(value, getField(scriptTask, attribute));
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
    final String scriptFormat = ScriptTask.SCRIPT_FORMAT_DEFAULT;

    return List.of(
      Arguments.of(
        helperFieldMap(NULL_STR, NULL_STR),
        helperFieldMap("",       scriptFormat)
      ),
      Arguments.of(
        helperFieldMap(VALUE,    NULL_STR),
        helperFieldMap(VALUE,    scriptFormat)
      ),
      Arguments.of(
        helperFieldMap(NULL_STR, VALUE),
        helperFieldMap("",       VALUE)
      )
    ).stream();
  }

  /**
   * Helper for reducing in line code repetition for assignments.
   *
   * @param code The code value.
   * @param scriptFormat The scriptFormat value.
   *
   * @return The built arguments map.
   */
  private static Map<String, Object> helperFieldMap(String code, String scriptFormat) {
    final Map<String, Object> map = new HashMap<>();

    map.put(CODE, code);
    map.put(SCRIPTFORMAT, scriptFormat);

    return map;
  }

}
