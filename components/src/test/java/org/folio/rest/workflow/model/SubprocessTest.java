package org.folio.rest.workflow.model;

import static org.folio.spring.test.mock.MockMvcConstant.VALUE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.util.ReflectionTestUtils.getField;
import static org.springframework.test.util.ReflectionTestUtils.setField;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

import org.folio.rest.workflow.enums.SubprocessType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class SubprocessTest {

  @Mock
  private EmbeddedLoopReference embeddedLoopReference;

  @Mock
  private Node node;

  private List<Node> nodes;

  private Subprocess subprocess;

  @BeforeEach
  void beforeEach() {
    subprocess = new Subprocess();
    nodes = new ArrayList<>();
    nodes.add(node);
  }

  @Test
  void getIdWorksTest() {
    setField(subprocess, "id", VALUE);

    assertEquals(VALUE, subprocess.getId());
  }

  @Test
  void setIdWorksTest() {
    setField(subprocess, "id", null);

    subprocess.setId(VALUE);
    assertEquals(VALUE, getField(subprocess, "id"));
  }

  @Test
  void getNameWorksTest() {
    setField(subprocess, "name", VALUE);

    assertEquals(VALUE, subprocess.getName());
  }

  @Test
  void setNameWorksTest() {
    setField(subprocess, "name", null);

    subprocess.setName(VALUE);
    assertEquals(VALUE, getField(subprocess, "name"));
  }

  @Test
  void getDescriptionWorksTest() {
    setField(subprocess, "description", VALUE);

    assertEquals(VALUE, subprocess.getDescription());
  }

  @Test
  void setDescriptionWorksTest() {
    setField(subprocess, "description", null);

    subprocess.setDescription(VALUE);
    assertEquals(VALUE, getField(subprocess, "description"));
  }

  @Test
  void getDeserializeAsWorksTest() {
    setField(subprocess, "deserializeAs", VALUE);

    assertEquals(VALUE, subprocess.getDeserializeAs());
  }

  @Test
  void setDeserializeAsWorksTest() {
    setField(subprocess, "deserializeAs", null);

    subprocess.setDeserializeAs(VALUE);
    assertEquals(VALUE, getField(subprocess, "deserializeAs"));
  }

  @Test
  void getAsyncBeforeWorksTest() {
    setField(subprocess, "asyncBefore", true);

    assertEquals(true, subprocess.getAsyncBefore());
  }

  @Test
  void setAsyncBeforeWorksTest() {
    setField(subprocess, "asyncBefore", false);

    subprocess.setAsyncBefore(true);
    assertEquals(true, getField(subprocess, "asyncBefore"));
  }

  @Test
  void getAsyncAfterWorksTest() {
    setField(subprocess, "asyncAfter", true);

    assertEquals(true, subprocess.getAsyncAfter());
  }

  @Test
  void setAsyncAfterWorksTest() {
    setField(subprocess, "asyncAfter", false);

    subprocess.setAsyncAfter(true);
    assertEquals(true, getField(subprocess, "asyncAfter"));
  }

  @Test
  void getNodesWorksTest() {
    setField(subprocess, "nodes", nodes);

    assertEquals(nodes, subprocess.getNodes());
  }

  @Test
  void setNodesWorksTest() {
    setField(subprocess, "nodes", null);

    subprocess.setNodes(nodes);
    assertEquals(nodes, getField(subprocess, "nodes"));
  }

  @Test
  void getTypeWorksTest() {
    setField(subprocess, "type", SubprocessType.EMBEDDED);

    assertEquals(SubprocessType.EMBEDDED, subprocess.getType());
  }

  @Test
  void setTypeWorksTest() {
    setField(subprocess, "type", null);

    subprocess.setType(SubprocessType.EMBEDDED);
    assertEquals(SubprocessType.EMBEDDED, getField(subprocess, "type"));
  }

  @Test
  void getLoopRefWorksTest() {
    setField(subprocess, "loopRef", embeddedLoopReference);

    assertEquals(embeddedLoopReference, subprocess.getLoopRef());
  }

  @Test
  void setLoopRefWorksTest() {
    setField(subprocess, "loopRef", null);

    subprocess.setLoopRef(embeddedLoopReference);
    assertEquals(embeddedLoopReference, getField(subprocess, "loopRef"));
  }

  @ParameterizedTest
  @MethodSource("providePrePersistFor")
  void prePersistWorksTest(Map<String, Object> initial, Map<String, Object> expected) {
    initial.forEach((String attribute, Object value) -> {
      setField(subprocess, attribute, value);
    });

    subprocess.prePersist();

    expected.forEach((String attribute, Object value) -> {
      assertEquals(value, getField(subprocess, attribute));
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
    final Set<EmbeddedVariable> ivList = new HashSet<>();
    ivList.add(new EmbeddedVariable());

    return Stream.of(
      Arguments.of(
        helperFieldMap(null),
        helperFieldMap(SubprocessType.EMBEDDED)
      ),
      Arguments.of(
        helperFieldMap(SubprocessType.TRANSACTION),
        helperFieldMap(SubprocessType.TRANSACTION)
      )
    );
  }

  /**
   * Helper for reducing inline code repititon for assignments.
   *
   * @param type The type value.
   *
   * @return The built arguments map.
   */
  private static Map<String, Object> helperFieldMap(SubprocessType type) {
    final Map<String, Object> map = new HashMap<>();

    map.put("type", type);

    return map;
  }


}
