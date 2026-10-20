package org.folio.rest.workflow.model;

import static org.folio.rest.workflow.enums.StartEventType.MESSAGE_CORRELATION;
import static org.folio.rest.workflow.enums.StartEventType.SCHEDULED;
import static org.folio.spring.test.mock.MockMvcConstant.VALUE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.util.ReflectionTestUtils.getField;
import static org.springframework.test.util.ReflectionTestUtils.setField;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;
import org.folio.rest.workflow.enums.StartEventType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class StartEventTest {

  private static final String ASYNCBEFORE   = "asyncBefore";
  private static final String DESCRIPTION   = "description";
  private static final String DESERIALIZEAS = "deserializeAs";
  private static final String ID            = "id";
  private static final String INTERRUPTING  = "interrupting";
  private static final String NAME          = "name";
  private static final String TYPE          = "type";

  private StartEvent startEvent;

  @BeforeEach
  void beforeEach() {
    startEvent = new Impl();
  }

  @Test
  void getIdWorksTest() {
    setField(startEvent, ID, VALUE);

    assertEquals(VALUE, startEvent.getId());
  }

  @Test
  void setIdWorksTest() {
    setField(startEvent, ID, null);

    startEvent.setId(VALUE);
    assertEquals(VALUE, getField(startEvent, ID));
  }

  @Test
  void getNameWorksTest() {
    setField(startEvent, NAME, VALUE);

    assertEquals(VALUE, startEvent.getName());
  }

  @Test
  void setNameWorksTest() {
    setField(startEvent, NAME, null);

    startEvent.setName(VALUE);
    assertEquals(VALUE, getField(startEvent, NAME));
  }

  @Test
  void getDescriptionWorksTest() {
    setField(startEvent, DESCRIPTION, VALUE);

    assertEquals(VALUE, startEvent.getDescription());
  }

  @Test
  void setDescriptionWorksTest() {
    setField(startEvent, DESCRIPTION, null);

    startEvent.setDescription(VALUE);
    assertEquals(VALUE, getField(startEvent, DESCRIPTION));
  }

  @Test
  void getDeserializeAsWorksTest() {
    setField(startEvent, DESERIALIZEAS, VALUE);

    assertEquals(VALUE, startEvent.getDeserializeAs());
  }

  @Test
  void setDeserializeAsWorksTest() {
    setField(startEvent, DESERIALIZEAS, null);

    startEvent.setDeserializeAs(VALUE);
    assertEquals(VALUE, getField(startEvent, DESERIALIZEAS));
  }

  @Test
  void getTypeWorksTest() {
    setField(startEvent, TYPE, MESSAGE_CORRELATION);

    assertEquals(MESSAGE_CORRELATION, startEvent.getType());
  }

  @Test
  void setTypeWorksTest() {
    setField(startEvent, TYPE, null);

    startEvent.setType(MESSAGE_CORRELATION);
    assertEquals(MESSAGE_CORRELATION, getField(startEvent, TYPE));
  }

  @Test
  void getExpressionWorksTest() {
    setField(startEvent, "expression", VALUE);

    assertEquals(VALUE, startEvent.getExpression());
  }

  @Test
  void setExpressionWorksTest() {
    setField(startEvent, "expression", null);

    startEvent.setExpression(VALUE);
    assertEquals(VALUE, getField(startEvent, "expression"));
  }

  @Test
  void getInterruptingWorksTest() {
    setField(startEvent, INTERRUPTING, true);

    assertEquals(true, startEvent.getInterrupting());
  }

  @Test
  void setInterruptingWorksTest() {
    setField(startEvent, INTERRUPTING, false);

    startEvent.setInterrupting(true);
    assertEquals(true, getField(startEvent, INTERRUPTING));
  }

  @Test
  void getAsyncBeforeWorksTest() {
    setField(startEvent, ASYNCBEFORE, true);

    assertEquals(true, startEvent.getAsyncBefore());
  }

  @Test
  void setAsyncBeforeWorksTest() {
    setField(startEvent, ASYNCBEFORE, false);

    startEvent.setAsyncBefore(true);
    assertEquals(true, getField(startEvent, ASYNCBEFORE));
  }

  @ParameterizedTest
  @MethodSource("providePrePersistFor")
  void prePersistWorksTest(Map<String, Object> initial, Map<String, Object> expected) {
    initial.forEach((String attribute, Object value) -> {
      setField(startEvent, attribute, value);
    });

    startEvent.prePersist();

    expected.forEach((String attribute, Object value) -> {
      assertEquals(value, getField(startEvent, attribute));
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
      helperFieldMap(null,  null,  null),
      helperFieldMap(false, false, MESSAGE_CORRELATION)
    ));

    arguments.add(Arguments.of(
      helperFieldMap(true,  null,  null),
      helperFieldMap(true,  false, MESSAGE_CORRELATION)
    ));

    arguments.add(Arguments.of(
      helperFieldMap(null,  true,  null),
      helperFieldMap(false, true,  MESSAGE_CORRELATION)
    ));

    arguments.add(Arguments.of(
      helperFieldMap(null,  null,  SCHEDULED),
      helperFieldMap(false, false, SCHEDULED)
    ));

    return arguments.stream();
  }

  /**
   * Helper for reducing in line code repetition for assignments.
   *
   * @param asyncBefore The asyncBefore value.
   * @param interrupting The interrupting value.
   * @param type The type value.
   *
   * @return The built arguments map.
   */
  private static Map<String, Object> helperFieldMap(Boolean asyncBefore, Boolean interrupting, StartEventType type) {
    final Map<String, Object> map = new HashMap<>();

    map.put(ASYNCBEFORE, asyncBefore);
    map.put(INTERRUPTING, interrupting);
    map.put(TYPE, type);

    return map;
  }

  private static class Impl extends StartEvent { }

}
