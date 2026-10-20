package org.folio.rest.workflow.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.PrePersist;
import org.folio.rest.workflow.enums.SubprocessType;
import org.folio.rest.workflow.model.components.Branch;
import org.folio.rest.workflow.model.components.MultiInstance;
import org.folio.rest.workflow.model.has.HasSubProcessType;
import org.hibernate.annotations.ColumnDefault;

@Entity
public class Subprocess extends AbstractProcess implements Branch, HasSubProcessType, MultiInstance {

  @Embedded
  private EmbeddedLoopReference loopRef;

  @Column(nullable = false)
  @ColumnDefault("'EMBEDDED'")
  @Enumerated(EnumType.STRING)
  private SubprocessType type;

  public Subprocess() {
    super();

    type = SubprocessType.EMBEDDED;
  }

  @Override
  @PrePersist
  public void prePersist() {
    super.prePersist();

    if (type == null) {
      type = SubprocessType.EMBEDDED;
    }

    // @Embeddable with @PrePersist do not consistently call PrePersist and so this must be manually triggered.
    if (loopRef != null) {
      loopRef.prePersist();
    }
  }

  @Override
  public EmbeddedLoopReference getLoopRef() {
    return loopRef;
  }

  @Override
  public void setLoopRef(EmbeddedLoopReference loopRef) {
    this.loopRef = loopRef;
  }

  @Override
  public SubprocessType getType() {
    return type;
  }

  @Override
  public void setType(SubprocessType type) {
    this.type = type;
  }

}
