package org.folio.rest.workflow.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.PrePersist;
import lombok.Getter;
import lombok.Setter;
import org.folio.rest.workflow.enums.FileOp;
import org.folio.rest.workflow.model.components.DelegateTask;
import org.folio.rest.workflow.model.has.common.HasFileTaskCommon;

@Entity
public class FileTask extends AbstractTask implements DelegateTask, HasFileTaskCommon {

  @Getter
  @Setter
  @Column(nullable = true)
  private String line;

  @Getter
  @Setter
  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private FileOp op;

  @Getter
  @Setter
  @Column(nullable = false)
  private String path;

  @Getter
  @Setter
  @Column(nullable = true)
  private String target;

  public FileTask() {
    super();

    op = FileOp.READ;
    path = "";
  }

  @Override
  @PrePersist
  public void prePersist() {
    super.prePersist();

    if (op  == null) {
      op = FileOp.READ;
    }

    if (path == null) {
      path = "";
    }
  }

}
