package org.folio.rest.workflow.model;

import static org.folio.spring.test.mock.MockMvcConstant.VALUE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.springframework.test.util.ReflectionTestUtils.getField;
import static org.springframework.test.util.ReflectionTestUtils.setField;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
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
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class FolioRequestTaskTest {

  private static final String ASYNCAFTER            = "asyncAfter";
  private static final String ASYNCBEFORE           = "asyncBefore";
  private static final String DESCRIPTION           = "description";
  private static final String DESERIALIZEAS         = "deserializeAs";
  private static final String HEADEROUTPUTVARIABLES = "headerOutputVariables";
  private static final String ID                    = "id";
  private static final String INPUTVARIABLES        = "inputVariables";
  private static final String NAME                  = "name";
  private static final String OUTPUTVARIABLE        = "outputVariable";
  private static final String REQUEST               = "request";

  @Mock
  private EmbeddedVariable embeddedVariable;

  @Mock
  private EmbeddedRequest embeddedRequest;

  private Set<EmbeddedVariable> inputVariables;

  private FolioRequestTask task;

  @BeforeEach
  void beforeEach() {
    task = new FolioRequestTask();
    inputVariables = new HashSet<>();
    inputVariables.add(embeddedVariable);
  }

  @Test
  void getIdWorksTest() {
    setField(task, ID, VALUE);

    assertEquals(VALUE, task.getId());
  }

  @Test
  void setIdWorksTest() {
    setField(task, ID, null);

    task.setId(VALUE);
    assertEquals(VALUE, getField(task, ID));
  }

  @Test
  void getNameWorksTest() {
    setField(task, NAME, VALUE);

    assertEquals(VALUE, task.getName());
  }

  @Test
  void setNameWorksTest() {
    setField(task, NAME, null);

    task.setName(VALUE);
    assertEquals(VALUE, getField(task, NAME));
  }

  @Test
  void getDescriptionWorksTest() {
    setField(task, DESCRIPTION, VALUE);

    assertEquals(VALUE, task.getDescription());
  }

  @Test
  void setDescriptionWorksTest() {
    setField(task, DESCRIPTION, null);

    task.setDescription(VALUE);
    assertEquals(VALUE, getField(task, DESCRIPTION));
  }

  @Test
  void getDeserializeAsWorksTest() {
    setField(task, DESERIALIZEAS, VALUE);

    assertEquals(VALUE, task.getDeserializeAs());
  }

  @Test
  void setDeserializeAsWorksTest() {
    setField(task, DESERIALIZEAS, null);

    task.setDeserializeAs(VALUE);
    assertEquals(VALUE, getField(task, DESERIALIZEAS));
  }

  @Test
  void getInputVariablesWorksTest() {
    setField(task, INPUTVARIABLES, inputVariables);

    assertEquals(inputVariables, task.getInputVariables());
  }

  @Test
  void setInputVariablesWorksTest() {
    setField(task, INPUTVARIABLES, null);

    task.setInputVariables(inputVariables);
    assertEquals(inputVariables, getField(task, INPUTVARIABLES));
  }

  @Test
  void getOutputVariableWorksTest() {
    setField(task, OUTPUTVARIABLE, embeddedVariable);

    assertEquals(embeddedVariable, task.getOutputVariable());
  }

  @Test
  void setOutputVariableWorksTest() {
    setField(task, OUTPUTVARIABLE, null);

    task.setOutputVariable(embeddedVariable);
    assertEquals(embeddedVariable, getField(task, OUTPUTVARIABLE));
  }

  @Test
  void getAsyncBeforeWorksTest() {
    setField(task, ASYNCBEFORE, true);

    assertEquals(true, task.getAsyncBefore());
  }

  @Test
  void setAsyncBeforeWorksTest() {
    setField(task, ASYNCBEFORE, false);

    task.setAsyncBefore(true);
    assertEquals(true, getField(task, ASYNCBEFORE));
  }

  @Test
  void getAsyncAfterWorksTest() {
    setField(task, ASYNCAFTER, true);

    assertEquals(true, task.getAsyncAfter());
  }

  @Test
  void setAsyncAfterWorksTest() {
    setField(task, ASYNCAFTER, false);

    task.setAsyncAfter(true);
    assertEquals(true, getField(task, ASYNCAFTER));
  }

  @Test
  void getHeaderOutputVariablesWorksTest() {
    setField(task, HEADEROUTPUTVARIABLES, inputVariables);

    assertEquals(inputVariables, task.getHeaderOutputVariables());
  }

  @Test
  void setHeaderOutputVariablesWorksTest() {
    setField(task, HEADEROUTPUTVARIABLES, null);

    task.setHeaderOutputVariables(inputVariables);
    assertEquals(inputVariables, getField(task, HEADEROUTPUTVARIABLES));
  }

  @Test
  void getRequestWorksTest() {
    setField(task, REQUEST, embeddedRequest);

    assertEquals(embeddedRequest, task.getRequest());
  }

  @Test
  void setRequestWorksTest() {
    setField(task, REQUEST, null);

    task.setRequest(embeddedRequest);
    assertEquals(embeddedRequest, getField(task, REQUEST));
  }

  @SuppressWarnings("unchecked")
  @ParameterizedTest
  @MethodSource("providePrePersistFor")
  void prePersistWorksTest(Map<String, Object> initial, Map<String, Object> expected, Map<String, Boolean> persist) {

    initial.forEach((String attribute, Object value) -> {
      setField(task, attribute, value);
    });

    task.prePersist();

    expected.forEach((String attribute, Object value) -> {
      if (attribute == HEADEROUTPUTVARIABLES) {
        final Set<EmbeddedVariable> eps = (Set<EmbeddedVariable>) value;

        assertNotNull(getField(task, attribute));
        assertEquals(eps.size(), ((Set<EmbeddedVariable>) getField(task, attribute)).size());

        eps.forEach(ep -> {
          if (ep != null) {
            if (Boolean.TRUE.equals(persist.get(attribute))) {
              verify(ep).prePersist();
            } else if (Boolean.FALSE.equals(persist.get(attribute))) {
              verify(ep, never()).prePersist();
            }
          }
        });
      } else {
        assertEquals(value, getField(task, attribute));
      }
    });
  }

  @ParameterizedTest
  @MethodSource("provideSendEmptyBodyFor")
  void prePersistSendEmptyBodyWorksTest(final Boolean sendEmptyBody, final String bodyTemplate, final String expected) {

    final EmbeddedRequest er = new EmbeddedRequest();

    setField(er, "sendEmptyBody", sendEmptyBody);
    setField(er, "bodyTemplate", bodyTemplate);

    er.prePersist();

    assertEquals(expected, getField(er, "bodyTemplate"));
  }

  /**
   * Helper function for parameterized tests for the prePersist function.
   *
   * @return
   *   The arguments array stream with the stream columns as:
   *     - Arguments initial The initial values.
   *     - Arguments expect  The expected values.
   *     - Arguments persist Boolean representing whether or not this will call prePersist() on the object and if so, then true/false depending on verify.
   */
  private static Stream<Arguments> providePrePersistFor() {

    final EmbeddedVariable variable = Mockito.spy(new EmbeddedVariable());
    final EmbeddedVariable nullValue = null;
    final Set<EmbeddedVariable> headerOutputVariables = Set.of(variable);
    final Set<EmbeddedVariable> headerOutputVariablesEmpty = Set.of();
    final Set<EmbeddedVariable> headerOutputVariablesNull = new HashSet<>();

    headerOutputVariablesNull.add(nullValue);

    return List.of(
      Arguments.of(
        helperFieldMap(null),
        helperFieldMap(headerOutputVariablesEmpty),
        helperPersistMap(false)
      ),
      Arguments.of(
        helperFieldMap(headerOutputVariablesEmpty),
        helperFieldMap(headerOutputVariablesEmpty),
        helperPersistMap(false)
      ),
      Arguments.of(
        helperFieldMap(headerOutputVariablesNull),
        helperFieldMap(headerOutputVariablesNull),
        helperPersistMap(false)
      ),
      Arguments.of(
        helperFieldMap(headerOutputVariables),
        helperFieldMap(headerOutputVariables),
        helperPersistMap(true)
      )
    ).stream();
  }

  /**
   * Helper function for parameterized tests for the prePersist function.
   *
   * @return
   *   The arguments array stream with the stream columns as:
   *     - Arguments sendEmptyBody The initial sendEmptyBody value.
   *     - Arguments bodyTemplate The initial bodyTemplate value.
   *     - Arguments expect The expected bodyTemplate value.
   */
  private static Stream<Arguments> provideSendEmptyBodyFor() {

    return List.of(
      Arguments.of(true,  null,  "{}"),
      Arguments.of(true,  "",    "{}"),
      Arguments.of(false, null,  null),
      Arguments.of(false, "",    null),
      Arguments.of(true,  VALUE, VALUE),
      Arguments.of(false, VALUE, VALUE)
    ).stream();
  }

  /**
   * Helper for reducing in line code repetition for assignments.
   *
   * @param headerOutputVariables The headerOutputVariables value.
   *
   * @return The built arguments map.
   */
  private static Map<String, Object> helperFieldMap(Set<EmbeddedVariable> headerOutputVariables) {

    final Map<String, Object> map = new HashMap<>();

    map.put(HEADEROUTPUTVARIABLES, headerOutputVariables);

    return map;
  }

  /**
   * Helper for reducing in line code repetition for assignments for persist setting.
   *
   * @param headerOutputVariables The headerOutputVariables persist value.
   *
   * @return The built persist map.
   */
  private static Map<String, Object> helperPersistMap(Boolean headerOutputVariables) {

    final Map<String, Object> map = new HashMap<>();

    map.put(HEADEROUTPUTVARIABLES, headerOutputVariables);

    return map;
  }

}
