package org.folio.rest.workflow.model;

import static org.folio.spring.test.mock.MockMvcConstant.INT_VALUE;
import static org.folio.spring.test.mock.MockMvcConstant.NULL_STR;
import static org.folio.spring.test.mock.MockMvcConstant.VALUE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.util.ReflectionTestUtils.getField;
import static org.springframework.test.util.ReflectionTestUtils.setField;

import java.util.HashMap;
import java.util.HashSet;
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
    setField(ftpTask, "id", VALUE);

    assertEquals(VALUE, ftpTask.getId());
  }

  @Test
  void setIdWorksTest() {
    setField(ftpTask, "id", null);

    ftpTask.setId(VALUE);
    assertEquals(VALUE, getField(ftpTask, "id"));
  }

  @Test
  void getNameWorksTest() {
    setField(ftpTask, "name", VALUE);

    assertEquals(VALUE, ftpTask.getName());
  }

  @Test
  void setNameWorksTest() {
    setField(ftpTask, "name", null);

    ftpTask.setName(VALUE);
    assertEquals(VALUE, getField(ftpTask, "name"));
  }

  @Test
  void getDescriptionWorksTest() {
    setField(ftpTask, "description", VALUE);

    assertEquals(VALUE, ftpTask.getDescription());
  }

  @Test
  void setDescriptionWorksTest() {
    setField(ftpTask, "description", null);

    ftpTask.setDescription(VALUE);
    assertEquals(VALUE, getField(ftpTask, "description"));
  }

  @Test
  void getDeserializeAsWorksTest() {
    setField(ftpTask, "deserializeAs", VALUE);

    assertEquals(VALUE, ftpTask.getDeserializeAs());
  }

  @Test
  void setDeserializeAsWorksTest() {
    setField(ftpTask, "deserializeAs", null);

    ftpTask.setDeserializeAs(VALUE);
    assertEquals(VALUE, getField(ftpTask, "deserializeAs"));
  }

  @Test
  void getInputVariablesWorksTest() {
    setField(ftpTask, "inputVariables", inputVariables);

    assertEquals(inputVariables, ftpTask.getInputVariables());
  }

  @Test
  void setInputVariablesWorksTest() {
    setField(ftpTask, "inputVariables", null);

    ftpTask.setInputVariables(inputVariables);
    assertEquals(inputVariables, getField(ftpTask, "inputVariables"));
  }

  @Test
  void getOutputVariableWorksTest() {
    setField(ftpTask, "outputVariable", embeddedVariable);

    assertEquals(embeddedVariable, ftpTask.getOutputVariable());
  }

  @Test
  void setOutputVariableWorksTest() {
    setField(ftpTask, "outputVariable", null);

    ftpTask.setOutputVariable(embeddedVariable);
    assertEquals(embeddedVariable, getField(ftpTask, "outputVariable"));
  }

  @Test
  void getAsyncBeforeWorksTest() {
    setField(ftpTask, "asyncBefore", true);

    assertEquals(true, ftpTask.getAsyncBefore());
  }

  @Test
  void setAsyncBeforeWorksTest() {
    setField(ftpTask, "asyncBefore", false);

    ftpTask.setAsyncBefore(true);
    assertEquals(true, getField(ftpTask, "asyncBefore"));
  }

  @Test
  void getAsyncAfterWorksTest() {
    setField(ftpTask, "asyncAfter", true);

    assertEquals(true, ftpTask.getAsyncAfter());
  }

  @Test
  void setAsyncAfterWorksTest() {
    setField(ftpTask, "asyncAfter", false);

    ftpTask.setAsyncAfter(true);
    assertEquals(true, getField(ftpTask, "asyncAfter"));
  }

  @Test
  void getOriginPathWorksTest() {
    setField(ftpTask, "originPath", VALUE);

    assertEquals(VALUE, ftpTask.getOriginPath());
  }

  @Test
  void setOriginPathWorksTest() {
    setField(ftpTask, "originPath", null);

    ftpTask.setOriginPath(VALUE);
    assertEquals(VALUE, getField(ftpTask, "originPath"));
  }

  @Test
  void getDestinationPathWorksTest() {
    setField(ftpTask, "destinationPath", VALUE);

    assertEquals(VALUE, ftpTask.getDestinationPath());
  }

  @Test
  void setDestinationPathWorksTest() {
    setField(ftpTask, "destinationPath", null);

    ftpTask.setDestinationPath(VALUE);
    assertEquals(VALUE, getField(ftpTask, "destinationPath"));
  }

  @Test
  void getOpWorksTest() {
    setField(ftpTask, "op", SftpOp.GET);

    assertEquals(SftpOp.GET, ftpTask.getOp());
  }

  @Test
  void setOpWorksTest() {
    setField(ftpTask, "op", null);

    ftpTask.setOp(SftpOp.GET);
    assertEquals(SftpOp.GET, getField(ftpTask, "op"));
  }

  @Test
  void getSchemeWorksTest() {
    setField(ftpTask, "scheme", VALUE);

    assertEquals(VALUE, ftpTask.getScheme());
  }

  @Test
  void setSchemeWorksTest() {
    setField(ftpTask, "scheme", null);

    ftpTask.setScheme(VALUE);
    assertEquals(VALUE, getField(ftpTask, "scheme"));
  }

  @Test
  void getHostWorksTest() {
    setField(ftpTask, "host", VALUE);

    assertEquals(VALUE, ftpTask.getHost());
  }

  @Test
  void setHostWorksTest() {
    setField(ftpTask, "host", null);

    ftpTask.setHost(VALUE);
    assertEquals(VALUE, getField(ftpTask, "host"));
  }

  @Test
  void getPortWorksTest() {
    setField(ftpTask, "port", 1);

    assertEquals(1, ftpTask.getPort());
  }

  @Test
  void setPortWorksTest() {
    setField(ftpTask, "port", 0);

    ftpTask.setPort(1);
    assertEquals(1, getField(ftpTask, "port"));
  }

  @Test
  void getUsernameWorksTest() {
    setField(ftpTask, "username", VALUE);

    assertEquals(VALUE, ftpTask.getUsername());
  }

  @Test
  void setUsernameWorksTest() {
    setField(ftpTask, "username", null);

    ftpTask.setUsername(VALUE);
    assertEquals(VALUE, getField(ftpTask, "username"));
  }

  @Test
  void getPasswordWorksTest() {
    setField(ftpTask, "password", VALUE);

    assertEquals(VALUE, ftpTask.getPassword());
  }

  @Test
  void setPasswordWorksTest() {
    setField(ftpTask, "password", null);

    ftpTask.setPassword(VALUE);
    assertEquals(VALUE, getField(ftpTask, "password"));
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

    return Stream.of(
      Arguments.of(
        helperFieldMap(NULL_STR, NULL_STR, null,       NULL_STR, null,      NULL_STR),
        helperFieldMap("",       "",       SftpOp.GET, "",       80,        "")
      ),
      Arguments.of(
        helperFieldMap(VALUE,    NULL_STR, SftpOp.PUT, NULL_STR, null,      NULL_STR),
        helperFieldMap(VALUE,    "",       SftpOp.PUT, "",       80,        "")
      ),
      Arguments.of(
        helperFieldMap(VALUE,    VALUE,     null,       VALUE,    null,      NULL_STR),
        helperFieldMap(VALUE,    VALUE  ,   SftpOp.GET, VALUE,    80,        "")
      ),
      Arguments.of(
        helperFieldMap(VALUE,   VALUE,     SftpOp.PUT, VALUE,    INT_VALUE, VALUE),
        helperFieldMap(VALUE,   VALUE  ,   SftpOp.PUT, VALUE,    INT_VALUE, VALUE)
      )
    );
  }

  /**
   * Helper for reducing inline code repititon for assignments.
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
  private static Map<String, Object> helperFieldMap(String destinationPath, String host, SftpOp op, String originPath, Integer port, String scheme ) {
    final Map<String, Object> map = new HashMap<>();

    map.put("destinationPath", destinationPath);
    map.put("host", host);
    map.put("op", op);
    map.put("originPath", originPath);
    map.put("port", port);
    map.put("scheme", scheme);

    return map;
  }

}
