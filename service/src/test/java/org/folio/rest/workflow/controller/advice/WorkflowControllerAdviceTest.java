package org.folio.rest.workflow.controller.advice;

import static org.folio.spring.test.mock.MockMvcConstant.INT_VALUE;
import static org.folio.spring.test.mock.MockMvcConstant.VALUE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import jakarta.persistence.EntityNotFoundException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;
import org.folio.rest.workflow.exception.WorkflowAlreadyActiveException;
import org.folio.rest.workflow.exception.WorkflowCreateAlreadyExistsException;
import org.folio.rest.workflow.exception.WorkflowDeploymentException;
import org.folio.rest.workflow.exception.WorkflowDeploymentNotActivated;
import org.folio.rest.workflow.exception.WorkflowDeploymentNotFound;
import org.folio.rest.workflow.exception.WorkflowEngineServiceException;
import org.folio.rest.workflow.exception.WorkflowImportAlreadyImported;
import org.folio.rest.workflow.exception.WorkflowImportException;
import org.folio.rest.workflow.exception.WorkflowImportInvalidOrMissingProperty;
import org.folio.rest.workflow.exception.WorkflowImportJsonFileIsDirectory;
import org.folio.rest.workflow.exception.WorkflowImportRequiredFileMissing;
import org.folio.rest.workflow.exception.WorkflowNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
class WorkflowControllerAdviceTest {

  private static final RuntimeException R_EXC = new RuntimeException(VALUE);

  private static final EntityNotFoundException ENF_EXC1 = new EntityNotFoundException(VALUE);
  private static final EntityNotFoundException ENF_EXC2 = new EntityNotFoundException(VALUE, R_EXC);

  private static final WorkflowCreateAlreadyExistsException WCAE_EXC1 = new WorkflowCreateAlreadyExistsException(VALUE, VALUE);
  private static final WorkflowCreateAlreadyExistsException WCAE_EXC2 = new WorkflowCreateAlreadyExistsException(VALUE, VALUE, R_EXC);

  private static final WorkflowDeploymentNotActivated WDNA_EXC1 = new WorkflowDeploymentNotActivated(VALUE);
  private static final WorkflowDeploymentNotActivated WDNA_EXC2 = new WorkflowDeploymentNotActivated(VALUE, R_EXC);

  private static final WorkflowNotFoundException WNF_EXC1 = new WorkflowNotFoundException(VALUE);
  private static final WorkflowNotFoundException WNF_EXC2 = new WorkflowNotFoundException(VALUE, R_EXC);

  private static final WorkflowAlreadyActiveException WAA_EXC1 = new WorkflowAlreadyActiveException(VALUE);
  private static final WorkflowAlreadyActiveException WAA_EXC2 = new WorkflowAlreadyActiveException(VALUE, R_EXC);

  private static final WorkflowDeploymentException WD_EXC1 = new WorkflowDeploymentException();

  private static final WorkflowDeploymentNotFound WDNF_EXC1 = new WorkflowDeploymentNotFound(VALUE);
  private static final WorkflowDeploymentNotFound WDNF_EXC2 = new WorkflowDeploymentNotFound(VALUE, R_EXC);
  private static final WorkflowDeploymentNotFound WDNF_EXC3 = new WorkflowDeploymentNotFound(INT_VALUE);
  private static final WorkflowDeploymentNotFound WDNF_EXC4 = new WorkflowDeploymentNotFound(INT_VALUE, R_EXC);

  private static final WorkflowEngineServiceException WES_EXC1 = new WorkflowEngineServiceException(VALUE);
  private static final WorkflowEngineServiceException WES_EXC2 = new WorkflowEngineServiceException(VALUE, R_EXC);
  private static final WorkflowEngineServiceException WES_EXC3 = new WorkflowEngineServiceException(INT_VALUE);
  private static final WorkflowEngineServiceException WES_EXC4 = new WorkflowEngineServiceException(INT_VALUE, R_EXC);

  private static final WorkflowImportAlreadyImported WIAI_EXC1 = new WorkflowImportAlreadyImported(VALUE);
  private static final WorkflowImportAlreadyImported WIAI_EXC2 = new WorkflowImportAlreadyImported(VALUE, R_EXC);

