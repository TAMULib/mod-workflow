package org.folio.rest.workflow.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.PrePersist;
import org.folio.rest.workflow.enums.StartEventType;
import org.folio.rest.workflow.model.components.Event;
import org.folio.rest.workflow.model.has.HasAsyncBefore;
import org.folio.rest.workflow.model.has.HasExpression;
import org.folio.rest.workflow.model.has.HasInterrupting;
import org.folio.rest.workflow.model.has.HasStartEventType;
import org.hibernate.annotations.ColumnDefault;

@Entity
public class StartEvent extends Node implements Event, HasAsyncBefore, HasExpression, HasInterrupting, HasStartEventType {

  @Column(nullable = false)
  @ColumnDefault("false")
  private Boolean asyncBefore;

  @Column(columnDefinition = "TEXT", nullable = true)
  private String expression;

  @Column(nullable = false)
  @ColumnDefault("false")
  private Boolean interrupting;

  @Column(nullable = false)
  @ColumnDefault("'MESSAGE_CORRELATION'")
  @Enumerated(EnumType.STRING)
  private StartEventType type;

  public StartEvent() {
    super();

    asyncBefore = false;
    interrupting = false;
    type = StartEventType.MESSAGE_CORRELATION;
  }

  @Override
  @PrePersist
  public void prePersist() {
    super.prePersist();

    if (asyncBefore == null) {
      asyncBefore = false;
    }

    if (interrupting == null) {
      interrupting = false;
    }

    if (type == null) {
      type = StartEventType.MESSAGE_CORRELATION;
    }
  }

  @Override
  public String getExpression() {
    return expression;
  }

  @Override
  public void setExpression(String expression) {
    this.expression = expression;
  }

  @Override
  public Boolean getInterrupting() {
    return interrupting;
  }

  @Override
  public void setInterrupting(Boolean interrupting) {
    this.interrupting = interrupting;
  }

  @Override
  public StartEventType getType() {
    return type;
  }

  @Override
  public void setType(StartEventType type) {
    this.type = type;
  }

  @Override
  public Boolean getAsyncBefore() {
    return asyncBefore;
  }

  @Override
  public void setAsyncBefore(Boolean asyncBefore) {
    this.asyncBefore = asyncBefore;
  }

}
