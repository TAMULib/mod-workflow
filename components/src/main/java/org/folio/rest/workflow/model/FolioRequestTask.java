package org.folio.rest.workflow.model;

import jakarta.persistence.Entity;

/**
 * A task for already logged in FOLIO HTTP requests.
 *
 * For FOLIO related requests other than logging, use the FolioRequestDelegate instead.
 */
@Entity
public class FolioRequestTask extends RequestTask {

  public FolioRequestTask() {
    super();
  }

}
