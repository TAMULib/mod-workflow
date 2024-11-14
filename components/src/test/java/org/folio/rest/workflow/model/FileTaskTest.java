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

import org.folio.rest.workflow.enums.FileOp;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class FileTaskTest {

  @Mock
  private EmbeddedVariable embeddedVariable;

  private Set<EmbeddedVariable> inputVariables;

  private FileTask fileTask;

  @BeforeEach
  void beforeEach() {
    fileTask = new FileTask();
    inputVariables = new HashSet<>();
    inputVariables.add(embeddedVariable);
  }

  @Test
  void getIdWorksTest() {
    setField(fileTask, "id", VALUE);

    assertEquals(VALUE, fileTask.getId());
  }

  @Test
  void setIdWorksTest() {
    setField(fileTask, "id", null);

    fileTask.setId(VALUE);
    assertEquals(VALUE, getField(fileTask, "id"));
  }

  @Test
  void getNameWorksTest() {
    setField(fileTask, "name", VALUE);

    assertEquals(VALUE, fileTask.getName());
  }

  @Test
  void setNameWorksTest() {
    setField(fileTask, "name", null);

    fileTask.setName(VALUE);
    assertEquals(VALUE, getField(fileTask, "name"));
  }

  @Test
  void getDescriptionWorksTest() {
    setField(fileTask, "description", VALUE);

    assertEquals(VALUE, fileTask.getDescription());
  }

  @Test
  void setDescriptionWorksTest() {
    setField(fileTask, "description", null);

    fileTask.setDescription(VALUE);
    assertEquals(VALUE, getField(fileTask, "description"));
  }

  @Test
  void getDeserializeAsWorksTest() {
    setField(fileTask, "deserializeAs", VALUE);

    assertEquals(VALUE, fileTask.getDeserializeAs());
  }

  @Test
  void setDeserializeAsWorksTest() {
    setField(fileTask, "deserializeAs", null);

    fileTask.setDeserializeAs(VALUE);
    assertEquals(VALUE, getField(fileTask, "deserializeAs"));
  }

  @Test
  void getInputVariablesWorksTest() {
    setField(fileTask, "inputVariables", inputVariables);

    assertEquals(inputVariables, fileTask.getInputVariables());
  }

  @Test
  void setInputVariablesWorksTest() {
    setField(fileTask, "inputVariables", null);

    fileTask.setInputVariables(inputVariables);
    assertEquals(inputVariables, getField(fileTask, "inputVariables"));
  }

  @Test
  void getOutputVariableWorksTest() {
    setField(fileTask, "outputVariable", embeddedVariable);

    assertEquals(embeddedVariable, fileTask.getOutputVariable());
  }

  @Test
  void setOutputVariableWorksTest() {
    setField(fileTask, "outputVariable", null);

    fileTask.setOutputVariable(embeddedVariable);
    assertEquals(embeddedVariable, getField(fileTask, "outputVariable"));
  }

  @Test
  void getAsyncBeforeWorksTest() {
    setField(fileTask, "asyncBefore", true);

    assertEquals(true, fileTask.getAsyncBefore());
  }

  @Test
  void setAsyncBeforeWorksTest() {
    setField(fileTask, "asyncBefore", false);

    fileTask.setAsyncBefore(true);
    assertEquals(true, getField(fileTask, "asyncBefore"));
  }

  @Test
  void getAsyncAfterWorksTest() {
    setField(fileTask, "asyncAfter", true);

    assertEquals(true, fileTask.getAsyncAfter());
  }

  @Test
  void setAsyncAfterWorksTest() {
    setField(fileTask, "asyncAfter", false);

    fileTask.setAsyncAfter(true);
    assertEquals(true, getField(fileTask, "asyncAfter"));
  }

  @Test
  void getOpWorksTest() {
    setField(fileTask, "op", FileOp.DELETE);

    assertEquals(FileOp.DELETE, fileTask.getOp());
  }

  @Test
  void setOpWorksTest() {
    setField(fileTask, "op", null);

    fileTask.setOp(FileOp.DELETE);
    assertEquals(FileOp.DELETE, getField(fileTask, "op"));
  }

  @Test
  void getPathWorksTest() {
    setField(fileTask, "path", VALUE);

    assertEquals(VALUE, fileTask.getPath());
  }

  @Test
  void setPathWorksTest() {
    setField(fileTask, "path", null);

    fileTask.setPath(VALUE);
    assertEquals(VALUE, getField(fileTask, "path"));
  }

  @Test
  void getTargetWorksTest() {
    setField(fileTask, "target", VALUE);

    assertEquals(VALUE, fileTask.getTarget());
  }

  @Test
  void setTargetWorksTest() {
    setField(fileTask, "target", null);

    fileTask.setTarget(VALUE);
    assertEquals(VALUE, getField(fileTask, "target"));
  }

  @Test
  void getLineWorksTest() {
    setField(fileTask, "line", VALUE);

    assertEquals(VALUE, fileTask.getLine());
  }

  @Test
  void setLineWorksTest() {
    setField(fileTask, "line", null);

    fileTask.setLine(VALUE);
    assertEquals(VALUE, getField(fileTask, "line"));
  }

  @ParameterizedTest
  @MethodSource("providePrePersistFor")
  void prePersistWorksTest(Map<String, Object> initial, Map<String, Object> expected) {
    initial.forEach((String attribute, Object value) -> {
      setField(fileTask, attribute, value);
    });

    fileTask.prePersist();

    expected.forEach((String attribute, Object value) -> {
      assertEquals(value, getField(fileTask, attribute));
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
        helperFieldMap(null,             NULL_STR),
        helperFieldMap(FileOp.READ,      "")
      ),
      Arguments.of(
        helperFieldMap(FileOp.WRITE,     NULL_STR),
        helperFieldMap(FileOp.WRITE,     "")
      ),
      Arguments.of(
        helperFieldMap(null,             VALUE),
        helperFieldMap(FileOp.READ,      VALUE)
      ),
      Arguments.of(
        helperFieldMap(FileOp.READ_LINE, VALUE),
        helperFieldMap(FileOp.READ_LINE, VALUE)
      )
    );
  }

  /**
   * Helper for reducing inline code repititon for assignments.
   *
   * @param op The op value.
   * @param path The path value.
   *
   * @return The built arguments map.
   */
  private static Map<String, Object> helperFieldMap(FileOp op, String path ) {
    final Map<String, Object> map = new HashMap<>();

    map.put("op", op);
    map.put("path", path);

    return map;
  }

}
