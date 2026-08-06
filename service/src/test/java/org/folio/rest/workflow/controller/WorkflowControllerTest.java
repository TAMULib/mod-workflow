package org.folio.rest.workflow.controller;

import static org.folio.spring.test.mock.MockMvcConstant.APP_JSON;
import static org.folio.spring.test.mock.MockMvcConstant.APP_RAML;
import static org.folio.spring.test.mock.MockMvcConstant.APP_SCHEMA;
import static org.folio.spring.test.mock.MockMvcConstant.APP_STAR;
import static org.folio.spring.test.mock.MockMvcConstant.APP_STREAM;
import static org.folio.spring.test.mock.MockMvcConstant.JSON_OBJECT;
import static org.folio.spring.test.mock.MockMvcConstant.KEY;
import static org.folio.spring.test.mock.MockMvcConstant.MT_APP_JSON;
import static org.folio.spring.test.mock.MockMvcConstant.NO_PARAM;
import static org.folio.spring.test.mock.MockMvcConstant.NULL_STR;
import static org.folio.spring.test.mock.MockMvcConstant.OKAPI_HEAD_NO_URL;
import static org.folio.spring.test.mock.MockMvcConstant.OKAPI_HEAD_TENANT;
import static org.folio.spring.test.mock.MockMvcConstant.OKAPI_HEAD_TOKEN;
import static org.folio.spring.test.mock.MockMvcConstant.PLAIN_BODY;
import static org.folio.spring.test.mock.MockMvcConstant.STAR;
import static org.folio.spring.test.mock.MockMvcConstant.TEXT_PLAIN;
import static org.folio.spring.test.mock.MockMvcConstant.VALUE;
import static org.folio.spring.test.mock.MockMvcReflection.DELETE;
import static org.folio.spring.test.mock.MockMvcReflection.GET;
import static org.folio.spring.test.mock.MockMvcReflection.PATCH;
import static org.folio.spring.test.mock.MockMvcReflection.POST;
import static org.folio.spring.test.mock.MockMvcReflection.PUT;
import static org.folio.spring.test.mock.MockMvcRequest.appendBody;
import static org.folio.spring.test.mock.MockMvcRequest.appendHeaders;
import static org.folio.spring.test.mock.MockMvcRequest.appendParameters;
import static org.folio.spring.test.mock.MockMvcRequest.buildArguments1;
import static org.folio.spring.test.mock.MockMvcRequest.buildArguments2;
import static org.folio.spring.test.mock.MockMvcRequest.invokeRequestBuilder;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.lenient;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.log;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.stream.Stream;
import org.assertj.core.api.Assertions;
import org.folio.rest.workflow.exception.WorkflowNotFoundException;
import org.folio.rest.workflow.model.Workflow;
import org.folio.rest.workflow.model.repo.WorkflowRepo;
import org.folio.rest.workflow.service.WorkflowCqlService;
import org.folio.rest.workflow.service.WorkflowEngineService;
import org.folio.rest.workflow.service.WorkflowImportService;
import org.folio.spring.tenant.properties.TenantProperties;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.CollectionUtils;
import org.springframework.util.MultiValueMap;

@WebMvcTest(WorkflowController.class)
@ExtendWith(MockitoExtension.class)
class WorkflowControllerTest {

  private static final String ID = "id";

  private static final String PATH = "/workflows";

  private static final String PATH_IMPORT = PATH + "/import";

  private static final String PATH_SEARCH = PATH + "/search";

  private static final String OFFSET = "offset";

  private static final String OFFSET_VALUE = "1";

  private static final String LIMIT = "limit";

  private static final String LIMIT_VALUE = "100";

  private static final String MULTIPART_FORM = MediaType.MULTIPART_FORM_DATA_VALUE;

  private static final String QUERY = "query";

  private static final String QUERY_VALUE = "cql.allRecords=1";

  private static final String UUID = "aec4faca-8c3d-4b3b-a943-6003636991c0";

  private static final String UUID_INVALID = "+";

  private static final String PATH_ACTIVATE = PATH + "/" + UUID + "/activate";

  private static final String PATH_DEACTIVATE = PATH + "/" + UUID + "/deactivate";

  private static final String PATH_DELETE = PATH + "/" + UUID + "/delete";

  private static final String PATH_DELETE_INVALID = PATH + "/" + UUID_INVALID + "/delete";

  private static final String PATH_GET = PATH + "/" + UUID;

  private static final String PATH_HISTORY = PATH + "/" + UUID + "/history";

  private static final String PATH_START = PATH + "/" + UUID + "/start";

  private static final MultiValueMap<String, String> ID_PARAM = CollectionUtils.toMultiValueMap(Map.of(ID, List.of(UUID)));

  private static final MultiValueMap<String, String> ID_INVALID_PARAM = CollectionUtils.toMultiValueMap(Map.of(ID, List.of(UUID_INVALID)));

  private static final MultiValueMap<String, String> LIM_PARAM = CollectionUtils.toMultiValueMap(Map.of(
    LIMIT, List.of(LIMIT_VALUE)
  ));

  private static final MultiValueMap<String, String> OFF_PARAM = CollectionUtils.toMultiValueMap(Map.of(
    OFFSET, List.of(OFFSET_VALUE)
  ));

  private static final MultiValueMap<String, String> OFF_LIM_PARAM = CollectionUtils.toMultiValueMap(Map.of(
    LIMIT, List.of(LIMIT_VALUE),
    OFFSET, List.of(OFFSET_VALUE)
  ));

  private static final MultiValueMap<String, String> QUE_PARAM = CollectionUtils.toMultiValueMap(Map.of(
    QUERY, List.of(QUERY_VALUE)
  ));

  private static final MultiValueMap<String, String> QUE_LIM_PARAM = CollectionUtils.toMultiValueMap(Map.of(
    QUERY, List.of(QUERY_VALUE),
    LIMIT, List.of(LIMIT_VALUE)
  ));

  private static final MultiValueMap<String, String> QUE_OFF_PARAM = CollectionUtils.toMultiValueMap(Map.of(
    QUERY, List.of(QUERY_VALUE),
    OFFSET, List.of(OFFSET_VALUE)
  ));

  private static final MultiValueMap<String, String> QUE_OFF_LIM_PARAM = CollectionUtils.toMultiValueMap(Map.of(
    QUERY, List.of(QUERY_VALUE),
    OFFSET, List.of(OFFSET_VALUE),
    LIMIT, List.of(LIMIT_VALUE)
  ));

  private MockMvc mvc;

  @Autowired
  protected ObjectMapper mapper;

  @Autowired
  private WorkflowController workflowController;

  @MockitoBean
  private WorkflowCqlService workflowCqlService;

  @MockitoBean
  private WorkflowEngineService workflowEngineService;

  @MockitoBean
  private WorkflowImportService workflowImportService;

  @MockitoBean
  private WorkflowRepo workflowRepo;

  @MockitoBean
  private TenantProperties tenantProperties;

  @BeforeEach
  void beforeEach() {
    mvc = MockMvcBuilders.standaloneSetup(workflowController).build();

    // Prevent problems where the object mapper randomly switches time zone thereby causing string comparisons to fail.
    mapper.disable(DeserializationFeature.ADJUST_DATES_TO_CONTEXT_TIME_ZONE);
    mapper.setTimeZone(TimeZone.getTimeZone("UTC"));
  }

  @ParameterizedTest
  @MethodSource("provideHeadersBodyStatusFor")
  void activateWorkflowTest(HttpHeaders headers, String contentType, String accept, MediaType mediaType, MultiValueMap<String, String> parameters, String body, int status) throws Exception {
    Workflow workflow = new Workflow();

    lenient().when(workflowEngineService.activate(any(), any(), any())).thenReturn(workflow);

    MockHttpServletRequestBuilder request = appendHeaders(put(PATH_ACTIVATE), headers, contentType, accept);
    request = appendParameters(request, parameters);

    MvcResult result = mvc.perform(appendBody(request, body))
      .andDo(log()).andExpect(status().is(status)).andReturn();

    if (status == 200) {
      MediaType responseType = MediaType.parseMediaType(result.getResponse().getContentType());
      String workflowJson = mapper.writeValueAsString(workflow);

      assertTrue(mediaType.isCompatibleWith(responseType));

      Workflow responseWorkflow = mapper.readValue(result.getResponse().getContentAsString(), Workflow.class);

      assertEquals(workflowJson, mapper.writeValueAsString(responseWorkflow));
    }
  }

  @ParameterizedTest
  @MethodSource("provideDeleteGetPatchPostFor")
  void activateWorkflowFailsTest(Method method, HttpHeaders headers, String contentType, String accept, MediaType mediaType, MultiValueMap<String, String> parameters, String body, int status) throws Exception {
    mvc.perform(invokeRequestBuilder(PATH_ACTIVATE, method, headers, contentType, accept, parameters, body))
    .andDo(log()).andExpect(status().is(status));
  }

