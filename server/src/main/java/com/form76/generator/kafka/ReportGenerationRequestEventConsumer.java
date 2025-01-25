package com.form76.generator.kafka;

import com.form76.generator.service.Form76ReportService;
import com.form76.generator.service.model.DoorOpeningLogRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class ReportGenerationRequestEventConsumer {

  Logger logger = LoggerFactory.getLogger(ReportGenerationRequestEventConsumer.class);

  @Autowired
  Form76ReportService form76ReportService;

  @KafkaListener(topics = KafkaTopicConfig.REPORT_GENERATION_REQUEST_TOPIC,
      groupId = KafkaTopicConfig.REPORT_FILES_TOPIC_CONSUMER_GROUP_ID
  )
  public void consume(DoorOpeningLogRequest doorOpeningLogRequest) throws Exception {
    logger.info("Received kafka event doorOpeningLogRequest: " + doorOpeningLogRequest);
    form76ReportService.generateReportForLocation(doorOpeningLogRequest);
    logger.info("Successfully processed doorOpeningLogRequest for location uuid: " + doorOpeningLogRequest.getLocationExtCommunityUuid());
  }


}

