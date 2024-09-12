package org.folio.rest.workflow.model;

import static org.folio.spring.test.mock.MockMvcConstant.VALUE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.util.ReflectionTestUtils.getField;
import static org.springframework.test.util.ReflectionTestUtils.setField;

import java.util.HashSet;
import java.util.Set;
import org.folio.rest.workflow.enums.DatabaseResultType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class DatabaseQueryTaskTest {

  @Mock
  private EmbeddedVariable embeddedVariable;

  private Set<EmbeddedVariable> inputVariables;

  private DatabaseQueryTask databaseQueryTask;

  @BeforeEach
  void beforeEach() {
    databaseQueryTask = new DatabaseQueryTask();
    inputVariables = new HashSet<>();
    inputVariables.add(embeddedVariable);
  }

  @Test
  void getIdWorksTest() {
    setField(databaseQueryTask, "id", VALUE);

    assertEquals(VALUE, databaseQueryTask.getId());
  }

  @Test
  void setIdWorksTest() {
    setField(databaseQueryTask, "id", null);

    databaseQueryTask.setId(VALUE);
    assertEquals(VALUE, getField(databaseQueryTask, "id"));
  }

  @Test
  void getNameWorksTest() {
    setField(databaseQueryTask, "name", VALUE);

    assertEquals(VALUE, databaseQueryTask.getName());
  }

  @Test
  void setNameWorksTest() {
    setField(databaseQueryTask, "name", null);

    databaseQueryTask.setName(VALUE);
    assertEquals(VALUE, getField(databaseQueryTask, "name"));
  }

  @Test
  void getDescriptionWorksTest() {
    setField(databaseQueryTask, "description", VALUE);

    assertEquals(VALUE, databaseQueryTask.getDescription());
  }

  @Test
  void setDescriptionWorksTest() {
    setField(databaseQueryTask, "description", null);

    databaseQueryTask.setDescription(VALUE);
    assertEquals(VALUE, getField(databaseQueryTask, "description"));
  }

  @Test
  void getDeserializeAsWorksTest() {
    setField(databaseQueryTask, "deserializeAs", VALUE);

    assertEquals(VALUE, databaseQueryTask.getDeserializeAs());
  }

  @Test
  void setDeserializeAsWorksTest() {
    setField(databaseQueryTask, "deserializeAs", null);

    databaseQueryTask.setDeserializeAs(VALUE);
    assertEquals(VALUE, getField(databaseQueryTask, "deserializeAs"));
  }

  @Test
  void getInputVariablesWorksTest() {
    setField(databaseQueryTask, "inputVariables", inputVariables);

    assertEquals(inputVariables, databaseQueryTask.getInputVariables());
  }

  @Test
  void setInputVariablesWorksTest() {
    setField(databaseQueryTask, "inputVariables", null);

    databaseQueryTask.setInputVariables(inputVariables);
    assertEquals(inputVariables, getField(databaseQueryTask, "inputVariables"));
  }

  @Test
  void getOutputVariableWorksTest() {
    setField(databaseQueryTask, "outputVariable", embeddedVariable);

    assertEquals(embeddedVariable, databaseQueryTask.getOutputVariable());
  }

  @Test
  void setOutputVariableWorksTest() {
    setField(databaseQueryTask, "outputVariable", null);

    databaseQueryTask.setOutputVariable(embeddedVariable);
    assertEquals(embeddedVariable, getField(databaseQueryTask, "outputVariable"));
  }

  @Test
  void getAsyncBeforeWorksTest() {
    setField(databaseQueryTask, "asyncBefore", true);

    assertEquals(true, databaseQueryTask.getAsyncBefore());
  }

  @Test
  void setAsyncBeforeWorksTest() {
    setField(databaseQueryTask, "asyncBefore", false);

    databaseQueryTask.setAsyncBefore(true);
    assertEquals(true, getField(databaseQueryTask, "asyncBefore"));
  }

  @Test
  void getAsyncAfterWorksTest() {
    setField(databaseQueryTask, "asyncAfter", true);

    assertEquals(true, databaseQueryTask.getAsyncAfter());
  }

  @Test
  void setAsyncAfterWorksTest() {
    setField(databaseQueryTask, "asyncAfter", false);

    databaseQueryTask.setAsyncAfter(true);
    assertEquals(true, getField(databaseQueryTask, "asyncAfter"));
  }

  @Test
  void getDesignationWorksTest() {
    setField(databaseQueryTask, "designation", VALUE);

    assertEquals(VALUE, databaseQueryTask.getDesignation());
  }

  @Test
  void setDesignationWorksTest() {
    setField(databaseQueryTask, "designation", null);

    databaseQueryTask.setDesignation(VALUE);
    assertEquals(VALUE, getField(databaseQueryTask, "designation"));
  }

  @Test
  void getOutputPathWorksTest() {
    setField(databaseQueryTask, "outputPath", VALUE);

    assertEquals(VALUE, databaseQueryTask.getOutputPath());
  }

  @Test
  void setOutputPathWorksTest() {
    setField(databaseQueryTask, "outputPath", null);

    databaseQueryTask.setOutputPath(VALUE);
    assertEquals(VALUE, getField(databaseQueryTask, "outputPath"));
  }

  @Test
  void getQueryWorksTest() {
    setField(databaseQueryTask, "query", VALUE);

    assertEquals(VALUE, databaseQueryTask.getQuery());
  }

  @Test
  void setQueryWorksTest() {
    setField(databaseQueryTask, "query", null);

    databaseQueryTask.setQuery(VALUE);
    assertEquals(VALUE, getField(databaseQueryTask, "query"));
  }

  @Test
  void getResultTypeWorksTest() {
    setField(databaseQueryTask, "resultType", DatabaseResultType.CSV);

    assertEquals(DatabaseResultType.CSV, databaseQueryTask.getResultType());
  }

  @Test
  void setResultTypeWorksTest() {
    setField(databaseQueryTask, "resultType", null);

    databaseQueryTask.setResultType(DatabaseResultType.CSV);
    assertEquals(DatabaseResultType.CSV, getField(databaseQueryTask, "resultType"));
  }

  @Test
  void getIncludeHeaderWorksTest() {
    setField(databaseQueryTask, "includeHeader", true);

    assertEquals(true, databaseQueryTask.getIncludeHeader());
  }

  @Test
  void setIncludeHeaderWorksTest() {
    setField(databaseQueryTask, "includeHeader", false);

    databaseQueryTask.setIncludeHeader(true);
    assertEquals(true, getField(databaseQueryTask, "includeHeader"));
  }

}