  @ParameterizedTest
  @MethodSource("provideHeadersBodyStatusFor")
  void deactivateWorkflowTest(HttpHeaders headers, String contentType, String accept, MediaType mediaType, MultiValueMap<String, String> parameters, String body, int status) throws Exception {
    Workflow workflow = new Workflow();

    lenient().when(workflowEngineService.deactivate(any(), any(), any())).thenReturn(workflow);

    MockHttpServletRequestBuilder request = appendHeaders(put(PATH_DEACTIVATE), headers, contentType, accept);
    request = appendParameters(request, parameters);

    MvcResult result = mvc.perform(appendBody(request, body))
      .andDo(log()).andExpect(status().is(status)).andReturn();

    if (status == 200) {
      MediaType responseType = MediaType.parseMediaType(result.getResponse().getContentType());
      String workflowJson = mapper.writeValueAsString(workflow);

      assertTrue(mediaType.isCompatibleWith(responseType));

      Workflow responseWorkflow = mapper.readValue(result.getResponse().getContentAsString(), Workflow.class);

      assertEquals(workflowJson, mapper.writeValueAsString(responseWorkflow));
    }
  }

  @ParameterizedTest
  @MethodSource("provideDeleteGetPatchPostFor")
  void deactivateWorkflowFailsTest(Method method, HttpHeaders headers, String contentType, String accept, MediaType mediaType, MultiValueMap<String, String> parameters, String body, int status) throws Exception {
    mvc.perform(invokeRequestBuilder(PATH_DEACTIVATE, method, headers, contentType, accept, parameters, body))
    .andDo(log()).andExpect(status().is(status));
  }

  @ParameterizedTest
  @MethodSource("provideHeadersBodyStatusForDeleteWorkflow")
  void deleteWorkflowTest(HttpHeaders headers, String contentType, String accept, MediaType mediaType, MultiValueMap<String, String> parameters, String body, int status) throws Exception {
    lenient().doNothing().when(workflowEngineService).exists(anyString());
    lenient().doNothing().when(workflowEngineService).delete(any(), any(), any());

    MockHttpServletRequestBuilder request = appendHeaders(delete(PATH_DELETE), headers, contentType, accept);
    request = appendParameters(request, parameters);

    mvc.perform(appendBody(request, body))
      .andDo(log()).andExpect(status().is(status)).andReturn();
  }

  @Test
  void deleteWorkflowThrowsNotFoundTest() throws Exception {
    lenient().doThrow(WorkflowNotFoundException.class).when(workflowEngineService).exists(anyString());

    Assertions.assertThatThrownBy(() -> {
      MockHttpServletRequestBuilder request = appendHeaders(delete(PATH_DELETE), OKAPI_HEAD_TENANT, APP_JSON, APP_JSON);
      request = appendParameters(request, ID_PARAM);

      mvc.perform(request)
        .andDo(log()).andExpect(status().is(404));
    }).hasCauseInstanceOf(WorkflowNotFoundException.class);
  }

  @Test
  void deleteWorkflowThrowsNotFoundInvalidIdSanitizeTest() throws Exception {
    lenient().doThrow(WorkflowNotFoundException.class).when(workflowEngineService).exists(anyString());

    Assertions.assertThatThrownBy(() -> {
      MockHttpServletRequestBuilder request = appendHeaders(delete(PATH_DELETE_INVALID), OKAPI_HEAD_TENANT, APP_JSON, APP_JSON);
      request = appendParameters(request, ID_INVALID_PARAM);

      mvc.perform(request)
        .andDo(log()).andExpect(status().is(404));
    }).hasCauseInstanceOf(WorkflowNotFoundException.class);
  }

  @ParameterizedTest
  @MethodSource("provideGetPatchPostPutFor")
  void deleteWorkflowFailsTest(Method method, HttpHeaders headers, String contentType, String accept, MediaType mediaType, MultiValueMap<String, String> parameters, String body, int status) throws Exception {
    lenient().doNothing().when(workflowEngineService).exists(anyString());

    mvc.perform(invokeRequestBuilder(PATH_DELETE, method, headers, contentType, accept, parameters, body))
    .andDo(log()).andExpect(status().is(status));
  }

  @ParameterizedTest
  @MethodSource("provideHeadersBodyStatusForStartWorkflow")
  void startWorkflowTest(HttpHeaders headers, String contentType, String accept, MediaType mediaType, MultiValueMap<String, String> parameters, String body, int status) throws Exception {
    ObjectNode objectNode = mapper.createObjectNode();
    objectNode.put(KEY, VALUE);

    lenient().when(workflowEngineService.start(any(), any(), any(), any())).thenReturn(objectNode);

    MockHttpServletRequestBuilder request = appendHeaders(post(PATH_START), headers, contentType, accept);
    request = appendParameters(request, parameters);

    MvcResult result = mvc.perform(appendBody(request, body))
      .andDo(log()).andExpect(status().is(status)).andReturn();

    if (status == 200) {
      MediaType responseType = MediaType.parseMediaType(result.getResponse().getContentType());

      assertTrue(mediaType.isCompatibleWith(responseType));
      assertEquals(objectNode.toString(), result.getResponse().getContentAsString());
    }
  }

  @ParameterizedTest
  @MethodSource("provideDeleteGetPatchPutFor")
  void startWorkflowFailsTest(Method method, HttpHeaders headers, String contentType, String accept, MediaType mediaType, MultiValueMap<String, String> parameters, String body, int status) throws Exception {
    mvc.perform(invokeRequestBuilder(PATH_START, method, headers, contentType, accept, parameters, body))
    .andDo(log()).andExpect(status().is(status));
  }

  @ParameterizedTest
  @MethodSource("provideHeadersBodyStatusFor")
  void workflowHistoryTest(HttpHeaders headers, String contentType, String accept, MediaType mediaType, MultiValueMap<String, String> parameters, String body, int status) throws Exception {
    ObjectNode objectNode = mapper.createObjectNode();
    objectNode.put(KEY, VALUE);

    lenient().when(workflowEngineService.history(any(), any(), any())).thenReturn(objectNode);

    MockHttpServletRequestBuilder request = appendHeaders(get(PATH_HISTORY), headers, contentType, accept);
    request = appendParameters(request, parameters);

    MvcResult result = mvc.perform(appendBody(request, body))
      .andDo(log()).andExpect(status().is(status)).andReturn();

    if (status == 200) {
      MediaType responseType = MediaType.parseMediaType(result.getResponse().getContentType());

      assertTrue(mediaType.isCompatibleWith(responseType));
      assertEquals(objectNode.toString(), result.getResponse().getContentAsString());
    }
  }

  @ParameterizedTest
  @MethodSource("provideDeletePatchPostPutFor")
  void workflowHistoryFailsTest(Method method, HttpHeaders headers, String contentType, String accept, MediaType mediaType, MultiValueMap<String, String> parameters, String body, int status) throws Exception {
    mvc.perform(invokeRequestBuilder(PATH_HISTORY, method, headers, contentType, accept, parameters, body))
    .andDo(log()).andExpect(status().is(status));
  }

  @ParameterizedTest
  @MethodSource("provideHeadersBodyStatusForGetWorkflow")
  void getWorkflowTest(HttpHeaders headers, String contentType, String accept, MediaType mediaType, MultiValueMap<String, String> parameters, String body, int status) throws Exception {
    Workflow workflow = new Workflow();
    workflow.setId(UUID);
    lenient().when(workflowRepo.getReferenceById(anyString())).thenReturn(workflow);

    MockHttpServletRequestBuilder request = appendHeaders(get(PATH_GET), headers, contentType, accept);
    request = appendParameters(request, parameters);

    MvcResult result = mvc.perform(appendBody(request, body))
      .andDo(log()).andExpect(status().is(status)).andReturn();

    if (status == 200) {
      MediaType responseType = MediaType.parseMediaType(result.getResponse().getContentType());

      assertTrue(mediaType.isCompatibleWith(responseType));
    }
  }

  @ParameterizedTest
  @MethodSource("provideHeadersBodyStatusForSearchWorkflows")
  void searchWorkflowsTest(HttpHeaders headers, String contentType, String accept, MediaType mediaType, MultiValueMap<String, String> parameters, String body, int status) throws Exception {
    ObjectNode objectNode = mapper.createObjectNode();
    objectNode.put(KEY, VALUE);

    lenient().when(workflowCqlService.findByCql(anyString(), anyLong(), anyInt())).thenReturn(objectNode);

    MockHttpServletRequestBuilder request = appendHeaders(get(PATH_SEARCH), headers, contentType, accept);
    request = appendParameters(request, parameters);

    MvcResult result = mvc.perform(appendBody(request, body))
      .andDo(log()).andExpect(status().is(status)).andReturn();

    if (status == 200) {
      MediaType responseType = MediaType.parseMediaType(result.getResponse().getContentType());

      assertTrue(mediaType.isCompatibleWith(responseType));
      assertEquals(objectNode.toString(), result.getResponse().getContentAsString());
    }
  }

