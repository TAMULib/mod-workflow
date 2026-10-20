package org.folio.rest.workflow.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.PrePersist;
import org.folio.rest.workflow.enums.DirectoryAction;
import org.folio.rest.workflow.model.components.DelegateTask;
import org.folio.rest.workflow.model.has.HasPath;
import org.folio.rest.workflow.model.has.common.HasDirectoryTaskCommon;
import org.hibernate.annotations.ColumnDefault;

@Entity
public class DirectoryTask extends AbstractTask implements DelegateTask, HasDirectoryTaskCommon, HasPath {

  @Column(nullable = false)
  private String path;

  @Column(nullable = false)
  private String workflow;

  @Column(nullable = false)
  @ColumnDefault("'LIST'")
  @Enumerated(EnumType.STRING)
  private DirectoryAction action;

  public DirectoryTask() {
    super();

    path = "";
    workflow = "";
    action = DirectoryAction.LIST;
  }

  @Override
  @PrePersist
  public void prePersist() {
    super.prePersist();

    if (path == null) {
      path = "";
    }

    if (workflow == null) {
      workflow = "";
    }

    if (action == null) {
      action = DirectoryAction.LIST;
    }
  }

  @Override
  public DirectoryAction getAction() {
    return action;
  }

  @Override
  public String getWorkflow() {
    return workflow;
  }

  @Override
  public void setAction(DirectoryAction action) {
    this.action = action;
  }

  @Override
  public void setWorkflow(String workflow) {
    this.workflow = workflow;
  }

  @Override
  public String getPath() {
    return path;
  }

  @Override
  public void setPath(String path) {
    this.path = path;
  }

}
