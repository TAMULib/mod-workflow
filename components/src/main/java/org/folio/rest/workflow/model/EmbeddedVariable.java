package org.folio.rest.workflow.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import org.folio.rest.workflow.enums.VariableType;
import org.folio.rest.workflow.model.has.common.HasEmbeddedVariableCommon;
import org.hibernate.annotations.ColumnDefault;

@Embeddable
public class EmbeddedVariable implements HasEmbeddedVariableCommon {

  @Column(name = "vkey", columnDefinition = "TEXT", nullable = true)
  private String key;

  // Must be designated as nullable even if this is not supposed to be NULL.
  @Column(nullable = true)
  @ColumnDefault("'PROCESS'")
  @Enumerated(EnumType.STRING)
  private VariableType type;

  // Must be designated as nullable even if this is not supposed to be NULL.
  @Column(nullable = true)
  @ColumnDefault("false")
  private Boolean spin;

  // Must be designated as nullable even if this is not supposed to be NULL.
  @Column(nullable = true)
  @ColumnDefault("false")
  private Boolean asArray;

  // Must be designated as nullable even if this is not supposed to be NULL.
  @Column(nullable = true)
  @ColumnDefault("false")
  private Boolean asJson;

  // Must be designated as nullable even if this is not supposed to be NULL.
  @Column(nullable = true)
  @ColumnDefault("false")
  private Boolean asTransient;

  public EmbeddedVariable() {
    super();

    asArray = false;
    asJson = false;
    asTransient = false;
    spin = false;
    type = VariableType.PROCESS;
  }

  /**
   * Perform pre-persist setup to ensure good state.
   *
   * Embeddables do not utilize @PrePersist annotation due to it not working well in certain circumstances.
   * Therefore, this must be manually called by the class utilizing this class.
   */
  public void prePersist() {
    if (asArray == null) {
      asArray = false;
    }

    if (asJson == null) {
      asJson = Boolean.TRUE.equals(spin);
    }

    if (asTransient == null) {
      asTransient = false;
    }

    if (spin == null) {
      spin = false;
    }

    if (type == null) {
      type = VariableType.PROCESS;
    }
  }

  @Override
  public Boolean getAsArray() {
    return asArray;
  }

  @Override
  public Boolean getAsJson() {
    return asJson;
  }

  @Override
  public Boolean getAsTransient() {
    return asTransient;
  }

  @Override
  public VariableType getType() {
    return type;
  }

  @Override
  public String getKey() {
    return key;
  }

  @Override
  public Boolean getSpin() {
    return spin;
  }

  @Override
  public void setAsArray(Boolean asArray) {
    this.asArray = asArray;
  }

  @Override
  public void setAsJson(Boolean asJson) {
    this.asJson = asJson;
  }

  @Override
  public void setAsTransient(Boolean asTransient) {
    this.asTransient = asTransient;
  }

  @Override
  public void setType(VariableType type) {
    this.type = type;
  }

  @Override
  public void setKey(String key) {
    this.key = key;
  }

  @Override
  public void setSpin(Boolean spin) {
    this.spin = spin;
  }

}
