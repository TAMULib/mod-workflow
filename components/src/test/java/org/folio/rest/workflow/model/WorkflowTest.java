package org.folio.rest.workflow.model;

import static org.folio.spring.test.mock.MockMvcConstant.INT_VALUE;
import static org.folio.spring.test.mock.MockMvcConstant.VALUE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.springframework.test.util.ReflectionTestUtils.getField;
import static org.springframework.test.util.ReflectionTestUtils.setField;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tools.jackson.databind.JsonNode;

@ExtendWith(MockitoExtension.class)
class WorkflowTest {

  private static final String ACTIVE            = "active";
  private static final String CHECKSUM          = "checksum";
  private static final String CREATEDON         = "createdOn";
  private static final String DESCRIPTION       = "description";
  private static final String DEPLOYMENTID      = "deploymentId";
  private static final String HISTORYTIMETOLIVE = "historyTimeToLive";
  private static final String ID                = "id";
  private static final String INITIALCONTEXT    = "initialContext";
  private static final String NAME              = "name";
  private static final String NODES             = "nodes";
  private static final String SETUP             = "setup";
  private static final String UPDATEDON         = "updatedOn";
  private static final String VERSIONTAG        = "versionTag";

  private static final Integer HIST_TTL = 90;

  /**
   * Provide a version string for the default version.
   */
  public static final String VERSION = "1.0";

  /**
   * An arbitrary hash.
   */
  public static final String HASH = "aee6d39cd3f123452aed7ada75440d7a";

  /**
   * An arbitrary date.
   */
  public static final Instant NOW = Instant.now();

  /**
   * An arbitrary date that occurs some time after the arbitrary now date.
   */
  public static final Instant LATER = Instant.from(NOW).plusSeconds(1000);

  @Mock
  private Setup setup;

  @Mock
  private Node node;

  private List<Node> nodes;

  private Map<String, JsonNode> initialContext;

  private Workflow workflow;

  @BeforeEach
  void beforeEach() {
    workflow = spy(Workflow.class);
    nodes = new ArrayList<>();
    nodes.add(node);
    initialContext = new HashMap<>();

    lenient().doReturn(NOW).when(workflow).now();
  }

  @Test
  void getIdWorksTest() {
    setField(workflow, ID, VALUE);

    assertEquals(VALUE, workflow.getId());
  }

  @Test
  void setIdWorksTest() {
    setField(workflow, ID, null);

    workflow.setId(VALUE);
    assertEquals(VALUE, getField(workflow, ID));
  }

  @Test
  void getNameWorksTest() {
    setField(workflow, NAME, VALUE);

    assertEquals(VALUE, workflow.getName());
  }

  @Test
  void setNameWorksTest() {
    setField(workflow, NAME, null);

    workflow.setName(VALUE);
    assertEquals(VALUE, getField(workflow, NAME));
  }

  @Test
  void getDescriptionWorksTest() {
    setField(workflow, DESCRIPTION, VALUE);

    assertEquals(VALUE, workflow.getDescription());
  }

  @Test
  void setDescriptionWorksTest() {
    setField(workflow, DESCRIPTION, null);

    workflow.setDescription(VALUE);
    assertEquals(VALUE, getField(workflow, DESCRIPTION));
  }

  @Test
  void getNodesWorksTest() {
    setField(workflow, NODES, nodes);

    assertEquals(nodes, workflow.getNodes());
  }

  @Test
  void setNodesWorksTest() {
    setField(workflow, NODES, null);

    workflow.setNodes(nodes);
    assertEquals(nodes, getField(workflow, NODES));
  }

  @Test
  void getVersionTagWorksTest() {
    setField(workflow, VERSIONTAG, VALUE);

    assertEquals(VALUE, workflow.getVersionTag());
  }

  @Test
  void setVersionTagWorksTest() {
    setField(workflow, VERSIONTAG, null);

    workflow.setVersionTag(VALUE);
    assertEquals(VALUE, getField(workflow, VERSIONTAG));
  }

  @Test
  void getHistoryTimeToLiveWorksTest() {
    setField(workflow, HISTORYTIMETOLIVE, 1);

    assertEquals(1, workflow.getHistoryTimeToLive());
  }

  @Test
  void setHistoryTimeToLiveWorksTest() {
    setField(workflow, HISTORYTIMETOLIVE, null);

    workflow.setHistoryTimeToLive(1);
    assertEquals(1, getField(workflow, HISTORYTIMETOLIVE));
  }

  @Test
  void getActiveWorksTest() {
    setField(workflow, ACTIVE, true);

    assertEquals(true, workflow.getActive());
  }

  @Test
  void setActiveWorksTest() {
    setField(workflow, ACTIVE, false);

    workflow.setActive(true);
    assertEquals(true, getField(workflow, ACTIVE));
  }

