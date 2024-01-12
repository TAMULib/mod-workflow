package org.folio.rest.workflow.model;

import java.util.HashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import lombok.Getter;
import lombok.Setter;
import org.folio.rest.workflow.enums.CompressFileContainer;
import org.folio.rest.workflow.enums.CompressFileFormat;
import org.folio.rest.workflow.model.components.DelegateTask;
import org.folio.rest.workflow.model.has.HasAsync;
import org.folio.rest.workflow.model.has.HasInputOutput;
import org.folio.rest.workflow.model.has.common.HasCompressFileTaskCommon;

@Entity
public class CompressFileTask extends Node implements DelegateTask, HasAsync, HasCompressFileTaskCommon, HasInputOutput {

  @Getter
  @Setter
  @ElementCollection
  private Set<EmbeddedVariable> inputVariables;

  @Getter
  @Setter
  @Embedded
  private EmbeddedVariable outputVariable;

  @Getter
  @Setter
  @Column(nullable = false)
  private boolean asyncBefore;

  @Getter
  @Setter
  @Column(nullable = false)
  private boolean asyncAfter;

  @Getter
  @Setter
  @Column(nullable = false)
  private String source;

  @Getter
  @Setter
  @Column(nullable = false)
  private String destination;

  @Getter
  @Setter
  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private CompressFileFormat format;

  @Getter
  @Setter
  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private CompressFileContainer container;

  public CompressFileTask() {
    super();

    inputVariables = new HashSet<EmbeddedVariable>();
    asyncBefore = false;
    asyncAfter = false;
  }

}
