package org.folio.rest.workflow.model;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class EnhancementComparison {

  @Column(nullable = false)
  private String sourceProperty;

  @Column(nullable = false)
  private String targetProperty;

  public EnhancementComparison() {
    super();
  }

  public String getSourceProperty() {
    return sourceProperty;
  }

  public String getTargetProperty() {
    return targetProperty;
  }

  public void setSourceProperty(String sourceProperty) {
    this.sourceProperty = sourceProperty;
  }

  public void setTargetProperty(String targetProperty) {
    this.targetProperty = targetProperty;
  }

}