package org.folio.rest.workflow.model.resolver;

import static java.util.Map.entry;

import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;
import com.fasterxml.jackson.databind.DatabindContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.jsontype.impl.TypeIdResolverBase;
import java.util.Map;
import org.folio.rest.workflow.model.CompressFileTask;
import org.folio.rest.workflow.model.Condition;
import org.folio.rest.workflow.model.ConnectTo;
import org.folio.rest.workflow.model.DatabaseConnectionTask;
import org.folio.rest.workflow.model.DatabaseDisconnectTask;
import org.folio.rest.workflow.model.DatabaseQueryTask;
import org.folio.rest.workflow.model.DirectoryTask;
import org.folio.rest.workflow.model.EmailTask;
import org.folio.rest.workflow.model.EndEvent;
import org.folio.rest.workflow.model.EventSubprocess;
import org.folio.rest.workflow.model.ExclusiveGateway;
import org.folio.rest.workflow.model.FileTask;
import org.folio.rest.workflow.model.FolioRequestTask;
import org.folio.rest.workflow.model.FtpTask;
import org.folio.rest.workflow.model.InclusiveGateway;
import org.folio.rest.workflow.model.InputTask;
import org.folio.rest.workflow.model.MoveToLastGateway;
import org.folio.rest.workflow.model.MoveToNode;
import org.folio.rest.workflow.model.Node;
import org.folio.rest.workflow.model.ParallelGateway;
import org.folio.rest.workflow.model.ProcessorTask;
import org.folio.rest.workflow.model.ReceiveTask;
import org.folio.rest.workflow.model.RequestTask;
import org.folio.rest.workflow.model.ScriptTask;
import org.folio.rest.workflow.model.StartEvent;
import org.folio.rest.workflow.model.Subprocess;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Resolve the type from the `deserializeAs` field from a `Node`.
 */
public class DeserializeAsNodeJsonResolver extends TypeIdResolverBase {

  /**
   * A map of classes that are allowed to be deserialized.
   */
  public static final Map<String, Class<? extends Node>> CLASSES = Map.ofEntries(
    entry("CompressFileTask", CompressFileTask.class),
    entry("Condition", Condition.class),
    entry("ConnectTo", ConnectTo.class),
    entry("DatabaseConnectionTask", DatabaseConnectionTask.class),
    entry("DatabaseDisconnectTask", DatabaseDisconnectTask.class),
    entry("DatabaseQueryTask", DatabaseQueryTask.class),
    entry("DirectoryTask", DirectoryTask.class),
    entry("EmailTask", EmailTask.class),
    entry("EndEvent", EndEvent.class),
    entry("EventSubprocess", EventSubprocess.class),
    entry("ExclusiveGateway", ExclusiveGateway.class),
    entry("FileTask", FileTask.class),
    entry("FolioRequestTask", FolioRequestTask.class),
    entry("FtpTask", FtpTask.class),
    entry("InclusiveGateway", InclusiveGateway.class),
    entry("InputTask", InputTask.class),
    entry("MoveToLastGateway", MoveToLastGateway.class),
    entry("MoveToNode", MoveToNode.class),
    entry("ParallelGateway", ParallelGateway.class),
    entry("ProcessorTask", ProcessorTask.class),
    entry("ReceiveTask", ReceiveTask.class),
    entry("RequestTask", RequestTask.class),
    entry("ScriptTask", ScriptTask.class),
    entry("StartEvent", StartEvent.class),
    entry("Subprocess", Subprocess.class)
  );

  private static final Logger logger = LoggerFactory.getLogger(DeserializeAsNodeJsonResolver.class);

  @Override
  public String idFromValue(Object value) {
    return value.getClass().getSimpleName();
  }

  @Override
  public String idFromValueAndType(Object value, Class<?> suggestedType) {
    return idFromValue(value);
  }

  @Override
  public Id getMechanism() {
    return Id.SIMPLE_NAME;
  }

  @Override
  public JavaType typeFromId(DatabindContext context, String id) {
    if (id != null && CLASSES.containsKey(id)) {
      return context.getTypeFactory().constructType(CLASSES.get(id));
    }

    logger.debug("Unknown or Invalid Workflow Model class {}.", id);
    return null;
  }

}
