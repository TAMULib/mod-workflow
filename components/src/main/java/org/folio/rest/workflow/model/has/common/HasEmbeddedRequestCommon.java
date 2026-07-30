package org.folio.rest.workflow.model.has.common;

import org.folio.rest.workflow.enums.HttpMethod;

/**
 * This interface provides common methods for {@link org.folio.rest.workflow.model.EmbeddedRequest EmbeddedRequest}.
 */
public interface HasEmbeddedRequestCommon {

  public String getAccept();
  public String getBodyTemplate();
  public String getContentType();
  public String getIterableKey();
  public HttpMethod getMethod();
  public String getResponseKey();
  public Boolean getSendEmptyBody();
  public boolean isIterable();

  public void setAccept(String accept);
  public void setBodyTemplate(String bodyTemplate);
  public void setContentType(String contentType);
  public void setIterable(boolean iterable);
  public void setIterableKey(String iterableKey);
  public void setMethod(HttpMethod method);
  public void setResponseKey(String responseKey);
  public void setSendEmptyBody(Boolean sendEmptyBody);

}
