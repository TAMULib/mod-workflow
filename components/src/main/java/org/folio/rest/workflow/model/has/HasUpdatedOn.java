package org.folio.rest.workflow.model.has;

import java.time.ZonedDateTime;

/**
 * This interface provides the UpdatedOn methods.
 */
public interface HasUpdatedOn {

  public ZonedDateTime getUpdatedOn();

  public void setUpdatedOn(ZonedDateTime updatedOn);
}
