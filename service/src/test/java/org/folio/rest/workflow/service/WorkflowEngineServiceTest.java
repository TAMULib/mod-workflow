package org.folio.rest.workflow.service;

import static org.folio.spring.test.mock.MockMvcConstant.OKAPI_TENANT;
import static org.folio.spring.test.mock.MockMvcConstant.OKAPI_TOKEN;
import static org.folio.spring.test.mock.MockMvcConstant.UUID;
import static org.folio.spring.test.mock.MockMvcConstant.VALUE;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.util.ReflectionTestUtils.setField;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.util.concurrent.atomic.AtomicInteger;
import org.folio.rest.workflow.dto.WorkflowDto;
import org.folio.rest.workflow.dto.WorkflowOperationalDto;
import org.folio.rest.workflow.exception.WorkflowEngineServiceException;
import org.folio.rest.workflow.exception.WorkflowNotFoundException;
import org.folio.rest.workflow.model.Workflow;
import org.folio.rest.workflow.model.repo.WorkflowRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
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
import org.springframework.web.client.RestTemplate;

@ExtendWith(SpringExtension.class)
@ExtendWith(MockitoExtension.class)
class WorkflowEngineServiceTest {

  @Mock
  private WorkflowRepo workflowRepo;

  @Mock
  private RestTemplate restTemplate;

  private WorkflowAsDto workflow;

  private WorkflowAsOperationalDto workflowOperational;

  private WorkflowEngineService workflowEngineService;

  private ObjectMapper mapper;

  @BeforeEach
  void beforeEach() {
    mapper = new ObjectMapper();
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
    when(restTemplate.exchange(anyString(), any(HttpMethod.class), any(HttpEntity.class), ArgumentMatchers.<Class<Workflow>>any())).thenReturn(responseEntity);
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
    when(restTemplate.exchange(anyString(), any(HttpMethod.class), any(HttpEntity.class), ArgumentMatchers.<Class<Workflow>>any())).thenReturn(responseEntity);
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
    when(restTemplate.exchange(anyString(), any(HttpMethod.class), any(HttpEntity.class), ArgumentMatchers.<Class<Workflow>>any())).thenReturn(responseEntity);
    when(workflowRepo.save(any())).thenReturn(workflow);

    workflowEngineService.deactivate(UUID, OKAPI_TENANT, OKAPI_TOKEN);

    verify(workflowRepo).save(any());
    verify(workflowRepo, never()).deleteById(anyString());
  }

  @Test
  void deleteWorksTest() throws WorkflowEngineServiceException {
    WorkflowDto workflowDto = (WorkflowDto) workflow;
    ResponseEntity<Workflow> responseEntity = new ResponseEntity<>(HttpStatus.OK);
    setField(responseEntity, "body", workflow);

    when(workflowRepo.getViewById(anyString(), ArgumentMatchers.<Class<WorkflowDto>>any())).thenReturn(workflowDto);
    when(restTemplate.exchange(anyString(), any(HttpMethod.class), any(HttpEntity.class), ArgumentMatchers.<Class<Workflow>>any())).thenReturn(responseEntity);
    when(workflowRepo.save(any())).thenReturn(workflow);
    doNothing().when(workflowRepo).deleteById(anyString());

    workflowEngineService.delete(UUID, OKAPI_TENANT, OKAPI_TOKEN);

    verify(workflowRepo).save(any());
    verify(workflowRepo).deleteById(anyString());
  }

  @Test
  void deleteThrowsExceptionUnableGetUpdatedTest() {
    WorkflowDto workflowDto = (WorkflowDto) workflow;
    ResponseEntity<Workflow> responseEntity = new ResponseEntity<>(HttpStatus.ACCEPTED);
    setField(responseEntity, "body", workflow);

    when(workflowRepo.getViewById(anyString(), ArgumentMatchers.<Class<WorkflowDto>>any())).thenReturn(workflowDto);
    when(restTemplate.exchange(anyString(), any(HttpMethod.class), any(HttpEntity.class), ArgumentMatchers.<Class<Workflow>>any())).thenReturn(responseEntity);

    assertThrows(WorkflowEngineServiceException.class, () ->
      workflowEngineService.delete(UUID, OKAPI_TENANT, OKAPI_TOKEN)
    );

    verify(workflowRepo, never()).save(any());
    verify(workflowRepo, never()).deleteById(anyString());
  }

