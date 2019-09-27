package org.folio.rest.controller;

import java.io.IOException;
import java.util.List;

import org.folio.rest.model.Action;
import org.folio.rest.service.OkapiDiscoveryService;
import org.folio.rest.tenant.annotation.TenantHeader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/actions")
public class ActionController {

  @Autowired
  private OkapiDiscoveryService okapiDiscoveryService;

  @GetMapping
  public List<Action> getActions(@TenantHeader String tenant) throws IOException {
    return okapiDiscoveryService.getActionsByTenant(tenant);
  }

}
