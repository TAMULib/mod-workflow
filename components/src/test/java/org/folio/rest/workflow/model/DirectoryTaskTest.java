package org.folio.rest.workflow.model;

import static org.folio.spring.test.mock.MockMvcConstant.VALUE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.util.ReflectionTestUtils.getField;
import static org.springframework.test.util.ReflectionTestUtils.setField;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

import org.folio.rest.workflow.enums.DirectoryAction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class DirectoryTaskTest {

  @Mock
  private EmbeddedVariable embeddedVariable;

  private Set<EmbeddedVariable> inputVariables;

  private DirectoryTask directoryTask;

  @BeforeEach
  void beforeEach() {
    directoryTask = new DirectoryTask();
    inputVariables = new HashSet<>();
    inputVariables.add(embeddedVariable);
  }

  @Test
  void getIdWorksTest() {
    setField(directoryTask, "id", VALUE);

    assertEquals(VALUE, directoryTask.getId());
  }

  @Test
  void setIdWorksTest() {
    setField(directoryTask, "id", null);

    directoryTask.setId(VALUE);
    assertEquals(VALUE, getField(directoryTask, "id"));
  }

  @Test
  void getNameWorksTest() {
    setField(directoryTask, "name", VALUE);

    assertEquals(VALUE, directoryTask.getName());
  }

  @Test
  void setNameWorksTest() {
    setField(directoryTask, "name", null);

    directoryTask.setName(VALUE);
    assertEquals(VALUE, getField(directoryTask, "name"));
  }

  @Test
  void getDescriptionWorksTest() {
    setField(directoryTask, "description", VALUE);

    assertEquals(VALUE, directoryTask.getDescription());
  }

  @Test
  void setDescriptionWorksTest() {
    setField(directoryTask, "description", null);

    directoryTask.setDescription(VALUE);
    assertEquals(VALUE, getField(directoryTask, "description"));
  }

  @Test
  void getDeserializeAsWorksTest() {
    setField(directoryTask, "deserializeAs", VALUE);

    assertEquals(VALUE, directoryTask.getDeserializeAs());
  }

  @Test
  void setDeserializeAsWorksTest() {
    setField(directoryTask, "deserializeAs", null);

    directoryTask.setDeserializeAs(VALUE);
    assertEquals(VALUE, getField(directoryTask, "deserializeAs"));
  }

  @Test
  void getInputVariablesWorksTest() {
    setField(directoryTask, "inputVariables", inputVariables);

    assertEquals(inputVariables, directoryTask.getInputVariables());
  }

  @Test
  void setInputVariablesWorksTest() {
    setField(directoryTask, "inputVariables", null);

    directoryTask.setInputVariables(inputVariables);
    assertEquals(inputVariables, getField(directoryTask, "inputVariables"));
  }

  @Test
  void getOutputVariableWorksTest() {
    setField(directoryTask, "outputVariable", embeddedVariable);

    assertEquals(embeddedVariable, directoryTask.getOutputVariable());
  }

  @Test
  void setOutputVariableWorksTest() {
    setField(directoryTask, "outputVariable", null);

    directoryTask.setOutputVariable(embeddedVariable);
    assertEquals(embeddedVariable, getField(directoryTask, "outputVariable"));
  }

  @Test
  void getAsyncBeforeWorksTest() {
    setField(directoryTask, "asyncBefore", true);

    assertEquals(true, directoryTask.getAsyncBefore());
  }

  @Test
  void setAsyncBeforeWorksTest() {
    setField(directoryTask, "asyncBefore", false);

    directoryTask.setAsyncBefore(true);
    assertEquals(true, getField(directoryTask, "asyncBefore"));
  }

  @Test
  void getAsyncAfterWorksTest() {
    setField(directoryTask, "asyncAfter", true);

    assertEquals(true, directoryTask.getAsyncAfter());
  }

  @Test
  void setAsyncAfterWorksTest() {
    setField(directoryTask, "asyncAfter", false);

    directoryTask.setAsyncAfter(true);
    assertEquals(true, getField(directoryTask, "asyncAfter"));
  }

  @Test
  void getPathWorksTest() {
    setField(directoryTask, "path", VALUE);

    assertEquals(VALUE, directoryTask.getPath());
  }

  @Test
  void setPathWorksTest() {
    setField(directoryTask, "path", null);

    directoryTask.setPath(VALUE);
    assertEquals(VALUE, getField(directoryTask, "path"));
  }

  @Test
  void setActionWorksTest() {
    setField(directoryTask, "action", DirectoryAction.LIST);

    directoryTask.setAction(DirectoryAction.LIST);
    assertEquals(DirectoryAction.LIST, directoryTask.getAction());
  }

  @Test
  void getActionWorksTest() {
    setField(directoryTask, "action", DirectoryAction.LIST);

    assertEquals(DirectoryAction.LIST, directoryTask.getAction());
  }

  @Test
  void getWorkflowWorksTest() {
    setField(directoryTask, "workflow", VALUE);

    assertEquals(VALUE, directoryTask.getWorkflow());
  }

  @Test
  void setWorkflowWorksTest() {
    setField(directoryTask, "workflow", null);

    directoryTask.setWorkflow(VALUE);
    assertEquals(VALUE, getField(directoryTask, "workflow"));
  }

  @Test
  void prePersistDefaultWorksTest() {
    setField(directoryTask, "path", null);
    setField(directoryTask, "workflow", null);
    setField(directoryTask, "action", null);

    directoryTask.prePersist();

    assertEquals("", directoryTask.getPath());
    assertEquals("", directoryTask.getWorkflow());
    assertEquals(DirectoryAction.LIST, directoryTask.getAction());
  }

  @ParameterizedTest
  @MethodSource("providePrePersistFor")
  void prePersistWorksTest(Map<String, Object> initial, Map<String, Object> expected) {
    initial.forEach((String attribute, Object value) -> {
      setField(directoryTask, attribute, value);
    });

    directoryTask.prePersist();

    expected.forEach((String attribute, Object value) -> {
      assertEquals(value, getField(directoryTask, attribute));
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
        helperFieldMap(null, null, null), 
        helperFieldMap("", "", DirectoryAction.LIST)
      ),
      Arguments.of(
        helperFieldMap(VALUE, null, null), 
        helperFieldMap(VALUE, "", DirectoryAction.LIST)
      ),
      Arguments.of(
        helperFieldMap(null, VALUE, null), 
        helperFieldMap("", VALUE, DirectoryAction.LIST)
      ),
      Arguments.of(
        helperFieldMap(null, null, DirectoryAction.DELETE_NEXT), 
        helperFieldMap("", "", DirectoryAction.DELETE_NEXT)
      )
    );
  }

  /**
   * Helper method for reducing inline code repetition.
   *
   * @param path      Initial value for 'path' field
   * @param workflow  Initial value for 'workflow' field
   * @param action    Initial value for 'action' field
   * @return          Map of the provided values
   */
  private static Map<String, Object> helperFieldMap(String path, String workflow, DirectoryAction action) {
    Map<String, Object> map = new HashMap<>();
    map.put("path", path);
    map.put("workflow", workflow);
    map.put("action", action);
    return map;
  }

}