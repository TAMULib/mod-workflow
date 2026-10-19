package org.folio.rest.workflow.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import org.folio.rest.workflow.enums.HttpMethod;
import org.folio.rest.workflow.has.common.HasActionCommon;
import org.folio.rest.workflow.model.has.HasMethod;
import org.folio.rest.workflow.model.has.HasPathPattern;

@Embeddable
public class Action implements HasActionCommon, HasMethod, HasPathPattern {

  @Column(nullable = false)
  private String interfaceName;

  @Column(nullable = false)
  private String pathPattern;

  @Column(nullable = false)
  @Enumerated(EnumType.STRING)
  private HttpMethod method;

  public Action() {
    super();
  }

  public Action(String interfaceName, String pathPattern, HttpMethod method) {
    this();

    this.interfaceName = interfaceName;
    this.pathPattern = pathPattern;
    this.method = method;
  }

  @Override
  public String getPathPattern() {
    return pathPattern;
  }

  @Override
  public void setPathPattern(String pathPattern) {
    this.pathPattern = pathPattern;
  }

  @Override
  public HttpMethod getMethod() {
    return method;
  }

  @Override
  public void setMethod(HttpMethod method) {
    this.method = method;
  }

  @Override
  public String getInterfaceName() {
    return interfaceName;
  }

  @Override
  public void setInterfaceName(String interfaceName) {
    this.interfaceName = interfaceName;
  }

}
