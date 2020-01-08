package org.folio.rest.workflow.components;

import javax.persistence.Entity;

@Entity
public class StreamingFileWriteTask extends Task {

  String path;

  String workflow;

  public StreamingFileWriteTask() {
    super();
    setDelegate("streamingFileWriteDelegate");
    setStreaming(true);
  }

  public StreamingFileWriteTask(String name) {
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

}
