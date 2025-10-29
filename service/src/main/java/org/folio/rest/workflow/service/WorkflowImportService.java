package org.folio.rest.workflow.service;

import static org.folio.rest.workflow.model.ExtractedWorkflow.CODE;
import static org.folio.rest.workflow.model.ExtractedWorkflow.DESERIALIZE_AS;
import static org.folio.rest.workflow.model.ExtractedWorkflow.FWZ_JSON;
import static org.folio.rest.workflow.model.ExtractedWorkflow.ID;
import static org.folio.rest.workflow.model.ExtractedWorkflow.JAVASCRIPT;
import static org.folio.rest.workflow.model.ExtractedWorkflow.JAVASCRIPT_EXT_NAME;
import static org.folio.rest.workflow.model.ExtractedWorkflow.JSON_EXT;
import static org.folio.rest.workflow.model.ExtractedWorkflow.NODES;
import static org.folio.rest.workflow.model.ExtractedWorkflow.PYTHON;
import static org.folio.rest.workflow.model.ExtractedWorkflow.PYTHON_EXT_NAME;
import static org.folio.rest.workflow.model.ExtractedWorkflow.RUBY;
import static org.folio.rest.workflow.model.ExtractedWorkflow.RUBY_EXT_NAME;
import static org.folio.rest.workflow.model.ExtractedWorkflow.SCRIPT_FORMAT;
import static org.folio.rest.workflow.model.ExtractedWorkflow.SCRIPT_TASK;
import static org.folio.rest.workflow.model.ExtractedWorkflow.SETUP_JSON;
import static org.folio.rest.workflow.model.ExtractedWorkflow.TRIGGERS;
import static org.folio.rest.workflow.model.ExtractedWorkflow.VERSION;
import static org.folio.rest.workflow.model.ExtractedWorkflow.VERSION_PATTERN_1_0;
import static org.folio.rest.workflow.model.ExtractedWorkflow.WORKFLOW_JSON;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeType;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.jknack.handlebars.internal.Files;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarFile;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipFile;
import org.apache.commons.compress.compressors.CompressorException;
import org.apache.commons.compress.compressors.bzip2.BZip2CompressorInputStream;
import org.apache.commons.compress.compressors.gzip.GzipCompressorInputStream;
import org.apache.commons.compress.utils.SeekableInMemoryByteChannel;
import org.folio.rest.workflow.enums.CompressFileFormat;
import org.folio.rest.workflow.exception.WorkflowImportAlreadyImported;
import org.folio.rest.workflow.exception.WorkflowImportException;
import org.folio.rest.workflow.exception.WorkflowImportInvalidOrMissingProperty;
import org.folio.rest.workflow.exception.WorkflowImportJsonFileIsDirectory;
import org.folio.rest.workflow.exception.WorkflowImportRequiredFileMissing;
import org.folio.rest.workflow.model.ExtractedWorkflow;
import org.folio.rest.workflow.model.Node;
import org.folio.rest.workflow.model.Trigger;
import org.folio.rest.workflow.model.Workflow;
import org.folio.rest.workflow.model.repo.NodeRepo;
import org.folio.rest.workflow.model.repo.TriggerRepo;
import org.folio.rest.workflow.model.repo.WorkflowRepo;
import org.folio.rest.workflow.utility.CompressFileMagic;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class WorkflowImportService {

  private ObjectMapper objectMapper;

  private NodeRepo nodeRepo;

  private TriggerRepo triggerRepo;

  private WorkflowRepo workflowRepo;

  public WorkflowImportService(ObjectMapper objectMapper, NodeRepo nodeRepo, TriggerRepo triggerRepo, WorkflowRepo workflowRepo) {
    this.objectMapper = objectMapper;
    this.nodeRepo = nodeRepo;
    this.triggerRepo = triggerRepo;
    this.workflowRepo = workflowRepo;
  }

  /**
   * Import the given FWZ file, creating a new Workflow.
   *
   * Every format, except for the ZIP format, uses the TAR format as a container.
   *
   * @param fwz The file to import.
   *
   * @return The created Workflow.
   *
   * @throws ArchiveException On archive error.
   * @throws CompressorException On compressor error.
   * @throws IOException On error reading the file stream, extracting the JSON, or other such errors.
   * @throws WorkflowImportException On import failure.
   */
  public Workflow importFile(Resource fwz) throws IOException, CompressorException, ArchiveException, WorkflowImportException {
    CompressFileFormat format = CompressFileMagic.detectFormat(fwz.getInputStream());

    if (format != null) {
      ExtractedWorkflow extracted = new ExtractedWorkflow();

      switch (format) {
        case BZIP2:
          processTar(fwz, extracted, CompressFileFormat.BZIP2);
          break;

        case GZIP:
          processTar(fwz, extracted, CompressFileFormat.GZIP);
          break;

        default:
          processZip(fwz, extracted);
          break;
      }

      if (!extracted.getRequired().containsKey(SETUP_JSON)) {
        throw new WorkflowImportRequiredFileMissing(SETUP_JSON);
      }

      if (!extracted.getRequired().containsKey(WORKFLOW_JSON)) {
        throw new WorkflowImportRequiredFileMissing(WORKFLOW_JSON);
      }

      collapseNodeScripts(extracted);
      verifyExistence(extracted.getRequired().get(WORKFLOW_JSON));
      expandWorkflow(extracted);
      createTriggers(extracted.getTriggers());
      createNodes(extracted.getNodes(), extracted.getExpanded());

      return createWorkflow(extracted.getRequired().get(WORKFLOW_JSON));
    }

    throw new WorkflowImportException("Cannot import due to unknown FWZ compression format.");
  }

  /**
   * Walk through each Node and merge any found scripts.
   *
   * @param extracted The extracted data.
   *
   * @throws WorkflowImportInvalidOrMissingProperty If a property is missing.
   * @throws WorkflowImportRequiredFileMissing If the script file is not found.
   * @throws JsonProcessingException On error processing the JSON.
   */
  private void collapseNodeScripts(ExtractedWorkflow extracted) throws WorkflowImportInvalidOrMissingProperty, WorkflowImportRequiredFileMissing, JsonProcessingException {
    for (Entry<String, JsonNode> entry : extracted.getNodes().entrySet()) {
      if (collapseNodeScriptsContinue(entry)) {
        continue;
      }

      String scriptFormat = entry.getValue().get(SCRIPT_FORMAT).asText();
      String fileName = entry.getValue().get(CODE).asText();
      String extension = scriptFormat.toLowerCase().trim();

      switch (scriptFormat) {
        case JAVASCRIPT:
          extension = JAVASCRIPT_EXT_NAME;
          break;
        case PYTHON:
          extension = PYTHON_EXT_NAME;
          break;
        case RUBY:
          extension = RUBY_EXT_NAME;
          break;
        default:
          break;
      }

      String scriptPath = String.format("%s/%s/%s", NODES, extension, fileName);
      if (!extracted.getScripts().containsKey(scriptPath)) {
        throw new WorkflowImportRequiredFileMissing(scriptPath);
      }

      String stringified = objectMapper.writeValueAsString(extracted.getScripts().get(scriptPath));

      ((ObjectNode) entry.getValue()).put(CODE, stringified);
    }
  }

  /**
   * Helper function for reducing the continue statements in collapseNodeScripts().
   *
   * This is added to appease SonarCloud's java:S135 issue.
   *
   * @param entry The entry of a single extracted data.
   *
   * @throws WorkflowImportInvalidOrMissingProperty If a property is missing.
   */
  private boolean collapseNodeScriptsContinue(Entry<String, JsonNode> entry) throws WorkflowImportInvalidOrMissingProperty {
    String deserializeAs = entry.getValue().get(DESERIALIZE_AS).asText();
    if (!SCRIPT_TASK.equalsIgnoreCase(deserializeAs)) {
      return true;
    }

    if (!entry.getValue().has(SCRIPT_FORMAT) || entry.getValue().get(SCRIPT_FORMAT).getNodeType() != JsonNodeType.STRING) {
      throw new WorkflowImportInvalidOrMissingProperty(entry.getKey(), SCRIPT_FORMAT);
    }

    if (!entry.getValue().has(CODE) || entry.getValue().get(CODE).getNodeType() != JsonNodeType.STRING) {
      throw new WorkflowImportInvalidOrMissingProperty(entry.getKey(), CODE);
    }

    return false;
  }

  /**
   * Create the Nodes in the database.
   *
   * @param nodes The Nodes to iterate over.
   * @param expanded An array of IDs of nodes representing the top-down order of creation.
   *
   * @throws JsonProcessingException On JSON parse failure.
   */
  private void createNodes(Map<String, JsonNode> nodes, List<String> expanded) throws JsonProcessingException {
    for (String uuid : expanded) {
      Node node = objectMapper.readValue(nodes.get(uuid).toString(), Node.class);
      nodeRepo.save(node);
    }
  }

  /**
   * Create the Triggers in the database.
   *
   * @param triggers The Triggers to iterate over.
   *
   * @throws JsonProcessingException On JSON parse failure.
   */
  private void createTriggers(Map<String, JsonNode> triggers) throws JsonProcessingException {
    for (JsonNode triggerNode : triggers.values()) {
      Trigger trigger = objectMapper.readValue(triggerNode.toString(), Trigger.class);
      triggerRepo.save(trigger);
    }
  }

  /**
   * Create the Workflow in the database.
   *
   * @param workflowJson The Workflow JSON to save.
   *
   * @return The created Workflow.
   *
   * @throws JsonProcessingException On JSON parse failure.
   */
  private Workflow createWorkflow(JsonNode workflowJson) throws JsonProcessingException {
    Workflow workflow = objectMapper.readValue(workflowJson.toString(), Workflow.class);
    return workflowRepo.save(workflow);
  }

  /**
   * Extract the Nodes and Scripts from the archive.
   *
   * @param name The file being processed.
   * @param pathParts The array of path parts.
   * @param inputStream The input stream to use.
   * @param extracted The extracted data.
   * @param isDirectory Designate the the entry is a directory.
   *
   * @throws IOException On error reading the file stream, extracting the JSON, or other such errors.
   * @throws WorkflowImportInvalidOrMissingProperty On import failure due to invalid or missing property.
   * @throws WorkflowImportJsonFileIsDirectory On import failure due to JSON file being actually a directory.
   */
  private void extractNodesAndScripts(String name, InputStream inputStream, ExtractedWorkflow extracted, boolean isDirectory) throws IOException, WorkflowImportInvalidOrMissingProperty, WorkflowImportJsonFileIsDirectory {
    String[] pathParts = name.split("/", 4);

    if (!verifyPathParts(name, pathParts)) {
      return;
    }

    if (pathParts.length == 1 || pathParts.length == 2) {
      if (pathParts[pathParts.length - 1].endsWith(JSON_EXT)) {
        if (isDirectory) {
          throw new WorkflowImportJsonFileIsDirectory(name);
        }

        if (pathParts.length == 1) {
          extractTopLevel(name, inputStream, extracted);
        } else {
          extractSubLevel(name, inputStream, extracted, pathParts);
        }
      } else if (!isDirectory) {
         warnOnUnknownFileOrDir(name);
      }
    } else {
      if (pathParts[0].equalsIgnoreCase(NODES)) {
        String script = Files.read(inputStream, StandardCharsets.UTF_8);
        extracted.getScripts().put(name, script);
      } else {
        warnOnUnknownFileOrDir(name);
      }
    }
  }

  /**
   * Extract the files that exist at the top-level.
   *
   * @param name The file being processed.
   * @param pathParts The array of path parts.
   * @param inputStream The input stream to use.
   * @param extracted The extracted data.
   *
   * @throws IOException On error reading the file stream, extracting the JSON, or other such errors.
   */
  private void extractTopLevel(String name, InputStream inputStream, ExtractedWorkflow extracted) throws IOException {
    if (FWZ_JSON.equalsIgnoreCase(name)) {
      extracted.getRequired().put(FWZ_JSON, objectMapper.readTree(inputStream));

      verifyVersion(extracted.getRequired().get(FWZ_JSON));
    } else if (WORKFLOW_JSON.equalsIgnoreCase(name)) {
      extracted.getRequired().put(WORKFLOW_JSON, objectMapper.readTree(inputStream));
    } else if (SETUP_JSON.equalsIgnoreCase(name)) {
      extracted.getRequired().put(SETUP_JSON, objectMapper.readTree(inputStream));
    } else {
      warnOnUnknownFileOrDir(name);
    }
  }

  /**
   * Extract the files that exist within the top-level directories.
   *
   * @param name The file being processed.
   * @param pathParts The array of path parts.
   * @param inputStream The input stream to use.
   * @param extracted The extracted data.
   * @param pathParts The broken up path parts.
   *
   * @throws IOException On error reading the file stream, extracting the JSON, or other such errors.
   * @throws WorkflowImportInvalidOrMissingProperty  On import failure due to invalid or missing property.
   */
  private void extractSubLevel(String name, InputStream inputStream, ExtractedWorkflow extracted, String[] pathParts) throws IOException, WorkflowImportInvalidOrMissingProperty {

    JsonNode json = objectMapper.readTree(inputStream);
    if (!json.has(ID) || json.get(ID).getNodeType() != JsonNodeType.STRING) {
      throw new WorkflowImportInvalidOrMissingProperty(name, ID);
    }

    if (!json.has(DESERIALIZE_AS) || json.get(DESERIALIZE_AS).getNodeType() != JsonNodeType.STRING) {
      throw new WorkflowImportInvalidOrMissingProperty(name, DESERIALIZE_AS);
    }

    if (pathParts[0].equalsIgnoreCase(NODES)) {
      extracted.getNodes().put(json.get(ID).asText(), json);
    } else {
      extracted.getTriggers().put(json.get(ID).asText(), json);
    }
  }

  /**
   * Replace the strings representing a Node with the actual Node for all Nodes.
   *
   * The expanded array effectively becomes a top-down dependency hierarchy order.
   *
   * @param extracted The extracted data.
   *
   * @throws JsonProcessingException On JSON parse failure.
   * @throws WorkflowImportInvalidOrMissingProperty On invalid property.
   */
  private void expandWorkflow(ExtractedWorkflow extracted) throws  JsonProcessingException, WorkflowImportInvalidOrMissingProperty {
    for (JsonNode node : extracted.getNodes().values()) {
      expandNode(extracted.getNodes(), node, extracted.getExpanded());
    }

    JsonNode workflowNode = extracted.getRequired().get(WORKFLOW_JSON);
    String workflowId = workflowNode.get(ID).asText();

    // The expandNode() method requires the Workflow to be on the getNodes(), so temporarily add it.
    extracted.getNodes().put(workflowId, extracted.getRequired().get(WORKFLOW_JSON));

    expandNode(extracted.getNodes(), extracted.getRequired().get(WORKFLOW_JSON), extracted.getExpanded());

    extracted.getNodes().remove(workflowId);
    extracted.getExpanded().remove(workflowId);
  }

  /**
   * Replace the strings representing a Node with the actual Node for all Nodes.
   *
   * The expanded array effectively becomes a top-down dependency hierarchy order.
   *
   * @param nodes The collection of all known Nodes.
   * @param node The node to process.
   * @param expanded An array of IDs of nodes that have already been expanded.
   *
   * @throws JsonProcessingException On JSON parse failure.
   * @throws WorkflowImportInvalidOrMissingProperty On invalid property.
   */
  private void expandNode(Map<String, JsonNode> nodes, JsonNode node, List<String> expanded) throws JsonProcessingException, WorkflowImportInvalidOrMissingProperty {
    String nodeId = node.get(ID).asText();
    if (expanded.contains(nodeId)) {
      return;
    }

    if (node.has(NODES)) {
      if (!node.get(NODES).isArray()) {
        throw new WorkflowImportInvalidOrMissingProperty(nodeId, NODES);
      }

      ArrayNode expandedNodes = objectMapper.createArrayNode();
      String[] values = objectMapper.readValue(node.get(NODES).toString(), String[].class);

      for (String value : values) {
        String[] parts = value.split("/");
        if (parts.length == 0) {
          throw new WorkflowImportInvalidOrMissingProperty(nodeId, NODES);
        }

        String uuid = parts[parts.length - 1];
        expandNode(nodes, nodes.get(uuid), expanded);
        expandedNodes.add(nodes.get(uuid));
      }

      ((ObjectNode) node).set(NODES, expandedNodes);
    }

    expanded.add(nodeId);
  }

  /**
   * Get the compression input stream based on the given format for TAR containers.
   *
   * @param inputStream The stream to de-compress.
   * @param format The compression format.
   *
   * @return An input stream based on the compression format.
   *
   * @throws IOException On failure to build the input stream.
   */
  private InputStream getCompressorInputStream(InputStream inputStream, CompressFileFormat format) throws IOException {
    if (CompressFileFormat.BZIP2.equals(format)) {
      return new BZip2CompressorInputStream(inputStream);
    }

    return new GzipCompressorInputStream(inputStream);
  }

  /**
   * Process the FWZ file for TAR formats.
   *
   * @param fwz The resource representing the FWZ JSON file.
   * @param extracted The extracted data.
   * @param format The compression format.
   *
   * @throws IOException On error reading the file stream, extracting the JSON, or other such errors.
   * @throws WorkflowImportException On import failure.
   */
  private void processTar(Resource fwz, ExtractedWorkflow extracted, CompressFileFormat format) throws IOException, WorkflowImportException {

    try (BufferedInputStream buffer = new BufferedInputStream(fwz.getInputStream());
          InputStream compressed = getCompressorInputStream(buffer, format);
          TarFile tarFile = new TarFile(compressed.readAllBytes());
        ) {

      for (TarArchiveEntry entry : tarFile.getEntries()) {
        extractNodesAndScripts(entry.getName(), tarFile.getInputStream(entry), extracted, entry.isDirectory());
      }
    }
  }

  /**
   * Process the FWZ file for a ZIP format.
   *
   * @param fwz The resource representing the FWZ file.
   * @param extracted The extracted data.
   *
   * @throws IOException On error reading the file stream, extracting the JSON, or other such errors.
   * @throws WorkflowImportException On import failure.
   */
  private void processZip(Resource fwz, ExtractedWorkflow extracted) throws IOException, WorkflowImportException {
    try (SeekableInMemoryByteChannel channel = new SeekableInMemoryByteChannel(fwz.getInputStream().readAllBytes());
          ZipFile zipFile = ZipFile.builder()
            .setSeekableByteChannel(channel).get();
        ) {

      Enumeration<ZipArchiveEntry> entries = zipFile.getEntries();

      while (entries.hasMoreElements()) {
        ZipArchiveEntry entry = entries.nextElement();
        extractNodesAndScripts(entry.getName(), zipFile.getInputStream(entry), extracted, entry.isDirectory());
      }
    }
  }

  /**
   * Get the Workflow UUID and Verify the existence of the Workflow.
   *
   * @param json The JSON data representing the Workflow.
   *
   * @throws WorkflowImportException On import failure.
   */
  private void verifyExistence(JsonNode json) throws WorkflowImportException {
    if (!json.has(ID) || json.get(ID) == null) {
      throw new WorkflowImportInvalidOrMissingProperty(null, ID);
    }

    String id = json.get(ID).asText();

    if (workflowRepo.existsById(id)) {
      throw new WorkflowImportAlreadyImported(id);
    }
  }

  /**
   * Verify that the path parts are valid.
   *
   * @param name The file being processed.
   * @param pathParts The array of path parts.
   *
   * @return TRUE if the paths are valid and FALSE otherwise.
   */
  private boolean verifyPathParts(String name, String[] pathParts) {
    if (pathParts.length == 1) {
      if (!FWZ_JSON.equalsIgnoreCase(pathParts[0]) && !SETUP_JSON.equalsIgnoreCase(pathParts[0]) && !WORKFLOW_JSON.equalsIgnoreCase(pathParts[0])) {
        if (!pathParts[0].equalsIgnoreCase(NODES) && !pathParts[0].equalsIgnoreCase(TRIGGERS)) {
          warnOnUnknownFileOrDir(name);
        }

        return false;
      }
    } else if (pathParts.length == 0) {
      return false;
    } else {
      if (!pathParts[0].equalsIgnoreCase(NODES) && !pathParts[0].equalsIgnoreCase(TRIGGERS) || pathParts.length > 3) {
        warnOnUnknownFileOrDir(name);
        return false;
      }
    }

    return true;
  }

  /**
   * Given fwz.json data, verify that the version is known.
   *
   * This only prints a warning for unknown versions.
   * Future implementations of this may potentially throw exceptions.
   *
   * @param json The JSON data representing the Workflow.
   */
  private void verifyVersion(JsonNode json) {
    if (json.has(VERSION)) {
      String version = json.get(VERSION).asText();

      if (!VERSION_PATTERN_1_0.matcher(version).find()) {
        log.warn("Unknown version '{}', attempting import anyway.", version);
      }
    } else {
      log.warn("No version is specified, attempting import anyway.");
    }
  }

  /**
   * Print warning for unknown file or directory.
   *
   * @param name The name of the unknown file or directory.
   */
  private void warnOnUnknownFileOrDir(String name) {
    log.warn("Ignoring unknown file or directory: '{}'.", name);
  }

}
