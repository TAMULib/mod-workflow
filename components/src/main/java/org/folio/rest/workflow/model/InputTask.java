package org.folio.rest.workflow.model;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.PrePersist;
import java.util.HashSet;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;

@Entity
public class InputTask extends AbstractTask {

  @Getter
  @Setter
  @ElementCollection
  private Set<EmbeddedInput> inputs;

  public InputTask() {
    super();

    inputs = new HashSet<>();
  }

  @Override
  @PrePersist
  public void prePersist() {
    super.prePersist();

    if (inputs == null) {
      inputs = new HashSet<>();
    } else {
      // @Embeddable with @PrePersist do not consistently call PrePersist and so this must be manually triggered.
      inputs.forEach((EmbeddedInput ei) -> {
        if (ei != null) ei.prePersist();
      });
    }
  }

}
