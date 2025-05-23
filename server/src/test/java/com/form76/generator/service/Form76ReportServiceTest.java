package com.form76.generator.service;

import com.form76.generator.db.entity.ReportAlgorithm;
import com.form76.generator.db.entity.ReportFileFormat;
import com.form76.generator.kafka.ReportGenerationRequestEventProducer;
import com.form76.generator.rest.model.AdministrationData;
import com.form76.generator.rest.model.LocationData;
import com.form76.generator.rest.model.ReportData;
import com.form76.generator.service.model.DoorEvent;
import com.form76.generator.service.model.DoorOpeningLog;
import com.form76.generator.service.model.DoorOpeningLogRequest;
import com.form76.generator.service.model.DoorOpeningLogResponse;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.kafka.ConfluentKafkaContainer;
import org.testcontainers.utility.DockerImageName;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import static com.form76.generator.kafka.KafkaTopicConfig.REPORT_GENERATION_REQUEST_TOPIC;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@SpringBootTest
@Testcontainers
class Form76ReportServiceTest {

  @Container
  static ConfluentKafkaContainer kafka = new ConfluentKafkaContainer(
      DockerImageName.parse("confluentinc/cp-kafka:7.5.1")
      .asCompatibleSubstituteFor("apache/kafka"));


  @DynamicPropertySource
  static void overrideProperties(DynamicPropertyRegistry registry) {
    System.out.println("~~~~~~~~~~~~~~~~~~~~ " + kafka.getBootstrapServers());
    registry.add("spring.kafka.bootstrap-servers", kafka::getBootstrapServers);
    registry.add("spring.kafka.consumer.bootstrap-servers", kafka::getBootstrapServers);
    registry.add("spring.kafka.producer.bootstrap-servers", kafka::getBootstrapServers);
  }

  private Form76ReportService reportService;

  @Autowired
  private ReportGenerationRequestEventProducer eventProducer;

  private KafkaConsumer<String, String> consumer;

  @BeforeEach
  void setup() {
    reportService = new Form76ReportService();

    reportService.administrationService = mock(AdministrationService.class);;
    reportService.locationService = mock(LocationService.class);;
    reportService.mylinkApiService = mock(MylinkApiService.class);
    reportService.emailService = mock(EmailService.class);
    reportService.reportService = mock(ReportService.class);
    reportService.reportGenerationRequestEventProducer = eventProducer;
    reportService.uploadReportService = mock(UploadReportService.class);
    reportService.outputFileNamePrefix = "form76_";

    // Kafka Consumer Setup
    Properties props = new Properties();
    props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafka.getBootstrapServers());
    props.put(ConsumerConfig.GROUP_ID_CONFIG, "test-consumer-group");
    props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
    props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
    props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

    consumer = new KafkaConsumer<>(props);
    consumer.subscribe(List.of(REPORT_GENERATION_REQUEST_TOPIC));
  }

  @AfterEach
  void cleanup() {
    consumer.close();
  }

  @Test
  void testTriggerReportsGeneration() {
    LocationData location = new LocationData(
        "1", "Loc1", 123123127, "UUID", "Rep", "rep@mail.com",
        ReportAlgorithm.FIRST_IN_LAST_OUT, ReportFileFormat.XLSX, true, true, "adminId", null
    );
    DoorOpeningLogRequest doorOpeningLogRequest = new DoorOpeningLogRequest("test", "1", "test1",
        213, "123", ReportAlgorithm.EVERY_IN_OUT, ReportFileFormat.XLSX,
        LocalDateTime.of(2025, 5, 1, 0, 0, 0),
        LocalDateTime.of(2025, 5, 31, 23, 59, 59), "a@a.com", true
    );

    AdministrationData administrationData = new AdministrationData("1", "Adm1", true, Set.of(location));
    DoorOpeningLogResponse doorOpeningLogResponse = new DoorOpeningLogResponse(213, "test", "2025-02-12 12:12:12",
        new DoorOpeningLog(1, 1, new ArrayList<>()));

    when(reportService.locationService.getActiveLocationsInActiveAdministrations()).thenReturn(List.of(location));
    when(reportService.administrationService.findById(eq(location.getAdministrationId()))).thenReturn(administrationData);
    when(reportService.mylinkApiService.loadDoorOpeningLog(any(DoorOpeningLogRequest.class))).thenReturn(doorOpeningLogResponse);

    reportService.triggerReportsGeneration();

    var records = consumer.poll(java.time.Duration.ofSeconds(5));

    assertTrue(!records.isEmpty(), "Expected at least one Kafka message");

    boolean found = false;
    for (var record : records) {
      System.out.println("Received Kafka message: " + record.value());
      if (record.value().contains("UUID")) {
        found = true;
        break;
      }
    }

    assertTrue(found, "Expected Kafka message to contain UUID");
    // TODO: varyfy that the consumer has consumed
    //verify(eventProducer, times(1)).publishReportGenerationRequest(eq("UUID"), any(DoorOpeningLogRequest.class));
  }

  @Test
  void testGenerateReportForLocation_FirstLast() throws Exception {
    DoorOpeningLogRequest request = new DoorOpeningLogRequest(
        "TestAdministration", "1", "Loc1", 123123127, "UUID", ReportAlgorithm.FIRST_IN_LAST_OUT, ReportFileFormat.XLSX,
        LocalDateTime.now().minusDays(5), LocalDateTime.now(),
        "rep@mail.com", true
    );

    DoorEvent inEvent = new DoorEvent();
    inEvent.setEmpId(101);
    inEvent.setEmpName("John Doe");
    inEvent.setEventTime("2023-05-01 08:00:00");
    inEvent.setDevName("MainGate-IN");

    DoorEvent outEvent = new DoorEvent();
    outEvent.setEmpId(101);
    outEvent.setEmpName("John Doe");
    outEvent.setEventTime("2023-05-01 17:00:00");
    outEvent.setDevName("MainGate-OUT");

    DoorOpeningLog logData = new DoorOpeningLog(1, 1, List.of(inEvent, outEvent));
    DoorOpeningLogResponse response = new DoorOpeningLogResponse(200, "OK", "now", logData);

    when(reportService.mylinkApiService.loadDoorOpeningLog(any())).thenReturn(response);
    doNothing().when(reportService.uploadReportService).uploadFile(anyString());
    when(reportService.reportService.saveReport(any(ReportData.class))).thenReturn(null);

    Form76ReportService.xlsxFile = true;

    reportService.generateReportForLocation(request);

    verify(reportService.mylinkApiService).loadDoorOpeningLog(any());
    verify(reportService.uploadReportService).uploadFile(anyString());
    verify(reportService.reportService).saveReport(any(ReportData.class));
    verify(reportService.emailService).sendMailWithAttachment(any());
  }
}
