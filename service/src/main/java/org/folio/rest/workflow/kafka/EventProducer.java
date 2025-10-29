package org.folio.rest.workflow.kafka;

import org.folio.spring.messaging.model.Event;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class EventProducer {

  private static final Logger logger = LoggerFactory.getLogger(EventProducer.class);

  @Value("${application.kafka.producer.events.topic}")
  private String topic;

  private KafkaTemplate<String, Event> eventTemplate;

  public EventProducer(KafkaTemplate<String, Event> eventTemplate) {
    this.eventTemplate = eventTemplate;
  }

  public void send(Event event) {
    logger.debug("Send []: {}, {}", event.getMethod(), event.getPath());
    this.eventTemplate.send(topic, event);
  }

}
