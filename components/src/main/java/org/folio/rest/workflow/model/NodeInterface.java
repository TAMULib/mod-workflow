package org.folio.rest.workflow.model;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.annotation.JsonTypeIdResolver;
import org.folio.rest.workflow.model.resolver.DeserializeAsNodeJsonResolver;

@JsonTypeIdResolver(DeserializeAsNodeJsonResolver.class)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.EXISTING_PROPERTY, property = "deserializeAs")
@JsonSubTypes({
  @JsonSubTypes.Type(value = CompressFileTask.class, name = "CompressFileTask"),
  @JsonSubTypes.Type(value = Condition.class, name = "Condition"),
  @JsonSubTypes.Type(value = ConnectTo.class, name = "ConnectTo"),
  @JsonSubTypes.Type(value = DatabaseConnectionTask.class, name = "DatabaseConnectionTask"),
  @JsonSubTypes.Type(value = DatabaseDisconnectTask.class, name = "DatabaseDisconnectTask"),
  @JsonSubTypes.Type(value = DatabaseQueryTask.class, name = "DatabaseQueryTask"),
  @JsonSubTypes.Type(value = DirectoryTask.class, name = "DirectoryTask"),
  @JsonSubTypes.Type(value = EmailTask.class, name = "EmailTask"),
  @JsonSubTypes.Type(value = EndEvent.class, name = "EndEvent"),
  @JsonSubTypes.Type(value = EventSubprocess.class, name = "EventSubprocess"),
  @JsonSubTypes.Type(value = ExclusiveGateway.class, name = "ExclusiveGateway"),
  @JsonSubTypes.Type(value = FileTask.class, name = "FileTask"),
  @JsonSubTypes.Type(value = FolioRequestTask.class, name = "FolioRequestTask"),
  @JsonSubTypes.Type(value = FtpTask.class, name = "FtpTask"),
  @JsonSubTypes.Type(value = InclusiveGateway.class, name = "InclusiveGateway"),
  @JsonSubTypes.Type(value = InputTask.class, name = "InputTask"),
  @JsonSubTypes.Type(value = MoveToLastGateway.class, name = "MoveToLastGateway"),
  @JsonSubTypes.Type(value = MoveToNode.class, name = "MoveToNode"),
  @JsonSubTypes.Type(value = ParallelGateway.class, name = "ParallelGateway"),
  @JsonSubTypes.Type(value = ProcessorTask.class, name = "ProcessorTask"),
  @JsonSubTypes.Type(value = ReceiveTask.class, name = "ReceiveTask"),
  @JsonSubTypes.Type(value = RequestTask.class, name = "RequestTask"),
  @JsonSubTypes.Type(value = ScriptTask.class, name = "ScriptTask"),
  @JsonSubTypes.Type(value = StartEvent.class, name = "StartEvent"),
  @JsonSubTypes.Type(value = Subprocess.class, name = "Subprocess")
})
public interface NodeInterface {

  public String getDeserializeAs();

}
