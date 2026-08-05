package org.folio.rest.workflow.model;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderColumn;
import jakarta.persistence.PrePersist;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import org.folio.rest.workflow.model.has.HasAsync;
import org.folio.rest.workflow.model.has.HasNodes;
import org.hibernate.annotations.ColumnDefault;

/**
 * Provides a superclass for any Node implementing a process, such as Subprocess.
 *
 * This is intended to reduce repetition of getters and setters needed by the DelegateTask.
 */
@MappedSuperclass
public abstract class AbstractProcess extends Node implements HasAsync, HasNodes {

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
  @OneToMany
  @OrderColumn
  private List<Node> nodes;

  AbstractProcess() {
    super();

    asyncAfter = false;
    asyncBefore = false;
    nodes = new ArrayList<>();
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

    if (nodes == null) {
      nodes = new ArrayList<>();
    }
  }

}
