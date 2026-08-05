package org.folio.rest.workflow.dto;

import org.folio.rest.workflow.model.has.HasDeploymentId;
import org.folio.rest.workflow.model.has.HasId;
import org.folio.rest.workflow.model.has.HasNodes;
import org.folio.rest.workflow.model.has.HasVersionTag;

/**
 * This is a DTO designed for operational purposes that also has the associated Nodes.
 *
 * This is intended to be used for a proper delete operation.
 */
public interface WorkflowOperationalNodeDto extends HasDeploymentId, HasId, HasNodes, HasVersionTag {

}
