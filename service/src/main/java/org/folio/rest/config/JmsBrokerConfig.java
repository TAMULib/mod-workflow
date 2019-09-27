package org.folio.rest.config;

import javax.jms.Queue;

import org.apache.activemq.broker.BrokerService;
import org.apache.activemq.command.ActiveMQQueue;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JmsBrokerConfig {

  @Value("${event.queue.name}")
  private String eventQueueName;

  @Value("${spring.activemq.broker-url}")
  private String activeMQBrokerURL;

  @Bean
  public Queue queue() {
    return new ActiveMQQueue(eventQueueName);
  }

  @Bean
  public BrokerService broker() throws Exception {
    BrokerService broker = new BrokerService();
    broker.addConnector(activeMQBrokerURL);
    return broker;
  }

}
