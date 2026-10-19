package org.folio.rest.workflow.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import org.folio.rest.workflow.model.has.common.HasEmbeddedLoopReferenceCommon;
import org.hibernate.annotations.ColumnDefault;

@Embeddable
public class EmbeddedLoopReference implements HasEmbeddedLoopReferenceCommon {

  @Column(nullable = true)
  private String cardinalityExpression;

  @Column(nullable = true)
  private String completeConditionExpression;

  @Column(nullable = true)
  private String dataInputRefExpression;

  @Column(nullable = true)
  private String inputDataName;

  // Must be designated as nullable even if this is not supposed to be NULL.
  @Column(nullable = true)
  @ColumnDefault("false")
  private Boolean parallel;

  public EmbeddedLoopReference() {
    super();

    parallel = false;
  }

  /**
   * Perform pre-persist setup to ensure good state.
   *
   * Embeddables do not utilize @PrePersist annotation due to it not working well in certain circumstances.
   * Therefore, this must be manually called by the class utilizing this class.
   */
  public void prePersist() {
    if (parallel == null) {
      parallel = false;
    }
  }

  @Override
  public boolean hasCardinalityExpression() {
    return cardinalityExpression != null;
  }

  @Override
  public boolean hasCompleteConditionExpression() {
    return completeConditionExpression != null;
  }

  @Override
  public boolean hasDataInput() {
    return dataInputRefExpression != null && inputDataName != null;
  }

  @Override
  public String getCardinalityExpression() {
    return cardinalityExpression;
  }

  @Override
  public String getCompleteConditionExpression() {
    return completeConditionExpression;
  }

  @Override
  public String getDataInputRefExpression() {
    return dataInputRefExpression;
  }

  @Override
  public String getInputDataName() {
    return inputDataName;
  }

  @Override
  public Boolean getParallel() {
    return parallel;
  }

  @Override
  public void setCardinalityExpression(String cardinalityExpression) {
    this.cardinalityExpression = cardinalityExpression;
  }

  @Override
  public void setCompleteConditionExpression(String completeConditionExpression) {
    this.completeConditionExpression = completeConditionExpression;
  }

  @Override
  public void setDataInputRefExpression(String dataInputRefExpression) {
    this.dataInputRefExpression = dataInputRefExpression;
  }

  @Override
  public void setInputDataName(String inputDataName) {
    this.inputDataName = inputDataName;
  }

  @Override
  public void setParallel(Boolean parallel) {
    this.parallel = parallel;
  }

}
