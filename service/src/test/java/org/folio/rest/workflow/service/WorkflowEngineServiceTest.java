package org.folio.rest.workflow.service;

import static org.folio.spring.test.mock.MockMvcConstant.OKAPI_TENANT;
import static org.folio.spring.test.mock.MockMvcConstant.OKAPI_TOKEN;
import static org.folio.spring.test.mock.MockMvcConstant.UUID;
import static org.folio.spring.test.mock.MockMvcConstant.VALUE;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.contains;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.util.ReflectionTestUtils.setField;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.folio.rest.workflow.dto.WorkflowDto;
import org.folio.rest.workflow.dto.WorkflowOperationalDto;
import org.folio.rest.workflow.exception.WorkflowDeploymentNotFound;
import org.folio.rest.workflow.exception.WorkflowEngineServiceException;
import org.folio.rest.workflow.exception.WorkflowNotFoundException;
import org.folio.rest.workflow.model.Workflow;
import org.folio.rest.workflow.model.repo.WorkflowRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@ExtendWith(SpringExtension.class)
@ExtendWith(MockitoExtension.class)
class WorkflowEngineServiceTest {

  private static final RestClientException RC_EXC = new RestClientException("Trigger Failure");

  private static final String DEACTIVATE = "workflow-engine/workflows/deactivate";
  private static final String HISTORY_INCIDENT = "history/incident";
  private static final String HISTORY_INSTANCE = "history/process-instance";
  private static final String PROCESS_DEFINITION = "process-definition";

  @Mock
  private WorkflowRepo workflowRepo;

  @Mock
  private RestTemplate restTemplate;

  private WorkflowAsDto workflow;

  private WorkflowAsOperationalDto workflowOperational;

  private WorkflowEngineService workflowEngineService;

  private JsonMapper mapper;

  @BeforeEach
  void beforeEach() {
    mapper = new JsonMapper();
    workflowEngineService = new WorkflowEngineService(workflowRepo, mapper, new RestTemplateBuilder());

    workflow = new WorkflowAsDto();
    workflow.setId(UUID);
    workflow.setDeploymentId(UUID);
    workflow.setName(VALUE);

    workflowOperational = new WorkflowAsOperationalDto();
    workflowOperational.setId(UUID);
    workflowOperational.setDeploymentId(UUID);
    workflowOperational.setName(VALUE);
    workflowOperational.setVersionTag(VALUE);

    setField(workflowEngineService, "workflowRepo", workflowRepo);
    setField(workflowEngineService, "restTemplate", restTemplate);
    setField(workflowEngineService, "tenantHeaderName", OKAPI_TENANT);
    setField(workflowEngineService, "tokenHeaderName", OKAPI_TOKEN);
  }

  @Test
  void activateWorksTest() throws WorkflowEngineServiceException {
    WorkflowDto workflowDto = (WorkflowDto) workflow;
    ResponseEntity<Workflow> responseEntity = new ResponseEntity<>(HttpStatus.OK);
    setField(responseEntity, "body", workflow);

    when(workflowRepo.getViewById(anyString(), ArgumentMatchers.<Class<WorkflowDto>>any())).thenReturn(workflowDto);

    when(restTemplate.exchange(anyString(), any(HttpMethod.class), any(HttpEntity.class), eq(Workflow.class), anyMap()))
      .thenReturn(responseEntity);

    when(workflowRepo.save(any())).thenReturn(workflow);

    workflowEngineService.activate(UUID, OKAPI_TENANT, OKAPI_TOKEN);

    verify(workflowRepo).save(any());
    verify(workflowRepo, never()).deleteById(anyString());
  }

  @Test
  void activateWithoutHeadersTest() throws WorkflowEngineServiceException {
    WorkflowDto workflowDto = (WorkflowDto) workflow;
    ResponseEntity<Workflow> responseEntity = new ResponseEntity<>(HttpStatus.OK);
    setField(responseEntity, "body", workflow);

    when(workflowRepo.getViewById(anyString(), ArgumentMatchers.<Class<WorkflowDto>>any())).thenReturn(workflowDto);

    when(restTemplate.exchange(anyString(), any(HttpMethod.class), any(HttpEntity.class), eq(Workflow.class), anyMap()))
      .thenReturn(responseEntity);

    when(workflowRepo.save(any())).thenReturn(workflow);

    workflowEngineService.activate(UUID, null, null);

    verify(workflowRepo).save(any());
    verify(workflowRepo, never()).deleteById(anyString());
  }

