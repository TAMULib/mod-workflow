package org.folio.rest.workflow.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.core.StreamReadFeature;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.io.IOException;
import org.folio.rest.workflow.exception.WorkflowImportAlreadyImported;
import org.folio.rest.workflow.exception.WorkflowImportException;
import org.folio.rest.workflow.exception.WorkflowImportInvalidOrMissingProperty;
import org.folio.rest.workflow.exception.WorkflowImportRequiredFileMissing;
import org.folio.rest.workflow.model.Workflow;
import org.folio.rest.workflow.model.repo.NodeRepo;
import org.folio.rest.workflow.model.repo.TriggerRepo;
import org.folio.rest.workflow.model.repo.WorkflowRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(webEnvironment = WebEnvironment.MOCK)
@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
class WorkflowImportServiceTest {

  private static final String WORKFLOW_UUID = "7dcd302f-a438-4ca5-a7eb-21653610d46f";

  @Mock
  private NodeRepo nodeRepo;

  @Mock
  private TriggerRepo triggerRepo;

  @Mock
  private WorkflowRepo workflowRepo;

  @Mock
  private Page<Workflow> page;

  @Value("classpath:fwz/unit_test_fake.fwz")
  private Resource fwzFakeResource;

  @Value("classpath:fwz/unit_test_bzip2.tar.bz2")
  private Resource fwzBzip2AsBz2Resource;

  @Value("classpath:fwz/unit_test_bzip2.fwz")
  private Resource fwzBzip2Resource;

  @Value("classpath:fwz/unit_test_gzip.tar.gz")
  private Resource fwzGzipAsGzResource;

  @Value("classpath:fwz/unit_test_gzip-bad_code.fwz")
  private Resource fwzGzipBadCodeResource;

  @Value("classpath:fwz/unit_test_gzip-bad_deserializeas.fwz")
  private Resource fwzGzipBadDeserializeasResource;

  @Value("classpath:fwz/unit_test_gzip-bad_id.fwz")
  private Resource fwzGzipBadIdResource;

  @Value("classpath:fwz/unit_test_gzip-bad_nodes.fwz")
  private Resource fwzGzipBadNodesResource;

  @Value("classpath:fwz/unit_test_gzip-bad_scriptformat.fwz")
  private Resource fwzGzipBadScriptformatResource;

  @Value("classpath:fwz/unit_test_gzip-bad_version.fwz")
  private Resource fwzGzipBadVersionResource;

  @Value("classpath:fwz/unit_test_gzip-java.fwz")
  private Resource fwzGzipJavaResource;

  @Value("classpath:fwz/unit_test_gzip-missing_code.fwz")
  private Resource fwzGzipMisCodeResource;

  @Value("classpath:fwz/unit_test_gzip-missing_deserializeas.fwz")
  private Resource fwzGzipMisDeserializeasResource;

  @Value("classpath:fwz/unit_test_gzip-missing_id.fwz")
  private Resource fwzGzipMisIdResource;

  @Value("classpath:fwz/unit_test_gzip-missing_id_workflow_json.fwz")
  private Resource fwzGzipMisIdWorkflowJsonResource;

  @Value("classpath:fwz/unit_test_gzip-missing_script.fwz")
  private Resource fwzGzipMisScriptResource;

  @Value("classpath:fwz/unit_test_gzip-missing_scriptformat.fwz")
  private Resource fwzGzipMisScriptformatResource;

  @Value("classpath:fwz/unit_test_gzip-missing_setup.fwz")
  private Resource fwzGzipMisSetupResource;

  @Value("classpath:fwz/unit_test_gzip-missing_version.fwz")
  private Resource fwzGzipMisVersionResource;

  @Value("classpath:fwz/unit_test_gzip-missing_workflow.fwz")
  private Resource fwzGzipMisWorkflowResource;

  @Value("classpath:fwz/unit_test_gzip-odd_files.fwz")
  private Resource fwzGzipOddFilesResource;

  @Value("classpath:fwz/unit_test_gzip-python.fwz")
  private Resource fwzGzipPythonResource;

  @Value("classpath:fwz/unit_test_gzip.fwz")
  private Resource fwzGzipResource;