  @ParameterizedTest
  @MethodSource("provideDeletePatchPostPutForSearchWorkflows")
  void searchWorkflowsFailsTest(Method method, HttpHeaders headers, String contentType, String accept, MediaType mediaType, MultiValueMap<String, String> parameters, String body, int status) throws Exception {
    mvc.perform(invokeRequestBuilder(PATH_SEARCH, method, headers, contentType, accept, parameters, body))
      .andDo(log()).andExpect(status().is(status));
  }

  @ParameterizedTest
  @MethodSource("provideHeadersBodyStatusForImportWorkflows")
  void importWorkflowsTest(HttpHeaders headers, String contentType, String accept, MediaType mediaType, MultiValueMap<String, String> parameters, String body, int status) throws Exception {
    Workflow workflow = new Workflow();
    workflow.setId(UUID);

    MockMultipartFile exampleFwz = new MockMultipartFile("file", "example.fwz", APP_JSON, JSON_OBJECT.getBytes());

    lenient().when(workflowImportService.importFile(any(Resource.class))).thenReturn(workflow);

    MockHttpServletRequestBuilder request = appendHeaders(multipart(PATH_IMPORT).file(exampleFwz), headers, contentType, accept);
    request = appendParameters(request, parameters);

    MvcResult result = mvc.perform(appendBody(request, body))
      .andDo(log()).andExpect(status().is(status)).andReturn();

    if (status == 200) {
      MediaType responseType = MediaType.parseMediaType(result.getResponse().getContentType());
      String workflowJson = mapper.writeValueAsString(workflow);

      assertTrue(mediaType.isCompatibleWith(responseType));

      Workflow responseWorkflow = mapper.readValue(result.getResponse().getContentAsString(), Workflow.class);

      assertEquals(workflowJson, mapper.writeValueAsString(responseWorkflow));
    }
  }

  @ParameterizedTest
  @MethodSource("provideDeletePatchPutForImportWorkflows")
  void importWorkflowsFailsTest(Method method, HttpHeaders headers, String contentType, String accept, MediaType mediaType, MultiValueMap<String, String> parameters, String body, int status) throws Exception {
    mvc.perform(invokeRequestBuilder(PATH_IMPORT, method, headers, contentType, accept, parameters, body))
      .andDo(log()).andExpect(status().is(status));
  }

  /**
   * Helper function for parameterized test providing DELETE, PATCH, POST, and PUT for search workflows end point.
   *
   * @return
   *   The arguments array stream with the stream columns as:
   *     - Method The (reflection) request method.
   *     - HttpHeaders headers.
   *     - String contentType (Content-Type request).
   *     - String accept (ask for this Content-Type on response).
   *       String mediaType (response Content-Type).
   *     - MultiValueMap<String, String> parameters.
   *     - String body (request body).
   *     - int status (response HTTP status code).
   *
   * @throws SecurityException
   */
  private static Stream<Arguments> provideDeletePatchPostPutForSearchWorkflows() throws SecurityException {
    Object[] params = { NO_PARAM, LIM_PARAM, OFF_LIM_PARAM, OFF_PARAM };

    return buildHttpDeletePatchPostPut(OKAPI_HEAD_NO_URL, params);
  }

  /**
   * Helper function for parameterized test providing DELETE, PATCH, POST, and PUT for import workflows end point.
   *
   * @return
   *   The arguments array stream with the stream columns as:
   *     - Method The (reflection) request method.
   *     - HttpHeaders headers.
   *     - String contentType (Content-Type request).
   *     - String accept (ask for this Content-Type on response).
   *       String mediaType (response Content-Type).
   *     - MultiValueMap<String, String> parameters.
   *     - String body (request body).
   *     - int status (response HTTP status code).
   *
   * @throws SecurityException
   */
  private static Stream<Arguments> provideDeletePatchPutForImportWorkflows() throws SecurityException {
    Object[] params = { NO_PARAM };

    return buildHttpDeletePatchPut(OKAPI_HEAD_NO_URL, params);
  }

  /**
   * Helper function for parameterized test providing DELETE, PATCH, POST, and PUT for several end points.
   *
   * @return
   *   The arguments array stream with the stream columns as:
   *     - Method The (reflection) request method.
   *     - HttpHeaders headers.
   *     - String contentType (Content-Type request).
   *     - String accept (ask for this Content-Type on response).
   *       String mediaType (response Content-Type).
   *     - MultiValueMap<String, String> parameters.
   *     - String body (request body).
   *     - int status (response HTTP status code).
   *
   * @throws SecurityException
   */
  private static Stream<Arguments> provideDeletePatchPostPutFor() throws SecurityException {
    Object[] params = { NO_PARAM, ID_PARAM };

    return buildHttpDeletePatchPostPut(OKAPI_HEAD_NO_URL, params);
  }

  /**
   * Helper function for parameterized test providing DELETE, GET, PATCH, and POST for several end points.
   *
   * @return
   *   The arguments array stream with the stream columns as:
   *     - Method The (reflection) request method.
   *     - HttpHeaders headers.
   *     - String contentType (Content-Type request).
   *     - String accept (ask for this Content-Type on response).
   *       String mediaType (response Content-Type).
   *     - MultiValueMap<String, String> parameters.
   *     - String body (request body).
   *     - int status (response HTTP status code).
   *
   * @throws SecurityException
   */
  private static Stream<Arguments> provideDeleteGetPatchPostFor() throws SecurityException {
    Object[] params = { NO_PARAM, ID_PARAM };

    return buildHttpDeleteGetPatchPost(OKAPI_HEAD_NO_URL, params);
  }

  /**
   * Helper function for parameterized test providing GET, PATCH, POST, and PUT for several end points.
   *
   * @return
   *   The arguments array stream with the stream columns as:
   *     - Method The (reflection) request method.
   *     - HttpHeaders headers.
   *     - String contentType (Content-Type request).
   *     - String accept (ask for this Content-Type on response).
   *       String mediaType (response Content-Type).
   *     - MultiValueMap<String, String> parameters.
   *     - String body (request body).
   *     - int status (response HTTP status code).
   *
   * @throws SecurityException
   */
  private static Stream<Arguments> provideGetPatchPostPutFor() throws SecurityException {
    Object[] params = { NO_PARAM, ID_PARAM };

    return buildHttpGetPatchPostPut(OKAPI_HEAD_NO_URL, params);
  }

  /**
   * Helper function for parameterized test providing DELETE, GET, PATCH, and PUT for several end points.
   *
   * @return
   *   The arguments array stream with the stream columns as:
   *     - Method The (reflection) request method.
   *     - HttpHeaders headers.
   *     - String contentType (Content-Type request).
   *     - String accept (ask for this Content-Type on response).
   *       String mediaType (response Content-Type).
   *     - MultiValueMap<String, String> parameters.
   *     - String body (request body).
   *     - int status (response HTTP status code).
   *
   * @throws SecurityException
   */
  private static Stream<Arguments> provideDeleteGetPatchPutFor() throws SecurityException {
    Object[] params = { NO_PARAM, ID_PARAM };

    return buildHttpDeleteGetPatchPut(OKAPI_HEAD_NO_URL, params);
  }

