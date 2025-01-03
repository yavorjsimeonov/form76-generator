package com.form76.generator.service;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class Form76ReportScheduler {

  Logger logger = LoggerFactory.getLogger(Form76ReportScheduler.class);

  @Autowired
  Form76ReportService form76ReportService;

  @Scheduled(cron = "${form76-generator.cron.schedule}")
  public void generateAndSendReports() {
    logger.info("Starting Form76 Reports generation....");
    form76ReportService.triggerReportsGeneration();
  }
}