  @Value("classpath:fwz/unit_test_gzip-ruby.fwz")
  private Resource fwzGzipRubyResource;

  @Value("classpath:fwz/unit_test_gzip-unknown_version.fwz")
  private Resource fwzGzipUnVerResource;

  @Value("classpath:fwz/unit_test_zip.zip")
  private Resource fwzZipAsZipResource;

  @Value("classpath:fwz/unit_test_zip.fwz")
  private Resource fwzZipResource;

  private JsonMapper mapper;

  private Workflow workflow;

  private WorkflowImportService workflowImportService;

  @BeforeEach
  void beforeEach() {
    mapper = JsonMapper.builder()
      .enable(StreamReadFeature.INCLUDE_SOURCE_IN_LOCATION)
      .enable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
      .disable(MapperFeature.REQUIRE_TYPE_ID_FOR_SUBTYPES)
      .disable(DeserializationFeature.FAIL_ON_MISSING_EXTERNAL_TYPE_ID_PROPERTY)
      .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
      .addModule(new JavaTimeModule())
      .build();

    workflow = new Workflow();
    workflow.setId(WORKFLOW_UUID);

    workflowImportService = new WorkflowImportService(mapper, nodeRepo, triggerRepo, workflowRepo);

    lenient().when(workflowRepo.save(any())).thenReturn(workflow);
  }

  @Test
  void importFileThrowsExceptionForFakeTest() {
    assertThrows(WorkflowImportException.class, () ->
      workflowImportService.importFile(fwzFakeResource)
    );
  }

  @Test
  void importFileThrowsExceptionForExistingIdTest() {
    when(workflowRepo.existsById(anyString())).thenReturn(true);

    assertThrows(WorkflowImportAlreadyImported.class, () ->
      workflowImportService.importFile(fwzGzipResource)
    );
  }

  @Test
  void importFileThrowsExceptionWithBadCodeTest() {
    assertThrows(WorkflowImportInvalidOrMissingProperty.class, () ->
      workflowImportService.importFile(fwzGzipBadCodeResource)
    );
  }

  @Test
  void importFileThrowsExceptionWithBadDeserializeasTest() {
    assertThrows(WorkflowImportInvalidOrMissingProperty.class, () ->
      workflowImportService.importFile(fwzGzipBadDeserializeasResource)
    );
  }

  @Test
  void importFileThrowsExceptionWithBadIdTest() {
    assertThrows(WorkflowImportInvalidOrMissingProperty.class, () ->
      workflowImportService.importFile(fwzGzipBadIdResource)
    );
  }

  @Test
  void importFileThrowsExceptionWithBadScriptformatTest() {
    assertThrows(WorkflowImportInvalidOrMissingProperty.class, () ->
      workflowImportService.importFile(fwzGzipBadScriptformatResource)
    );
  }

  @Test
  void importFileThrowsExceptionWithBadNodesTest() {
    assertThrows(WorkflowImportInvalidOrMissingProperty.class, () ->
      workflowImportService.importFile(fwzGzipBadNodesResource)
    );
  }

  @Test
  void importFileThrowsExceptionWithMisCodeTest() {
    assertThrows(WorkflowImportInvalidOrMissingProperty.class, () ->
      workflowImportService.importFile(fwzGzipMisCodeResource)
    );
  }

  @Test
  void importFileThrowsExceptionWithMisDeserializeasTest() {
    assertThrows(WorkflowImportInvalidOrMissingProperty.class, () ->
      workflowImportService.importFile(fwzGzipMisDeserializeasResource)
    );
  }

  @Test
  void importFileThrowsExceptionWithMisIdTest() {
    assertThrows(WorkflowImportInvalidOrMissingProperty.class, () ->
      workflowImportService.importFile(fwzGzipMisIdResource)
    );
  }

  @Test
  void importFileThrowsExceptionWithMisIdWorkflowJsonTest() {
    assertThrows(WorkflowImportInvalidOrMissingProperty.class, () ->
      workflowImportService.importFile(fwzGzipMisIdWorkflowJsonResource)
    );
  }


  @Test
  void importFileThrowsExceptionWithMisScriptTest() {
    assertThrows(WorkflowImportRequiredFileMissing.class, () ->
      workflowImportService.importFile(fwzGzipMisScriptResource)
    );
  }

