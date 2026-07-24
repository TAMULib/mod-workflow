package org.folio.rest.workflow.controller;

import com.fasterxml.jackson.databind.JsonNode;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.folio.rest.workflow.exception.WorkflowDeploymentNotFound;
import org.folio.rest.workflow.exception.WorkflowEngineServiceException;
import org.folio.rest.workflow.exception.WorkflowImportException;
import org.folio.rest.workflow.exception.WorkflowNotFoundException;
import org.folio.rest.workflow.model.Workflow;
import org.folio.rest.workflow.model.repo.WorkflowRepo;
import org.folio.rest.workflow.service.WorkflowCqlService;
import org.folio.rest.workflow.service.WorkflowEngineService;
import org.folio.rest.workflow.service.WorkflowImportService;
import org.folio.spring.tenant.annotation.TenantHeader;
import org.folio.spring.web.annotation.TokenHeader;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/workflows")
public class WorkflowController {

  private static final Log LOG = LogFactory.getLog(WorkflowController.class);

  private static final Pattern REGX_NOT_GRAPH = Pattern.compile("[^\\p{C}]");
  private static final Pattern REGX_EOL = Pattern.compile("[\r\n]");

  private WorkflowEngineService workflowEngineService;

  private WorkflowCqlService workflowCqlService;

  private WorkflowImportService workflowImportService;

  private WorkflowRepo workflowRepo;

  public WorkflowController(WorkflowEngineService workflowEngineService, WorkflowCqlService workflowCqlService, WorkflowImportService workflowImportService, WorkflowRepo workflowRepo) {
    this.workflowEngineService = workflowEngineService;
    this.workflowCqlService = workflowCqlService;
    this.workflowImportService = workflowImportService;
    this.workflowRepo = workflowRepo;
  }

  @PostMapping(value = "/import", produces = { MediaType.APPLICATION_JSON_VALUE }, consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
  public ResponseEntity<Object> importWorkflow(
      @RequestPart(name = "file") MultipartFile fwz,
      @TenantHeader String tenant,
      @TokenHeader String token
    ) throws URISyntaxException, IOException, WorkflowImportException {

    LOG.debug("Importing FWZ");

    Workflow workflow = workflowImportService.importFile(fwz.getResource());
    URI location = new URI(String.format("/workflows/%s", workflow.getId()));

    return ResponseEntity.created(location).body(workflow);
  }

  @GetMapping(value = "/search", produces = { MediaType.APPLICATION_JSON_VALUE })
  public JsonNode searchWorkflows(
    @RequestParam String query,
    @RequestParam(defaultValue="0") Long offset,
    @RequestParam(defaultValue="20") Integer limit,
    @TenantHeader String tenant
  ) {
    LOG.debug(String.format("Performing CQL search: %s, %s, %s", sanitize(query), offset, limit));
    return workflowCqlService.findByCql(query, offset, limit);
  }

  @GetMapping(value = "/{id}", produces = { MediaType.APPLICATION_JSON_VALUE })
  public Workflow getWorkflow(
    @PathVariable String id,
    @TenantHeader String tenant,
    @TokenHeader String token
  ) {
    return workflowRepo.getReferenceById(id);
  }

  @PutMapping(value = "/{id}/activate", produces = { MediaType.APPLICATION_JSON_VALUE })
  public Workflow activateWorkflow(
    @PathVariable String id,
    @TenantHeader String tenant,
    @TokenHeader String token
  ) throws WorkflowEngineServiceException, WorkflowNotFoundException {
    LOG.info(String.format("Activating: %s", sanitize(id)));

    workflowEngineService.exists(id);

    return workflowEngineService.activate(id, tenant, token);
  }

  @PutMapping(value = "/{id}/deactivate", produces = { MediaType.APPLICATION_JSON_VALUE })
  public Workflow deactivateWorkflow(
    @PathVariable String id,
    @TenantHeader String tenant,
    @TokenHeader String token
  ) throws WorkflowEngineServiceException, WorkflowNotFoundException {
    LOG.info(String.format("Deactivating: %s", sanitize(id)));

    workflowEngineService.exists(id);

    return workflowEngineService.deactivate(id, tenant, token);
  }

  @DeleteMapping(value = "/{id}/delete")
  public ResponseEntity<Object> deleteWorkflow(
    @PathVariable String id,
    @TenantHeader String tenant,
    @TokenHeader String token
  ) throws WorkflowEngineServiceException, WorkflowNotFoundException {
    LOG.info(String.format("Deleting: %s", sanitize(id)));

    workflowEngineService.exists(id);

    workflowEngineService.delete(id, tenant, token);

    // Ensure that a HTTP 204 is returned on success.
    return ResponseEntity.noContent().build();
  }

  @GetMapping(value = "/{id}/history", produces = { MediaType.APPLICATION_JSON_VALUE })
  public JsonNode workflowHistory(
    @PathVariable String id,
    @TenantHeader String tenant,
    @TokenHeader String token
  ) throws WorkflowDeploymentNotFound, WorkflowEngineServiceException {
    LOG.debug(String.format("Retrieving History: %s", sanitize(id)));
    return workflowEngineService.history(id, tenant, token);
  }

  @PostMapping(value = "/{id}/start", produces = { MediaType.APPLICATION_JSON_VALUE })
  public JsonNode startWorkflow(
    @PathVariable String id,
    @TenantHeader String tenant,
    @TokenHeader String token,
    @RequestBody JsonNode context
  ) throws WorkflowDeploymentNotFound, WorkflowEngineServiceException, WorkflowNotFoundException {
    LOG.info(String.format("Starting: %s with context %s", sanitize(id), sanitize(context)));
    return workflowEngineService.start(id, tenant, token, context);
  }

  /**
   * Sanitize string parameter for logging purposes.
   *
   * Strip out all non-graph, non-whitespace, non-newline, non-carriage return characters.
   *
   * @param param The parameter to sanitize.
   *
   * @return A sanitized string.
   */
  private String sanitize(String param) {
    if (param == null) return "";

    final Matcher matchNotGraph = REGX_NOT_GRAPH.matcher(param);
    final Matcher matchEol = REGX_EOL.matcher(matchNotGraph.replaceAll(""));

    return matchEol.replaceAll(" ");
  }

  /**
   * Sanitize JsonNode parameter for logging purposes.
   *
   * Strip out all non-graph, non-whitespace, non-newline, non-carriage return characters.
   *
   * @param param The parameter to sanitize.
   *
   * @return A sanitized string.
   */
  private String sanitize(JsonNode param) {
    if (param == null) return "";

    return sanitize(param.toPrettyString());
  }

}
