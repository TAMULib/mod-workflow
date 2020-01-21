package org.folio.rest.workflow.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.validation.constraints.NotNull;

import org.folio.rest.workflow.components.Task;

@Entity
public class RequestTask extends Node implements Task {

  @NotNull
  @Embedded
  private Request request;

  @ElementCollection
  private Set<Variable> inputVariables;

  @Embedded
  private Variable outputVariable;

  @Column(nullable = false)
  private boolean asyncBefore;

  @Column(nullable = false)
  private boolean asyncAfter;

  public RequestTask() {
    super();
    inputVariables = new HashSet<Variable>();
    asyncBefore = false;
    asyncAfter = false;
  }

  public Request getRequest() {
    return request;
  }

  public void setRequest(Request request) {
    this.request = request;
  }

  public Set<Variable> getInputVariables() {
    return inputVariables;
  }

  public void setInputVariables(Set<Variable> inputVariables) {
    this.inputVariables = inputVariables;
  }

  public Variable getOutputVariable() {
    return outputVariable;
  }

  public void setOutputVariable(Variable outputVariable) {
    this.outputVariable = outputVariable;
  }

  public boolean isAsyncBefore() {
    return asyncBefore;
  }

  public void setAsyncBefore(boolean asyncBefore) {
    this.asyncBefore = asyncBefore;
  }

  public boolean isAsyncAfter() {
    return asyncAfter;
  }

  public void setAsyncAfter(boolean asyncAfter) {
    this.asyncAfter = asyncAfter;
  }

}