  /**
   * Helper function for parameterized test providing tests with headers, body, and status for several end points.
   *
   * This is intended to be used for when the correct HTTP method is being used in the request.
   *
   * @return
   *   The arguments array stream with the stream columns as:
   *     - HttpHeaders headers.
   *     - String contentType (Content-Type request).
   *     - String accept (ask for this Content-Type on response).
   *       String mediaType (response Content-Type).
   *     - MultiValueMap<String, String> parameters.
   *     - String body (request body).
   *     - int status (response HTTP status code).
   */
  private static Stream<Arguments> provideHeadersBodyStatusFor() {
    Stream<Arguments> stream1 = Stream.of(
      Arguments.of(OKAPI_HEAD_TENANT, APP_JSON,   APP_SCHEMA, MT_APP_JSON, ID_PARAM, JSON_OBJECT, 406),
      Arguments.of(OKAPI_HEAD_TENANT, APP_JSON,   APP_JSON,   MT_APP_JSON, ID_PARAM, JSON_OBJECT, 200),
      Arguments.of(OKAPI_HEAD_TENANT, APP_JSON,   TEXT_PLAIN, MT_APP_JSON, ID_PARAM, JSON_OBJECT, 406),
      Arguments.of(OKAPI_HEAD_TENANT, APP_JSON,   APP_STREAM, MT_APP_JSON, ID_PARAM, JSON_OBJECT, 406),
      Arguments.of(OKAPI_HEAD_TENANT, APP_JSON,   NULL_STR,   MT_APP_JSON, ID_PARAM, JSON_OBJECT, 200),
      Arguments.of(OKAPI_HEAD_TENANT, APP_JSON,   APP_STAR,   MT_APP_JSON, ID_PARAM, JSON_OBJECT, 200),
      Arguments.of(OKAPI_HEAD_TENANT, APP_JSON,   STAR,       MT_APP_JSON, ID_PARAM, JSON_OBJECT, 200),
      Arguments.of(OKAPI_HEAD_TENANT, TEXT_PLAIN, APP_SCHEMA, MT_APP_JSON, ID_PARAM, PLAIN_BODY,  406),
      Arguments.of(OKAPI_HEAD_TENANT, TEXT_PLAIN, APP_JSON,   MT_APP_JSON, ID_PARAM, PLAIN_BODY,  200),
      Arguments.of(OKAPI_HEAD_TENANT, TEXT_PLAIN, TEXT_PLAIN, MT_APP_JSON, ID_PARAM, PLAIN_BODY,  406),
      Arguments.of(OKAPI_HEAD_TENANT, TEXT_PLAIN, APP_STREAM, MT_APP_JSON, ID_PARAM, PLAIN_BODY,  406),
      Arguments.of(OKAPI_HEAD_TENANT, TEXT_PLAIN, NULL_STR,   MT_APP_JSON, ID_PARAM, PLAIN_BODY,  200),
      Arguments.of(OKAPI_HEAD_TENANT, TEXT_PLAIN, STAR,       MT_APP_JSON, ID_PARAM, PLAIN_BODY,  200),
      Arguments.of(OKAPI_HEAD_TENANT, TEXT_PLAIN, APP_STAR,   MT_APP_JSON, ID_PARAM, PLAIN_BODY,  200),
      Arguments.of(OKAPI_HEAD_TENANT, APP_STREAM, APP_SCHEMA, MT_APP_JSON, ID_PARAM, JSON_OBJECT, 406),
      Arguments.of(OKAPI_HEAD_TENANT, APP_STREAM, APP_JSON,   MT_APP_JSON, ID_PARAM, JSON_OBJECT, 200),
      Arguments.of(OKAPI_HEAD_TENANT, APP_STREAM, TEXT_PLAIN, MT_APP_JSON, ID_PARAM, JSON_OBJECT, 406),
      Arguments.of(OKAPI_HEAD_TENANT, APP_STREAM, APP_STREAM, MT_APP_JSON, ID_PARAM, JSON_OBJECT, 406),
      Arguments.of(OKAPI_HEAD_TENANT, APP_STREAM, NULL_STR,   MT_APP_JSON, ID_PARAM, JSON_OBJECT, 200),
      Arguments.of(OKAPI_HEAD_TENANT, APP_STREAM, APP_STAR,   MT_APP_JSON, ID_PARAM, JSON_OBJECT, 200),
      Arguments.of(OKAPI_HEAD_TENANT, APP_STREAM, STAR,       MT_APP_JSON, ID_PARAM, JSON_OBJECT, 200)
    );

    Object[] params = { };

    Stream<Arguments> stream2 = Stream.concat(stream1, buildAppJsonBodyStatus(OKAPI_HEAD_TOKEN, params));
    return Stream.concat(stream2, buildAppJsonBodyStatus(OKAPI_HEAD_TENANT, params));
  }

  /**
   * Helper function for parameterized test providing tests with headers, body, and status for the delete workflow end point.
   *
   * This is intended to be used for when the correct HTTP method is being used in the request.
   *
   * @return
   *   The arguments array stream with the stream columns as:
   *     - HttpHeaders headers.
   *     - String contentType (Content-Type request).
   *     - String accept (ask for this Content-Type on response).
   *       String mediaType (response Content-Type).
   *     - MultiValueMap<String, String> parameters.
   *     - String body (request body).
   *     - int status (response HTTP status code).
   */
  private static Stream<Arguments> provideHeadersBodyStatusForDeleteWorkflow() {
    Stream<Arguments> stream1 = Stream.of(
      Arguments.of(OKAPI_HEAD_TENANT, APP_JSON,   APP_SCHEMA, MT_APP_JSON, ID_PARAM, JSON_OBJECT, 204),
      Arguments.of(OKAPI_HEAD_TENANT, APP_JSON,   APP_JSON,   MT_APP_JSON, ID_PARAM, JSON_OBJECT, 204),
      Arguments.of(OKAPI_HEAD_TENANT, APP_JSON,   TEXT_PLAIN, MT_APP_JSON, ID_PARAM, JSON_OBJECT, 204),
      Arguments.of(OKAPI_HEAD_TENANT, APP_JSON,   APP_STREAM, MT_APP_JSON, ID_PARAM, JSON_OBJECT, 204),
      Arguments.of(OKAPI_HEAD_TENANT, APP_JSON,   NULL_STR,   MT_APP_JSON, ID_PARAM, JSON_OBJECT, 204),
      Arguments.of(OKAPI_HEAD_TENANT, APP_JSON,   APP_STAR,   MT_APP_JSON, ID_PARAM, JSON_OBJECT, 204),
      Arguments.of(OKAPI_HEAD_TENANT, APP_JSON,   STAR,       MT_APP_JSON, ID_PARAM, JSON_OBJECT, 204),
      Arguments.of(OKAPI_HEAD_TENANT, TEXT_PLAIN, APP_SCHEMA, MT_APP_JSON, ID_PARAM, PLAIN_BODY,  204),
      Arguments.of(OKAPI_HEAD_TENANT, TEXT_PLAIN, APP_JSON,   MT_APP_JSON, ID_PARAM, PLAIN_BODY,  204),
      Arguments.of(OKAPI_HEAD_TENANT, TEXT_PLAIN, TEXT_PLAIN, MT_APP_JSON, ID_PARAM, PLAIN_BODY,  204),
      Arguments.of(OKAPI_HEAD_TENANT, TEXT_PLAIN, APP_STREAM, MT_APP_JSON, ID_PARAM, PLAIN_BODY,  204),
      Arguments.of(OKAPI_HEAD_TENANT, TEXT_PLAIN, NULL_STR,   MT_APP_JSON, ID_PARAM, PLAIN_BODY,  204),
      Arguments.of(OKAPI_HEAD_TENANT, TEXT_PLAIN, STAR,       MT_APP_JSON, ID_PARAM, PLAIN_BODY,  204),
      Arguments.of(OKAPI_HEAD_TENANT, TEXT_PLAIN, APP_STAR,   MT_APP_JSON, ID_PARAM, PLAIN_BODY,  204),
      Arguments.of(OKAPI_HEAD_TENANT, APP_STREAM, APP_SCHEMA, MT_APP_JSON, ID_PARAM, JSON_OBJECT, 204),
      Arguments.of(OKAPI_HEAD_TENANT, APP_STREAM, APP_JSON,   MT_APP_JSON, ID_PARAM, JSON_OBJECT, 204),
      Arguments.of(OKAPI_HEAD_TENANT, APP_STREAM, TEXT_PLAIN, MT_APP_JSON, ID_PARAM, JSON_OBJECT, 204),
      Arguments.of(OKAPI_HEAD_TENANT, APP_STREAM, APP_STREAM, MT_APP_JSON, ID_PARAM, JSON_OBJECT, 204),
      Arguments.of(OKAPI_HEAD_TENANT, APP_STREAM, NULL_STR,   MT_APP_JSON, ID_PARAM, JSON_OBJECT, 204),
      Arguments.of(OKAPI_HEAD_TENANT, APP_STREAM, APP_STAR,   MT_APP_JSON, ID_PARAM, JSON_OBJECT, 204),
      Arguments.of(OKAPI_HEAD_TENANT, APP_STREAM, STAR,       MT_APP_JSON, ID_PARAM, JSON_OBJECT, 204)
    );

    Object[] params = { };

    Stream<Arguments> stream2 = Stream.concat(stream1, buildAppJsonBodyStatus(OKAPI_HEAD_TOKEN, params));
    return Stream.concat(stream2, buildAppJsonBodyStatus(OKAPI_HEAD_TENANT, params));
  }

