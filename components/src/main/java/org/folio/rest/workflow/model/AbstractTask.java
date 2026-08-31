package org.folio.rest.workflow.model;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import java.util.HashSet;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;
import org.folio.rest.workflow.model.components.Task;
import org.folio.rest.workflow.model.converter.EmbeddedVariableConverter;
import org.folio.rest.workflow.model.has.HasInputOutput;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

/**
 * Provides a superclass for any Node implementing a DelegateTask.
 *
 * This is intended to reduce repitition of getters and setters needed by the Task.
 */
@MappedSuperclass
public abstract class AbstractTask extends Node implements HasInputOutput, Task {

  @Getter
  @Setter
  @Column(nullable = false)
  @ColumnDefault("false")
  private Boolean asyncAfter;

  @Getter
  @Setter
  @Column(nullable = false)
  @ColumnDefault("false")
  private Boolean asyncBefore;

  @Getter
  @Setter
  @ElementCollection
  @OnDelete(action = OnDeleteAction.CASCADE)
  private Set<EmbeddedVariable> inputVariables;

  @Getter
  @Setter
  @Column(columnDefinition = "TEXT", nullable = true)
  @Convert(converter = EmbeddedVariableConverter.class)
  private EmbeddedVariable outputVariable;

  AbstractTask() {
    super();

    asyncAfter = false;
    asyncBefore = false;
    inputVariables = new HashSet<>();
  }

  @Override
  @PrePersist
  public void prePersist() {
    super.prePersist();

    if (asyncAfter == null) {
      asyncAfter = false;
    }

    if (asyncBefore == null) {
      asyncBefore = false;
    }

    if (inputVariables == null) {
      inputVariables = new HashSet<>();
    } else {
      // @Embeddable with @PrePersist do not consistently call PrePersist and so this must be manually triggered.
      inputVariables.forEach((EmbeddedVariable ev) -> {
        if (ev != null) ev.prePersist();
      });
    }

    // @Embeddable with @PrePersist do not consistently call PrePersist and so this must be manually triggered.
    if (outputVariable != null) {
      outputVariable.prePersist();
    }
  }

}
