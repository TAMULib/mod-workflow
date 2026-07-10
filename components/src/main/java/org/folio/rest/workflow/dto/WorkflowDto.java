package org.folio.rest.workflow.dto;

import org.folio.rest.workflow.model.has.HasChecksum;
import org.folio.rest.workflow.model.has.HasCreatedOn;
import org.folio.rest.workflow.model.has.HasDeploymentId;
import org.folio.rest.workflow.model.has.HasId;
import org.folio.rest.workflow.model.has.HasInformational;
import org.folio.rest.workflow.model.has.HasName;
import org.folio.rest.workflow.model.has.HasNodes;
import org.folio.rest.workflow.model.has.HasUpdatedOn;
import org.folio.rest.workflow.model.has.HasVersionTag;
import org.folio.rest.workflow.model.has.common.HasWorkflowCommon;

/**
 * This is a DTO design for providing the entire {@link org.folio.rest.workflow.model.Workflow Workflow} model.
 */
public interface WorkflowDto extends HasChecksum, HasCreatedOn, HasDeploymentId, HasId, HasInformational, HasName, HasNodes, HasUpdatedOn, HasVersionTag, HasWorkflowCommon {

}
