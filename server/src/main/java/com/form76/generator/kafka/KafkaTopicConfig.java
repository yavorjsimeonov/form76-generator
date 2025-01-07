package com.form76.generator.kafka;

import org.apache.kafka.clients.admin.NewTopic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;

@EnableKafka
@Configuration
public class KafkaTopicConfig {
  Logger logger = LoggerFactory.getLogger(KafkaTopicConfig.class);

  public static final String REPORT_GENERATION_REQUEST_TOPIC = "kafka.topic.report.generation.request";
  public static final String REPORT_FILES_TOPIC_CONSUMER_GROUP_ID = "kafka.consumer.report.generation.request";

  @Value(value = "${spring.kafka.producer.bootstrap-servers}")
  private String bootstrapAddress;

//
//  @Bean
//  public NewTopic doorOpeningLogRequestsTopic() {
//    return new NewTopic(REPORT_GENERATION_REQUEST_TOPIC, 1, (short) 1);
//  }
//
//  @Bean
//  public NewTopic doorOpeningLogResponsesTopic() {
//    return new NewTopic("door-opening-log-responses", 1, (short) 1);
//  }
//


}