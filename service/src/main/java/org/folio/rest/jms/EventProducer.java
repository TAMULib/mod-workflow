package org.folio.rest.jms;

import java.io.IOException;
import java.util.ArrayList;

import javax.jms.JMSException;
import javax.jms.Queue;

import org.folio.rest.workflow.jms.model.Event;
import org.folio.rest.model.repo.WorkflowRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class EventProducer {

  private static final Logger logger = LoggerFactory.getLogger(EventProducer.class);

  @Autowired
  private JmsMessagingTemplate jmsMessagingTemplate;

  @Autowired
  private WorkflowRepo workflowRepo;

  @Autowired
  private Queue queue;

  public void send(Event event) throws JMSException, IOException {
    workflowRepo.findByStartTriggerId(event.getTriggerId()).forEach(workflow -> {
      event.setProcessDefinitionIds(new ArrayList<String>(workflow.getProcessDefinitionIds()));
    });
    logger.info("Send [{}]: {}, {}, {}", queue.getQueueName(), event.getMethod(), event.getPath(), event.getPayload());
    this.jmsMessagingTemplate.convertAndSend(queue, event);
  }

}