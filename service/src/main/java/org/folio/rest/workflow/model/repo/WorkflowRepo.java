package org.folio.rest.workflow.model.repo;

import org.folio.rest.workflow.model.Workflow;
import org.folio.spring.cql.JpaCqlRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

@RepositoryRestResource
public interface WorkflowRepo extends JpaCqlRepository<Workflow, String> {

  public <T> T getViewById(String id, Class<T> type);

  @Override
  @RestResource(exported = false)
  public boolean existsById(String id);

  public boolean existsByIdAndVersionTag(String id, String versionTag);

  @Override
  @RestResource(exported = false)
  public void deleteById(String id);
}
