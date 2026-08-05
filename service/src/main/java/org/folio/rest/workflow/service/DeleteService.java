package org.folio.rest.workflow.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import org.folio.rest.workflow.dto.WorkflowOperationalNodeDto;
import org.folio.rest.workflow.exception.WorkflowEngineServiceException;
import org.folio.rest.workflow.model.Node;
import org.folio.rest.workflow.model.resolver.DeserializeAsNodeJsonResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * A class to facilitate recursive deletion of workflows.
 *
 * Workflows does not use SQL relations.
 * Each entity, etc.., must be found and explicitly deleted.
 */
@Service
@Transactional
public class DeleteService {

  private static final Logger LOG = LoggerFactory.getLogger(DeleteService.class);

  static final List<String> NODE_ENTITIES = List.of(
    "EventSubprocess",
    "ExclusiveGateway",
    "InclusiveGateway",
    "MoveToLastGateway",
    "MoveToNode",
    "ParallelGateway",
    "Subprocess"
  );

  static final List<String> SIMPLE_ENTITIES = List.of(
    "CompressFileTask",
    "Condition",
    "ConnectTo",
    "DatabaseConnectionTask",
    "DatabaseDisconnectTask",
    "DatabaseQueryTask",
    "DirectoryTask",
    "EmailTask",
    "EndEvent",
    "FileTask",
    "FolioRequestTask",
    "FtpTask",
    "InputTask",
    "ProcessorTask",
    "ReceiveTask",
    "RequestTask",
    "ScriptTask",
    "StartEvent"
  );

  @PersistenceContext
  private EntityManager entityManager;

  DeleteService() {
    // Do nothing, the @PersistenceContext handles entityManager.
  }

  /**
   * Recursively delete all associated nodes.
   *
   * @param workflow The workflow to delete the nodes of.
   *
   * @throws WorkflowEngineServiceException When the request fails in some way preventing the return of an HttpEntity.
   */
  public void deleteNodes(WorkflowOperationalNodeDto workflow) {

    final List<Node> nodes = workflow.getNodes();

    if (nodes == null || nodes.isEmpty()) return;

    nodes.forEach(node -> deleteEntity(node.getDeserializeAs(), node.getId()));
  }

  /**
   * Delete id for the given entity using the serialize as name.
   *
   * @param name The serialize as name for an entity.
   * @param id   The ID of the row to delete.
   */
  void deleteEntity(String name, String id) {

    if (name == null || id == null) {
      LOG.warn("Cannot delete with entity name '{}' and id '{}', both values must not be NULL.", name, id);

      return;
    }

    String entityName = extractEntityName(SIMPLE_ENTITIES, name);

    if (entityName == null) {
      entityName = extractEntityName(NODE_ENTITIES, name);

      if (entityName == null) {
        LOG.warn("Unknown name '{}' for id '{}', got entityName '{}'.", name, id, entityName);

        return;
      }

      deleteNodeEntity(entityName, id);
    }

    deleteSimpleEntity(entityName, id);
  }

  /**
   * Delete node entities using the serialize as name.
   *
   * For use by those that implement `hasNode`.
   *
   * This must recurse the date.
   *
   * @param entityName The entity name for direct use in SQL.
   * @param id         The ID of the row to delete.
   */
  void deleteNodeEntity(String entityName, String id) {

    for (final Map.Entry<String, String> entry : findNodeTables(entityName, id).entrySet()) {
      deleteEntity(entry.getValue(), entry.getKey());
    }
  }

  /**
   * Delete simple entities using the serialize as name.
   *
   * Do not use this to delete complex entities, such as those that implement `hasNode`.
   *
   * @param entityName The entity name for direct use in SQL.
   * @param id         The ID of the row to delete.
   */
  @SuppressWarnings("S2077") // SonarQube false positive, the query is protected by extractEntityName() and cannot produce SQL escapes from entityName.
  void deleteSimpleEntity(String entityName, String id) {

    final int total = entityManager.createQuery("DELETE FROM " + entityName + " e WHERE e.id = :id")
      .setParameter("id", id)
      .executeUpdate();

    if (total > 0) {
      entityManager.clear();
    }

    LOG.debug("Deleted '{}' entities for entityName '{}' with id '{}'.", total, entityName, id);
  }

  /**
   * Attempt to match the entity in the list and return the list value.
   *
   * The match is case-insensitive.
   *
   * @param list The list to search through.
   * @param name The entity serialize as name.
   *
   * @return The entity name for direct use in SQL.
   */
  String extractEntityName(List<String> list, String name) {

    final Optional<String> match = list.stream()
      .filter(s -> s.equalsIgnoreCase(name))
      .findFirst();

    return match.isEmpty() ? null : match.get();
  }

  /**
   * Build a list of all nodes and their associated tables associated with the given ID.
   *
   * @param entityName The entity name for direct use in SQL.
   * @param id         The ID of the row to fetch the associated nodes of.
   *
   * @return A map where the key represents the ID and the value represents the table.
   */
  @SuppressWarnings("unchecked")
  Map<String, String> findNodeTables(String entityName, String id) {

    final String query = findNodeTablesBuildQuery(entityName, id);

    final List<Object[]> results = entityManager.createNativeQuery(query).getResultList();

    final Map<String, String> map = new HashMap<>();

    for (final Object[] row : results) {
      if (row.length >= 2) {
        map.put(String.valueOf(row[0]), String.valueOf(row[1]));
      }
    }

    return map;
  }

  /**
   * Build the query for finding all entities associated with a node.
   *
   * @param entityName The entity name for direct use in SQL.
   * @param id         The ID of the row to fetch the associated nodes of.
   *
   * @return The generated SQL query.
   */
  String findNodeTablesBuildQuery(String entityName, String id) {

    final String entity = entityName.toLowerCase();

    return DeserializeAsNodeJsonResolver.CLASSES.keySet().stream()
      .map(String::toLowerCase)
      .map(s -> String.format("SELECT t.id, '%s' FROM %s t INNER JOIN %s_node n ON n.nodes_id = t.id WHERE n.%s_id = '%s'", s, s, entity, entity, id))
      .collect(Collectors.joining(" UNION "));
  }

}
