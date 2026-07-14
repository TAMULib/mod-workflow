package org.folio.rest.workflow.model.has;

import java.time.Instant;

/**
 * This interface provides the UpdatedOn methods.
 */
public interface HasUpdatedOn {

  public Instant getUpdatedOn();

  public void setUpdatedOn(Instant updatedOn);
}