  @Test
  void getChecksumWorksTest() {
    setField(workflow, CHECKSUM, HASH);

    assertEquals(HASH, workflow.getChecksum());
  }

  @Test
  void setChecksumWorksTest() {
    setField(workflow, CHECKSUM, null);

    workflow.setChecksum(HASH);
    assertEquals(HASH, getField(workflow, CHECKSUM));
  }

  @Test
  void getCreatedOnWorksTest() {
    setField(workflow, CREATEDON, NOW);

    assertEquals(NOW, workflow.getCreatedOn());
  }

  @Test
  void setCreatedOnWorksTest() {
    setField(workflow, CREATEDON, null);

    workflow.setCreatedOn(NOW);
    assertEquals(NOW, getField(workflow, CREATEDON));
  }

  @Test
  void getDeploymentIdWorksTest() {
    setField(workflow, DEPLOYMENTID, VALUE);

    assertEquals(VALUE, workflow.getDeploymentId());
  }

  @Test
  void setDeploymentIdWorksTest() {
    setField(workflow, DEPLOYMENTID, null);

    workflow.setDeploymentId(VALUE);
    assertEquals(VALUE, getField(workflow, DEPLOYMENTID));
  }

  @Test
  void getSetupWorksTest() {
    setField(workflow, SETUP, setup);

    assertEquals(setup, workflow.getSetup());
  }

  @Test
  void setSetupWorksTest() {
    setField(workflow, SETUP, null);

    workflow.setSetup(setup);
    assertEquals(setup, getField(workflow, SETUP));
  }

  @Test
  void getInitialContextWorksTest() {
    setField(workflow, INITIALCONTEXT, initialContext);

    assertEquals(initialContext, workflow.getInitialContext());
  }

  @Test
  void setInitialContextWorksTest() {
    setField(workflow, INITIALCONTEXT, null);

    workflow.setInitialContext(initialContext);
    assertEquals(initialContext, getField(workflow, INITIALCONTEXT));
  }

  @Test
  void getUpdatedOnWorksTest() {
    setField(workflow, UPDATEDON, NOW);

    assertEquals(NOW, workflow.getUpdatedOn());
  }

  @Test
  void setUpdatedOnWorksTest() {
    setField(workflow, UPDATEDON, null);

    workflow.setUpdatedOn(NOW);
    assertEquals(NOW, getField(workflow, UPDATEDON));
  }

  @ParameterizedTest
  @MethodSource("providePrePersistFor")
  void prePersistWorksTest(Map<String, Object> initial, Map<String, Object> expected, Map<String, Boolean> persist) {

    initial.forEach((String attribute, Object value) -> {
      setField(workflow, attribute, value);
    });

    workflow.prePersist();

    expected.forEach((String attribute, Object value) -> {
      if (Boolean.TRUE.equals(persist.get(attribute))) {
        if (attribute == SETUP) {
          verify((Setup) value).prePersist();
        }
      } else if (Boolean.FALSE.equals(persist.get(attribute))) {
        if (attribute == SETUP) {
          verify((Setup) value, never()).prePersist();
        }
      } else {
        assertEquals(value, getField(workflow, attribute));
      }
    });
  }

