package org.folio.rest.model;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotNull;

import org.springframework.http.HttpMethod;

@Embeddable
public class Action {

  @Column
  @NotNull
  private String interfaceName;

  @Column
  @NotNull
  private String pathPattern;

  @Column
  @NotNull
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

  public String getInterfaceName() {
    return interfaceName;
  }

  public void setInterfaceName(String interfaceName) {
    this.interfaceName = interfaceName;
  }

  public String getPathPattern() {
    return pathPattern;
  }

  public void setPathPattern(String pathPattern) {
    this.pathPattern = pathPattern;
  }

  public HttpMethod getMethod() {
    return method;
  }

  public void setMethod(HttpMethod method) {
    this.method = method;
  }

}
