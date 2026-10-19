package org.folio.rest.workflow.model;

import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.PrePersist;
import org.folio.rest.workflow.model.components.DelegateTask;
import org.folio.rest.workflow.model.has.common.HasProcessorTaskCommon;

@Entity
public class ProcessorTask extends AbstractTask implements DelegateTask, HasProcessorTaskCommon {

  @Embedded
  private EmbeddedProcessor processor;

  public ProcessorTask() {
    super();

    processor = new EmbeddedProcessor();
  }

  @Override
  @PrePersist
  public void prePersist() {
    super.prePersist();

    // @Embeddable with @PrePersist do not consistently call PrePersist and so this must be manually triggered.
    if (processor != null) {
      processor.prePersist();
    }
  }

  @Override
  public EmbeddedProcessor getProcessor() {
    return processor;
  }

  @Override
  public void setProcessor(EmbeddedProcessor processor) {
    this.processor = processor;
  }

}