  /**
   * Helper function for parameterized tests for the prePersist function.
   *
   * @return
   *   The arguments array stream with the stream columns as:
   *     - Arguments initial The initial values to be stored on the object.
   *     - Arguments expect The expected values to be after the pre-presistence call.
   *     - The parent field setup structure for nested pre-persistence.
   */
  private static Stream<Arguments> providePrePersistFor() {
    final Map<String, JsonNode> ic = new HashMap<>();
    ic.put(VALUE, null);

    final Map<String, JsonNode> icEmpty = new HashMap<>();

    final List<Node> nodeList = new ArrayList<>();
    nodeList.add(new NodeImpl());

    final List<Node> emptyList = new ArrayList<>();

    final Setup setup = spy(new Setup());
    final Setup setupNull = null;

    final List<Arguments> arguments = new ArrayList<>();

    arguments.add(Arguments.of(
      helperFieldMap(null,  null, null,  null,       null,  null,    null,      null,  null,    setupNull),
      helperFieldMap(false, null, NOW,   HIST_TTL,   "",    icEmpty, emptyList, NOW,   VERSION, setupNull),
      helperPersistMap(                                                                        null)
    ));

    arguments.add(Arguments.of(
      helperFieldMap(null,  HASH, null,  null,      null,  null,    null,      null,  null,    setupNull),
      helperFieldMap(false, HASH, NOW,   HIST_TTL,  "",    icEmpty, emptyList, NOW,   VERSION, setupNull),
      helperPersistMap(                                                                        null)
    ));

    arguments.add(Arguments.of(
      helperFieldMap(null,  null, LATER, null,      null,  null,    null,      null,  null,    setupNull),
      helperFieldMap(false, null, LATER, HIST_TTL,  "",    icEmpty, emptyList, NOW,   VERSION, setupNull),
      helperPersistMap(                                                                        null)
    ));

    arguments.add(Arguments.of(
      helperFieldMap(true,  null, null,  null,      null,  null,    null,      null,  null,    setupNull),
      helperFieldMap(true,  null, NOW,   HIST_TTL,  "",    icEmpty, emptyList, NOW,   VERSION, setupNull),
      helperPersistMap(                                                                        null)
    ));

    arguments.add(Arguments.of(
      helperFieldMap(null,  null, null,  INT_VALUE, null,  null,    null,      null,  null,    setupNull),
      helperFieldMap(false, null, NOW,   INT_VALUE, "",    icEmpty, emptyList, NOW,   VERSION, setupNull),
      helperPersistMap(                                                                        null)
    ));

    arguments.add(Arguments.of(
      helperFieldMap(null,  null, null, null,       VALUE, null,    null,      null,  null,    setupNull),
      helperFieldMap(false, null, NOW,  HIST_TTL,          VALUE, icEmpty, emptyList, NOW,   VERSION, setupNull),
      helperPersistMap(                                                                           null)
    ));

    arguments.add(Arguments.of(
      helperFieldMap(null,  null, null,  null,      null,  ic,      null,      null,  null,    setupNull),
      helperFieldMap(false, null, NOW,   HIST_TTL,  "",    ic,      emptyList, NOW,   VERSION, setupNull),
      helperPersistMap(                                                                        null)
    ));

    arguments.add(Arguments.of(
      helperFieldMap(null,  null, null,  null,      null,  null,    nodeList,  null,  null,    setupNull),
      helperFieldMap(false, null, NOW,   HIST_TTL,  "",    icEmpty, nodeList,  NOW,   VERSION, setupNull),
      helperPersistMap(                                                                        null)
    ));

    arguments.add(Arguments.of(
      helperFieldMap(null,  null, null,  null,      null,  null,    null,      LATER, null,    setupNull),
      helperFieldMap(false, null, NOW,   HIST_TTL,  "",    icEmpty, emptyList, LATER, VERSION, setupNull),
      helperPersistMap(                                                                        null)
    ));

    arguments.add(Arguments.of(
      helperFieldMap(null,  null, null,  null,      null,  null,    null,      null,  VALUE,   setupNull),
      helperFieldMap(false, null, NOW,   HIST_TTL,  "",    icEmpty, emptyList, NOW,   VALUE,   setupNull),
      helperPersistMap(                                                                        null)
    ));

    arguments.add(Arguments.of(
      helperFieldMap(null,  null, null,  null,      null,  null,    null,      null,  null,    setup),
      helperFieldMap(false, null, NOW,   HIST_TTL,  "",    icEmpty, emptyList, NOW,   VERSION, setup),
      helperPersistMap(                                                                        true)
    ));

    return arguments.stream();
  }

  /**
   * Helper for reducing in line code repetition for assignments.
   *
   * @param active            The active value.
   * @param checksum          The checksum value.
   * @param createdOn         The createdOn value.
   * @param historyTimeToLive The historyTimeToLive value.
   * @param name              The name value.
   * @param initialContext    The initialContext value.
   * @param nodes             The nodes value.
   * @param updatedOn         The updatedOn value.
   * @param versionTag        The versionTag value.
   * @param setup             The setup value.
   *
   * @return The built arguments map.
   */
  private static Map<String, Object> helperFieldMap(Boolean active, String checksum, Instant createdOn, Integer historyTimeToLive, String name, Map<String, JsonNode> initialContext, List<Node> nodes, Instant updatedOn, String versionTag, Setup setup) {

    final Map<String, Object> map = new HashMap<>();

    map.put(ACTIVE, active);
    map.put(CHECKSUM, checksum);
    map.put(CREATEDON, createdOn);
    map.put(HISTORYTIMETOLIVE, historyTimeToLive);
    map.put(INITIALCONTEXT, initialContext);
    map.put(NAME, name);
    map.put(NODES, nodes);
    map.put(SETUP, setup);
    map.put(UPDATEDON, updatedOn);
    map.put(VERSIONTAG, versionTag);

    return map;
  }

  /**
   * Helper for reducing in line code repetition for assignments for persist setting.
   *
   * @param setup The setup persist value.
   *
   * @return The built persist map.
   */
  private static Map<String, Object> helperPersistMap(Boolean setup) {

    final Map<String, Object> map = new HashMap<>();

    map.put(SETUP, setup);

    return map;
  }

  private static class NodeImpl extends Node { }

}
