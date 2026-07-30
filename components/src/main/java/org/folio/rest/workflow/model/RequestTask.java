package org.folio.rest.workflow.model;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.PrePersist;
import java.util.HashSet;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;
import org.folio.rest.workflow.model.components.DelegateTask;
import org.folio.rest.workflow.model.has.common.HasRequestTaskCommon;

@Entity
public class RequestTask extends AbstractTask implements DelegateTask, HasRequestTaskCommon {

  @Getter
  @Setter
  @ElementCollection
  private Set<EmbeddedVariable> headerOutputVariables;

  @Getter
  @Setter
  @Embedded
  private EmbeddedRequest request;

  public RequestTask() {
    super();

    headerOutputVariables = new HashSet<>();
  }

  @Override
  @PrePersist
  public void prePersist() {
    super.prePersist();

    if (headerOutputVariables == null) {
      headerOutputVariables = new HashSet<>();
    } else {
      // @Embeddable with @PrePersist do not consistently call PrePersist and so this must be manually triggered.
      headerOutputVariables.forEach((EmbeddedVariable ev) -> {
        if (ev != null) ev.prePersist();
      });
    }
  }


}