  /**
   * Helper function for parameterized test providing tests with headers, body, and status for the start workflow end point.
   *
   * This is intended to be used for when the correct HTTP method is being used in the request.
   *
   * @return
   *   The arguments array stream with the stream columns as:
   *     - HttpHeaders headers.
   *     - String contentType (Content-Type request).
   *     - String accept (ask for this Content-Type on response).
   *       String mediaType (response Content-Type).
   *     - MultiValueMap<String, String> parameters.
   *     - String body (request body).
   *     - int status (response HTTP status code).
   */
  private static Stream<Arguments> provideHeadersBodyStatusForStartWorkflow() {
    Stream<Arguments> stream1 = Stream.of(
      Arguments.of(OKAPI_HEAD_TENANT, APP_JSON,   APP_SCHEMA, MT_APP_JSON, ID_PARAM, JSON_OBJECT, 406),
      Arguments.of(OKAPI_HEAD_TENANT, APP_JSON,   APP_JSON,   MT_APP_JSON, ID_PARAM, JSON_OBJECT, 200),
      Arguments.of(OKAPI_HEAD_TENANT, APP_JSON,   TEXT_PLAIN, MT_APP_JSON, ID_PARAM, JSON_OBJECT, 406),
      Arguments.of(OKAPI_HEAD_TENANT, APP_JSON,   APP_STREAM, MT_APP_JSON, ID_PARAM, JSON_OBJECT, 406),
      Arguments.of(OKAPI_HEAD_TENANT, APP_JSON,   NULL_STR,   MT_APP_JSON, ID_PARAM, JSON_OBJECT, 200),
      Arguments.of(OKAPI_HEAD_TENANT, APP_JSON,   APP_STAR,   MT_APP_JSON, ID_PARAM, JSON_OBJECT, 200),
      Arguments.of(OKAPI_HEAD_TENANT, APP_JSON,   STAR,       MT_APP_JSON, ID_PARAM, JSON_OBJECT, 200),
      Arguments.of(OKAPI_HEAD_TENANT, TEXT_PLAIN, APP_SCHEMA, MT_APP_JSON, ID_PARAM, PLAIN_BODY,  406),
      Arguments.of(OKAPI_HEAD_TENANT, TEXT_PLAIN, APP_JSON,   MT_APP_JSON, ID_PARAM, PLAIN_BODY,  415),
      Arguments.of(OKAPI_HEAD_TENANT, TEXT_PLAIN, TEXT_PLAIN, MT_APP_JSON, ID_PARAM, PLAIN_BODY,  406),
      Arguments.of(OKAPI_HEAD_TENANT, TEXT_PLAIN, APP_STREAM, MT_APP_JSON, ID_PARAM, PLAIN_BODY,  406),
      Arguments.of(OKAPI_HEAD_TENANT, TEXT_PLAIN, NULL_STR,   MT_APP_JSON, ID_PARAM, PLAIN_BODY,  415),
      Arguments.of(OKAPI_HEAD_TENANT, TEXT_PLAIN, STAR,       MT_APP_JSON, ID_PARAM, PLAIN_BODY,  415),
      Arguments.of(OKAPI_HEAD_TENANT, TEXT_PLAIN, APP_STAR,   MT_APP_JSON, ID_PARAM, PLAIN_BODY,  415),
      Arguments.of(OKAPI_HEAD_TENANT, APP_STREAM, APP_SCHEMA, MT_APP_JSON, ID_PARAM, JSON_OBJECT, 406),
      Arguments.of(OKAPI_HEAD_TENANT, APP_STREAM, APP_JSON,   MT_APP_JSON, ID_PARAM, JSON_OBJECT, 415),
      Arguments.of(OKAPI_HEAD_TENANT, APP_STREAM, TEXT_PLAIN, MT_APP_JSON, ID_PARAM, JSON_OBJECT, 406),
      Arguments.of(OKAPI_HEAD_TENANT, APP_STREAM, APP_STREAM, MT_APP_JSON, ID_PARAM, JSON_OBJECT, 406),
      Arguments.of(OKAPI_HEAD_TENANT, APP_STREAM, NULL_STR,   MT_APP_JSON, ID_PARAM, JSON_OBJECT, 415),
      Arguments.of(OKAPI_HEAD_TENANT, APP_STREAM, APP_STAR,   MT_APP_JSON, ID_PARAM, JSON_OBJECT, 415),
      Arguments.of(OKAPI_HEAD_TENANT, APP_STREAM, STAR,       MT_APP_JSON, ID_PARAM, JSON_OBJECT, 415)
    );

    Object[] params = { };

    Stream<Arguments> stream2 = Stream.concat(stream1, buildAppJsonBodyStatus(OKAPI_HEAD_TOKEN, params));
    return Stream.concat(stream2, buildAppJsonBodyStatus(OKAPI_HEAD_TENANT, params));
  }

  /**
   * Helper function for parameterized test providing tests with headers, body, and status for get workflow end point.
   *
   * This is intended to be used for when the correct HTTP method is being used in the request.
   *
   * @return
   *   The arguments array stream with the stream columns as:
   *     - HttpHeaders headers.
   *     - String contentType (Content-Type request).
   *     - String accept (ask for this Content-Type on response).
   *       String mediaType (response Content-Type).
   *     - MultiValueMap<String, String> parameters.
   *     - String body (request body).
   *     - int status (response HTTP status code).
   */
  private static Stream<Arguments> provideHeadersBodyStatusForGetWorkflow() {
    return Stream.of(
      Arguments.of(OKAPI_HEAD_TENANT, APP_JSON,   APP_SCHEMA, MT_APP_JSON, QUE_PARAM,         JSON_OBJECT, 406),
      Arguments.of(OKAPI_HEAD_TENANT, APP_JSON,   APP_JSON,   MT_APP_JSON, QUE_PARAM,         JSON_OBJECT, 200),
      Arguments.of(OKAPI_HEAD_TENANT, APP_JSON,   TEXT_PLAIN, MT_APP_JSON, QUE_PARAM,         JSON_OBJECT, 406),
      Arguments.of(OKAPI_HEAD_TENANT, APP_JSON,   APP_STREAM, MT_APP_JSON, QUE_PARAM,         JSON_OBJECT, 406),
      Arguments.of(OKAPI_HEAD_TENANT, APP_JSON,   NULL_STR,   MT_APP_JSON, QUE_PARAM,         JSON_OBJECT, 200),
      Arguments.of(OKAPI_HEAD_TENANT, APP_JSON,   APP_STAR,   MT_APP_JSON, QUE_PARAM,         JSON_OBJECT, 200),
      Arguments.of(OKAPI_HEAD_TENANT, APP_JSON,   STAR,       MT_APP_JSON, QUE_PARAM,         JSON_OBJECT, 200),
      Arguments.of(OKAPI_HEAD_TENANT, TEXT_PLAIN, APP_SCHEMA, MT_APP_JSON, QUE_PARAM,         PLAIN_BODY,  406),
      Arguments.of(OKAPI_HEAD_TENANT, TEXT_PLAIN, APP_JSON,   MT_APP_JSON, QUE_PARAM,         PLAIN_BODY,  200),
      Arguments.of(OKAPI_HEAD_TENANT, TEXT_PLAIN, TEXT_PLAIN, MT_APP_JSON, QUE_PARAM,         PLAIN_BODY,  406),
      Arguments.of(OKAPI_HEAD_TENANT, TEXT_PLAIN, APP_STREAM, MT_APP_JSON, QUE_PARAM,         PLAIN_BODY,  406),
      Arguments.of(OKAPI_HEAD_TENANT, TEXT_PLAIN, NULL_STR,   MT_APP_JSON, QUE_PARAM,         PLAIN_BODY,  200),
      Arguments.of(OKAPI_HEAD_TENANT, TEXT_PLAIN, STAR,       MT_APP_JSON, QUE_PARAM,         PLAIN_BODY,  200),
      Arguments.of(OKAPI_HEAD_TENANT, TEXT_PLAIN, APP_STAR,   MT_APP_JSON, QUE_PARAM,         PLAIN_BODY,  200),
      Arguments.of(OKAPI_HEAD_TENANT, APP_STREAM, APP_SCHEMA, MT_APP_JSON, QUE_PARAM,         JSON_OBJECT, 406),
      Arguments.of(OKAPI_HEAD_TENANT, APP_STREAM, APP_JSON,   MT_APP_JSON, QUE_PARAM,         JSON_OBJECT, 200),
      Arguments.of(OKAPI_HEAD_TENANT, APP_STREAM, TEXT_PLAIN, MT_APP_JSON, QUE_PARAM,         JSON_OBJECT, 406),
      Arguments.of(OKAPI_HEAD_TENANT, APP_STREAM, APP_STREAM, MT_APP_JSON, QUE_PARAM,         JSON_OBJECT, 406),
      Arguments.of(OKAPI_HEAD_TENANT, APP_STREAM, NULL_STR,   MT_APP_JSON, QUE_PARAM,         JSON_OBJECT, 200),
      Arguments.of(OKAPI_HEAD_TENANT, APP_STREAM, APP_STAR,   MT_APP_JSON, QUE_PARAM,         JSON_OBJECT, 200),
      Arguments.of(OKAPI_HEAD_TENANT, APP_STREAM, STAR,       MT_APP_JSON, QUE_PARAM,         JSON_OBJECT, 200),
      Arguments.of(OKAPI_HEAD_TENANT, APP_JSON,   APP_SCHEMA, MT_APP_JSON, QUE_LIM_PARAM,     JSON_OBJECT, 406),
      Arguments.of(OKAPI_HEAD_TENANT, APP_JSON,   APP_JSON,   MT_APP_JSON, QUE_LIM_PARAM,     JSON_OBJECT, 200),
      Arguments.of(OKAPI_HEAD_TENANT, APP_JSON,   TEXT_PLAIN, MT_APP_JSON, QUE_LIM_PARAM,     JSON_OBJECT, 406),
      Arguments.of(OKAPI_HEAD_TENANT, APP_JSON,   APP_STREAM, MT_APP_JSON, QUE_LIM_PARAM,     JSON_OBJECT, 406),
      Arguments.of(OKAPI_HEAD_TENANT, APP_JSON,   NULL_STR,   MT_APP_JSON, QUE_LIM_PARAM,     JSON_OBJECT, 200),
      Arguments.of(OKAPI_HEAD_TENANT, APP_JSON,   APP_STAR,   MT_APP_JSON, QUE_LIM_PARAM,     JSON_OBJECT, 200),
      Arguments.of(OKAPI_HEAD_TENANT, APP_JSON,   STAR,       MT_APP_JSON, QUE_LIM_PARAM,     JSON_OBJECT, 200),
      Arguments.of(OKAPI_HEAD_TENANT, APP_JSON,   APP_SCHEMA, MT_APP_JSON, QUE_OFF_LIM_PARAM, JSON_OBJECT, 406),
      Arguments.of(OKAPI_HEAD_TENANT, APP_JSON,   APP_JSON,   MT_APP_JSON, QUE_OFF_LIM_PARAM, JSON_OBJECT, 200),
      Arguments.of(OKAPI_HEAD_TENANT, APP_JSON,   TEXT_PLAIN, MT_APP_JSON, QUE_OFF_LIM_PARAM, JSON_OBJECT, 406),
      Arguments.of(OKAPI_HEAD_TENANT, APP_JSON,   APP_STREAM, MT_APP_JSON, QUE_OFF_LIM_PARAM, JSON_OBJECT, 406),
      Arguments.of(OKAPI_HEAD_TENANT, APP_JSON,   NULL_STR,   MT_APP_JSON, QUE_OFF_LIM_PARAM, JSON_OBJECT, 200),
      Arguments.of(OKAPI_HEAD_TENANT, APP_JSON,   APP_STAR,   MT_APP_JSON, QUE_OFF_LIM_PARAM, JSON_OBJECT, 200),
      Arguments.of(OKAPI_HEAD_TENANT, APP_JSON,   STAR,       MT_APP_JSON, QUE_OFF_LIM_PARAM, JSON_OBJECT, 200),
      Arguments.of(OKAPI_HEAD_TENANT, APP_JSON,   APP_SCHEMA, MT_APP_JSON, QUE_OFF_PARAM,     JSON_OBJECT, 406),
      Arguments.of(OKAPI_HEAD_TENANT, APP_JSON,   APP_JSON,   MT_APP_JSON, QUE_OFF_PARAM,     JSON_OBJECT, 200),
      Arguments.of(OKAPI_HEAD_TENANT, APP_JSON,   TEXT_PLAIN, MT_APP_JSON, QUE_OFF_PARAM,     JSON_OBJECT, 406),
      Arguments.of(OKAPI_HEAD_TENANT, APP_JSON,   APP_STREAM, MT_APP_JSON, QUE_OFF_PARAM,     JSON_OBJECT, 406),
      Arguments.of(OKAPI_HEAD_TENANT, APP_JSON,   NULL_STR,   MT_APP_JSON, QUE_OFF_PARAM,     JSON_OBJECT, 200),
      Arguments.of(OKAPI_HEAD_TENANT, APP_JSON,   APP_STAR,   MT_APP_JSON, QUE_OFF_PARAM,     JSON_OBJECT, 200),
      Arguments.of(OKAPI_HEAD_TENANT, APP_JSON,   STAR,       MT_APP_JSON, QUE_OFF_PARAM,     JSON_OBJECT, 200)
    );
  }

