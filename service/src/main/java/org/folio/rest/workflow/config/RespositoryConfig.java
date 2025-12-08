package org.folio.rest.workflow.config;

import org.folio.rest.workflow.events.WorkflowEventHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RespositoryConfig {

  @Bean
  WorkflowEventHandler workflowEventHandler() {
    return new WorkflowEventHandler();
  }
}
