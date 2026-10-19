package org.folio.rest.workflow.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.PrePersist;
import org.folio.rest.workflow.enums.DatabaseResultType;
import org.folio.rest.workflow.model.components.DelegateTask;
import org.folio.rest.workflow.model.has.HasDesignation;
import org.folio.rest.workflow.model.has.common.HasDatabaseQueryTaskCommon;
import org.hibernate.annotations.ColumnDefault;

@Entity
public class DatabaseQueryTask extends AbstractTask implements DelegateTask, HasDatabaseQueryTaskCommon, HasDesignation {

  @Column(nullable = false)
  private String designation;

  @Column(nullable = true)
  private String outputPath;

  @Column(columnDefinition = "TEXT", nullable = false)
  private String query;

  @Enumerated(EnumType.STRING)
  @Column(nullable = true)
  private DatabaseResultType resultType;

  @Column(nullable = false)
  @ColumnDefault("false")
  private Boolean includeHeader;

  public DatabaseQueryTask() {
    super();

    designation = "";
    query = "";
    includeHeader = false;
  }

  @Override
  @PrePersist
  public void prePersist() {
    super.prePersist();

    if (designation == null) {
      designation = "";
    }

    if (query == null) {
      query = "";
    }

    if (includeHeader == null) {
      includeHeader = false;
    }
  }

  @Override
  public String getDesignation() {
    return designation;
  }

  @Override
  public void setDesignation(String designation) {
    this.designation = designation;
  }

  @Override
  public Boolean getIncludeHeader() {
    return includeHeader;
  }

  @Override
  public String getOutputPath() {
    return outputPath;
  }

  @Override
  public String getQuery() {
    return query;
  }

  @Override
  public DatabaseResultType getResultType() {
    return resultType;
  }

  @Override
  public void setIncludeHeader(Boolean includeHeader) {
    this.includeHeader = includeHeader;
  }

  @Override
  public void setOutputPath(String outputPath) {
    this.outputPath = outputPath;
  }

  @Override
  public void setQuery(String query) {
    this.query = query;
  }

  @Override
  public void setResultType(DatabaseResultType resultType) {
    this.resultType = resultType;
  }

}