  /**
   * Helper function for parameterized test providing tests with headers, body, and status for search workflows end point.
   *
   * This is intended to be used for when the correct HTTP method is being used in the request.
   *
   * @return
   *   The arguments array stream with the stream columns as:
   *     - HttpHeaders headers.
   *     - String contentType (Content-Type request).
   *     - String accept (ask for this Content-Type on response).
   *       String mediaType (response Content-Type).
   *     - MultiValueMap<String, String> parameters.
   *     - String body (request body).
   *     - int status (response HTTP status code).
   */
  private static Stream<Arguments> provideHeadersBodyStatusForSearchWorkflows() {
    Stream<Arguments> stream1 = Stream.of(
      Arguments.of(OKAPI_HEAD_TENANT, APP_JSON,   APP_SCHEMA, MT_APP_JSON, QUE_PARAM,         JSON_OBJECT, 406),
      Arguments.of(OKAPI_HEAD_TENANT, APP_JSON,   APP_JSON,   MT_APP_JSON, QUE_PARAM,         JSON_OBJECT, 200),
      Arguments.of(OKAPI_HEAD_TENANT, APP_JSON,   TEXT_PLAIN, MT_APP_JSON, QUE_PARAM,         JSON_OBJECT, 406),
      Arguments.of(OKAPI_HEAD_TENANT, APP_JSON,   APP_STREAM, MT_APP_JSON, QUE_PARAM,         JSON_OBJECT, 406),
      Arguments.of(OKAPI_HEAD_TENANT, APP_JSON,   NULL_STR,   MT_APP_JSON, QUE_PARAM,         JSON_OBJECT, 200),
      Arguments.of(OKAPI_HEAD_TENANT, APP_JSON,   APP_STAR,   MT_APP_JSON, QUE_PARAM,         JSON_OBJECT, 200),
      Arguments.of(OKAPI_HEAD_TENANT, APP_JSON,   STAR,       MT_APP_JSON, QUE_PARAM,         JSON_OBJECT, 200),
      Arguments.of(OKAPI_HEAD_TENANT, TEXT_PLAIN, APP_SCHEMA, MT_APP_JSON, QUE_PARAM,         PLAIN_BODY,  406),
      Arguments.of(OKAPI_HEAD_TENANT, TEXT_PLAIN, APP_JSON,   MT_APP_JSON, QUE_PARAM,         PLAIN_BODY,  200),
      Arguments.of(OKAPI_HEAD_TENANT, TEXT_PLAIN, TEXT_PLAIN, MT_APP_JSON, QUE_PARAM,         PLAIN_BODY,  406),
      Arguments.of(OKAPI_HEAD_TENANT, TEXT_PLAIN, APP_STREAM, MT_APP_JSON, QUE_PARAM,         PLAIN_BODY,  406),
      Arguments.of(OKAPI_HEAD_TENANT, TEXT_PLAIN, NULL_STR,   MT_APP_JSON, QUE_PARAM,         PLAIN_BODY,  200),
      Arguments.of(OKAPI_HEAD_TENANT, TEXT_PLAIN, STAR,       MT_APP_JSON, QUE_PARAM,         PLAIN_BODY,  200),
      Arguments.of(OKAPI_HEAD_TENANT, TEXT_PLAIN, APP_STAR,   MT_APP_JSON, QUE_PARAM,         PLAIN_BODY,  200),
      Arguments.of(OKAPI_HEAD_TENANT, APP_STREAM, APP_SCHEMA, MT_APP_JSON, QUE_PARAM,         JSON_OBJECT, 406),
      Arguments.of(OKAPI_HEAD_TENANT, APP_STREAM, APP_JSON,   MT_APP_JSON, QUE_PARAM,         JSON_OBJECT, 200),
      Arguments.of(OKAPI_HEAD_TENANT, APP_STREAM, TEXT_PLAIN, MT_APP_JSON, QUE_PARAM,         JSON_OBJECT, 406),
      Arguments.of(OKAPI_HEAD_TENANT, APP_STREAM, APP_STREAM, MT_APP_JSON, QUE_PARAM,         JSON_OBJECT, 406),
      Arguments.of(OKAPI_HEAD_TENANT, APP_STREAM, NULL_STR,   MT_APP_JSON, QUE_PARAM,         JSON_OBJECT, 200),
      Arguments.of(OKAPI_HEAD_TENANT, APP_STREAM, APP_STAR,   MT_APP_JSON, QUE_PARAM,         JSON_OBJECT, 200),
      Arguments.of(OKAPI_HEAD_TENANT, APP_STREAM, STAR,       MT_APP_JSON, QUE_PARAM,         JSON_OBJECT, 200),
      Arguments.of(OKAPI_HEAD_TENANT, APP_JSON,   APP_SCHEMA, MT_APP_JSON, QUE_LIM_PARAM,     JSON_OBJECT, 406),
      Arguments.of(OKAPI_HEAD_TENANT, APP_JSON,   APP_JSON,   MT_APP_JSON, QUE_LIM_PARAM,     JSON_OBJECT, 200),
      Arguments.of(OKAPI_HEAD_TENANT, APP_JSON,   TEXT_PLAIN, MT_APP_JSON, QUE_LIM_PARAM,     JSON_OBJECT, 406),
      Arguments.of(OKAPI_HEAD_TENANT, APP_JSON,   APP_STREAM, MT_APP_JSON, QUE_LIM_PARAM,     JSON_OBJECT, 406),
      Arguments.of(OKAPI_HEAD_TENANT, APP_JSON,   NULL_STR,   MT_APP_JSON, QUE_LIM_PARAM,     JSON_OBJECT, 200),
      Arguments.of(OKAPI_HEAD_TENANT, APP_JSON,   APP_STAR,   MT_APP_JSON, QUE_LIM_PARAM,     JSON_OBJECT, 200),
      Arguments.of(OKAPI_HEAD_TENANT, APP_JSON,   STAR,       MT_APP_JSON, QUE_LIM_PARAM,     JSON_OBJECT, 200),
      Arguments.of(OKAPI_HEAD_TENANT, APP_JSON,   APP_SCHEMA, MT_APP_JSON, QUE_OFF_LIM_PARAM, JSON_OBJECT, 406),
      Arguments.of(OKAPI_HEAD_TENANT, APP_JSON,   APP_JSON,   MT_APP_JSON, QUE_OFF_LIM_PARAM, JSON_OBJECT, 200),
      Arguments.of(OKAPI_HEAD_TENANT, APP_JSON,   TEXT_PLAIN, MT_APP_JSON, QUE_OFF_LIM_PARAM, JSON_OBJECT, 406),
      Arguments.of(OKAPI_HEAD_TENANT, APP_JSON,   APP_STREAM, MT_APP_JSON, QUE_OFF_LIM_PARAM, JSON_OBJECT, 406),
      Arguments.of(OKAPI_HEAD_TENANT, APP_JSON,   NULL_STR,   MT_APP_JSON, QUE_OFF_LIM_PARAM, JSON_OBJECT, 200),
      Arguments.of(OKAPI_HEAD_TENANT, APP_JSON,   APP_STAR,   MT_APP_JSON, QUE_OFF_LIM_PARAM, JSON_OBJECT, 200),
      Arguments.of(OKAPI_HEAD_TENANT, APP_JSON,   STAR,       MT_APP_JSON, QUE_OFF_LIM_PARAM, JSON_OBJECT, 200),
      Arguments.of(OKAPI_HEAD_TENANT, APP_JSON,   APP_SCHEMA, MT_APP_JSON, QUE_OFF_PARAM,     JSON_OBJECT, 406),
      Arguments.of(OKAPI_HEAD_TENANT, APP_JSON,   APP_JSON,   MT_APP_JSON, QUE_OFF_PARAM,     JSON_OBJECT, 200),
      Arguments.of(OKAPI_HEAD_TENANT, APP_JSON,   TEXT_PLAIN, MT_APP_JSON, QUE_OFF_PARAM,     JSON_OBJECT, 406),
      Arguments.of(OKAPI_HEAD_TENANT, APP_JSON,   APP_STREAM, MT_APP_JSON, QUE_OFF_PARAM,     JSON_OBJECT, 406),
      Arguments.of(OKAPI_HEAD_TENANT, APP_JSON,   NULL_STR,   MT_APP_JSON, QUE_OFF_PARAM,     JSON_OBJECT, 200),
      Arguments.of(OKAPI_HEAD_TENANT, APP_JSON,   APP_STAR,   MT_APP_JSON, QUE_OFF_PARAM,     JSON_OBJECT, 200),
      Arguments.of(OKAPI_HEAD_TENANT, APP_JSON,   STAR,       MT_APP_JSON, QUE_OFF_PARAM,     JSON_OBJECT, 200)
    );

    Object[] params = { NO_PARAM, LIM_PARAM, OFF_LIM_PARAM, OFF_PARAM };

    Stream<Arguments> stream2 = Stream.concat(stream1, buildAppJsonBodyStatus(OKAPI_HEAD_TOKEN, params));
    return Stream.concat(stream2, buildAppJsonBodyStatus(OKAPI_HEAD_TENANT, params));
  }

