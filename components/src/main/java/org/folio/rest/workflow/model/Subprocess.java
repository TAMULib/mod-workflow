package org.folio.rest.workflow.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.PrePersist;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.folio.rest.workflow.enums.SubprocessType;
import org.folio.rest.workflow.model.components.Branch;
import org.folio.rest.workflow.model.components.MultiInstance;

@Entity
public class Subprocess extends AbstractProcess implements Branch, MultiInstance {

  @Getter
  @Setter
  @Embedded
  private EmbeddedLoopReference loopRef;

  @Getter
  @Setter
  @NotNull
  @Column(nullable = false)
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

}
