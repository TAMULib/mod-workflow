package org.folio.rest.model.repo;

import java.util.List;

import org.folio.rest.model.Trigger;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.http.HttpMethod;

@RepositoryRestResource
public interface TriggerRepo extends JpaRepository<Trigger, String> {

  public List<Trigger> findByMethod(HttpMethod method);
  public List<Trigger> findByMethodAndDeserializeAs(HttpMethod method, String deserializeAs);

}
