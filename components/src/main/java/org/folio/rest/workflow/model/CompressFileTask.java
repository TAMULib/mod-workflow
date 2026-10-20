package org.folio.rest.workflow.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.PrePersist;
import org.folio.rest.workflow.enums.CompressFileContainer;
import org.folio.rest.workflow.enums.CompressFileFormat;
import org.folio.rest.workflow.model.components.DelegateTask;
import org.folio.rest.workflow.model.has.common.HasCompressFileTaskCommon;
import org.hibernate.annotations.ColumnDefault;

@Entity
public class CompressFileTask extends AbstractTask implements DelegateTask, HasCompressFileTaskCommon {

  @Column(nullable = false)
  private String source;

  @Column(nullable = false)
  private String destination;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  @ColumnDefault("'ZIP'")
  private CompressFileFormat format;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  @ColumnDefault("'NONE'")
  private CompressFileContainer container;

  public CompressFileTask() {
    super();

    source = "";
    destination = "";
    format = CompressFileFormat.ZIP;
    container = CompressFileContainer.NONE;
  }

  @Override
  @PrePersist
  public void prePersist() {
    super.prePersist();

    if (source == null) {
      source = "";
    }

    if (destination == null) {
      destination = "";
    }

    if (format == null) {
      format = CompressFileFormat.ZIP;
    }

    if (container == null) {
      container = CompressFileContainer.NONE;
    }
  }

  @Override
  public CompressFileContainer getContainer() {
    return container;
  }

  @Override
  public String getDestination() {
    return destination;
  }

  @Override
  public CompressFileFormat getFormat() {
    return format;
  }

  @Override
  public String getSource() {
    return source;
  }

  @Override
  public void setContainer(CompressFileContainer container) {
    this.container = container;
  }

  @Override
  public void setDestination(String destination) {
    this.destination = destination;
  }

  @Override
  public void setFormat(CompressFileFormat format) {
    this.format = format;
  }

  @Override
  public void setSource(String source) {
    this.source = source;
  }

}
