package org.folio.rest.workflow.model;

import static org.folio.rest.workflow.enums.SftpOp.GET;
import static org.folio.rest.workflow.enums.SftpOp.PUT;
import static org.folio.spring.test.mock.MockMvcConstant.INT_VALUE;
import static org.folio.spring.test.mock.MockMvcConstant.NULL_STR;
import static org.folio.spring.test.mock.MockMvcConstant.VALUE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.util.ReflectionTestUtils.getField;
import static org.springframework.test.util.ReflectionTestUtils.setField;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;
import org.folio.rest.workflow.enums.SftpOp;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class FtpTaskTest {

  private static final String ASYNCAFTER      = "asyncAfter";
  private static final String ASYNCBEFORE     = "asyncBefore";
  private static final String DESCRIPTION     = "description";
  private static final String DESERIALIZEAS   = "deserializeAs";
  private static final String DESTINATIONPATH = "destinationPath";
  private static final String FTP             = "ftp";
  private static final String HOST            = "host";
  private static final String ID              = "id";
  private static final String INPUTVARIABLES  = "inputVariables";
  private static final String NAME            = "name";
  private static final String ORIGINPATH      = "originPath";
  private static final String OP              = "op";
  private static final String OUTPUTVARIABLE  = "outputVariable";
  private static final String PASSWORD        = "password";
  private static final String PORT            = "port";
  private static final String SCHEME          = "scheme";
  private static final String USERNAME        = "username";

  private static final Integer DEFAULT_PORT = 80;

  @Mock
  private EmbeddedVariable embeddedVariable;

  private Set<EmbeddedVariable> inputVariables;

  private FtpTask ftpTask;

  @BeforeEach
  void beforeEach() {
    ftpTask = new FtpTask();
    inputVariables = new HashSet<>();
    inputVariables.add(embeddedVariable);
  }

  @Test
  void getIdWorksTest() {
    setField(ftpTask, ID, VALUE);

    assertEquals(VALUE, ftpTask.getId());
  }

  @Test
  void setIdWorksTest() {
    setField(ftpTask, ID, null);

    ftpTask.setId(VALUE);
    assertEquals(VALUE, getField(ftpTask, ID));
  }

  @Test
  void getNameWorksTest() {
    setField(ftpTask, NAME, VALUE);

    assertEquals(VALUE, ftpTask.getName());
  }

  @Test
  void setNameWorksTest() {
    setField(ftpTask, NAME, null);

    ftpTask.setName(VALUE);
    assertEquals(VALUE, getField(ftpTask, NAME));
  }

  @Test
  void getDescriptionWorksTest() {
    setField(ftpTask, DESCRIPTION, VALUE);

    assertEquals(VALUE, ftpTask.getDescription());
  }

  @Test
  void setDescriptionWorksTest() {
    setField(ftpTask, DESCRIPTION, null);

    ftpTask.setDescription(VALUE);
    assertEquals(VALUE, getField(ftpTask, DESCRIPTION));
  }

  @Test
  void getDeserializeAsWorksTest() {
    setField(ftpTask, DESERIALIZEAS, VALUE);

    assertEquals(VALUE, ftpTask.getDeserializeAs());
  }

  @Test
  void setDeserializeAsWorksTest() {
    setField(ftpTask, DESERIALIZEAS, null);

    ftpTask.setDeserializeAs(VALUE);
    assertEquals(VALUE, getField(ftpTask, DESERIALIZEAS));
  }

  @Test
  void getInputVariablesWorksTest() {
    setField(ftpTask, INPUTVARIABLES, inputVariables);

    assertEquals(inputVariables, ftpTask.getInputVariables());
  }

  @Test
  void setInputVariablesWorksTest() {
    setField(ftpTask, INPUTVARIABLES, null);

    ftpTask.setInputVariables(inputVariables);
    assertEquals(inputVariables, getField(ftpTask, INPUTVARIABLES));
  }

  @Test
  void getOutputVariableWorksTest() {
    setField(ftpTask, OUTPUTVARIABLE, embeddedVariable);

    assertEquals(embeddedVariable, ftpTask.getOutputVariable());
  }

  @Test
  void setOutputVariableWorksTest() {
    setField(ftpTask, OUTPUTVARIABLE, null);

    ftpTask.setOutputVariable(embeddedVariable);
    assertEquals(embeddedVariable, getField(ftpTask, OUTPUTVARIABLE));
  }

  @Test
  void getAsyncBeforeWorksTest() {
    setField(ftpTask, ASYNCBEFORE, true);

    assertEquals(true, ftpTask.getAsyncBefore());
  }

  @Test
  void setAsyncBeforeWorksTest() {
    setField(ftpTask, ASYNCBEFORE, false);

    ftpTask.setAsyncBefore(true);
    assertEquals(true, getField(ftpTask, ASYNCBEFORE));
  }

  @Test
  void getAsyncAfterWorksTest() {
    setField(ftpTask, ASYNCAFTER, true);

    assertEquals(true, ftpTask.getAsyncAfter());
  }

  @Test
  void setAsyncAfterWorksTest() {
    setField(ftpTask, ASYNCAFTER, false);

    ftpTask.setAsyncAfter(true);
    assertEquals(true, getField(ftpTask, ASYNCAFTER));
  }

  @Test
  void getOriginPathWorksTest() {
    setField(ftpTask, ORIGINPATH, VALUE);

    assertEquals(VALUE, ftpTask.getOriginPath());
  }

  @Test
  void setOriginPathWorksTest() {
    setField(ftpTask, ORIGINPATH, null);

    ftpTask.setOriginPath(VALUE);
    assertEquals(VALUE, getField(ftpTask, ORIGINPATH));
  }

  @Test
  void getDestinationPathWorksTest() {
    setField(ftpTask, DESTINATIONPATH, VALUE);

    assertEquals(VALUE, ftpTask.getDestinationPath());
  }

  @Test
  void setDestinationPathWorksTest() {
    setField(ftpTask, DESTINATIONPATH, null);

    ftpTask.setDestinationPath(VALUE);
    assertEquals(VALUE, getField(ftpTask, DESTINATIONPATH));
  }

  @Test
  void getOpWorksTest() {
    setField(ftpTask, OP, GET);

    assertEquals(GET, ftpTask.getOp());
  }

  @Test
  void setOpWorksTest() {
    setField(ftpTask, OP, null);

    ftpTask.setOp(GET);
    assertEquals(GET, getField(ftpTask, OP));
  }

  @Test
  void getSchemeWorksTest() {
    setField(ftpTask, SCHEME, VALUE);

    assertEquals(VALUE, ftpTask.getScheme());
  }

  @Test
  void setSchemeWorksTest() {
    setField(ftpTask, SCHEME, null);

    ftpTask.setScheme(VALUE);
    assertEquals(VALUE, getField(ftpTask, SCHEME));
  }

  @Test
  void getHostWorksTest() {
    setField(ftpTask, HOST, VALUE);

    assertEquals(VALUE, ftpTask.getHost());
  }

  @Test
  void setHostWorksTest() {
    setField(ftpTask, HOST, null);

    ftpTask.setHost(VALUE);
    assertEquals(VALUE, getField(ftpTask, HOST));
  }

  @Test
  void getPortWorksTest() {
    setField(ftpTask, PORT, 1);

    assertEquals(1, ftpTask.getPort());
  }

  @Test
  void setPortWorksTest() {
    setField(ftpTask, PORT, 0);

    ftpTask.setPort(1);
    assertEquals(1, getField(ftpTask, PORT));
  }

  @Test
  void getUsernameWorksTest() {
    setField(ftpTask, USERNAME, VALUE);

    assertEquals(VALUE, ftpTask.getUsername());
  }

  @Test
  void setUsernameWorksTest() {
    setField(ftpTask, USERNAME, null);

    ftpTask.setUsername(VALUE);
    assertEquals(VALUE, getField(ftpTask, USERNAME));
  }

  @Test
  void getPasswordWorksTest() {
    setField(ftpTask, PASSWORD, VALUE);

    assertEquals(VALUE, ftpTask.getPassword());
  }

  @Test
  void setPasswordWorksTest() {
    setField(ftpTask, PASSWORD, null);

    ftpTask.setPassword(VALUE);
    assertEquals(VALUE, getField(ftpTask, PASSWORD));
  }

  @Test
  void setBasePathDoesNothingTest() {
    ftpTask.setBasePath("");
    assertEquals("", ftpTask.getBasePath());
  }

  @Test
  void getBasePathWorksTest() {

    assertEquals("", ftpTask.getBasePath());
  }

  @ParameterizedTest
  @MethodSource("providePrePersistFor")
  void prePersistWorksTest(Map<String, Object> initial, Map<String, Object> expected) {
    initial.forEach((String attribute, Object value) -> {
      setField(ftpTask, attribute, value);
    });

    ftpTask.prePersist();

    expected.forEach((String attribute, Object value) -> {
      assertEquals(value, getField(ftpTask, attribute));
    });
  }

  /**
   * Helper function for parameterized tests for the prePersist function.
   *
   * @return
   *   The arguments array stream with the stream columns as:
   *     - Arguments initial The initial values.
   *     - Arguments expect The expected values.
   */
  private static Stream<Arguments> providePrePersistFor() {

    return List.of(
      Arguments.of(
        helperFieldMap(NULL_STR, NULL_STR, null, NULL_STR, null,        NULL_STR),
        helperFieldMap("",       "",       GET,  "",       DEFAULT_PORT, FTP)
      ),
      Arguments.of(
        helperFieldMap(VALUE,    NULL_STR, null, NULL_STR, null,        NULL_STR),
        helperFieldMap(VALUE,    "",       GET,  "",       DEFAULT_PORT, FTP)
      ),
      Arguments.of(
        helperFieldMap(NULL_STR, VALUE,    null, NULL_STR, null,        NULL_STR),
        helperFieldMap("",       VALUE,    GET,  "",       DEFAULT_PORT, FTP)
      ),
      Arguments.of(
        helperFieldMap(NULL_STR, NULL_STR, PUT,  NULL_STR, null,        NULL_STR),
        helperFieldMap("",       "",       PUT,  "",       DEFAULT_PORT, FTP)
      ),
      Arguments.of(
        helperFieldMap(NULL_STR, NULL_STR, null, VALUE,    null,        NULL_STR),
        helperFieldMap("",       "",       GET,  VALUE,    DEFAULT_PORT, FTP)
      ),
      Arguments.of(
        helperFieldMap(NULL_STR, NULL_STR, null, NULL_STR, INT_VALUE,   NULL_STR),
        helperFieldMap("",       "",       GET,  "",       INT_VALUE,   FTP)
      ),
      Arguments.of(
        helperFieldMap(NULL_STR, NULL_STR, null, NULL_STR, null,        VALUE),
        helperFieldMap("",       "",       GET,  "",       DEFAULT_PORT, VALUE)
      ),
      Arguments.of(
        helperFieldMap(VALUE,    VALUE,    PUT,  VALUE,    INT_VALUE,   VALUE),
        helperFieldMap(VALUE,    VALUE,    PUT,  VALUE,    INT_VALUE,   VALUE)
      )
    ).stream();
  }

  /**
   * Helper for reducing in line code repetition for assignments.
   *
   * @param destinationPath The destinationPath value.
   * @param host The host value.
   * @param op The op value.
   * @param originPath The originPath value.
   * @param port The port value.
   * @param scheme The scheme value.
   *
   * @return The built arguments map.
   */
  private static Map<String, Object> helperFieldMap(
    String destinationPath, String host, SftpOp op, String originPath, Integer port, String scheme
  ) {

    final Map<String, Object> map = new HashMap<>();

    map.put(DESTINATIONPATH, destinationPath);
    map.put(HOST, host);
    map.put(OP, op);
    map.put(ORIGINPATH, originPath);
    map.put(PORT, port);
    map.put(SCHEME, scheme);

    return map;
  }

}