  @Test
  void deleteThrowsExceptionFailedToSaveTest() {
    WorkflowDto workflowDto = (WorkflowDto) workflow;
    ResponseEntity<Workflow> responseEntity = new ResponseEntity<>(HttpStatus.OK);
    setField(responseEntity, "body", workflow);

    when(workflowRepo.getViewById(anyString(), ArgumentMatchers.<Class<WorkflowDto>>any())).thenReturn(workflowDto);
    when(restTemplate.exchange(anyString(), any(HttpMethod.class), any(HttpEntity.class), ArgumentMatchers.<Class<Workflow>>any())).thenReturn(responseEntity);
    when(workflowRepo.save(any())).thenThrow(new RuntimeException("Trigger Failure"));

    assertThrows(WorkflowEngineServiceException.class, () -> {
      workflowEngineService.delete(UUID, OKAPI_TENANT, OKAPI_TOKEN);
    });

    verify(workflowRepo).save(any());
    verify(workflowRepo, never()).deleteById(anyString());
  }

  @Test
  void deleteThrowsExceptionFailedToSendWithNullResponseBodyTest() {
    WorkflowDto workflowDto = (WorkflowDto) workflow;
    ResponseEntity<Workflow> responseEntity = new ResponseEntity<>(HttpStatus.OK);
    setField(responseEntity, "body", null);

    when(workflowRepo.getViewById(anyString(), ArgumentMatchers.<Class<WorkflowDto>>any())).thenReturn(workflowDto);
    when(restTemplate.exchange(anyString(), any(HttpMethod.class), any(HttpEntity.class), ArgumentMatchers.<Class<Workflow>>any())).thenReturn(responseEntity);

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
  void startWorksTest() throws WorkflowEngineServiceException {
    WorkflowOperationalDto workflowOperationalDto = (WorkflowOperationalDto) workflowOperational;
    ResponseEntity<ArrayNode> responseEntity = new ResponseEntity<>(HttpStatus.OK);
    JsonNode context = mapper.createObjectNode();
    ObjectNode objectNode = mapper.createObjectNode();
    objectNode.put("id", UUID);

    ArrayNode arrayNode = mapper.createArrayNode();
    arrayNode.add(objectNode);
    setField(responseEntity, "body", arrayNode);

    when(workflowRepo.getViewById(anyString(), ArgumentMatchers.<Class<WorkflowOperationalDto>>any())).thenReturn(workflowOperationalDto);
    when(restTemplate.exchange(anyString(), any(HttpMethod.class), any(HttpEntity.class), ArgumentMatchers.<Class<ArrayNode>>any())).thenReturn(responseEntity);

    JsonNode response = workflowEngineService.start(UUID, OKAPI_TENANT, OKAPI_TOKEN, context);
    assertEquals(arrayNode, response);
  }

  @Test
  void startThrowsExceptionHttpNotOkTest() {
    WorkflowOperationalDto workflowOperationalDto = (WorkflowOperationalDto) workflowOperational;
    ResponseEntity<ArrayNode> responseEntity = new ResponseEntity<>(HttpStatus.RESET_CONTENT);
    JsonNode context = mapper.createObjectNode();
    ObjectNode objectNode = mapper.createObjectNode();
    objectNode.put("id", UUID);

    ArrayNode arrayNode = mapper.createArrayNode();
    arrayNode.add(objectNode);
    setField(responseEntity, "body", arrayNode);

    when(workflowRepo.getViewById(anyString(), ArgumentMatchers.<Class<WorkflowOperationalDto>>any())).thenReturn(workflowOperationalDto);
    when(restTemplate.exchange(anyString(), any(HttpMethod.class), any(HttpEntity.class), ArgumentMatchers.<Class<ArrayNode>>any())).thenReturn(responseEntity);

    assertThrows(WorkflowEngineServiceException.class, () -> {
      workflowEngineService.start(UUID, OKAPI_TENANT, OKAPI_TOKEN, context);
    });
  }

  @Test
  void startThrowsExceptionNullOrEmptyDefinitionsTest() {
    WorkflowOperationalDto workflowOperationalDto = (WorkflowOperationalDto) workflowOperational;
    ResponseEntity<ArrayNode> responseEntity = new ResponseEntity<>(HttpStatus.OK);
    JsonNode context = mapper.createObjectNode();
    ObjectNode objectNode = mapper.createObjectNode();
    objectNode.put("id", UUID);

    ArrayNode arrayNode = mapper.createArrayNode();
    setField(responseEntity, "body", arrayNode);

    when(workflowRepo.getViewById(anyString(), ArgumentMatchers.<Class<WorkflowOperationalDto>>any())).thenReturn(workflowOperationalDto);
    when(restTemplate.exchange(anyString(), any(HttpMethod.class), any(HttpEntity.class), ArgumentMatchers.<Class<ArrayNode>>any())).thenReturn(responseEntity);

    assertThrows(WorkflowEngineServiceException.class, () -> {
      workflowEngineService.start(UUID, OKAPI_TENANT, OKAPI_TOKEN, context);
    });

    setField(responseEntity, "body", null);

    assertThrows(WorkflowEngineServiceException.class, () -> {
      workflowEngineService.start(UUID, OKAPI_TENANT, OKAPI_TOKEN, context);
    });
  }

  @Test
  void startThrowsExceptionOnBadExchangeTest() {
    WorkflowOperationalDto workflowOperationalDto = (WorkflowOperationalDto) workflowOperational;
    ResponseEntity<ArrayNode> responseEntity = new ResponseEntity<>(HttpStatus.OK);
    JsonNode context = mapper.createObjectNode();
    ObjectNode objectNode = mapper.createObjectNode();
    objectNode.put("id", UUID);

    ArrayNode arrayNode = mapper.createArrayNode();
    arrayNode.add(objectNode);
    setField(responseEntity, "body", arrayNode);

    when(workflowRepo.getViewById(anyString(), ArgumentMatchers.<Class<WorkflowOperationalDto>>any())).thenReturn(workflowOperationalDto);

    AtomicInteger exchangeCount = new AtomicInteger(0);
    doAnswer(invocation -> {
      if (exchangeCount.getAndIncrement() > 0) {
        throw new RuntimeException("Failure on second exchange call.");
      }

      return responseEntity;
    }).when(restTemplate).exchange(anyString(), any(HttpMethod.class), any(HttpEntity.class), ArgumentMatchers.<Class<ArrayNode>>any());

    assertThrows(WorkflowEngineServiceException.class, () -> {
      workflowEngineService.start(UUID, OKAPI_TENANT, OKAPI_TOKEN, context);
    });
  }

  @Test
  void historyWorksTest() throws WorkflowEngineServiceException {
    WorkflowOperationalDto workflowOperationalDto = (WorkflowOperationalDto) workflowOperational;
    ResponseEntity<ArrayNode> responseEntity = new ResponseEntity<>(HttpStatus.OK);
    ResponseEntity<ArrayNode> historyResponseEntity = new ResponseEntity<>(HttpStatus.OK);
    ObjectNode objectNode = mapper.createObjectNode();
    objectNode.put("id", UUID);

    ObjectNode historyNode = mapper.createObjectNode();
    historyNode.put("id", UUID);
    historyNode.put("history", VALUE);

    ArrayNode arrayNode = mapper.createArrayNode();
    arrayNode.add(objectNode);
    setField(responseEntity, "body", arrayNode);

    ArrayNode historyArrayNode = mapper.createArrayNode();
    historyArrayNode.add(historyNode);
    setField(historyResponseEntity, "body", historyArrayNode);

    when(workflowRepo.getViewById(anyString(), ArgumentMatchers.<Class<WorkflowOperationalDto>>any())).thenReturn(workflowOperationalDto);

    AtomicInteger exchangeCount = new AtomicInteger(0);
    doAnswer(invocation -> {
      return (exchangeCount.getAndIncrement() > 0) ? historyResponseEntity : responseEntity;
    }).when(restTemplate).exchange(anyString(), any(HttpMethod.class), any(HttpEntity.class), ArgumentMatchers.<Class<ArrayNode>>any());

    JsonNode response = workflowEngineService.history(UUID, OKAPI_TENANT, OKAPI_TOKEN);
    assertEquals(historyArrayNode, response);
  }

  @Test
  void historyThrowsExceptionWithNotOkHttpStatusForProcessTest() {
    WorkflowOperationalDto workflowOperationalDto = (WorkflowOperationalDto) workflowOperational;
    ResponseEntity<ArrayNode> responseEntity = new ResponseEntity<>(HttpStatus.OK);
    ResponseEntity<ArrayNode> historyResponseEntity = new ResponseEntity<>(HttpStatus.RESET_CONTENT);
    ObjectNode objectNode = mapper.createObjectNode();
    objectNode.put("id", UUID);

    ArrayNode arrayNode = mapper.createArrayNode();
    arrayNode.add(objectNode);
    setField(responseEntity, "body", arrayNode);

    setField(historyResponseEntity, "body", null);

    when(workflowRepo.getViewById(anyString(), ArgumentMatchers.<Class<WorkflowOperationalDto>>any())).thenReturn(workflowOperationalDto);

    AtomicInteger exchangeCount = new AtomicInteger(0);
    doAnswer(invocation -> {
      return (exchangeCount.getAndIncrement() > 0) ? historyResponseEntity : responseEntity;
    }).when(restTemplate).exchange(anyString(), any(HttpMethod.class), any(HttpEntity.class), ArgumentMatchers.<Class<ArrayNode>>any());

    assertThrows(WorkflowEngineServiceException.class, () -> {
      workflowEngineService.history(UUID, OKAPI_TENANT, OKAPI_TOKEN);
    });
  }

  @Test
  void historyThrowsExceptionWithNullIncidentsForProcessTest() {
    WorkflowOperationalDto workflowOperationalDto = (WorkflowOperationalDto) workflowOperational;
    ResponseEntity<ArrayNode> responseEntity = new ResponseEntity<>(HttpStatus.OK);
    ResponseEntity<ArrayNode> historyResponseEntity = new ResponseEntity<>(HttpStatus.OK);
    ObjectNode objectNode = mapper.createObjectNode();
    objectNode.put("id", UUID);

    ArrayNode arrayNode = mapper.createArrayNode();
    arrayNode.add(objectNode);
    setField(responseEntity, "body", arrayNode);

    setField(historyResponseEntity, "body", null);

    when(workflowRepo.getViewById(anyString(), ArgumentMatchers.<Class<WorkflowOperationalDto>>any())).thenReturn(workflowOperationalDto);

    AtomicInteger exchangeCount = new AtomicInteger(0);
    doAnswer(invocation -> {
      return (exchangeCount.getAndIncrement() > 0) ? historyResponseEntity : responseEntity;
    }).when(restTemplate).exchange(anyString(), any(HttpMethod.class), any(HttpEntity.class), ArgumentMatchers.<Class<ArrayNode>>any());

    assertThrows(WorkflowEngineServiceException.class, () -> {
      workflowEngineService.history(UUID, OKAPI_TENANT, OKAPI_TOKEN);
    });
  }

  @Test
  void historyWorksThrowsExceptionForFetchingIncidentsHistoryTest() {
    WorkflowOperationalDto workflowOperationalDto = (WorkflowOperationalDto) workflowOperational;
    ResponseEntity<ArrayNode> responseEntity = new ResponseEntity<>(HttpStatus.OK);
    ResponseEntity<ArrayNode> historyResponseEntity = new ResponseEntity<>(HttpStatus.OK);
    ObjectNode objectNode = mapper.createObjectNode();
    objectNode.put("id", UUID);

    ObjectNode historyNode = mapper.createObjectNode();
    historyNode.put("id", UUID);
    historyNode.put("history", VALUE);

    ArrayNode arrayNode = mapper.createArrayNode();
    arrayNode.add(objectNode);
    setField(responseEntity, "body", arrayNode);

    ArrayNode historyArrayNode = mapper.createArrayNode();
    historyArrayNode.add(historyNode);
    setField(historyResponseEntity, "body", historyArrayNode);

    when(workflowRepo.getViewById(anyString(), ArgumentMatchers.<Class<WorkflowOperationalDto>>any())).thenReturn(workflowOperationalDto);

    AtomicInteger exchangeCount = new AtomicInteger(0);
    doAnswer(invocation -> {
      if (exchangeCount.getAndIncrement() > 1) {
        throw new RuntimeException("Failure on third exchange call.");
      }

      return (exchangeCount.get() > 0) ? historyResponseEntity : responseEntity;
    }).when(restTemplate).exchange(anyString(), any(HttpMethod.class), any(HttpEntity.class), ArgumentMatchers.<Class<ArrayNode>>any());

    assertThrows(WorkflowEngineServiceException.class, () -> {
      workflowEngineService.history(UUID, OKAPI_TENANT, OKAPI_TOKEN);
    });
  }

  @Disabled
  @Test
  void historyWorksThrowsExceptionForNotOkHttpOnIncidentsHistoryTest() {
    WorkflowOperationalDto workflowOperationalDto = (WorkflowOperationalDto) workflowOperational;
    ResponseEntity<ArrayNode> responseEntity = new ResponseEntity<>(HttpStatus.OK);
    ResponseEntity<ArrayNode> historyResponseEntity = new ResponseEntity<>(HttpStatus.OK);
    ResponseEntity<ArrayNode> thirdResponseEntity = new ResponseEntity<>(HttpStatus.RESET_CONTENT);
    ObjectNode objectNode = mapper.createObjectNode();
    objectNode.put("id", UUID);

    ObjectNode historyNode = mapper.createObjectNode();
    historyNode.put("id", UUID);
    historyNode.put("history", VALUE);

    ArrayNode arrayNode = mapper.createArrayNode();
    arrayNode.add(objectNode);
    setField(responseEntity, "body", arrayNode);

    ArrayNode historyArrayNode = mapper.createArrayNode();
    historyArrayNode.add(historyNode);
    setField(historyResponseEntity, "body", historyArrayNode);

    when(workflowRepo.getViewById(anyString(), ArgumentMatchers.<Class<WorkflowOperationalDto>>any())).thenReturn(workflowOperationalDto);

    AtomicInteger exchangeCount = new AtomicInteger(0);
    doAnswer(invocation -> {
      if (exchangeCount.getAndIncrement() > 1) {
        return thirdResponseEntity;
      }

      return (exchangeCount.get() > 0) ? historyResponseEntity : responseEntity;
    }).when(restTemplate).exchange(anyString(), any(HttpMethod.class), any(HttpEntity.class), ArgumentMatchers.<Class<ArrayNode>>any());

    assertThrows(WorkflowEngineServiceException.class, () -> {
      workflowEngineService.history(UUID, OKAPI_TENANT, OKAPI_TOKEN);
    });
  }

  @Disabled
  @Test
  void historyWorksThrowsExceptionForNullIncidentsOnIncidentsHistoryTest() {
    WorkflowOperationalDto workflowOperationalDto = (WorkflowOperationalDto) workflowOperational;
    ResponseEntity<ArrayNode> responseEntity = new ResponseEntity<>(HttpStatus.OK);
    ResponseEntity<ArrayNode> historyResponseEntity = new ResponseEntity<>(HttpStatus.OK);
    ResponseEntity<ArrayNode> thirdResponseEntity = new ResponseEntity<>(HttpStatus.OK);
    ObjectNode objectNode = mapper.createObjectNode();
    objectNode.put("id", UUID);

    ObjectNode historyNode = mapper.createObjectNode();
    historyNode.put("id", UUID);
    historyNode.put("history", VALUE);

    ArrayNode arrayNode = mapper.createArrayNode();
    arrayNode.add(objectNode);
    setField(responseEntity, "body", arrayNode);

    ArrayNode historyArrayNode = mapper.createArrayNode();
    historyArrayNode.add(historyNode);
    setField(historyResponseEntity, "body", historyArrayNode);

    when(workflowRepo.getViewById(anyString(), ArgumentMatchers.<Class<WorkflowOperationalDto>>any())).thenReturn(workflowOperationalDto);

    AtomicInteger exchangeCount = new AtomicInteger(0);
    doAnswer(invocation -> {
      if (exchangeCount.getAndIncrement() > 1) {
        return thirdResponseEntity;
      }

      return (exchangeCount.get() > 0) ? historyResponseEntity : responseEntity;
    }).when(restTemplate).exchange(anyString(), any(HttpMethod.class), any(HttpEntity.class), ArgumentMatchers.<Class<ArrayNode>>any());

    assertThrows(WorkflowEngineServiceException.class, () -> {
      workflowEngineService.history(UUID, OKAPI_TENANT, OKAPI_TOKEN);
    });
  }

  private class WorkflowAsDto extends Workflow implements WorkflowDto {}

  private class WorkflowAsOperationalDto extends Workflow implements WorkflowOperationalDto {}

}
