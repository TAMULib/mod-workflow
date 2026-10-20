package org.folio.rest.workflow.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapKeyColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderColumn;
import jakarta.persistence.PrePersist;
import jakarta.validation.constraints.Min;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.folio.rest.workflow.model.converter.JsonNodeConverter;
import org.folio.rest.workflow.model.has.HasChecksum;
import org.folio.rest.workflow.model.has.HasCreatedOn;
import org.folio.rest.workflow.model.has.HasDeploymentId;
import org.folio.rest.workflow.model.has.HasId;
import org.folio.rest.workflow.model.has.HasInformational;
import org.folio.rest.workflow.model.has.HasName;
import org.folio.rest.workflow.model.has.HasNodes;
import org.folio.rest.workflow.model.has.HasUpdatedOn;
import org.folio.rest.workflow.model.has.HasVersionTag;
import org.folio.rest.workflow.model.has.common.HasWorkflowCommon;
import org.folio.spring.domain.model.AbstractBaseEntity;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.data.annotation.Version;
import tools.jackson.databind.JsonNode;

@Entity
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
public class Workflow extends AbstractBaseEntity implements HasChecksum, HasCreatedOn, HasDeploymentId, HasId, HasInformational, HasName, HasNodes, HasUpdatedOn, HasVersionTag, HasWorkflowCommon {

  @Column(nullable = true)
  @ColumnDefault("false")
  private Boolean active;

  @Column
  private String checksum;

  @Column(columnDefinition="TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW()", nullable = false)
  private Instant createdOn;

  @Column(unique = true)
  private String deploymentId;

  @Column(columnDefinition = "TEXT", nullable = true)
  private String description;

  @Min(0)
  @Column(nullable = false)
  @ColumnDefault("90")
  private Integer historyTimeToLive;

  @ElementCollection
  @CollectionTable(name = "workflow_initial_context", joinColumns = @JoinColumn(name = "workflow_id"))
  @MapKeyColumn(name = "context_key")
  @Column(name = "context_value")
  @Convert(converter = JsonNodeConverter.class, attributeName = "value")
  @OnDelete(action = OnDeleteAction.CASCADE)
  private Map<String, JsonNode> initialContext;

  @Column(columnDefinition = "TEXT default false", unique = true)
  private String name;

  @OneToMany
  @OrderColumn
  private List<Node> nodes;

  @Embedded
  private Setup setup;

  @Column(columnDefinition="TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW()")
  private Instant updatedOn;

  @Version
  @Column(nullable = false)
  @ColumnDefault("'1.0'")
  private String versionTag;

  public Workflow() {
    super();

    active = false;
    checksum = null;
    createdOn = now();
    name = "";
    historyTimeToLive = 90;
    initialContext = new HashMap<>();
    nodes = new ArrayList<>();
    setup = new Setup();
    updatedOn = now();
    versionTag = "1.0";
  }

  @PrePersist
  public void prePersist() {

    if (active == null) {
      active = false;
    }

    if (createdOn == null) {
      createdOn = now();
    }

    if (historyTimeToLive == null) {
      historyTimeToLive = 90;
    }

    if (name == null) {
      name = "";
    }

    if (initialContext == null) {
      initialContext = new HashMap<>();
    }

    if (nodes == null) {
      nodes = new ArrayList<>();
    }

    if (updatedOn == null) {
      updatedOn = now();
    }

    if (versionTag == null) {
      versionTag = "1.0";
    }

    // @Embeddable with @PrePersist do not consistently call PrePersist and so this must be manually triggered.
    if (setup != null) {
      setup.prePersist();
    }
  }

  @Override
  public Boolean getActive() {
    return active;
  }

  @Override
  public String getChecksum() {
    return checksum;
  }

  @Override
  public Instant getCreatedOn() {
    return createdOn;
  }

  @Override
  public Integer getHistoryTimeToLive() {
    return historyTimeToLive;
  }

  @Override
  public Map<String, JsonNode> getInitialContext() {
    return initialContext;
  }

  @Override
  public Setup getSetup() {
    return setup;
  }

  @Override
  public Instant getUpdatedOn() {
    return updatedOn;
  }

  @Override
  public String getVersionTag() {
    return versionTag;
  }

  @Override
  public List<Node> getNodes() {
    return nodes;
  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  public String getDescription() {
    return description;
  }

  @Override
  public String getDeploymentId() {
    return deploymentId;
  }

  @Override
  public void setActive(Boolean active) {
    this.active = active;
  }

  @Override
  public void setChecksum(String checksum) {
    this.checksum = checksum;
  }

  @Override
  public void setCreatedOn(Instant createdOn) {
    this.createdOn = createdOn;
  }

  @Override
  public void setHistoryTimeToLive(Integer historyTimeToLive) {
    this.historyTimeToLive = historyTimeToLive;
  }

  @Override
  public void setInitialContext(Map<String, JsonNode> initialContext) {
    this.initialContext = initialContext;
  }

  @Override
  public void setSetup(Setup setup) {
    this.setup = setup;
  }

  @Override
  public void setUpdatedOn(Instant updatedOn) {
    this.updatedOn = updatedOn;
  }

  @Override
  public void setVersionTag(String versionTag) {
    this.versionTag = versionTag;
  }

  @Override
  public void setNodes(List<Node> nodes) {
    this.nodes = nodes;
  }

  @Override
  public void setName(String name) {
    this.name = name;
  }

  @Override
  public void setDescription(String description) {
    this.description = description;
  }

  @Override
  public void setDeploymentId(String deploymentId) {
    this.deploymentId = deploymentId;
  }

  /**
   * Get the current time.
   *
   * This is used to allow for easier unit testing where the unit tests need only override this method through mocks or similar.
   *
   * @return The current instant.
   */
  Instant now() {

    return Instant.now();
  }

}