  /**
   * Helper function for parameterized test providing tests with headers, body, and status for search workflows end point.
   *
   * This is intended to be used for when the correct HTTP method is being used in the request.
   *
   * @return
   *   The arguments array stream with the stream columns as:
   *     - HttpHeaders headers.
   *     - String contentType (Content-Type request).
   *     - String accept (ask for this Content-Type on response).
   *       String mediaType (response Content-Type).
   *     - MultiValueMap<String, String> parameters.
   *     - String body (request body).
   *     - int status (response HTTP status code).
   */
  private static Stream<Arguments> provideHeadersBodyStatusForImportWorkflows() {
    return Stream.of(
      Arguments.of(OKAPI_HEAD_TENANT, APP_JSON,       APP_SCHEMA, MT_APP_JSON, NO_PARAM, JSON_OBJECT, 415),
      Arguments.of(OKAPI_HEAD_TENANT, APP_JSON,       APP_JSON,   MT_APP_JSON, NO_PARAM, JSON_OBJECT, 415),
      Arguments.of(OKAPI_HEAD_TENANT, APP_JSON,       TEXT_PLAIN, MT_APP_JSON, NO_PARAM, JSON_OBJECT, 415),
      Arguments.of(OKAPI_HEAD_TENANT, APP_JSON,       APP_STREAM, MT_APP_JSON, NO_PARAM, JSON_OBJECT, 415),
      Arguments.of(OKAPI_HEAD_TENANT, APP_JSON,       NULL_STR,   MT_APP_JSON, NO_PARAM, JSON_OBJECT, 415),
      Arguments.of(OKAPI_HEAD_TENANT, APP_JSON,       APP_STAR,   MT_APP_JSON, NO_PARAM, JSON_OBJECT, 415),
      Arguments.of(OKAPI_HEAD_TENANT, APP_JSON,       STAR,       MT_APP_JSON, NO_PARAM, JSON_OBJECT, 415),
      Arguments.of(OKAPI_HEAD_TENANT, TEXT_PLAIN,     APP_SCHEMA, MT_APP_JSON, NO_PARAM, PLAIN_BODY,  415),
      Arguments.of(OKAPI_HEAD_TENANT, TEXT_PLAIN,     APP_JSON,   MT_APP_JSON, NO_PARAM, PLAIN_BODY,  415),
      Arguments.of(OKAPI_HEAD_TENANT, TEXT_PLAIN,     TEXT_PLAIN, MT_APP_JSON, NO_PARAM, PLAIN_BODY,  415),
      Arguments.of(OKAPI_HEAD_TENANT, TEXT_PLAIN,     APP_STREAM, MT_APP_JSON, NO_PARAM, PLAIN_BODY,  415),
      Arguments.of(OKAPI_HEAD_TENANT, TEXT_PLAIN,     NULL_STR,   MT_APP_JSON, NO_PARAM, PLAIN_BODY,  415),
      Arguments.of(OKAPI_HEAD_TENANT, TEXT_PLAIN,     STAR,       MT_APP_JSON, NO_PARAM, PLAIN_BODY,  415),
      Arguments.of(OKAPI_HEAD_TENANT, TEXT_PLAIN,     APP_STAR,   MT_APP_JSON, NO_PARAM, PLAIN_BODY,  415),
      Arguments.of(OKAPI_HEAD_TENANT, APP_STREAM,     APP_SCHEMA, MT_APP_JSON, NO_PARAM, JSON_OBJECT, 415),
      Arguments.of(OKAPI_HEAD_TENANT, APP_STREAM,     APP_JSON,   MT_APP_JSON, NO_PARAM, JSON_OBJECT, 415),
      Arguments.of(OKAPI_HEAD_TENANT, APP_STREAM,     TEXT_PLAIN, MT_APP_JSON, NO_PARAM, JSON_OBJECT, 415),
      Arguments.of(OKAPI_HEAD_TENANT, APP_STREAM,     APP_STREAM, MT_APP_JSON, NO_PARAM, JSON_OBJECT, 415),
      Arguments.of(OKAPI_HEAD_TENANT, APP_STREAM,     NULL_STR,   MT_APP_JSON, NO_PARAM, JSON_OBJECT, 415),
      Arguments.of(OKAPI_HEAD_TENANT, APP_STREAM,     APP_STAR,   MT_APP_JSON, NO_PARAM, JSON_OBJECT, 415),
      Arguments.of(OKAPI_HEAD_TENANT, APP_STREAM,     STAR,       MT_APP_JSON, NO_PARAM, JSON_OBJECT, 415),
      Arguments.of(OKAPI_HEAD_TENANT, MULTIPART_FORM, APP_SCHEMA, MT_APP_JSON, NO_PARAM, JSON_OBJECT, 406),
      Arguments.of(OKAPI_HEAD_TENANT, MULTIPART_FORM, APP_JSON,   MT_APP_JSON, NO_PARAM, JSON_OBJECT, 201),
      Arguments.of(OKAPI_HEAD_TENANT, MULTIPART_FORM, TEXT_PLAIN, MT_APP_JSON, NO_PARAM, JSON_OBJECT, 406),
      Arguments.of(OKAPI_HEAD_TENANT, MULTIPART_FORM, APP_STREAM, MT_APP_JSON, NO_PARAM, JSON_OBJECT, 406),
      Arguments.of(OKAPI_HEAD_TENANT, MULTIPART_FORM, NULL_STR,   MT_APP_JSON, NO_PARAM, JSON_OBJECT, 201),
      Arguments.of(OKAPI_HEAD_TENANT, MULTIPART_FORM, APP_STAR,   MT_APP_JSON, NO_PARAM, JSON_OBJECT, 201),
      Arguments.of(OKAPI_HEAD_TENANT, MULTIPART_FORM, STAR,       MT_APP_JSON, NO_PARAM, JSON_OBJECT, 201)
    );
  }

