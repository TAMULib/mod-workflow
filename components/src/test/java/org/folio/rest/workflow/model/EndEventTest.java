package org.folio.rest.workflow.model;

import static org.folio.spring.test.mock.MockMvcConstant.VALUE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.util.ReflectionTestUtils.getField;
import static org.springframework.test.util.ReflectionTestUtils.setField;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class EndEventTest {

  private EndEvent endEvent;

  @BeforeEach
  void beforeEach() {
    endEvent = new Impl();
  }

  @Test
  void getIdWorksTest() {
    setField(endEvent, "id", VALUE);

    assertEquals(VALUE, endEvent.getId());
  }

  @Test
  void setIdWorksTest() {
    setField(endEvent, "id", null);

    endEvent.setId(VALUE);
    assertEquals(VALUE, getField(endEvent, "id"));
  }

  @Test
  void getNameWorksTest() {
    setField(endEvent, "name", VALUE);

    assertEquals(VALUE, endEvent.getName());
  }

  @Test
  void setNameWorksTest() {
    setField(endEvent, "name", null);

    endEvent.setName(VALUE);
    assertEquals(VALUE, getField(endEvent, "name"));
  }

  @Test
  void getDescriptionWorksTest() {
    setField(endEvent, "description", VALUE);

    assertEquals(VALUE, endEvent.getDescription());
  }

  @Test
  void setDescriptionWorksTest() {
    setField(endEvent, "description", null);

    endEvent.setDescription(VALUE);
    assertEquals(VALUE, getField(endEvent, "description"));
  }

  @Test
  void getDeserializeAsWorksTest() {
    setField(endEvent, "deserializeAs", VALUE);

    assertEquals(VALUE, endEvent.getDeserializeAs());
  }

  @Test
  void setDeserializeAsWorksTest() {
    setField(endEvent, "deserializeAs", null);

    endEvent.setDeserializeAs(VALUE);
    assertEquals(VALUE, getField(endEvent, "deserializeAs"));
  }

  private static class Impl extends EndEvent { }

}
