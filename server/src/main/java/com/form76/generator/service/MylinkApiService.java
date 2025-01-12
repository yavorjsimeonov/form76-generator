package com.form76.generator.service;

import com.form76.generator.db.entity.Location;
import com.form76.generator.service.model.DoorOpeningLogRequest;
import com.form76.generator.service.model.DoorOpeningLogResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.sql.Date;
import java.time.ZoneOffset;

@Service
public class MylinkApiService {

  Logger logger = LoggerFactory.getLogger(MylinkApiService.class);

  @Autowired
  WebClient.Builder webClientBuilder;

  @Value("${mylink.host}")
  private String myLinkHost;

  @Value("${mylink.door.opening.report.uri}")
  private String myLinkDoorOpeningReportUri;

  @Value("${mylink.api.token}")
  private String myLinkApiToken;


  public DoorOpeningLogResponse loadDoorOpeningLog(DoorOpeningLogRequest doorOpeningLogRequest) {
    logger.info("Calling mylink to obtain door opening log for doorOpeningLogRequest: " + doorOpeningLogRequest);
    ResponseEntity<DoorOpeningLogResponse> response = callExternalApi(doorOpeningLogRequest).block();
    logger.info("Received response from mylink: " + ( response != null && response.getBody() != null  ? response.getBody().msg : null));

    return response != null ? response.getBody() : null;
  }

  public Mono<ResponseEntity<DoorOpeningLogResponse>> callExternalApi(DoorOpeningLogRequest doorOpeningLogRequest) {
    String fromDate = DateHelper.formatReportDate(Date.from(doorOpeningLogRequest.startDateTime.toInstant(ZoneOffset.UTC)));
    String toDate = DateHelper.formatReportDate(Date.from(doorOpeningLogRequest.endDateTime.toInstant(ZoneOffset.UTC)));

    return webClientBuilder.baseUrl(myLinkHost)
        .build()
        .get()
        .uri(uriBuilder ->
            uriBuilder.path(myLinkDoorOpeningReportUri)
                .queryParam("accessToken", "{token}")
                .queryParam("extCommunityId", "{commId}")
                .queryParam("extCommunityUuid", "{commUuid}")
                .queryParam("startDateTime", "{startTime}")
                .queryParam("endDateTime", "{endTime}")
                .queryParam("openStatus", "{status}")
                .queryParam("currentPage", "{page}")
                .queryParam("pageSize", "{size}")
                .build(
        myLinkApiToken,
                    doorOpeningLogRequest.locationExtCommunityId,
                    doorOpeningLogRequest.locationExtCommunityUuid,
                    fromDate,
                    toDate,
                    "1",
                    "1",
                    "100"
                )
        )
        .retrieve()
        .toEntity(DoorOpeningLogResponse.class);
  }


}