  @Test
  void importFileThrowsExceptionWithMisSetupTest() {
    assertThrows(WorkflowImportRequiredFileMissing.class, () ->
      workflowImportService.importFile(fwzGzipMisSetupResource)
    );
  }

  @Test
  void importFileThrowsExceptionWithMisWorkflowTest() {
    assertThrows(WorkflowImportRequiredFileMissing.class, () ->
      workflowImportService.importFile(fwzGzipMisWorkflowResource)
    );
  }

  @Test
  void importFileWorksForBzip2Test() throws IOException, WorkflowImportException {
    Workflow imported = workflowImportService.importFile(fwzBzip2Resource);
    assertNotNull(imported);
    assertEquals(workflow.getId(), imported.getId());
  }

  @Test
  void importFileWorksForBzip2AsBz2Test() throws IOException, WorkflowImportException {
    Workflow imported = workflowImportService.importFile(fwzBzip2AsBz2Resource);
    assertNotNull(imported);
    assertEquals(workflow.getId(), imported.getId());
  }

  @Test
  void importFileWorksForGzipTest() throws IOException, WorkflowImportException {
    Workflow imported = workflowImportService.importFile(fwzGzipResource);
    assertNotNull(imported);
    assertEquals(workflow.getId(), imported.getId());
  }

  @Test
  void importFileWorksForGzipAsGzTest() throws IOException, WorkflowImportException {
    Workflow imported = workflowImportService.importFile(fwzGzipAsGzResource);
    assertNotNull(imported);
    assertEquals(workflow.getId(), imported.getId());
  }

  @Test
  void importFileWorksForGzipWithBadVersionTest() throws IOException, WorkflowImportException {
    Workflow imported = workflowImportService.importFile(fwzGzipBadVersionResource);
    assertNotNull(imported);
    assertEquals(workflow.getId(), imported.getId());
  }

  @Test
  void importFileWorksForGzipWithJavaTest() throws IOException, WorkflowImportException {
    Workflow imported = workflowImportService.importFile(fwzGzipJavaResource);
    assertNotNull(imported);
    assertEquals(workflow.getId(), imported.getId());
  }

  @Test
  void importFileWorksForGzipWithOddFilesTest() throws IOException, WorkflowImportException {
    Workflow imported = workflowImportService.importFile(fwzGzipOddFilesResource);
    assertNotNull(imported);
    assertEquals(workflow.getId(), imported.getId());
  }

  @Test
  void importFileWorksForGzipWithPythonTest() throws IOException, WorkflowImportException {
    Workflow imported = workflowImportService.importFile(fwzGzipPythonResource);
    assertNotNull(imported);
    assertEquals(workflow.getId(), imported.getId());
  }

  @Test
  void importFileWorksForGzipWithRubyTest() throws IOException, WorkflowImportException {
    Workflow imported = workflowImportService.importFile(fwzGzipRubyResource);
    assertNotNull(imported);
    assertEquals(workflow.getId(), imported.getId());
  }

  @Test
  void importFileWorksForGzipWithMissingVersionTest() throws IOException, WorkflowImportException {
    Workflow imported = workflowImportService.importFile(fwzGzipMisVersionResource);
    assertNotNull(imported);
    assertEquals(workflow.getId(), imported.getId());
  }

  @Test
  void importFileWorksForGzipWithUnknownVersionTest() throws IOException, WorkflowImportException {
    Workflow imported = workflowImportService.importFile(fwzGzipUnVerResource);
    assertNotNull(imported);
    assertEquals(workflow.getId(), imported.getId());
  }

  @Test
  void importFileWorksForZipTest() throws IOException, WorkflowImportException {
    Workflow imported = workflowImportService.importFile(fwzZipResource);
    assertNotNull(imported);
    assertEquals(workflow.getId(), imported.getId());
  }

  @Test
  void importFileWorksForZipAsZipTest() throws IOException, WorkflowImportException {
    Workflow imported = workflowImportService.importFile(fwzZipAsZipResource);
    assertNotNull(imported);
    assertEquals(workflow.getId(), imported.getId());
  }

}