  /**
   * Build a common status that returns either HTTP 400 or HTTP 406.
   *
   * @param headers
   *   The HTTP headers to use.
   * @param params
   *   The array of parameter sets that would result in a HTTP 400 (when a 406 is not returned).
   *   Set to an empty array to not have 400 return codes.
   *
   * @return The stream of combinations.
   */
  private static Stream<Arguments> buildAppJsonBodyStatus(HttpHeaders headers, Object[] params) {
    String[] contentTypes = { APP_JSON, TEXT_PLAIN, APP_STREAM };
    String[] bodys = { JSON_OBJECT, PLAIN_BODY, JSON_OBJECT };
    String[] accepts = { APP_JSON, NULL_STR, APP_STAR, STAR };
    String[] acceptsInvalid = { APP_SCHEMA, TEXT_PLAIN, APP_STREAM };
    MediaType[] mediaTypes = { MT_APP_JSON };

    Stream<Arguments> stream1 = buildArguments1(headers, contentTypes, acceptsInvalid, mediaTypes, params, bodys, 406);

    if (params.length == 0) {
      return stream1;
    }

    Stream<Arguments> stream2 = buildArguments1(headers, contentTypes, accepts, mediaTypes, params, bodys, 400);
    return Stream.concat(stream1, stream2);
  }

  /**
   * Build a common status that returns HTTP 405.
   *
   * This is generally for HTTP DELETE, GET, PATCH, POST, and PUT.
   *
   * @param headers
   *   The HTTP headers to use.
   * @param params
   *   The array of parameter sets that would result in a HTTP 405.
   *
   * @return The stream of combinations.
   */
  private static Stream<Arguments> buildHttpDeletePatchPostPut(HttpHeaders headers, Object[] params) {
    String[] contentTypes = { APP_JSON, TEXT_PLAIN, APP_STREAM };
    String[] bodys = { JSON_OBJECT, PLAIN_BODY, JSON_OBJECT };
    String[] accepts = { APP_RAML, APP_SCHEMA, APP_JSON, TEXT_PLAIN, APP_STREAM, NULL_STR, APP_STAR, STAR };
    MediaType[] mediaTypes = { MT_APP_JSON };

    Stream<Arguments> stream1 = buildArguments2(DELETE, headers, contentTypes, accepts, mediaTypes, params, bodys, 405);
    Stream<Arguments> stream2 = buildArguments2(PATCH, headers, contentTypes, accepts, mediaTypes, params, bodys, 405);
    Stream<Arguments> stream3 = Stream.concat(stream1, stream2);

    stream1 = buildArguments2(POST, headers, contentTypes, accepts, mediaTypes, params, bodys, 405);
    stream2 = Stream.concat(stream3, stream1);

    stream1 = buildArguments2(PUT, headers, contentTypes, accepts, mediaTypes, params, bodys, 405);
    return Stream.concat(stream2, stream1);
  }

  /**
   * Build a common status that returns HTTP 405.
   *
   * This is generally for HTTP DELETE, GET, PATCH, POST, and PUT.
   *
   * @param headers
   *   The HTTP headers to use.
   * @param params
   *   The array of parameter sets that would result in a HTTP 405.
   *
   * @return The stream of combinations.
   */
  private static Stream<Arguments> buildHttpDeleteGetPatchPost(HttpHeaders headers, Object[] params) {
    String[] contentTypes = { APP_JSON, TEXT_PLAIN, APP_STREAM };
    String[] bodys = { JSON_OBJECT, PLAIN_BODY, JSON_OBJECT };
    String[] accepts = { APP_RAML, APP_SCHEMA, APP_JSON, TEXT_PLAIN, APP_STREAM, NULL_STR, APP_STAR, STAR };
    MediaType[] mediaTypes = { MT_APP_JSON };

    Stream<Arguments> stream1 = buildArguments2(GET, headers, contentTypes, accepts, mediaTypes, params, bodys, 405);
    Stream<Arguments> stream2 = buildArguments2(DELETE, headers, contentTypes, accepts, mediaTypes, params, bodys, 405);
    Stream<Arguments> stream3 = Stream.concat(stream1, stream2);

    stream1 = buildArguments2(PATCH, headers, contentTypes, accepts, mediaTypes, params, bodys, 405);
    stream2 = Stream.concat(stream3, stream1);

    stream1 = buildArguments2(POST, headers, contentTypes, accepts, mediaTypes, params, bodys, 405);
    return Stream.concat(stream2, stream1);
  }

  /**
   * Build a common status that returns HTTP 405.
   *
   * This is generally for HTTP GET, PATCH, POST, POST, and PUT.
   *
   * @param headers
   *   The HTTP headers to use.
   * @param params
   *   The array of parameter sets that would result in a HTTP 405.
   *
   * @return The stream of combinations.
   */
  private static Stream<Arguments> buildHttpGetPatchPostPut(HttpHeaders headers, Object[] params) {
    String[] contentTypes = { APP_JSON, TEXT_PLAIN, APP_STREAM };
    String[] bodys = { JSON_OBJECT, PLAIN_BODY, JSON_OBJECT };
    String[] accepts = { APP_RAML, APP_SCHEMA, APP_JSON, TEXT_PLAIN, APP_STREAM, NULL_STR, APP_STAR, STAR };
    MediaType[] mediaTypes = { MT_APP_JSON };

    Stream<Arguments> stream1 = buildArguments2(GET, headers, contentTypes, accepts, mediaTypes, params, bodys, 405);
    Stream<Arguments> stream2 = buildArguments2(PATCH, headers, contentTypes, accepts, mediaTypes, params, bodys, 405);
    Stream<Arguments> stream3 = Stream.concat(stream1, stream2);

    stream1 = buildArguments2(POST, headers, contentTypes, accepts, mediaTypes, params, bodys, 405);
    stream2 = Stream.concat(stream3, stream1);

    stream1 = buildArguments2(PUT, headers, contentTypes, accepts, mediaTypes, params, bodys, 405);
    return Stream.concat(stream2, stream1);
  }

  /**
   * Build a common status that returns HTTP 405.
   *
   * This is generally for HTTP DELETE, GET, PATCH, POST, and PUT.
   *
   * @param headers
   *   The HTTP headers to use.
   * @param params
   *   The array of parameter sets that would result in a HTTP 405.
   *
   * @return The stream of combinations.
   */
  private static Stream<Arguments> buildHttpDeleteGetPatchPut(HttpHeaders headers, Object[] params) {
    String[] contentTypes = { APP_JSON, TEXT_PLAIN, APP_STREAM };
    String[] bodys = { JSON_OBJECT, PLAIN_BODY, JSON_OBJECT };
    String[] accepts = { APP_RAML, APP_SCHEMA, APP_JSON, TEXT_PLAIN, APP_STREAM, NULL_STR, APP_STAR, STAR };
    MediaType[] mediaTypes = { MT_APP_JSON };

    Stream<Arguments> stream1 = buildArguments2(GET, headers, contentTypes, accepts, mediaTypes, params, bodys, 405);
    Stream<Arguments> stream2 = buildArguments2(DELETE, headers, contentTypes, accepts, mediaTypes, params, bodys, 405);
    Stream<Arguments> stream3 = Stream.concat(stream1, stream2);

    stream1 = buildArguments2(PATCH, headers, contentTypes, accepts, mediaTypes, params, bodys, 405);
    stream2 = Stream.concat(stream3, stream1);

    stream1 = buildArguments2(PUT, headers, contentTypes, accepts, mediaTypes, params, bodys, 405);
    return Stream.concat(stream2, stream1);
  }

  /**
   * Build a common status that returns HTTP 405.
   *
   * This is generally for HTTP DELETE, PATCH, POST, and PUT.
   *
   * @param headers
   *   The HTTP headers to use.
   * @param params
   *   The array of parameter sets that would result in a HTTP 405.
   *
   * @return The stream of combinations.
   */
  private static Stream<Arguments> buildHttpDeletePatchPut(HttpHeaders headers, Object[] params) {
    String[] contentTypes = { APP_JSON, TEXT_PLAIN, APP_STREAM };
    String[] bodys = { JSON_OBJECT, PLAIN_BODY, JSON_OBJECT };
    String[] accepts = { APP_RAML, APP_SCHEMA, APP_JSON, TEXT_PLAIN, APP_STREAM, NULL_STR, APP_STAR, STAR };
    MediaType[] mediaTypes = { MT_APP_JSON };

    Stream<Arguments> stream1 = buildArguments2(DELETE, headers, contentTypes, accepts, mediaTypes, params, bodys, 405);
    Stream<Arguments> stream2 = buildArguments2(PATCH, headers, contentTypes, accepts, mediaTypes, params, bodys, 405);
    Stream<Arguments> stream3 = Stream.concat(stream1, stream2);

    stream1 = buildArguments2(PUT, headers, contentTypes, accepts, mediaTypes, params, bodys, 405);
    return Stream.concat(stream3, stream1);
  }

}
