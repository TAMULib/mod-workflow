package org.folio.rest.workflow.dto;

import static org.folio.spring.test.mock.MockMvcConstant.VALUE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.util.ReflectionTestUtils.getField;
import static org.springframework.test.util.ReflectionTestUtils.setField;

import org.folio.rest.workflow.enums.HttpMethod;
import org.folio.rest.workflow.model.Trigger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TriggerDtoTest {

  private TriggerDto triggerDto;

  @BeforeEach
  void beforeEach() {
    triggerDto = new Impl();
  }

  @Test
  void getIdWorksTest() {
    setField(triggerDto, "id", VALUE);

    assertEquals(VALUE, triggerDto.getId());
  }

  @Test
  void setIdWorksTest() {
    setField(triggerDto, "id", null);

    triggerDto.setId(VALUE);
    assertEquals(VALUE, getField(triggerDto, "id"));
  }

  @Test
  void getNameWorksTest() {
    setField(triggerDto, "name", VALUE);

    assertEquals(VALUE, triggerDto.getName());
  }

  @Test
  void setNameWorksTest() {
    setField(triggerDto, "name", null);

    triggerDto.setName(VALUE);
    assertEquals(VALUE, getField(triggerDto, "name"));
  }

  @Test
  void getDescriptionWorksTest() {
    setField(triggerDto, "description", VALUE);

    assertEquals(VALUE, triggerDto.getDescription());
  }

  @Test
  void setDescriptionWorksTest() {
    setField(triggerDto, "description", null);

    triggerDto.setDescription(VALUE);
    assertEquals(VALUE, getField(triggerDto, "description"));
  }

  @Test
  void getMethodWorksTest() {
    setField(triggerDto, "method", HttpMethod.DELETE);

    assertEquals(HttpMethod.DELETE, triggerDto.getMethod());
  }

  @Test
  void setMethodWorksTest() {
    setField(triggerDto, "method", null);

    triggerDto.setMethod(HttpMethod.DELETE);
    assertEquals(HttpMethod.DELETE, getField(triggerDto, "method"));
  }

  @Test
  void getPathPatternWorksTest() {
    setField(triggerDto, "pathPattern", VALUE);

    assertEquals(VALUE, triggerDto.getPathPattern());
  }

  @Test
  void setPathPatternWorksTest() {
    setField(triggerDto, "pathPattern", null);

    triggerDto.setPathPattern(VALUE);
    assertEquals(VALUE, getField(triggerDto, "pathPattern"));
  }

  private static class Impl extends Trigger implements TriggerDto { }

}
