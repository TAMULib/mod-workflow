package org.folio.rest.workflow.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.PrePersist;
import org.folio.rest.workflow.model.converter.JsonNodeConverter;
import org.folio.rest.workflow.model.has.HasId;
import org.folio.rest.workflow.model.has.HasInformational;
import org.folio.rest.workflow.model.has.HasName;
import org.folio.rest.workflow.model.has.common.HasNodeCommon;
import org.folio.spring.domain.model.AbstractBaseEntity;

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class Node extends AbstractBaseEntity implements HasId, HasInformational, HasName, HasNodeCommon, NodeInterface {

  @Column(columnDefinition = "TEXT", nullable = false)
  private String name;

  @Column(columnDefinition = "TEXT", nullable = true)
  private String description;

  @JsonProperty(access = JsonProperty.Access.READ_ONLY)
  @Convert(converter = JsonNodeConverter.class, attributeName = "deserializeAs")
  private String deserializeAs = this.getClass().getSimpleName();

  protected Node() {
    super();

    name = "";
  }

  @PrePersist
  public void prePersist() {
    if (name == null) {
      name = "";
    }
  }

  public String getIdentifier() {
    final String regex = "([a-z])([A-Z]+)";
    final String replacement = "$1_$2";
    final String simpleName = getClass().getSimpleName();

    return String.format("%s_%s", simpleName.replaceAll(regex, replacement).toLowerCase(), getId().replace("-", "_"));
  }

  @Override
  public String getDeserializeAs() {
    return deserializeAs;
  }

  @Override
  public void setDeserializeAs(String deserializeAs) {
    this.deserializeAs = deserializeAs;
  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  public void setName(String name) {
    this.name = name;
  }

  @Override
  public String getDescription() {
    return description;
  }

  @Override
  public void setDescription(String description) {
    this.description = description;
  }

}