  @Test
  void deactivateWorksTest() throws WorkflowEngineServiceException {
    WorkflowDto workflowDto = (WorkflowDto) workflow;
    ResponseEntity<Workflow> responseEntity = new ResponseEntity<>(HttpStatus.OK);
    setField(responseEntity, "body", workflow);

    when(workflowRepo.getViewById(anyString(), ArgumentMatchers.<Class<WorkflowDto>>any())).thenReturn(workflowDto);

    when(restTemplate.exchange(anyString(), any(HttpMethod.class), any(HttpEntity.class), eq(Workflow.class), anyMap()))
      .thenReturn(responseEntity);

    when(workflowRepo.save(any())).thenReturn(workflow);

    workflowEngineService.deactivate(UUID, OKAPI_TENANT, OKAPI_TOKEN);

    verify(workflowRepo).save(any());
    verify(workflowRepo, never()).deleteById(anyString());
  }

  @Test
  void deleteWorksTest() throws WorkflowEngineServiceException {

    final ObjectNode objectNode = JsonNodeFactory.instance.objectNode();
    objectNode.put("id", UUID);

    ArrayNode arrayNodeSingle = JsonNodeFactory.instance.arrayNode();
    arrayNodeSingle.add(objectNode);

    ResponseEntity<ArrayNode> responseArray = new ResponseEntity<>(arrayNodeSingle, HttpStatus.OK);
    ResponseEntity<Workflow> responseEntity = new ResponseEntity<>(HttpStatus.OK);
    setField(responseEntity, "body", workflow);

    final WorkflowDto workflowDto = (WorkflowDto) workflow;

    when(workflowRepo.getViewById(anyString(), eq(WorkflowOperationalDto.class))).thenReturn(workflowOperational);
    when(workflowRepo.getViewById(anyString(), eq(WorkflowDto.class))).thenReturn(workflowDto);

    when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), any(HttpEntity.class), eq(ArrayNode.class), anyMap()))
      .thenReturn(responseArray);

    when(restTemplate.exchange(contains(DEACTIVATE), any(HttpMethod.class), any(HttpEntity.class), eq(Workflow.class), anyMap()))
      .thenReturn(responseEntity);

    when(workflowRepo.save(any())).thenReturn(workflow);
    doNothing().when(workflowRepo).deleteById(anyString());

    workflowEngineService.delete(UUID, OKAPI_TENANT, OKAPI_TOKEN);

    verify(workflowRepo).save(any());
    verify(workflowRepo).deleteById(anyString());
  }

  @Test
  void deleteNotActiveWorksTest() throws WorkflowEngineServiceException {

    ArrayNode arrayNodeSingle = JsonNodeFactory.instance.arrayNode();

    ResponseEntity<ArrayNode> responseArray = new ResponseEntity<>(arrayNodeSingle, HttpStatus.OK);

    when(workflowRepo.getViewById(anyString(), eq(WorkflowOperationalDto.class))).thenReturn(workflowOperational);

    when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), any(HttpEntity.class), eq(ArrayNode.class), anyMap()))
      .thenReturn(responseArray);

    doNothing().when(workflowRepo).deleteById(anyString());

    workflowEngineService.delete(UUID, OKAPI_TENANT, OKAPI_TOKEN);

    verify(workflowRepo).deleteById(anyString());
  }

  @Test
  void deleteThrowsExceptionUnableGetUpdatedTest() {

    final ObjectNode objectNode = JsonNodeFactory.instance.objectNode();
    objectNode.put("id", UUID);

    ArrayNode arrayNodeSingle = JsonNodeFactory.instance.arrayNode();
    arrayNodeSingle.add(objectNode);

    ResponseEntity<ArrayNode> responseArray = new ResponseEntity<>(arrayNodeSingle, HttpStatus.OK);

    ResponseEntity<Workflow> workflowEntity = new ResponseEntity<>(HttpStatus.ACCEPTED);

    setField(workflowEntity, "body", workflow);

    final WorkflowDto workflowDto = (WorkflowDto) workflow;

    when(workflowRepo.getViewById(anyString(), eq(WorkflowOperationalDto.class))).thenReturn(workflowOperational);
    when(workflowRepo.getViewById(anyString(), eq(WorkflowDto.class))).thenReturn(workflowDto);

    when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), any(HttpEntity.class), eq(ArrayNode.class), anyMap()))
      .thenReturn(responseArray);

    when(restTemplate.exchange(contains(DEACTIVATE), any(HttpMethod.class), any(HttpEntity.class), eq(Workflow.class), anyMap()))
      .thenReturn(workflowEntity);

    assertThrows(WorkflowEngineServiceException.class, () ->
      workflowEngineService.delete(UUID, OKAPI_TENANT, OKAPI_TOKEN)
    );

    verify(workflowRepo, never()).save(any());
    verify(workflowRepo, never()).deleteById(anyString());
  }

  @Test
  void deleteThrowsExceptionFailedToSaveTest() {

    final ObjectNode objectNode = JsonNodeFactory.instance.objectNode();
    objectNode.put("id", UUID);

    ArrayNode arrayNodeSingle = JsonNodeFactory.instance.arrayNode();
    arrayNodeSingle.add(objectNode);

    ResponseEntity<ArrayNode> processEntity = new ResponseEntity<>(arrayNodeSingle, HttpStatus.OK);
    ResponseEntity<Workflow> workflowEntity = new ResponseEntity<>(HttpStatus.OK);
    setField(workflowEntity, "body", workflow);

    final WorkflowDto workflowDto = (WorkflowDto) workflow;

    when(workflowRepo.getViewById(anyString(), eq(WorkflowOperationalDto.class))).thenReturn(workflowOperational);
    when(workflowRepo.getViewById(anyString(), eq(WorkflowDto.class))).thenReturn(workflowDto);

    when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), any(HttpEntity.class), eq(ArrayNode.class), anyMap()))
      .thenReturn(processEntity);

    when(restTemplate.exchange(contains(DEACTIVATE), any(HttpMethod.class), any(HttpEntity.class), eq(Workflow.class), anyMap()))
      .thenReturn(workflowEntity);

    when(workflowRepo.save(any())).thenThrow(RC_EXC);

    assertThrows(WorkflowEngineServiceException.class, () -> {
      workflowEngineService.delete(UUID, OKAPI_TENANT, OKAPI_TOKEN);
    });

    verify(workflowRepo).save(any());
    verify(workflowRepo, never()).deleteById(anyString());
  }

  @Test
  void deleteThrowsExceptionFailedToSendWithNullResponseBodyTest() {

    ResponseEntity<ArrayNode> processEntity = new ResponseEntity<>(HttpStatus.OK);
    ResponseEntity<Workflow> workflowEntity = new ResponseEntity<>(HttpStatus.OK);
    setField(workflowEntity, "body", null);

    ObjectNode processNode = mapper.createObjectNode();
    processNode.put("id", UUID);

    ArrayNode processArrayNode = mapper.createArrayNode();
    processArrayNode.add(processNode);
    setField(processEntity, "body", processArrayNode);

    final WorkflowDto workflowDto = (WorkflowDto) workflow;

    when(workflowRepo.getViewById(anyString(), eq(WorkflowOperationalDto.class))).thenReturn(workflowOperational);
    when(workflowRepo.getViewById(anyString(), eq(WorkflowDto.class))).thenReturn(workflowDto);

    when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), any(HttpEntity.class), eq(ArrayNode.class), anyMap()))
      .thenReturn(processEntity);

    when(restTemplate.exchange(contains(DEACTIVATE), any(HttpMethod.class), any(HttpEntity.class), eq(Workflow.class), anyMap()))
      .thenReturn(workflowEntity);

    assertThrows(WorkflowEngineServiceException.class, () -> {
      workflowEngineService.delete(UUID, OKAPI_TENANT, OKAPI_TOKEN);
    });

    verify(workflowRepo, never()).save(any());
    verify(workflowRepo, never()).deleteById(anyString());
  }

  @Test
  void existsWorksTest() {
    when(workflowRepo.existsById(anyString())).thenReturn(true);

    assertDoesNotThrow(() -> workflowEngineService.exists(UUID));
  }

  @Test
  void existsThrowsExceptionWorkflowNotFoundTest() {
    when(workflowRepo.existsById(anyString())).thenReturn(false);

    assertThrows(WorkflowNotFoundException.class, () -> {
      workflowEngineService.exists(UUID);
    });
  }

  @Test
  void startWorksTest() throws WorkflowDeploymentNotFound, WorkflowEngineServiceException, WorkflowNotFoundException {

    final ObjectNode objectNode = JsonNodeFactory.instance.objectNode();
    objectNode.put("id", UUID);

    final ArrayNode arrayNodeSingle = JsonNodeFactory.instance.arrayNode();
    arrayNodeSingle.add(objectNode);

    final ResponseEntity<JsonNode> workflowEntity = new ResponseEntity<>(HttpStatus.OK);
    final ResponseEntity<ArrayNode> processEntity = new ResponseEntity<>(arrayNodeSingle, HttpStatus.OK);
    final JsonNode context = JsonNodeFactory.instance.objectNode();

    final ArrayNode arrayNode = JsonNodeFactory.instance.arrayNode();
    arrayNode.add(objectNode);
    setField(workflowEntity, "body", arrayNode);

    when(workflowRepo.getViewById(anyString(), eq(WorkflowOperationalDto.class))).thenReturn(workflowOperational);

    when(restTemplate.exchange(contains(PROCESS_DEFINITION), eq(HttpMethod.GET), any(HttpEntity.class), eq(ArrayNode.class), anyMap()))
      .thenReturn(processEntity);

    when(restTemplate.exchange(contains(PROCESS_DEFINITION), eq(HttpMethod.POST), any(HttpEntity.class), eq(JsonNode.class), anyMap()))
      .thenReturn(workflowEntity);

    final JsonNode response = workflowEngineService.start(UUID, OKAPI_TENANT, OKAPI_TOKEN, context);
    assertEquals(arrayNode, response);
  }

  @Test
  void startThrowsExceptionHttpNotOkTest() {
    WorkflowOperationalDto workflowOperationalDto = (WorkflowOperationalDto) workflowOperational;
    ResponseEntity<ArrayNode> processEntity = new ResponseEntity<>(HttpStatus.RESET_CONTENT);

    JsonNode context = mapper.createObjectNode();
    ObjectNode objectNode = mapper.createObjectNode();
    objectNode.put("id", UUID);

    ArrayNode processNode = mapper.createArrayNode();
    processNode.add(objectNode);
    setField(processEntity, "body", processNode);

    when(workflowRepo.getViewById(anyString(), eq(WorkflowOperationalDto.class)))
      .thenReturn(workflowOperationalDto);

    when(restTemplate.exchange(contains(PROCESS_DEFINITION), eq(HttpMethod.GET), any(HttpEntity.class), eq(ArrayNode.class), anyMap()))
      .thenReturn(processEntity);

    assertThrows(WorkflowEngineServiceException.class, () -> {
      workflowEngineService.start(UUID, OKAPI_TENANT, OKAPI_TOKEN, context);
    });
  }

  @Test
  void startThrowsExceptionOnBadExchangeTest() {
    WorkflowOperationalDto workflowOperationalDto = (WorkflowOperationalDto) workflowOperational;
    ResponseEntity<ArrayNode> processEntity = new ResponseEntity<>(HttpStatus.OK);

    JsonNode context = mapper.createObjectNode();
    ObjectNode objectNode = mapper.createObjectNode();
    objectNode.put("id", UUID);

    ArrayNode processNode = mapper.createArrayNode();
    processNode.add(objectNode);
    setField(processEntity, "body", processNode);

    when(workflowRepo.getViewById(anyString(), eq(WorkflowOperationalDto.class)))
      .thenReturn(workflowOperationalDto);

    when(restTemplate.exchange(contains(PROCESS_DEFINITION), eq(HttpMethod.GET), any(HttpEntity.class), eq(ArrayNode.class), anyMap()))
      .thenReturn(processEntity);

    when(restTemplate.exchange(contains(PROCESS_DEFINITION), eq(HttpMethod.POST), any(HttpEntity.class), eq(JsonNode.class), anyMap()))
      .thenThrow(RC_EXC);

    assertThrows(WorkflowEngineServiceException.class, () -> {
      workflowEngineService.start(UUID, OKAPI_TENANT, OKAPI_TOKEN, context);
    });
  }

  @Test
  void startThrowsExceptionDeploymentIdMissingTest() {
    WorkflowOperationalDto workflowOperationalDto = (WorkflowOperationalDto) workflowOperational;
    workflowOperationalDto.setDeploymentId(null);

    ResponseEntity<ArrayNode> responseEntity = new ResponseEntity<>(HttpStatus.RESET_CONTENT);
    JsonNode context = mapper.createObjectNode();
    ObjectNode objectNode = mapper.createObjectNode();

    ArrayNode arrayNode = mapper.createArrayNode();
    arrayNode.add(objectNode);
    setField(responseEntity, "body", arrayNode);

    when(workflowRepo.getViewById(anyString(), ArgumentMatchers.<Class<WorkflowOperationalDto>>any())).thenReturn(workflowOperationalDto);

    final Exception exception = assertThrows(WorkflowEngineServiceException.class, () -> {
      workflowEngineService.start(UUID, OKAPI_TENANT, OKAPI_TOKEN, context);
    });

    assertTrue(exception.getMessage().contains("Deployment ID is missing"));
  }

  @Test
  void startThrowsExceptionWorkflowNullTest() {

    final ObjectNode objectNode = JsonNodeFactory.instance.objectNode();
    objectNode.put("id", UUID);

    final JsonNode context = JsonNodeFactory.instance.objectNode();

    when(workflowRepo.getViewById(anyString(), eq(WorkflowOperationalDto.class))).thenReturn(null);

    assertThrows(WorkflowNotFoundException.class, () -> {
      workflowEngineService.start(UUID, OKAPI_TENANT, OKAPI_TOKEN, context);
    });
  }

  @Test
  void startThrowsExceptionDefinitionNoIdTest() {

    final ObjectNode objectNode = JsonNodeFactory.instance.objectNode();

    final ArrayNode arrayNodeSingle = JsonNodeFactory.instance.arrayNode();
    arrayNodeSingle.add(objectNode);

    final ResponseEntity<ArrayNode> processEntity = new ResponseEntity<>(arrayNodeSingle, HttpStatus.OK);
    final JsonNode context = JsonNodeFactory.instance.objectNode();

    when(workflowRepo.getViewById(anyString(), eq(WorkflowOperationalDto.class))).thenReturn(workflowOperational);

    when(restTemplate.exchange(contains(PROCESS_DEFINITION), eq(HttpMethod.GET), any(HttpEntity.class), eq(ArrayNode.class), anyMap()))
      .thenReturn(processEntity);

    final Exception exception = assertThrows(WorkflowEngineServiceException.class, () -> {
      workflowEngineService.start(UUID, OKAPI_TENANT, OKAPI_TOKEN, context);
    });

    assertTrue(exception.getMessage().contains("has no definition ID"));
  }

  @Test
  void startThrowsExceptionWorkflowNotFoundTest() {

    final ObjectNode objectNode = JsonNodeFactory.instance.objectNode();
    objectNode.put("id", UUID);

    final ArrayNode arrayNodeSingle = JsonNodeFactory.instance.arrayNode();
    arrayNodeSingle.add(objectNode);

    final ResponseEntity<JsonNode> workflowEntity = new ResponseEntity<>(HttpStatus.NOT_FOUND);
    final ResponseEntity<ArrayNode> processEntity = new ResponseEntity<>(arrayNodeSingle, HttpStatus.OK);
    final JsonNode context = JsonNodeFactory.instance.objectNode();

    when(workflowRepo.getViewById(anyString(), eq(WorkflowOperationalDto.class))).thenReturn(workflowOperational);

    when(restTemplate.exchange(contains(PROCESS_DEFINITION), eq(HttpMethod.GET), any(HttpEntity.class), eq(ArrayNode.class), anyMap()))
      .thenReturn(processEntity);

    when(restTemplate.exchange(contains(PROCESS_DEFINITION), eq(HttpMethod.POST), any(HttpEntity.class), eq(JsonNode.class), anyMap()))
      .thenReturn(workflowEntity);

    assertThrows(WorkflowNotFoundException.class, () -> {
      workflowEngineService.start(UUID, OKAPI_TENANT, OKAPI_TOKEN, context);
    });
  }

  @Test
  void startThrowsExceptionWorkflowNotFoundViaViewTest() {

    final JsonNode context = JsonNodeFactory.instance.objectNode();

    when(workflowRepo.getViewById(anyString(), eq(WorkflowOperationalDto.class))).thenReturn(null);

    assertThrows(WorkflowNotFoundException.class, () -> {
      workflowEngineService.start(UUID, OKAPI_TENANT, OKAPI_TOKEN, context);
    });
  }

  @Test
  void startThrowsExceptionWorkflowDeploymentNotFoundViaViewTest() {

    final ObjectNode objectNode = JsonNodeFactory.instance.objectNode();
    objectNode.put("id", UUID);

    final ArrayNode arrayNodeSingle = JsonNodeFactory.instance.arrayNode();

    final ResponseEntity<ArrayNode> processEntity = new ResponseEntity<>(arrayNodeSingle, HttpStatus.OK);
    final JsonNode context = JsonNodeFactory.instance.objectNode();

    when(workflowRepo.getViewById(anyString(), eq(WorkflowOperationalDto.class))).thenReturn(workflowOperational);

    when(restTemplate.exchange(contains(PROCESS_DEFINITION), eq(HttpMethod.GET), any(HttpEntity.class), eq(ArrayNode.class), anyMap()))
      .thenReturn(processEntity);

    assertThrows(WorkflowDeploymentNotFound.class, () -> {
      workflowEngineService.start(UUID, OKAPI_TENANT, OKAPI_TOKEN, context);
    });
  }

  @Test
  void historyWorksTest() throws WorkflowDeploymentNotFound, WorkflowEngineServiceException {
    WorkflowOperationalDto workflowOperationalDto = (WorkflowOperationalDto) workflowOperational;
    ResponseEntity<ArrayNode> processEntity = new ResponseEntity<>(HttpStatus.OK);
    ResponseEntity<ArrayNode> historyEntity = new ResponseEntity<>(HttpStatus.OK);
    ResponseEntity<ArrayNode> incidentEntity = new ResponseEntity<>(HttpStatus.OK);

    ObjectNode objectNode = mapper.createObjectNode();
    objectNode.put("id", UUID);

    ObjectNode historyNode = mapper.createObjectNode();
    historyNode.put("id", UUID);
    historyNode.put("history", VALUE);

    ObjectNode incidentNode = mapper.createObjectNode();
    incidentNode.put("id", UUID);

    ArrayNode processNode = mapper.createArrayNode();
    processNode.add(objectNode);
    setField(processEntity, "body", processNode);

    ArrayNode historyArrayNode = mapper.createArrayNode();
    historyArrayNode.add(historyNode);
    setField(historyEntity, "body", historyArrayNode);

    when(workflowRepo.getViewById(anyString(), eq(WorkflowOperationalDto.class))).thenReturn(workflowOperationalDto);

    when(restTemplate.exchange(contains(PROCESS_DEFINITION), eq(HttpMethod.GET), any(HttpEntity.class), eq(ArrayNode.class), anyMap()))
      .thenReturn(processEntity);

    when(restTemplate.exchange(contains(HISTORY_INSTANCE), eq(HttpMethod.GET), any(HttpEntity.class), eq(ArrayNode.class), anyMap()))
      .thenReturn(historyEntity);

    when(restTemplate.exchange(contains(HISTORY_INCIDENT), eq(HttpMethod.GET), any(HttpEntity.class), eq(ArrayNode.class), anyMap()))
      .thenReturn(incidentEntity);

    JsonNode response = workflowEngineService.history(UUID, OKAPI_TENANT, OKAPI_TOKEN);
    assertEquals(historyArrayNode, response);
  }

  @Test
  void historyWorksWithoutOkFetchingIncidentsHistoryTest() throws WorkflowDeploymentNotFound, WorkflowEngineServiceException {

    WorkflowOperationalDto workflowOperationalDto = (WorkflowOperationalDto) workflowOperational;

    ResponseEntity<ArrayNode> deploymentEntity = new ResponseEntity<>(HttpStatus.OK);
    ResponseEntity<ArrayNode> processEntity = new ResponseEntity<>(HttpStatus.OK);
    ResponseEntity<ArrayNode> incidentsEntity = new ResponseEntity<>(HttpStatus.FOUND);

    ObjectNode deploymentNode = mapper.createObjectNode();
    deploymentNode.put("id", UUID);

    ObjectNode processNode = mapper.createObjectNode();
    processNode.put("id", UUID);

    ArrayNode deploymentArrayNode = mapper.createArrayNode();
    deploymentArrayNode.add(deploymentNode);
    setField(deploymentEntity, "body", deploymentArrayNode);

    ArrayNode processArrayNode = mapper.createArrayNode();
    processArrayNode.add(processNode);
    setField(processEntity, "body", processArrayNode);

    ArrayNode incidentsArrayNode = mapper.createArrayNode();
    setField(incidentsEntity, "body", incidentsArrayNode);

    when(workflowRepo.getViewById(anyString(), eq(WorkflowOperationalDto.class))).thenReturn(workflowOperationalDto);

    when(restTemplate.exchange(anyString(), any(HttpMethod.class), any(HttpEntity.class), eq(ArrayNode.class), anyMap()))
      .thenReturn(processEntity, processEntity, incidentsEntity);

    JsonNode response = workflowEngineService.history(UUID, OKAPI_TENANT, OKAPI_TOKEN);
    assertEquals(processArrayNode, response);
  }

  @Test
  void historyThrowsExceptionWithNotOkHttpStatusForProcessTest() {
    WorkflowOperationalDto workflowOperationalDto = (WorkflowOperationalDto) workflowOperational;
    ResponseEntity<ArrayNode> processEntity = new ResponseEntity<>(HttpStatus.OK);
    ResponseEntity<ArrayNode> historyEntity = new ResponseEntity<>(HttpStatus.RESET_CONTENT);

    ObjectNode objectNode = mapper.createObjectNode();
    objectNode.put("id", UUID);

    ArrayNode processNode = mapper.createArrayNode();
    processNode.add(objectNode);
    setField(processEntity, "body", processNode);

    setField(historyEntity, "body", null);

    when(workflowRepo.getViewById(anyString(), eq(WorkflowOperationalDto.class))).thenReturn(workflowOperationalDto);

    when(restTemplate.exchange(contains(PROCESS_DEFINITION), eq(HttpMethod.GET), any(HttpEntity.class), eq(ArrayNode.class), anyMap()))
      .thenReturn(processEntity);

    when(restTemplate.exchange(contains(HISTORY_INSTANCE), eq(HttpMethod.GET), any(HttpEntity.class), eq(ArrayNode.class), anyMap()))
      .thenReturn(historyEntity);

    assertThrows(WorkflowEngineServiceException.class, () -> {
      workflowEngineService.history(UUID, OKAPI_TENANT, OKAPI_TOKEN);
    });
  }

  @Test
  void historyThrowsExceptionWithNullIncidentsForProcessTest() {
    WorkflowOperationalDto workflowOperationalDto = (WorkflowOperationalDto) workflowOperational;
    ResponseEntity<ArrayNode> processEntity = new ResponseEntity<>(HttpStatus.OK);
    ResponseEntity<ArrayNode> historyEntity = new ResponseEntity<>(HttpStatus.OK);

    ObjectNode objectNode = mapper.createObjectNode();
    objectNode.put("id", UUID);

    ArrayNode processNode = mapper.createArrayNode();
    processNode.add(objectNode);
    setField(processEntity, "body", processNode);

    setField(historyEntity, "body", null);

    when(workflowRepo.getViewById(anyString(), eq(WorkflowOperationalDto.class))).thenReturn(workflowOperationalDto);

    when(restTemplate.exchange(contains(PROCESS_DEFINITION), eq(HttpMethod.GET), any(HttpEntity.class), eq(ArrayNode.class), anyMap()))
      .thenReturn(processEntity);

    when(restTemplate.exchange(contains(HISTORY_INSTANCE), eq(HttpMethod.GET), any(HttpEntity.class), eq(ArrayNode.class), anyMap()))
      .thenReturn(historyEntity);

    assertThrows(WorkflowEngineServiceException.class, () -> {
      workflowEngineService.history(UUID, OKAPI_TENANT, OKAPI_TOKEN);
    });
  }

  @Test
  void historyWorksThrowsExceptionForFetchingIncidentsHistoryTest() {
    WorkflowOperationalDto workflowOperationalDto = (WorkflowOperationalDto) workflowOperational;
    ResponseEntity<ArrayNode> processEntity = new ResponseEntity<>(HttpStatus.OK);
    ResponseEntity<ArrayNode> historyEntity = new ResponseEntity<>(HttpStatus.NOT_FOUND);

    ObjectNode objectNode = mapper.createObjectNode();
    objectNode.put("id", UUID);

    ObjectNode historyNode = mapper.createObjectNode();
    historyNode.put("id", UUID);
    historyNode.put("history", VALUE);

    ArrayNode processNode = mapper.createArrayNode();
    processNode.add(objectNode);
    setField(processEntity, "body", processNode);

    ArrayNode historyArrayNode = mapper.createArrayNode();
    historyArrayNode.add(historyNode);
    setField(historyEntity, "body", historyArrayNode);

    when(workflowRepo.getViewById(anyString(), eq(WorkflowOperationalDto.class))).thenReturn(workflowOperationalDto);

    when(restTemplate.exchange(contains(PROCESS_DEFINITION), eq(HttpMethod.GET), any(HttpEntity.class), eq(ArrayNode.class), anyMap()))
      .thenReturn(processEntity);

    when(restTemplate.exchange(contains(HISTORY_INSTANCE), eq(HttpMethod.GET), any(HttpEntity.class), eq(ArrayNode.class), anyMap()))
      .thenReturn(historyEntity);

    assertThrows(WorkflowEngineServiceException.class, () -> {
      workflowEngineService.history(UUID, OKAPI_TENANT, OKAPI_TOKEN);
    });
  }

  @Test
  void historyWorksThrowsExceptionForNotOkHttpOnIncidentsHistoryTest() {
    WorkflowOperationalDto workflowOperationalDto = (WorkflowOperationalDto) workflowOperational;
    ResponseEntity<ArrayNode> processEntity = new ResponseEntity<>(HttpStatus.OK);
    ResponseEntity<ArrayNode> historyEntity = new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    ResponseEntity<ArrayNode> thirdEntity = new ResponseEntity<>(HttpStatus.RESET_CONTENT);

    ObjectNode objectNode = mapper.createObjectNode();
    objectNode.put("id", UUID);

    ObjectNode historyNode = mapper.createObjectNode();
    historyNode.put("id", UUID);
    historyNode.put("history", VALUE);

    ArrayNode processNode = mapper.createArrayNode();
    processNode.add(objectNode);
    setField(processEntity, "body", processNode);

    ArrayNode historyArrayNode = mapper.createArrayNode();
    historyArrayNode.add(historyNode);
    setField(historyEntity, "body", historyArrayNode);

    when(workflowRepo.getViewById(anyString(), eq(WorkflowOperationalDto.class))).thenReturn(workflowOperationalDto);

    when(restTemplate.exchange(contains(PROCESS_DEFINITION), eq(HttpMethod.GET), any(HttpEntity.class), eq(ArrayNode.class), anyMap()))
      .thenReturn(processEntity);

    when(restTemplate.exchange(contains(HISTORY_INSTANCE), eq(HttpMethod.GET), any(HttpEntity.class), eq(ArrayNode.class), anyMap()))
      .thenReturn(historyEntity, thirdEntity);

    assertThrows(WorkflowEngineServiceException.class, () -> {
      workflowEngineService.history(UUID, OKAPI_TENANT, OKAPI_TOKEN);
    });
  }

  @Test
  void historyWorksThrowsExceptionForNullIncidentsOnIncidentsHistoryTest() {
    WorkflowOperationalDto workflowOperationalDto = (WorkflowOperationalDto) workflowOperational;
    ResponseEntity<ArrayNode> processEntity = new ResponseEntity<>(HttpStatus.OK);
    ResponseEntity<ArrayNode> historyEntity = new ResponseEntity<>(HttpStatus.OK);
    ObjectNode objectNode = mapper.createObjectNode();
    objectNode.put("id", UUID);

    ObjectNode historyNode = mapper.createObjectNode();
    historyNode.put("id", UUID);
    historyNode.put("history", VALUE);

    ArrayNode processNode = mapper.createArrayNode();
    processNode.add(objectNode);
    setField(processEntity, "body", processNode);

    setField(historyEntity, "body", null);

    when(workflowRepo.getViewById(anyString(), eq(WorkflowOperationalDto.class))).thenReturn(workflowOperationalDto);

    when(restTemplate.exchange(contains(PROCESS_DEFINITION), eq(HttpMethod.GET), any(HttpEntity.class), eq(ArrayNode.class), anyMap()))
      .thenReturn(processEntity);

    when(restTemplate.exchange(contains(HISTORY_INSTANCE), eq(HttpMethod.GET), any(HttpEntity.class), eq(ArrayNode.class), anyMap()))
      .thenReturn(historyEntity);

    assertThrows(WorkflowEngineServiceException.class, () -> {
      workflowEngineService.history(UUID, OKAPI_TENANT, OKAPI_TOKEN);
    });
  }

  private class WorkflowAsDto extends Workflow implements WorkflowDto {}

  private class WorkflowAsOperationalDto extends Workflow implements WorkflowOperationalDto {}

}
