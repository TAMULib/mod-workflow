package org.folio.rest.workflow.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.PrePersist;
import jakarta.validation.constraints.NotNull;
import org.folio.rest.workflow.enums.HttpMethod;
import org.folio.rest.workflow.model.has.HasUrl;
import org.folio.rest.workflow.model.has.common.HasEmbeddedRequestCommon;
import org.springframework.http.MediaType;

@Embeddable
public class EmbeddedRequest implements HasEmbeddedRequestCommon, HasUrl {

  // Must be designated as nullable even if this is not supposed to be NULL.
  @NotNull
  @Column(nullable = true)
  private String accept;

  @Column(columnDefinition = "TEXT", nullable = true)
  private String bodyTemplate;

  // Must be designated as nullable even if this is not supposed to be NULL.
  @NotNull
  @Column(nullable = true)
  private String contentType;

  private boolean iterable;

  private String iterableKey;

  // Must be designated as nullable even if this is not supposed to be NULL.
  @NotNull
  @Column(nullable = true)
  @Enumerated(EnumType.STRING)
  private HttpMethod method;

  private String responseKey;

  @Column(nullable = false)
  private Boolean sendEmptyBody;

  // Must be designated as nullable even if this is not supposed to be NULL.
  @NotNull
  @Column(nullable = true)
  private String url;

  public EmbeddedRequest() {
    super();

    accept = MediaType.APPLICATION_JSON_VALUE;
    bodyTemplate = null;
    contentType = MediaType.APPLICATION_JSON_VALUE;
    iterable = false;
    method = HttpMethod.GET;
    sendEmptyBody = true;
    url = "";
  }

  @PrePersist
  public void prePersist() {
    if (accept == null) {
      accept = MediaType.APPLICATION_JSON_VALUE;
    }

    if (sendEmptyBody == null) {
      sendEmptyBody = true;
    }

    if (bodyTemplate == null || bodyTemplate.isEmpty()) {
      bodyTemplate = Boolean.TRUE.equals(sendEmptyBody) ? "{}" : null;
    }

    if (contentType == null) {
      contentType = MediaType.APPLICATION_JSON_VALUE;
    }

    if (method == null) {
      method = HttpMethod.GET;
    }

    if (url == null) {
      url = "";
    }
  }

  @Override
  public String getAccept() {
    return accept;
  }

  @Override
  public String getBodyTemplate() {
    return bodyTemplate;
  }

  @Override
  public String getContentType() {
    return contentType;
  }

  @Override
  public boolean isIterable() {
    return iterable;
  }

  @Override
  public String getIterableKey() {
    return iterableKey;
  }

  @Override
  public Boolean getSendEmptyBody() {
    return sendEmptyBody;
  }

  @Override
  public void setAccept(String accept) {
    this.accept = accept;
  }

  @Override
  public void setBodyTemplate(String bodyTemplate) {
    this.bodyTemplate = bodyTemplate;
  }

  @Override
  public void setContentType(String contentType) {
    this.contentType = contentType;
  }

  @Override
  public void setIterable(boolean iterable) {
    this.iterable = iterable;
  }

  @Override
  public void setIterableKey(String iterableKey) {
    this.iterableKey = iterableKey;
  }

  @Override
  public HttpMethod getMethod() {
    return method;
  }

  @Override
  public String getResponseKey() {
    return responseKey;
  }

  @Override
  public void setMethod(HttpMethod method) {
    this.method = method;
  }

  @Override
  public void setResponseKey(String responseKey) {
    this.responseKey = responseKey;
  }

  @Override
  public String getUrl() {
    return url;
  }

  @Override
  public void setUrl(String url) {
    this.url = url;
  }

  @Override
  public void setSendEmptyBody(Boolean sendEmptyBody) {
    this.sendEmptyBody = sendEmptyBody;
  }

}
