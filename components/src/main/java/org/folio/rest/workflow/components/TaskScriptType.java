package org.folio.rest.workflow.components;

public enum TaskScriptType {
  NONE(null),
  GROOVY("groovy"),
  JAVA("java"),
  JS("js"),
  PYTHON("py"),
  RUBY("rb"),
  XSLT("xslt");

  public final String engineName;
 
  private TaskScriptType(String engineName) {
      this.engineName = engineName;
  }

}