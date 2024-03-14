package org.folio.rest.workflow.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.commons.lang3.StringUtils;
import org.folio.rest.workflow.model.Workflow;
import org.folio.rest.workflow.model.repo.WorkflowRepo;
import org.folio.spring.data.OffsetRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
public class WorkflowCqlService extends AbstractCqlService<Workflow> {

  private WorkflowRepo repo;

  @Autowired
  public WorkflowCqlService(ObjectMapper mapper, WorkflowRepo repo) {
    this.mapper = mapper;
    this.repo = repo;
  }

  @Override
  protected String getTypeName() {
    return Workflow.class.getSimpleName().toLowerCase() + "s";
  }

  @Override
  public ObjectNode findByCql(String query, Long offset, Integer limit) {
    Page<Workflow> page = null;

    if (StringUtils.isBlank(query)) {
      page = repo.findAll(new OffsetRequest(offset, limit));
    } else {
      page = repo.findByCql(query, new OffsetRequest(offset, limit));
    }

    return toJson(page.toList(), repo.count());
  }

}
