package org.folio.rest.workflow.model;

import static org.folio.rest.workflow.enums.HttpMethod.DELETE;
import static org.folio.rest.workflow.enums.HttpMethod.GET;
import static org.folio.rest.workflow.enums.HttpMethod.POST;
import static org.folio.spring.test.mock.MockMvcConstant.APP_JSON;
import static org.folio.spring.test.mock.MockMvcConstant.JSON_OBJECT;
import static org.folio.spring.test.mock.MockMvcConstant.NULL_STR;
import static org.folio.spring.test.mock.MockMvcConstant.VALUE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.util.ReflectionTestUtils.getField;
import static org.springframework.test.util.ReflectionTestUtils.setField;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;
import org.folio.rest.workflow.enums.HttpMethod;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class EmbeddedRequestTest {

  private static final String ACCEPT        = "accept";
  private static final String BODYTEMPLATE  = "bodyTemplate";
  private static final String CONTENTTYPE   = "contentType";
  private static final String ITERABLE      = "iterable";
  private static final String ITERABLEKEY   = "iterableKey";
  private static final String METHOD        = "method";
  private static final String RESPONSEKEY   = "responseKey";
  private static final String SENDEMPTYBODY = "sendEmptyBody";
  private static final String URL           = "url";

  private EmbeddedRequest embeddedRequest;

  @BeforeEach
  void beforeEach() {
    embeddedRequest = new EmbeddedRequest();
  }

  @Test
  void getUrlWorksTest() {
    setField(embeddedRequest, URL, VALUE);

    assertEquals(VALUE, embeddedRequest.getUrl());
  }

  @Test
  void setUrlWorksTest() {
    setField(embeddedRequest, URL, null);

    embeddedRequest.setUrl(VALUE);
    assertEquals(VALUE, getField(embeddedRequest, URL));
  }

  @Test
  void getMethodWorksTest() {
    setField(embeddedRequest, METHOD, DELETE);

    assertEquals(DELETE, embeddedRequest.getMethod());
  }

  @Test
  void setMethodWorksTest() {
    setField(embeddedRequest, METHOD, null);

    embeddedRequest.setMethod(DELETE);
    assertEquals(DELETE, getField(embeddedRequest, METHOD));
  }

  @Test
  void getContentTypeWorksTest() {
    setField(embeddedRequest, CONTENTTYPE, VALUE);

    assertEquals(VALUE, embeddedRequest.getContentType());
  }

  @Test
  void setContentTypeWorksTest() {
    setField(embeddedRequest, CONTENTTYPE, null);

    embeddedRequest.setContentType(VALUE);
    assertEquals(VALUE, getField(embeddedRequest, CONTENTTYPE));
  }

  @Test
  void getAcceptWorksTest() {
    setField(embeddedRequest, ACCEPT, VALUE);

    assertEquals(VALUE, embeddedRequest.getAccept());
  }

  @Test
  void setAcceptWorksTest() {
    setField(embeddedRequest, ACCEPT, null);

    embeddedRequest.setAccept(VALUE);
    assertEquals(VALUE, getField(embeddedRequest, ACCEPT));
  }

  @Test
  void getBodyTemplateWorksTest() {
    setField(embeddedRequest, BODYTEMPLATE, VALUE);

    assertEquals(VALUE, embeddedRequest.getBodyTemplate());
  }

  @Test
  void setBodyTemplateWorksTest() {
    setField(embeddedRequest, BODYTEMPLATE, null);

    embeddedRequest.setBodyTemplate(VALUE);
    assertEquals(VALUE, getField(embeddedRequest, BODYTEMPLATE));
  }

  @Test
  void isIterableWorksTest() {
    setField(embeddedRequest, ITERABLE, true);

    assertEquals(true, embeddedRequest.isIterable());
  }

  @Test
  void setIterableWorksTest() {
    setField(embeddedRequest, ITERABLE, false);

    embeddedRequest.setIterable(true);
    assertEquals(true, getField(embeddedRequest, ITERABLE));
  }

  @Test
  void getIterableKeyWorksTest() {
    setField(embeddedRequest, ITERABLEKEY, VALUE);

    assertEquals(VALUE, embeddedRequest.getIterableKey());
  }

  @Test
  void setIterableKeyWorksTest() {
    setField(embeddedRequest, ITERABLEKEY, null);

    embeddedRequest.setIterableKey(VALUE);
    assertEquals(VALUE, getField(embeddedRequest, ITERABLEKEY));
  }

  @Test
  void getResponseKeyWorksTest() {
    setField(embeddedRequest, RESPONSEKEY, VALUE);

    assertEquals(VALUE, embeddedRequest.getResponseKey());
  }

  @Test
  void setResponseKeyWorksTest() {
    setField(embeddedRequest, RESPONSEKEY, null);

    embeddedRequest.setResponseKey(VALUE);
    assertEquals(VALUE, getField(embeddedRequest, RESPONSEKEY));
  }

  @Test
  void getSendEmptyBodyWorksTest() {
    final boolean value = false;

    setField(embeddedRequest, SENDEMPTYBODY, value);

    assertEquals(value, embeddedRequest.getSendEmptyBody());
  }

  @Test
  void setSendEmptyBodyWorksTest() {
    final boolean value = true;

    setField(embeddedRequest, SENDEMPTYBODY, null);

    embeddedRequest.setSendEmptyBody(value);
    assertEquals(value, getField(embeddedRequest, SENDEMPTYBODY));
  }

  @ParameterizedTest
  @MethodSource("providePrePersistFor")
  void prePersistWorksTest(Map<String, Object> initial, Map<String, Object> expected) {
    initial.forEach((String attribute, Object value) -> {
      setField(embeddedRequest, attribute, value);
    });

    embeddedRequest.prePersist();

    expected.forEach((String attribute, Object value) -> {
      assertEquals(value, getField(embeddedRequest, attribute));
    });
  }

  /**
   * Helper function for parameterized tests for the prePersist function.
   *
   * @return
   *   The arguments array stream with the stream columns as:
   *     - Arguments initial The initial values.
   *     - Arguments expect The expected values.
   */
  private static Stream<Arguments> providePrePersistFor() {

    return List.of(
      Arguments.of(
        helperFieldMap(NULL_STR, NULL_STR,    NULL_STR, null, null,  NULL_STR),
        helperFieldMap(APP_JSON, JSON_OBJECT, APP_JSON, GET,  true,  "")
      ),
      Arguments.of(
        helperFieldMap(VALUE,    NULL_STR,    NULL_STR, null, null,  NULL_STR),
        helperFieldMap(VALUE,    JSON_OBJECT, APP_JSON, GET,  true,  "")
      ),
      Arguments.of(
        helperFieldMap(NULL_STR, VALUE,       NULL_STR, null, null,  NULL_STR),
        helperFieldMap(APP_JSON, VALUE,       APP_JSON, GET,  true,  "")
      ),
      Arguments.of(
        helperFieldMap(NULL_STR, NULL_STR,    VALUE,    null, null,  NULL_STR),
        helperFieldMap(APP_JSON, JSON_OBJECT, VALUE,    GET,  true,  "")
      ),
      Arguments.of(
        helperFieldMap(NULL_STR, NULL_STR,    NULL_STR, POST, null,  NULL_STR),
        helperFieldMap(APP_JSON, JSON_OBJECT, APP_JSON, POST, true,  "")
      ),
      Arguments.of(
        helperFieldMap(NULL_STR, NULL_STR,    NULL_STR, POST, false, NULL_STR),
        helperFieldMap(APP_JSON, NULL_STR,    APP_JSON, POST, false, "")
      ),
      Arguments.of(
        helperFieldMap(NULL_STR, NULL_STR,    NULL_STR, null, null,  VALUE),
        helperFieldMap(APP_JSON, JSON_OBJECT, APP_JSON, GET,  true,  VALUE)
      )
    ).stream();
  }

  /**
   * Helper for reducing in line code repetition for assignments.
   *
   * @param accept        The accept value.
   * @param bodyTemplate  The bodyTemplate value.
   * @param contentType   The contentType value.
   * @param method        The method value.
   * @param sendEmptyBody The sendEmptyBody value.
   * @param url           The url value.
   *
   * @return The built arguments map.
   */
  private static Map<String, Object> helperFieldMap(String accept, String bodyTemplate, String contentType, HttpMethod method, Boolean sendEmptyBody, String url) {
    final Map<String, Object> map = new HashMap<>();

    map.put(ACCEPT, accept);
    map.put(BODYTEMPLATE, bodyTemplate);
    map.put(CONTENTTYPE, contentType);
    map.put(METHOD, method);
    map.put(SENDEMPTYBODY, sendEmptyBody);
    map.put(URL, url);

    return map;
  }

}
