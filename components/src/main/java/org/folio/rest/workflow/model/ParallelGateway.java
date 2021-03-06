package org.folio.rest.workflow.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.OrderColumn;

import org.folio.rest.workflow.model.components.Gateway;

@Entity
public class ParallelGateway extends Node implements Gateway {

  @OneToMany
  @OrderColumn
  private List<Node> nodes;

  public ParallelGateway() {
    super();
    nodes = new ArrayList<Node>();
  }

  public List<Node> getNodes() {
    return nodes;
  }

  public void setNodes(List<Node> nodes) {
    this.nodes = nodes;
  }

}