  private static final WorkflowImportInvalidOrMissingProperty WIIOMP_EXC1 = new WorkflowImportInvalidOrMissingProperty(VALUE, VALUE);
  private static final WorkflowImportInvalidOrMissingProperty WIIOMP_EXC2 = new WorkflowImportInvalidOrMissingProperty(VALUE, VALUE, R_EXC);

  private static final WorkflowImportJsonFileIsDirectory WIJFID_EXC1 = new WorkflowImportJsonFileIsDirectory(VALUE);
  private static final WorkflowImportJsonFileIsDirectory WIJFID_EXC2 = new WorkflowImportJsonFileIsDirectory(VALUE, R_EXC);

  private static final WorkflowImportRequiredFileMissing WIRFM_EXC1 = new WorkflowImportRequiredFileMissing(VALUE);
  private static final WorkflowImportRequiredFileMissing WIRFM_EXC2 = new WorkflowImportRequiredFileMissing(VALUE, R_EXC);

  private WorkflowControllerAdvice advice;

  @BeforeEach
  void beforeEach() {
    advice = new WorkflowControllerAdvice();
  }

  @ParameterizedTest
  @MethodSource("provideEntityNotFoundExceptions")
  void handleEntityNotFoundExceptionTest(EntityNotFoundException exception, String simpleName) {

    final ResponseEntity<String> response = advice.handleEntityNotFoundException(exception);

    assertNotNull(response);
    assertNotNull(response.getBody());

    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    assertEquals(MediaType.APPLICATION_JSON, response.getHeaders().getContentType());
    assertTrue(matchBody(response, simpleName));
  }

  @ParameterizedTest
  @MethodSource("provideWorkflowCreateAlreadyExistsExceptions")
  void handleWorkflowCreateAlreadyExistsExceptionTest(WorkflowCreateAlreadyExistsException exception, String simpleName) {

    final ResponseEntity<String> response = advice.handleWorkflowCreateAlreadyExistsException(exception);

    assertNotNull(response);
    assertNotNull(response.getBody());

    assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
    assertEquals(MediaType.APPLICATION_JSON, response.getHeaders().getContentType());
    assertTrue(matchBody(response, simpleName));
  }

  @ParameterizedTest
  @MethodSource("provideWorkflowDeploymentNotActivateds")
  void handleorkflowDeploymentNotActivatedTest(WorkflowDeploymentNotActivated exception, String simpleName) {

    final ResponseEntity<String> response = advice.handleWorkflowDeploymentNotActivated(exception);

    assertNotNull(response);
    assertNotNull(response.getBody());

    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    assertEquals(MediaType.APPLICATION_JSON, response.getHeaders().getContentType());
    assertTrue(matchBody(response, simpleName));
  }

  @ParameterizedTest
  @MethodSource("provideWorkflowNotFoundExceptions")
  void handleWorkflowNotFoundExceptionTest(WorkflowNotFoundException exception, String simpleName) {

    final ResponseEntity<String> response = advice.handleWorkflowNotFoundException(exception);

    assertNotNull(response);
    assertNotNull(response.getBody());

    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    assertEquals(MediaType.APPLICATION_JSON, response.getHeaders().getContentType());
    assertTrue(matchBody(response, simpleName));
  }

  @ParameterizedTest
  @MethodSource("provideWorkflowAlreadyActiveExceptions")
  void handleWorkflowAlreadyActiveExceptionTest(WorkflowAlreadyActiveException exception, String simpleName) {

    final ResponseEntity<String> response = advice.handleWorkflowAlreadyActiveException(exception);

    assertNotNull(response);
    assertNotNull(response.getBody());

    assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
    assertEquals(MediaType.APPLICATION_JSON, response.getHeaders().getContentType());
    assertTrue(matchBody(response, simpleName));
  }

  @ParameterizedTest
  @MethodSource("provideWorkflowDeploymentExceptions")
  void handleWorkflowDeploymentExceptionTest(WorkflowDeploymentException exception, String simpleName) {

    final ResponseEntity<String> response = advice.handleWorkflowDeploymentException(exception);

    assertNotNull(response);
    assertNotNull(response.getBody());

    assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    assertEquals(MediaType.APPLICATION_JSON, response.getHeaders().getContentType());
    assertTrue(matchBody(response, simpleName));
  }

