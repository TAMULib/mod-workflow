package org.folio.rest.model.repo;

import org.folio.rest.workflow.components.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface TaskRepo extends JpaRepository<Task, String> {

}
