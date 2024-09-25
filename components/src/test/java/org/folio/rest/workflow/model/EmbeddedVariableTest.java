package org.folio.rest.workflow.model;

import static org.folio.spring.test.mock.MockMvcConstant.VALUE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.util.ReflectionTestUtils.getField;
import static org.springframework.test.util.ReflectionTestUtils.setField;

import org.folio.rest.workflow.enums.VariableType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class EmbeddedVariableTest {

  private EmbeddedVariable embeddedVariable;

  @BeforeEach
  void beforeEach() {
    embeddedVariable = new EmbeddedVariable();
  }

  @Test
  void getKeyWorksTest() {
    setField(embeddedVariable, "key", VALUE);

    assertEquals(VALUE, embeddedVariable.getKey());
  }

  @Test
  void setKeyWorksTest() {
    setField(embeddedVariable, "key", null);

    embeddedVariable.setKey(VALUE);
    assertEquals(VALUE, getField(embeddedVariable, "key"));
  }

  @Test
  void getTypeWorksTest() {
    setField(embeddedVariable, "type", VariableType.PROCESS);

    assertEquals(VariableType.PROCESS, embeddedVariable.getType());
  }

  @Test
  void setTypeWorksTest() {
    setField(embeddedVariable, "type", null);

    embeddedVariable.setType(VariableType.PROCESS);
    assertEquals(VariableType.PROCESS, getField(embeddedVariable, "type"));
  }

  @Test
  void getSpinWorksTest() {
    setField(embeddedVariable, "spin", true);

    assertEquals(true, embeddedVariable.getSpin());
  }

  @Test
  void setSpinWorksTest() {
    setField(embeddedVariable, "spin", false);

    embeddedVariable.setSpin(true);
    assertEquals(true, getField(embeddedVariable, "spin"));
  }

  @Test
  void getAsJsonWorksTest() {
    setField(embeddedVariable, "asJson", true);

    assertEquals(true, embeddedVariable.getAsJson());
  }

  @Test
  void setAsJsonWorksTest() {
    setField(embeddedVariable, "asJson", false);

    embeddedVariable.setAsJson(true);
    assertEquals(true, getField(embeddedVariable, "asJson"));
  }

  @Test
  void getAsTransientWorksTest() {
    setField(embeddedVariable, "asTransient", true);

    assertEquals(true, embeddedVariable.getAsTransient());
  }

  @Test
  void setAsTransientWorksTest() {
    setField(embeddedVariable, "asTransient", false);

    embeddedVariable.setAsTransient(true);
    assertEquals(true, getField(embeddedVariable, "asTransient"));
  }

  @Test
  void getAsSecureWorksTest() {
    setField(embeddedVariable, "asSecure", true);

    assertEquals(true, embeddedVariable.getAsSecure());
  }

  @Test
  void setAsSecureWorksTest() {
    setField(embeddedVariable, "asSecure", false);

    embeddedVariable.setAsSecure(true);
    assertEquals(true, getField(embeddedVariable, "asSecure"));
  }

}
