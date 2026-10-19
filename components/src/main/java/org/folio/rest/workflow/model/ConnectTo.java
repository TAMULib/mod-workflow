package org.folio.rest.workflow.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.PrePersist;
import org.folio.rest.workflow.model.components.Navigation;

@Entity
public class ConnectTo extends Node implements Navigation {

  @Column(nullable = false)
  private String nodeId;

  public ConnectTo() {
    super();

    nodeId = "";
  }

  @Override
  @PrePersist
  public void prePersist() {
    super.prePersist();

    if (nodeId == null) {
      nodeId = "";
    }
  }

  @Override
  public String getNodeId() {
    return nodeId;
  }

  @Override
  public void setNodeId(String nodeId) {
    this.nodeId = nodeId;
  }

}
