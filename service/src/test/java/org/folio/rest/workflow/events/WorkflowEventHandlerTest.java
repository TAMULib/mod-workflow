package org.folio.rest.workflow.events;

import static org.folio.spring.test.mock.MockMvcConstant.UUID;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import org.folio.rest.workflow.exception.WorkflowCreateAlreadyExistsException;
import org.folio.rest.workflow.model.Workflow;
import org.folio.rest.workflow.model.repo.WorkflowRepo;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
class WorkflowEventHandlerTest {

  @Mock
  private WorkflowRepo workflowRepo;

  private Workflow workflow;

  private WorkflowEventHandler workflowEventHandler;

  @BeforeEach
  void setUp() {
    workflow = new Workflow();
    workflow.setId(UUID);

    workflowEventHandler = new WorkflowEventHandler();

    ReflectionTestUtils.setField(workflowEventHandler, "workflowRepo", workflowRepo);
  }

  @Test
  void testHandleWorkflowCreateDoesNotExist() {
    when(workflowRepo.existsByIdAndVersionTag(anyString(), anyString())).thenReturn(false);

    Assertions.assertDoesNotThrow(() -> {
      workflowEventHandler.handleWorkflowBeforeCreate(workflow);
    });
  }

  @Test
  void testHandleWorkflowCreateDoesExist() {
    when(workflowRepo.existsByIdAndVersionTag(anyString(), anyString())).thenReturn(true);

    Assertions.assertThrows(WorkflowCreateAlreadyExistsException.class, () -> {
      workflowEventHandler.handleWorkflowBeforeCreate(workflow);
    });
  }

}
