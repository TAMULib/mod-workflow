package org.folio.rest.workflow.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.PrePersist;
import org.folio.rest.workflow.model.has.HasCode;
import org.folio.rest.workflow.model.has.common.HasScriptTaskCommon;

@Entity
public class ScriptTask extends AbstractTask implements HasCode, HasScriptTaskCommon {

  @Column(columnDefinition = "TEXT", nullable = false)
  private String code;

  @Column(nullable = true)
  private String resultVariable;

  @Column(nullable = false)
  private String scriptFormat;

  public ScriptTask() {
    super();

    code = "";
    scriptFormat = "javascript";
  }

  @Override
  @PrePersist
  public void prePersist() {
    super.prePersist();

    if (code == null) {
      code = "";
    }

    if (scriptFormat == null) {
      scriptFormat = "javascript";
    }
  }

  public boolean hasResultVariable() {
    return resultVariable != null;
  }

  @Override
  public String getResultVariable() {
    return resultVariable;
  }

  @Override
  public String getScriptFormat() {
    return scriptFormat;
  }

  @Override
  public void setResultVariable(String resultVariable) {
    this.resultVariable = resultVariable;
  }

  @Override
  public void setScriptFormat(String scriptFormat) {
    this.scriptFormat = scriptFormat;
  }

  @Override
  public String getCode() {
    return code;
  }

  @Override
  public void setCode(String code) {
    this.code = code;
  }

}
