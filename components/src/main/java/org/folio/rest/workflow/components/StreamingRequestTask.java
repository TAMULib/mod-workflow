package org.folio.rest.workflow.components;

import java.util.List;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;

@Entity
public class StreamingRequestTask extends Task {

  @ElementCollection
  private List<Request> requests;

  public StreamingRequestTask() {
    super();
    setDelegate("streamingRequestDelegate");
  }

  public List<Request> getRequests() {
    return requests;
  }

  public void setRequests(List<Request> requests) {
    this.requests = requests;
  }

}