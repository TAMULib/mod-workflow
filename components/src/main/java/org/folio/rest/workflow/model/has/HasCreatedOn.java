package org.folio.rest.workflow.model.has;

import java.time.Instant;

/**
 * This interface provides the CreatedOn methods.
 */
public interface HasCreatedOn {

  public Instant getCreatedOn();

  public void setCreatedOn(Instant createdOn);
}
