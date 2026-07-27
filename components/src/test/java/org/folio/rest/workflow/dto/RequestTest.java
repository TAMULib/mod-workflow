package org.folio.rest.workflow.dto;

import static org.folio.spring.test.mock.MockMvcConstant.VALUE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.util.ReflectionTestUtils.getField;
import static org.springframework.test.util.ReflectionTestUtils.setField;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpMethod;

class RequestTest {

  private static final String ACCEPT        = "accept";
  private static final String BODYTEMPLATE  = "bodyTemplate";
  private static final String CONTENTTYPE   = "contentType";
  private static final String ITERABLE      = "iterable";
  private static final String METHOD        = "method";
  private static final String RESPONSEKEY   = "responseKey";
  private static final String SENDEMPTYBODY = "sendEmptyBody";
  private static final String URL           = "url";

  private Request request;

  @BeforeEach
  void beforeEach() {
    request = new Impl();
  }

  @Test
  void getUrlWorksTest() {
    setField(request, URL, VALUE);

    assertEquals(VALUE, request.getUrl());
  }

  @Test
  void setUrlWorksTest() {
    setField(request, URL, null);

    request.setUrl(VALUE);
    assertEquals(VALUE, getField(request, URL));
  }

  @Test
  void getMethodWorksTest() {
    setField(request, METHOD, HttpMethod.DELETE);

    assertEquals(HttpMethod.DELETE, request.getMethod());
  }

  @Test
  void setMethodWorksTest() {
    setField(request, METHOD, null);

    request.setMethod(HttpMethod.DELETE);
    assertEquals(HttpMethod.DELETE, getField(request, METHOD));
  }

  @Test
  void getContentTypeWorksTest() {
    setField(request, CONTENTTYPE, VALUE);

    assertEquals(VALUE, request.getContentType());
  }

  @Test
  void setContentTypeWorksTest() {
    setField(request, CONTENTTYPE, null);

    request.setContentType(VALUE);
    assertEquals(VALUE, getField(request, CONTENTTYPE));
  }

  @Test
  void getAcceptWorksTest() {
    setField(request, ACCEPT, VALUE);

    assertEquals(VALUE, request.getAccept());
  }

  @Test
  void setAcceptWorksTest() {
    setField(request, ACCEPT, null);

    request.setAccept(VALUE);
    assertEquals(VALUE, getField(request, ACCEPT));
  }

  @Test
  void getBodyTemplateWorksTest() {
    setField(request, BODYTEMPLATE, VALUE);

    assertEquals(VALUE, request.getBodyTemplate());
  }

  @Test
  void setBodyTemplateWorksTest() {
    setField(request, BODYTEMPLATE, null);

    request.setBodyTemplate(VALUE);
    assertEquals(VALUE, getField(request, BODYTEMPLATE));
  }

  @Test
  void getIterableWorksTest() {
    setField(request, ITERABLE, true);

    assertEquals(true, request.isIterable());
  }

  @Test
  void setIterableWorksTest() {
    setField(request, ITERABLE, false);

    request.setIterable(true);
    assertEquals(true, getField(request, ITERABLE));
  }

  @Test
  void getResponseKeyWorksTest() {
    setField(request, RESPONSEKEY, VALUE);

    assertEquals(VALUE, request.getResponseKey());
  }

  @Test
  void setResponseKeyWorksTest() {
    setField(request, RESPONSEKEY, null);

    request.setResponseKey(VALUE);
    assertEquals(VALUE, getField(request, RESPONSEKEY));
  }

  @Test
  void getSendEmptyBodyWorksTest() {
    final boolean value = false;

    setField(request, SENDEMPTYBODY, value);

    assertEquals(value, request.getSendEmptyBody());
  }

  @Test
  void setSendEmptyBodyWorksTest() {
    final boolean value = true;

    setField(request, SENDEMPTYBODY, null);

    request.setSendEmptyBody(value);
    assertEquals(value, getField(request, SENDEMPTYBODY));
  }

  private static class Impl extends Request { }

}
