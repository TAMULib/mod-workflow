package org.folio.rest.workflow.components;

import javax.persistence.Entity;

@Entity
public class StreamingFileReadTask extends Task {

  String path;

  String workflow;

  Long delay;

  public StreamingFileReadTask() {
    super();
    setDelegate("streamingFileReadDelegate");
    setDelay(0L);
    setStreaming(true);
  }

  public StreamingFileReadTask(String name) {
    this();
    setName(name);
  }

  public String getPath() {
    return path;
  }

  public void setPath(String path) {
    this.path = path;
  }

  public String getWorkflow() {
    return workflow;
  }

  public void setWorkflow(String workflow) {
    this.workflow = workflow;
  }

  public Long getDelay() {
    return delay;
  }

  public void setDelay(Long delay) {
    this.delay = delay;
  }

}
