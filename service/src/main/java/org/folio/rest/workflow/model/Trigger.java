package org.folio.rest.workflow.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.PrePersist;
import jakarta.validation.constraints.Size;
import org.folio.rest.workflow.enums.HttpMethod;
import org.folio.rest.workflow.model.has.HasId;
import org.folio.rest.workflow.model.has.HasInformational;
import org.folio.rest.workflow.model.has.HasMethod;
import org.folio.rest.workflow.model.has.HasName;
import org.folio.rest.workflow.model.has.HasPathPattern;
import org.folio.spring.domain.model.AbstractBaseEntity;

@Entity
public class Trigger extends AbstractBaseEntity implements HasId, HasInformational, HasMethod, HasName, HasPathPattern {

  @Size(min = 4, max = 64)
  @Column(unique = true)
  private String name;

  @Size(min = 0, max = 256)
  @Column(nullable = true)
  private String description;

  @Size(min = 2, max = 256)
  @Column(nullable = false)
  private String pathPattern;

  @Column(nullable = false)
  @Enumerated(EnumType.STRING)
  private HttpMethod method;

  public Trigger() {
    super();

    method = HttpMethod.POST;
  }

  @PrePersist
  public void prePersist() {

    if (method == null) {
      method = HttpMethod.POST;
    }
  }

  /**
   * @return the name
   */
  public String getName() {
    return name;
  }

  /**
   * @return the description
   */
  public String getDescription() {
    return description;
  }

  /**
   * @return the pathPattern
   */
  public String getPathPattern() {
    return pathPattern;
  }

  /**
   * @return the method
   */
  public HttpMethod getMethod() {
    return method;
  }

  /**
   * @param name the name to set
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * @param description the description to set
   */
  public void setDescription(String description) {
    this.description = description;
  }

  /**
   * @param pathPattern the pathPattern to set
   */
  public void setPathPattern(String pathPattern) {
    this.pathPattern = pathPattern;
  }

  /**
   * @param method the method to set
   */
  public void setMethod(HttpMethod method) {
    this.method = method;
  }

}
