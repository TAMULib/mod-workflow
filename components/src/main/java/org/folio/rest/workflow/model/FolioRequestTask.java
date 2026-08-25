package org.folio.rest.workflow.model;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.PrePersist;
import java.util.HashSet;
import java.util.Set;
import org.folio.rest.workflow.model.components.DelegateTask;
import org.folio.rest.workflow.model.has.common.HasRequestTaskCommon;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

/**
 * A task for already logged in FOLIO HTTP requests.
 *
 * For FOLIO related requests other than logging, use the FolioRequestDelegate instead.
 */
@Entity
public class FolioRequestTask extends AbstractTask implements DelegateTask, HasRequestTaskCommon {

  @ElementCollection
  @OnDelete(action = OnDeleteAction.CASCADE)
  private Set<EmbeddedVariable> headerOutputVariables;

  @Embedded
  private EmbeddedRequest request;

  public FolioRequestTask() {
    super();

    headerOutputVariables = new HashSet<>();
  }

  @Override
  @PrePersist
  public void prePersist() {
    super.prePersist();

    if (headerOutputVariables == null) {
      headerOutputVariables = new HashSet<>();
    } else {
      // @Embeddable with @PrePersist do not consistently call PrePersist and so this must be manually triggered.
      headerOutputVariables.forEach((EmbeddedVariable ev) -> {
        if (ev != null) ev.prePersist();
      });
    }
  }

  @Override
  public Set<EmbeddedVariable> getHeaderOutputVariables() {
    return headerOutputVariables;
  }

  @Override
  public EmbeddedRequest getRequest() {
    return request;
  }

  @Override
  public void setHeaderOutputVariables(Set<EmbeddedVariable> headerOutputVariables) {
    this.headerOutputVariables = headerOutputVariables;
  }

  @Override
  public void setRequest(EmbeddedRequest request) {
    this.request = request;
  }

}
