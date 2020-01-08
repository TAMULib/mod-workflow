package org.folio.rest.workflow.components;

import javax.persistence.Embeddable;

import org.springframework.http.MediaType;

@Embeddable
public class Request {

  private String storageDestination;

  private String sourceProperty;

  private String contentType;

  private String accept;

  public Request() {
    this.contentType = MediaType.APPLICATION_JSON_VALUE;
    this.accept = MediaType.APPLICATION_JSON_VALUE;
  }

  public String getStorageDestination() {
    return storageDestination;
  }

  public void setStorageDestination(String storageDestination) {
    this.storageDestination = storageDestination;
  }

  public String getSourceProperty() {
    return sourceProperty;
  }

  public void setSourceProperty(String sourceProperty) {
    this.sourceProperty = sourceProperty;
  }

  public String getContentType() {
    return contentType;
  }

  public void setContentType(String contentType) {
    this.contentType = contentType;
  }

  public String getAccept() {
    return accept;
  }

  public void setAccept(String accept) {
    this.accept = accept;
  }

}
