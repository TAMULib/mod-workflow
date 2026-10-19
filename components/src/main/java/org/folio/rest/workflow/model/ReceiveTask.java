package org.folio.rest.workflow.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.PrePersist;
import jakarta.validation.constraints.Size;
import org.folio.rest.workflow.model.components.Wait;

@Entity
public class ReceiveTask extends AbstractTask implements Wait {

  @Size(min = 4, max = 256)
  @Column(nullable = false)
  private String message;

  public ReceiveTask() {
    super();

    message = "";
  }

  @Override
  @PrePersist
  public void prePersist() {
    super.prePersist();

    if (message == null) {
      message = "";
    }
  }

  @Override
  public String getMessage() {
    return message;
  }

  @Override
  public void setMessage(String message) {
    this.message = message;
  }

}
