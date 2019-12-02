package org.folio.rest.workflow.components;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
public class ProcessorTask extends Task {

  @Column(columnDefinition="TEXT")
  private String script;

  private TaskScriptType scriptType;

  public ProcessorTask() {
    super();
    this.setDelegate("streamingProcessDelegate");
  }

  public String getScript() {
    return script;
  }

  public void setScript(String script) {
    this.script = script;
  }

  public TaskScriptType getScriptType() {
    return scriptType;
  }

  public void setScriptType(TaskScriptType scriptType) {
    this.scriptType = scriptType;
  }

}
