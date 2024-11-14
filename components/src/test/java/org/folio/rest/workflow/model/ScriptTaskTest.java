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
import org.mockito.Mock;

class ScriptTaskTest {

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
    setField(scriptTask, "id", VALUE);

    assertEquals(VALUE, scriptTask.getId());
  }

  @Test
  void setIdWorksTest() {
    setField(scriptTask, "id", null);

    scriptTask.setId(VALUE);
    assertEquals(VALUE, getField(scriptTask, "id"));
  }

  @Test
  void getNameWorksTest() {
    setField(scriptTask, "name", VALUE);

    assertEquals(VALUE, scriptTask.getName());
  }

  @Test
  void setNameWorksTest() {
    setField(scriptTask, "name", null);

    scriptTask.setName(VALUE);
    assertEquals(VALUE, getField(scriptTask, "name"));
  }

  @Test
  void getDescriptionWorksTest() {
    setField(scriptTask, "description", VALUE);

    assertEquals(VALUE, scriptTask.getDescription());
  }

  @Test
  void setDescriptionWorksTest() {
    setField(scriptTask, "description", null);

    scriptTask.setDescription(VALUE);
    assertEquals(VALUE, getField(scriptTask, "description"));
  }

  @Test
  void getDeserializeAsWorksTest() {
    setField(scriptTask, "deserializeAs", VALUE);

    assertEquals(VALUE, scriptTask.getDeserializeAs());
  }

  @Test
  void setDeserializeAsWorksTest() {
    setField(scriptTask, "deserializeAs", null);

    scriptTask.setDeserializeAs(VALUE);
    assertEquals(VALUE, getField(scriptTask, "deserializeAs"));
  }

  @Test
  void getInputVariablesWorksTest() {
    setField(scriptTask, "inputVariables", inputVariables);

    assertEquals(inputVariables, scriptTask.getInputVariables());
  }

  @Test
  void setInputVariablesWorksTest() {
    setField(scriptTask, "inputVariables", null);

    scriptTask.setInputVariables(inputVariables);
    assertEquals(inputVariables, getField(scriptTask, "inputVariables"));
  }

  @Test
  void getOutputVariableWorksTest() {
    setField(scriptTask, "outputVariable", embeddedVariable);

    assertEquals(embeddedVariable, scriptTask.getOutputVariable());
  }

  @Test
  void setOutputVariableWorksTest() {
    setField(scriptTask, "outputVariable", null);

    scriptTask.setOutputVariable(embeddedVariable);
    assertEquals(embeddedVariable, getField(scriptTask, "outputVariable"));
  }

  @Test
  void getAsyncBeforeWorksTest() {
    setField(scriptTask, "asyncBefore", true);

    assertEquals(true, scriptTask.getAsyncBefore());
  }

  @Test
  void setAsyncBeforeWorksTest() {
    setField(scriptTask, "asyncBefore", false);

    scriptTask.setAsyncBefore(true);
    assertEquals(true, getField(scriptTask, "asyncBefore"));
  }

  @Test
  void getAsyncAfterWorksTest() {
    setField(scriptTask, "asyncAfter", true);

    assertEquals(true, scriptTask.getAsyncAfter());
  }

  @Test
  void setAsyncAfterWorksTest() {
    setField(scriptTask, "asyncAfter", false);

    scriptTask.setAsyncAfter(true);
    assertEquals(true, getField(scriptTask, "asyncAfter"));
  }

  @Test
  void getScriptFormatWorksTest() {
    setField(scriptTask, "scriptFormat", VALUE);

    assertEquals(VALUE, scriptTask.getScriptFormat());
  }

  @Test
  void setScriptFormatWorksTest() {
    setField(scriptTask, "scriptFormat", null);

    scriptTask.setScriptFormat(VALUE);
    assertEquals(VALUE, getField(scriptTask, "scriptFormat"));
  }

  @Test
  void getCodeWorksTest() {
    setField(scriptTask, "code", VALUE);

    assertEquals(VALUE, scriptTask.getCode());
  }

  @Test
  void setCodeWorksTest() {
    setField(scriptTask, "code", null);

    scriptTask.setCode(VALUE);
    assertEquals(VALUE, getField(scriptTask, "code"));
  }

  @Test
  void getResultVariableWorksTest() {
    setField(scriptTask, "resultVariable", VALUE);

    assertEquals(VALUE, scriptTask.getResultVariable());
  }

  @Test
  void setResultVariableWorksTest() {
    setField(scriptTask, "resultVariable", null);

    scriptTask.setResultVariable(VALUE);
    assertEquals(VALUE, getField(scriptTask, "resultVariable"));
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
    final Set<EmbeddedVariable> ivList = new HashSet<>();
    ivList.add(new EmbeddedVariable());

    return Stream.of(
      Arguments.of(
        helperFieldMap(NULL_STR, NULL_STR),
        helperFieldMap("",       "javaScript")
      ),
      Arguments.of(
        helperFieldMap(VALUE,    NULL_STR),
        helperFieldMap(VALUE,    "javaScript")
      ),
      Arguments.of(
        helperFieldMap(NULL_STR, VALUE),
        helperFieldMap("",       VALUE)
      )
    );
  }

  /**
   * Helper for reducing inline code repititon for assignments.
   *
   * @param code The code value.
   * @param scriptFormat The scriptFormat value.
   *
   * @return The built arguments map.
   */
  private static Map<String, Object> helperFieldMap(String code, String scriptFormat) {
    final Map<String, Object> map = new HashMap<>();

    map.put("code", code);
    map.put("scriptFormat", scriptFormat);

    return map;
  }

}
