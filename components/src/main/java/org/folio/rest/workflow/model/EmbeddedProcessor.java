package org.folio.rest.workflow.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.PrePersist;
import org.folio.rest.workflow.enums.ScriptType;
import org.folio.rest.workflow.model.has.common.HasEmbeddedProcessorCommon;
import org.hibernate.annotations.ColumnDefault;

@Embeddable
public class EmbeddedProcessor implements HasEmbeddedProcessorCommon {

  // Must be designated as nullable even if this is not supposed to be NULL.
  @Column(nullable = true)
  @ColumnDefault("0")
  private Integer buffer;

  // Must be designated as nullable even if this is not supposed to be NULL.
  @Column(columnDefinition = "TEXT default ''", nullable = true)
  private String code;

  // Must be designated as nullable even if this is not supposed to be NULL.
  @Column(nullable = true)
  @ColumnDefault("0")
  private Integer delay;

  // Must be designated as nullable even if this is not supposed to be NULL.
  @Column(nullable = true)
  private String functionName;

  // Must be designated as nullable even if this is not supposed to be NULL.
  @Column(columnDefinition = "TEXT default 'js'", nullable = true)
  @Enumerated(EnumType.STRING)
  private ScriptType scriptType;

  public EmbeddedProcessor() {
    super();

    buffer = 0;
    code = "";
    delay = 0;
    functionName = "";
    scriptType = ScriptType.JS;
  }

  /**
   * Perform pre-persist setup to ensure good state.
   *
   * Embeddables do not utilize @PrePersist annotation due to it not working well in certain circumstances.
   * Therefore, this must be manually called by the class utilizing this class.
   */
  @PrePersist
  public void prePersist() {
    if (buffer == null) {
      buffer = 0;
    }

    if (code == null) {
      code = "";
    }

    if (delay == null) {
      delay = 0;
    }

    if (functionName == null) {
      functionName = "";
    }

    if (scriptType == null) {
      scriptType = ScriptType.JS;
    }
  }

  @Override
  public Integer getBuffer() {
    return buffer;
  }

  @Override
  public String getCode() {
    return code;
  }

  @Override
  public Integer getDelay() {
    return delay;
  }

  @Override
  public String getFunctionName() {
    return functionName;
  }

  @Override
  public ScriptType getScriptType() {
    return scriptType;
  }

  @Override
  public void setBuffer(Integer buffer) {
    this.buffer = buffer;
  }

  @Override
  public void setCode(String code) {
    this.code = code;
  }

  @Override
  public void setDelay(Integer delay) {
    this.delay = delay;
  }

  @Override
  public void setFunctionName(String functionName) {
    this.functionName = functionName;
  }

  @Override
  public void setScriptType(ScriptType scriptType) {
    this.scriptType = scriptType;
  }

}