  @ParameterizedTest
  @MethodSource("provideWorkflowDeploymentNotFounds")
  void handleWorkflowDeploymentNotFoundTest(WorkflowDeploymentNotFound exception, String simpleName) {

    final ResponseEntity<String> response = advice.handleWorkflowDeploymentNotFound(exception);

    assertNotNull(response);
    assertNotNull(response.getBody());

    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    assertEquals(MediaType.APPLICATION_JSON, response.getHeaders().getContentType());
    assertTrue(matchBody(response, simpleName));
  }

  @ParameterizedTest
  @MethodSource("provideWorkflowEngineServiceExceptions")
  void handleWorkflowEngineServiceExceptionTest(WorkflowEngineServiceException exception, String simpleName) {

    final ResponseEntity<String> response = advice.handleWorkflowEngineServiceException(exception);

    assertNotNull(response);
    assertNotNull(response.getBody());

    assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    assertEquals(MediaType.APPLICATION_JSON, response.getHeaders().getContentType());
    assertTrue(matchBody(response, simpleName));
  }

  @ParameterizedTest
  @MethodSource("provideWorkflowImportExceptions")
  void handleWorkflowImportExceptionTest(WorkflowImportException exception, String simpleName) {

    final ResponseEntity<String> response = advice.handleWorkflowImportException(exception);

    assertNotNull(response);
    assertNotNull(response.getBody());

    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    assertEquals(MediaType.APPLICATION_JSON, response.getHeaders().getContentType());
    assertTrue(matchBody(response, simpleName));
  }

  /**
   * Match the class simple name in the response.
   *
   * @param response The response to search.
   * @param simpleName The class name to match.
   *
   * @return TRUE on match; FALSE otherwise.
   */
  private boolean matchBody(ResponseEntity<String> response, String simpleName) {

    final Pattern pattern = Pattern.compile("\"type\":\"" + simpleName + "\"");
    final Matcher matcher = pattern.matcher(response.getBody());

    return matcher.find();
  }

  /**
   * Helper function for parameterized test providing different types of EntityNotFoundException.
   *
   * @return
   *   The arguments array stream with the stream columns as:
   *     - Exception exception.
   *     - String simpleName (exception name to match).
   */
  private static Stream<Arguments> provideEntityNotFoundExceptions() {

    return Stream.of(
      Arguments.of(ENF_EXC1, EntityNotFoundException.class.getSimpleName()),
      Arguments.of(ENF_EXC2, EntityNotFoundException.class.getSimpleName())
    );
  }

  /**
   * Helper function for parameterized test providing different types of WorkflowCreateAlreadyExistsException.
   *
   * @return
   *   The arguments array stream with the stream columns as:
   *     - Exception exception.
   *     - String simpleName (exception name to match).
   */
  private static Stream<Arguments> provideWorkflowCreateAlreadyExistsExceptions() {

    return Stream.of(
      Arguments.of(WCAE_EXC1, WorkflowCreateAlreadyExistsException.class.getSimpleName()),
      Arguments.of(WCAE_EXC2, WorkflowCreateAlreadyExistsException.class.getSimpleName())
    );
  }

  /**
   * Helper function for parameterized test providing different types of WorkflowDeploymentNotActivated.
   *
   * @return
   *   The arguments array stream with the stream columns as:
   *     - Exception exception.
   *     - String simpleName (exception name to match).
   */
  private static Stream<Arguments> provideWorkflowDeploymentNotActivateds() {

    return Stream.of(
      Arguments.of(WDNA_EXC1, WorkflowDeploymentNotActivated.class.getSimpleName()),
      Arguments.of(WDNA_EXC2, WorkflowDeploymentNotActivated.class.getSimpleName())
    );
  }

