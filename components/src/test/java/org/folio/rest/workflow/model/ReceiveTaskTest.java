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
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ReceiveTaskTest {

  @Mock
  private EmbeddedVariable embeddedVariable;

  private Set<EmbeddedVariable> inputVariables;

  private ReceiveTask receiveTask;

  @BeforeEach
  void beforeEach() {
    receiveTask = new ReceiveTask();
  }

  @Test
  void getIdWorksTest() {
    setField(receiveTask, "id", VALUE);

    assertEquals(VALUE, receiveTask.getId());
  }

  @Test
  void setIdWorksTest() {
    setField(receiveTask, "id", null);

    receiveTask.setId(VALUE);
    assertEquals(VALUE, getField(receiveTask, "id"));
  }

  @Test
  void getNameWorksTest() {
    setField(receiveTask, "name", VALUE);

    assertEquals(VALUE, receiveTask.getName());
  }

  @Test
  void setNameWorksTest() {
    setField(receiveTask, "name", null);

    receiveTask.setName(VALUE);
    assertEquals(VALUE, getField(receiveTask, "name"));
  }

  @Test
  void getDescriptionWorksTest() {
    setField(receiveTask, "description", VALUE);

    assertEquals(VALUE, receiveTask.getDescription());
  }

  @Test
  void setDescriptionWorksTest() {
    setField(receiveTask, "description", null);

    receiveTask.setDescription(VALUE);
    assertEquals(VALUE, getField(receiveTask, "description"));
  }

  @Test
  void getDeserializeAsWorksTest() {
    setField(receiveTask, "deserializeAs", VALUE);

    assertEquals(VALUE, receiveTask.getDeserializeAs());
  }

  @Test
  void setDeserializeAsWorksTest() {
    setField(receiveTask, "deserializeAs", null);

    receiveTask.setDeserializeAs(VALUE);
    assertEquals(VALUE, getField(receiveTask, "deserializeAs"));
  }

  @Test
  void getInputVariablesWorksTest() {
    setField(receiveTask, "inputVariables", inputVariables);

    assertEquals(inputVariables, receiveTask.getInputVariables());
  }

  @Test
  void setInputVariablesWorksTest() {
    setField(receiveTask, "inputVariables", null);

    receiveTask.setInputVariables(inputVariables);
    assertEquals(inputVariables, getField(receiveTask, "inputVariables"));
  }

  @Test
  void getOutputVariableWorksTest() {
    setField(receiveTask, "outputVariable", embeddedVariable);

    assertEquals(embeddedVariable, receiveTask.getOutputVariable());
  }

  @Test
  void setOutputVariableWorksTest() {
    setField(receiveTask, "outputVariable", null);

    receiveTask.setOutputVariable(embeddedVariable);
    assertEquals(embeddedVariable, getField(receiveTask, "outputVariable"));
  }

  @Test
  void getAsyncBeforeWorksTest() {
    setField(receiveTask, "asyncBefore", true);

    assertEquals(true, receiveTask.getAsyncBefore());
  }

  @Test
  void setAsyncBeforeWorksTest() {
    setField(receiveTask, "asyncBefore", false);

    receiveTask.setAsyncBefore(true);
    assertEquals(true, getField(receiveTask, "asyncBefore"));
  }

  @Test
  void getAsyncAfterWorksTest() {
    setField(receiveTask, "asyncAfter", true);

    assertEquals(true, receiveTask.getAsyncAfter());
  }

  @Test
  void setAsyncAfterWorksTest() {
    setField(receiveTask, "asyncAfter", false);

    receiveTask.setAsyncAfter(true);
    assertEquals(true, getField(receiveTask, "asyncAfter"));
  }

  @Test
  void getMessageWorksTest() {
    setField(receiveTask, "message", VALUE);

    assertEquals(VALUE, receiveTask.getMessage());
  }

  @Test
  void setMessageWorksTest() {
    setField(receiveTask, "message", null);

    receiveTask.setMessage(VALUE);
    assertEquals(VALUE, getField(receiveTask, "message"));
  }

  @ParameterizedTest
  @MethodSource("providePrePersistFor")
  void prePersistWorksTest(Map<String, Object> initial, Map<String, Object> expected) {
    initial.forEach((String attribute, Object value) -> {
      setField(receiveTask, attribute, value);
    });

    receiveTask.prePersist();

    expected.forEach((String attribute, Object value) -> {
      assertEquals(value, getField(receiveTask, attribute));
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
   * @param message The message value.
   *
   * @return The built arguments map.
   */
  private static Map<String, Object> helperFieldMap(String message) {
    final Map<String, Object> map = new HashMap<>();

    map.put("message", message);

    return map;
  }

}
