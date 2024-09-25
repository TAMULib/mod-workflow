package org.folio.rest.workflow.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.PrePersist;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.folio.rest.workflow.enums.VariableType;
import org.folio.rest.workflow.model.has.common.HasEmbeddedVariableCommon;
import org.hibernate.annotations.ColumnDefault;

@Embeddable
public class EmbeddedVariable implements HasEmbeddedVariableCommon {

  @Getter
  @Setter
  @NotNull
  @Size(min = 4, max = 64)
  @Column(name = "vkey", nullable = true)
  private String key;

  @Getter
  @Setter
  @Column(nullable = false)
  @Enumerated(EnumType.STRING)
  private VariableType type;

  @Getter
  @Setter
  @Column(nullable = false)
  @ColumnDefault("false")
  private Boolean spin;

  @Getter
  @Setter
  @Column(nullable = false)
  @ColumnDefault("false")
  private Boolean asJson;

  @Getter
  @Setter
  @Column(nullable = false)
  @ColumnDefault("false")
  private Boolean asTransient;

  @Getter
  @Setter
  @Column(nullable = false)
  @ColumnDefault("false")
  private Boolean asSecure;

  public EmbeddedVariable() {
    super();

    asJson = false;
    asTransient = false;
    asSecure = false;
    spin = false;
    type = VariableType.PROCESS;
  }

  @PrePersist
  public void prePersist() {
    if (asJson == null) {
      asJson = false;
    }

    if (asTransient == null) {
      asTransient = false;
    }

    if (asTransient == null) {
      asSecure = false;
    }

    if (spin == null) {
      spin = false;
    }

    if (type == null) {
      type = VariableType.PROCESS;
    }
  }

}
