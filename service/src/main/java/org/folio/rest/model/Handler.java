package org.folio.rest.model;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpMethod;

public class Handler {

  private List<String> methods;

  private String pathPattern;

  private List<String> permissionsRequired;

  private List<String> permissionsDesired;

  public Handler() {
    super();
  }

  public List<String> getMethods() {
    return methods;
  }

  public void setMethods(List<String> methods) {
    this.methods = methods;
  }

  public String getPathPattern() {
    return pathPattern;
  }

  public void setPathPattern(String pathPattern) {
    this.pathPattern = pathPattern;
  }

  public List<String> getPermissionsRequired() {
    return permissionsRequired;
  }

  public void setPermissionsRequired(List<String> permissionsRequired) {
    this.permissionsRequired = permissionsRequired;
  }

  public List<String> getPermissionsDesired() {
    return permissionsDesired;
  }

  public void setPermissionsDesired(List<String> permissionsDesired) {
    this.permissionsDesired = permissionsDesired;
  }

  public List<Action> getActionByInterface(String interfaceName) {
    List<Action> actions = new ArrayList<>();
    methods.forEach(method -> actions.add(new Action(interfaceName, pathPattern, HttpMethod.valueOf(method))));
    return actions;
  }

}
