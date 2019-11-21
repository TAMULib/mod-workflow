package org.folio.rest.workflow.components;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import org.folio.spring.domain.model.AbstractBaseEntity;

@Entity(name="tasks")
@Inheritance
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.EXISTING_PROPERTY, property = "deserializeAs")
@JsonSubTypes({
    @JsonSubTypes.Type(value = ProcessorTask.class, name = "ProcessorTask"),

    @JsonSubTypes.Type(value = ExtractorTask.class, name = "ExtractorTask"),

    @JsonSubTypes.Type(value = StreamingExtractorTask.class, name = "StreamingExtractorTask"),

    @JsonSubTypes.Type(value = AccumulatorTask.class, name = "AccumulatorTask"),

    @JsonSubTypes.Type(value = StreamCreateForEachTask.class, name = "StreamCreateForEachTask"),

    @JsonSubTypes.Type(value = StreamingRequestTask.class, name = "StreamingRequestTask"),

    @JsonSubTypes.Type(value = RestRequestTask.class, name = "RestRequestTask"),

    @JsonSubTypes.Type(value = StreamingReportingTask.class, name = "StreamingReportingTask")

  }
)
public abstract class Task extends AbstractBaseEntity {

  @NotNull
  @Size(min = 4, max = 64)
  @Column(unique = true)
  private String name;

  @JsonProperty(access = JsonProperty.Access.READ_ONLY)
  private String delegate;

  @JsonProperty(access = JsonProperty.Access.READ_ONLY)
  private String deserializeAs = this.getClass().getSimpleName();

  @JsonProperty(access = JsonProperty.Access.READ_ONLY)
  private Boolean streaming;

  public Task() {
    super();
    setStreaming(false);
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getDelegate() {
    return delegate;
  }

  public void setDelegate(String delegate) {
    this.delegate = delegate;
  }

  public String getDeserializeAs() {
    return deserializeAs;
  }

  public void setDeserializeAs(String deserializeAs) {
    this.deserializeAs = deserializeAs;
  }

  public Boolean isStreaming() {
    return streaming;
  }

  public void setStreaming(Boolean streaming) {
    this.streaming = streaming;
  }

}
