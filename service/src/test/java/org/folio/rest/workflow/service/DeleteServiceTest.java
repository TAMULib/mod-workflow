package org.folio.rest.workflow.service;

import static org.folio.spring.test.mock.MockMvcConstant.UUID;
import static org.folio.spring.test.mock.MockMvcConstant.VALUE;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.util.ReflectionTestUtils.getField;
import static org.springframework.test.util.ReflectionTestUtils.setField;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import java.util.ArrayList;
import java.util.List;
import org.folio.rest.workflow.dto.WorkflowOperationalNodeDto;
import org.folio.rest.workflow.model.Node;
import org.folio.rest.workflow.model.StartEvent;
import org.folio.rest.workflow.model.Subprocess;
import org.folio.rest.workflow.model.Workflow;
import org.folio.rest.workflow.model.has.HasNodes;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class DeleteServiceTest {

  /**
   * Needed to test using different IDs.
   */
  private static final String UUID_ALT = "d2ad0ddb-8f0a-40e6-b543-1f43d62b8a95";

  /**
   * Needed to test using different names.
   */
  private static final String VALUE_ALT = "value_alt";

  @Mock
  private EntityManager entityManager;

  @Mock
  private TypedQuery<?> typedQuery;

  @Spy
  private DeleteService deleteService;

  private List<Node> nodes;

  private StartEvent startEvent;

  private Subprocess subprocess;

  private WorkflowAsOperationalNodeDto workflowOperationalNode;

  @BeforeEach
  void beforeEach() {

    nodes = new ArrayList<>();
    startEvent = new StartEvent();
    subprocess = new Subprocess();
    workflowOperationalNode = new WorkflowAsOperationalNodeDto();

    setField(deleteService, "entityManager", entityManager);

    setField(workflowOperationalNode, "id", UUID);
    setField(workflowOperationalNode, "deploymentId", UUID);
    setField(workflowOperationalNode, "name", VALUE);
    setField(workflowOperationalNode, "nodes", nodes);
    setField(workflowOperationalNode, "versionTag", VALUE);

    setField(startEvent, "id", UUID);
    setField(startEvent, "name", VALUE);

    setField(subprocess, "id", UUID_ALT);
    setField(subprocess, "name", VALUE_ALT);
  }

  @Test
  void deleteNodesReturnsOnNullTest() {

    setField(workflowOperationalNode, "nodes", null);

    deleteService.deleteNodes(workflowOperationalNode);

    verify(deleteService, never()).deleteEntity(VALUE, UUID);
  }

  @Test
  void deleteNodesReturnsOnEmptyTest() {

    deleteService.deleteNodes(workflowOperationalNode);

    verify(deleteService, never()).deleteEntity(VALUE, UUID);
  }

  @Test
  void deleteNodesNoDeserializeAsTest() {

    final NoSerializeNode node = new NoSerializeNode();

    setField(node, "id", UUID);

    nodes.add(node);

    deleteService.deleteNodes(workflowOperationalNode);

    verify(deleteService, never()).deleteEntity(VALUE, UUID);
  }

  @Test
  void deleteNodesNoIdTest() {

    setField(startEvent, "id", null);

    nodes.add(startEvent);

    deleteService.deleteNodes(workflowOperationalNode);

    verify(deleteService, never()).deleteEntity(VALUE, UUID);
  }

  @Test
  void deleteNodesForSimpleTest() {

    nodes.add(startEvent);

    when(entityManager.createQuery(anyString())).thenReturn(typedQuery);

    when(typedQuery.setParameter(anyString(), anyString())).thenAnswer(invocation -> {
      return typedQuery;
    });

    when(typedQuery.executeUpdate()).thenReturn(1);

    deleteService.deleteNodes(workflowOperationalNode);

    verify(typedQuery).executeUpdate();
  }

  @Test
  void deleteNodesForNodeWithoutChildrenTest() {

    final List<Object[]> results = new ArrayList<>();

    nodes.add(subprocess);

    when(entityManager.createNativeQuery(anyString())).thenReturn(typedQuery);

    when(typedQuery.getResultList()).thenAnswer(invocation -> {
      return results;
    });

    when(entityManager.createQuery(anyString())).thenReturn(typedQuery);

    when(typedQuery.setParameter(anyString(), anyString())).thenAnswer(invocation -> {
      return typedQuery;
    });

    when(typedQuery.executeUpdate()).thenReturn(1);

    deleteService.deleteNodes(workflowOperationalNode);

    verify(typedQuery).executeUpdate();
  }

  @Test
  void deleteNodesForNodeWithChildrenTest() {

    final String id = (String) getField(startEvent, "id");
    final String[] child = new String[] { id, StartEvent.class.getSimpleName() };

    final List<Object[]> results = new ArrayList<>();
    results.add(child);

    nodes.add(subprocess);

    when(entityManager.createNativeQuery(anyString())).thenReturn(typedQuery);

    when(typedQuery.getResultList()).thenAnswer(invocation -> {
      return results;
    });

    when(entityManager.createQuery(anyString())).thenReturn(typedQuery);

    when(typedQuery.setParameter(anyString(), anyString())).thenAnswer(invocation -> {
      return typedQuery;
    });

    when(typedQuery.executeUpdate()).thenReturn(1);

    deleteService.deleteNodes(workflowOperationalNode);

    verify(typedQuery, times(2)).executeUpdate();
  }

  @Test
  void deleteNodesForNodeWithChildrenIncompleteRowsTest() {

    final String[] child = new String[] { "" };

    final List<Object[]> results = new ArrayList<>();
    results.add(child);

    nodes.add(subprocess);

    when(entityManager.createNativeQuery(anyString())).thenReturn(typedQuery);

    when(typedQuery.getResultList()).thenAnswer(invocation -> {
      return results;
    });

    when(entityManager.createQuery(anyString())).thenReturn(typedQuery);

    when(typedQuery.setParameter(anyString(), anyString())).thenAnswer(invocation -> {
      return typedQuery;
    });

    when(typedQuery.executeUpdate()).thenReturn(1);

    deleteService.deleteNodes(workflowOperationalNode);

    verify(typedQuery).executeUpdate();
  }

  @Test
  void deleteNodesForUnknownNodeTest() {

    final UnknownNode node = new UnknownNode();

    setField(node, "id", UUID);
    setField(node, "name", VALUE);

    nodes.add(node);

    deleteService.deleteNodes(workflowOperationalNode);

    verify(typedQuery, never()).executeUpdate();
  }

  @Test
  void deleteNodesForMaliciousNodeTest() {

    final MaliciousNode node = new MaliciousNode();

    setField(node, "id", UUID);
    setField(node, "name", VALUE);

    nodes.add(node);

    deleteService.deleteNodes(workflowOperationalNode);

    verify(typedQuery, never()).executeUpdate();
  }

  /**
   * Mocked node that is intended to return nothing when the getDeserializeAs() is called.
   */
  private class NoSerializeNode extends Node {

    @Override
    public String getDeserializeAs() {

      return null;
    }

  }

  /**
   * Mocked node that is intended to return an unknown entity name when the getDeserializeAs() is called and has nodes.
   */
  private class UnknownNode extends Node implements HasNodes {

    private final List<Node> list = new ArrayList<>();

    @Override
    public String getDeserializeAs() {

      return "This Should Not Match";
    }

    @Override
    public List<Node> getNodes() {

      return list;
    }

    @Override
    public void setNodes(List<Node> nodes) {

      nodes.forEach(list::add);
    }

  }

  /**
   * Mocked node that is intended to return a malicious SQL string when the getDeserializeAs() is called.
   */
  private class MaliciousNode extends Node {

    @Override
    public String getDeserializeAs() {

      return "workflow; SELECT * from workflow";
    }

  }

  private class WorkflowAsOperationalNodeDto extends Workflow implements WorkflowOperationalNodeDto {}

}
