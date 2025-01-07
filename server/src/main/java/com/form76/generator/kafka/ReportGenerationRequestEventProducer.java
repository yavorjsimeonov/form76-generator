package com.form76.generator.kafka;

import com.form76.generator.service.model.DoorOpeningLogRequest;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class ReportGenerationRequestEventProducer {

  private final KafkaTemplate<String, DoorOpeningLogRequest> kafkaTemplate;

  public ReportGenerationRequestEventProducer(KafkaTemplate<String, DoorOpeningLogRequest> kafkaTemplate) {
    this.kafkaTemplate = kafkaTemplate;
  }

  public void publishReportGenerationRequest(String key, DoorOpeningLogRequest request) {
    kafkaTemplate.send(KafkaTopicConfig.REPORT_GENERATION_REQUEST_TOPIC, key, request);
  }
}