  /**
   * Helper function for parameterized test providing different types of WorkflowNotFoundException.
   *
   * @return
   *   The arguments array stream with the stream columns as:
   *     - Exception exception.
   *     - String simpleName (exception name to match).
   */
  private static Stream<Arguments> provideWorkflowNotFoundExceptions() {

    return Stream.of(
      Arguments.of(WNF_EXC1, WorkflowNotFoundException.class.getSimpleName()),
      Arguments.of(WNF_EXC2, WorkflowNotFoundException.class.getSimpleName())
    );
  }

  /**
   * Helper function for parameterized test providing different types of WorkflowAlreadyActiveException.
   *
   * @return
   *   The arguments array stream with the stream columns as:
   *     - Exception exception.
   *     - String simpleName (exception name to match).
   */
  private static Stream<Arguments> provideWorkflowAlreadyActiveExceptions() {

    return Stream.of(
      Arguments.of(WAA_EXC1, WorkflowAlreadyActiveException.class.getSimpleName()),
      Arguments.of(WAA_EXC2, WorkflowAlreadyActiveException.class.getSimpleName())
    );
  }

  /**
   * Helper function for parameterized test providing different types of WorkflowDeploymentException.
   *
   * @return
   *   The arguments array stream with the stream columns as:
   *     - Exception exception.
   *     - String simpleName (exception name to match).
   */
  private static Stream<Arguments> provideWorkflowDeploymentExceptions() {

    return Stream.of(
      Arguments.of(WD_EXC1, WorkflowDeploymentException.class.getSimpleName())
    );
  }

  /**
   * Helper function for parameterized test providing different types of WorkflowDeploymentNotFound.
   *
   * @return
   *   The arguments array stream with the stream columns as:
   *     - Exception exception.
   *     - String simpleName (exception name to match).
   */
  private static Stream<Arguments> provideWorkflowDeploymentNotFounds() {

    return Stream.of(
      Arguments.of(WDNF_EXC1, WorkflowDeploymentNotFound.class.getSimpleName()),
      Arguments.of(WDNF_EXC2, WorkflowDeploymentNotFound.class.getSimpleName()),
      Arguments.of(WDNF_EXC3, WorkflowDeploymentNotFound.class.getSimpleName()),
      Arguments.of(WDNF_EXC4, WorkflowDeploymentNotFound.class.getSimpleName())
    );
  }

  /**
   * Helper function for parameterized test providing different types of WorkflowEngineServiceException.
   *
   * @return
   *   The arguments array stream with the stream columns as:
   *     - Exception exception.
   *     - String simpleName (exception name to match).
   */
  private static Stream<Arguments> provideWorkflowEngineServiceExceptions() {

    return Stream.of(
      Arguments.of(WES_EXC1, WorkflowEngineServiceException.class.getSimpleName()),
      Arguments.of(WES_EXC2, WorkflowEngineServiceException.class.getSimpleName()),
      Arguments.of(WES_EXC3, WorkflowEngineServiceException.class.getSimpleName()),
      Arguments.of(WES_EXC4, WorkflowEngineServiceException.class.getSimpleName())
    );
  }

  /**
   * Helper function for parameterized test providing different types of WorkflowImportException.
   *
   * @return
   *   The arguments array stream with the stream columns as:
   *     - Exception exception.
   *     - String simpleName (exception name to match).
   */
  private static Stream<Arguments> provideWorkflowImportExceptions() {

    return Stream.of(
      Arguments.of(WIAI_EXC1,   WorkflowImportAlreadyImported.class.getSimpleName()),
      Arguments.of(WIAI_EXC2,   WorkflowImportAlreadyImported.class.getSimpleName()),
      Arguments.of(WIIOMP_EXC1, WorkflowImportInvalidOrMissingProperty.class.getSimpleName()),
      Arguments.of(WIIOMP_EXC2, WorkflowImportInvalidOrMissingProperty.class.getSimpleName()),
      Arguments.of(WIJFID_EXC1, WorkflowImportJsonFileIsDirectory.class.getSimpleName()),
      Arguments.of(WIJFID_EXC2, WorkflowImportJsonFileIsDirectory.class.getSimpleName()),
      Arguments.of(WIRFM_EXC1,  WorkflowImportRequiredFileMissing.class.getSimpleName()),
      Arguments.of(WIRFM_EXC2,  WorkflowImportRequiredFileMissing.class.getSimpleName())
    );
  }

}
