package org.folio.rest.workflow.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderColumn;
import jakarta.persistence.PrePersist;
import java.util.ArrayList;
import java.util.List;
import org.folio.rest.workflow.model.components.Branch;
import org.folio.rest.workflow.model.has.common.HasMoveToNodeCommon;

@Entity
public class MoveToNode extends Node implements Branch, HasMoveToNodeCommon {

  @Column(nullable = false)
  private String gatewayId;

  @OneToMany
  @OrderColumn
  private List<Node> nodes;

  public MoveToNode() {
    super();

    gatewayId = "";
    nodes = new ArrayList<>();
  }

  @Override
  @PrePersist
  public void prePersist() {
    super.prePersist();

    if (gatewayId == null) {
      gatewayId = "";
    }

    if (nodes == null) {
      nodes = new ArrayList<>();
    }
  }

  @Override
  public List<Node> getNodes() {
    return nodes;
  }

  @Override
  public void setNodes(List<Node> nodes) {
    this.nodes = nodes;
  }

  @Override
  public String getGatewayId() {
    return gatewayId;
  }

  @Override
  public void setGatewayId(String gatewayId) {
    this.gatewayId = gatewayId;
  }

}
