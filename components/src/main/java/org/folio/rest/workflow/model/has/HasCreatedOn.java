package org.folio.rest.workflow.model.has;

import java.time.ZonedDateTime;

/**
 * This interface provides the CreatedOn methods.
 */
public interface HasCreatedOn {

  public ZonedDateTime getCreatedOn();

  public void setCreatedOn(ZonedDateTime createdOn);
}
