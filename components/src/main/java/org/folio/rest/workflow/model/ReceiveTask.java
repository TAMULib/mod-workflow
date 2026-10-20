package org.folio.rest.workflow.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.PrePersist;
import org.folio.rest.workflow.model.components.Wait;

@Entity
public class ReceiveTask extends AbstractTask implements Wait {

  @Column(columnDefinition = "TEXT", nullable = false)
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
