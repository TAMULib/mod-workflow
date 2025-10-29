package org.folio.rest.workflow.controller;

import java.io.IOException;
import java.util.List;
import org.folio.rest.workflow.model.Action;
import org.folio.rest.workflow.service.OkapiDiscoveryService;
import org.folio.spring.tenant.annotation.TenantHeader;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/actions")
public class ActionController {

  private OkapiDiscoveryService okapiDiscoveryService;

  public ActionController(OkapiDiscoveryService okapiDiscoveryService) {
    this.okapiDiscoveryService = okapiDiscoveryService;
  }

  @GetMapping(produces = { MediaType.APPLICATION_JSON_VALUE })
  public List<Action> getActions(@TenantHeader String tenant) throws IOException {
    return okapiDiscoveryService.getActionsByTenant(tenant);
  }

}
