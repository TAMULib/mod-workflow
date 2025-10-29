package org.folio.rest.workflow.model.has.common;

import org.folio.rest.workflow.enums.VariableType;

/**
 * This interface provides common methods for {@link org.folio.rest.workflow.model.EmbeddedVariable EmbeddedVariable}.
 */
public interface HasEmbeddedVariableCommon {

  public Boolean getAsArray();
  public Boolean getAsJson();
  public Boolean getAsTransient();
  public VariableType getType();
  public String getKey();
  public Boolean getSpin();

  public void setAsArray(Boolean asArray);
  public void setAsJson(Boolean asJson);
  public void setAsTransient(Boolean asTransient);
  public void setType(VariableType type);
  public void setKey(String key);
  public void setSpin(Boolean spin);

}
