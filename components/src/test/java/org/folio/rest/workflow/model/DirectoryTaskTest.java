package org.folio.rest.workflow.model;

import static org.folio.spring.test.mock.MockMvcConstant.VALUE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.util.ReflectionTestUtils.getField;
import static org.springframework.test.util.ReflectionTestUtils.setField;

import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
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
  void testWorkflowGetterAndSetter() {
      directoryTask.setWorkflow("sampleWorkflow");
      assertEquals("sampleWorkflow", directoryTask.getWorkflow());
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

}
