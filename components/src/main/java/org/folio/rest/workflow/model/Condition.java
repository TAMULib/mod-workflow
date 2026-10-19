package org.folio.rest.workflow.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.PrePersist;
import jakarta.validation.constraints.Size;
import org.folio.rest.workflow.model.components.Conditional;

@Entity
public class Condition extends Node implements Conditional {

  @Size(min = 2, max = 64)
  @Column(nullable = false)
  private String answer;

  @Size(min = 4, max = 128)
  @Column(nullable = false)
  private String expression;

  public Condition() {
    super();

    answer = "";
    expression = "";
  }

  @Override
  @PrePersist
  public void prePersist() {
    super.prePersist();

    if (answer == null) {
      answer = "";
    }

    if (expression == null) {
      expression = "";
    }
  }

  @Override
  public String getAnswer() {
    return answer;
  }

  @Override
  public void setAnswer(String answer) {
    this.answer = answer;
  }

  @Override
  public String getExpression() {
    return expression;
  }

  @Override
  public void setExpression(String expression) {
    this.expression = expression;
  }

}
