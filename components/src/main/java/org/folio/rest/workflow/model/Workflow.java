package org.folio.rest.workflow.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.JsonNode;
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
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.Getter;
import lombok.Setter;
import org.folio.rest.workflow.model.converter.JsonNodeConverter;
import org.folio.rest.workflow.model.has.HasDeploymentId;
import org.folio.rest.workflow.model.has.HasId;
import org.folio.rest.workflow.model.has.HasInformational;
import org.folio.rest.workflow.model.has.HasName;
import org.folio.rest.workflow.model.has.HasNodes;
import org.folio.rest.workflow.model.has.HasVersionTag;
import org.folio.rest.workflow.model.has.common.HasWorkflowCommon;
import org.folio.spring.domain.model.AbstractBaseEntity;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.data.annotation.Version;

@Entity
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
public class Workflow extends AbstractBaseEntity implements HasDeploymentId, HasId, HasInformational, HasName, HasNodes, HasVersionTag, HasWorkflowCommon {

  @Getter
  @Setter
  @Column(nullable = true)
  @ColumnDefault("false")
  private Boolean active;

  @Getter
  @Setter
  @Column(unique = true)
  private String deploymentId;

  @Getter
  @Setter
  @Size(min = 0, max = 512)
  @Column(nullable = true)
  private String description;

  @Getter
  @Setter
  @Min(0)
  @Column(nullable = false)
  private Integer historyTimeToLive;

  @Getter
  @Setter
  @ElementCollection
  @CollectionTable(name = "workflow_initial_context", joinColumns = @JoinColumn(name = "workflow_id"))
  @MapKeyColumn(name = "context_key")
  @Column(name = "context_value")
  @Convert(converter = JsonNodeConverter.class, attributeName = "value")
  private Map<String, JsonNode> initialContext;

  @Getter
  @Setter
  @NotNull
  @Size(min = 4, max = 64)
  @Column(nullable = false, unique = true)
  private String name;

  @Getter
  @Setter
  @OneToMany
  @OrderColumn
  private List<Node> nodes;

  @Getter
  @Setter
  @Embedded
  private Setup setup;

  @Version
  @Getter
  @Setter
  @NotNull
  @Size(min = 1, max = 64)
  @Column(nullable = false)
  private String versionTag;

  public Workflow() {
    super();

    active = false;
    name = "";
    historyTimeToLive = 0;
    initialContext = new HashMap<>();
    nodes = new ArrayList<>();
    versionTag = "";
  }

  @PrePersist
  public void prePersist() {
    if (active == null) {
      active = false;
    }

    if (historyTimeToLive == null) {
      historyTimeToLive = 0;
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

    if (versionTag == null) {
      versionTag = "";
    }
  }

}
