package org.folio.rest.workflow.model;

import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;
import org.folio.rest.workflow.model.components.DelegateTask;
import org.folio.rest.workflow.model.has.common.HasProcessorTaskCommon;

@Entity
public class ProcessorTask extends AbstractTask implements DelegateTask, HasProcessorTaskCommon {

  @Getter
  @Setter
  @Embedded
  private EmbeddedProcessor processor;

  public ProcessorTask() {
    super();

    // @Embeddable with @PrePersist do not consistently call PrePersist and so this must be manually triggered.
    if (processor != null) {
      processor.prePersist();
    }
  }

}
