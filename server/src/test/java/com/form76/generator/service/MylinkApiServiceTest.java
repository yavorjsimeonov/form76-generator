package com.form76.generator.service;

import com.form76.generator.service.model.DoorOpeningLogRequest;
import com.form76.generator.service.model.DoorOpeningLogResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MylinkApiServiceTest {

  @Mock
  private WebClient webClientMock;

  @Mock
  private WebClient.Builder requestBuilderMock;

  @SuppressWarnings("rawtypes")
  @Mock
  private WebClient.RequestHeadersSpec requestHeadersSpecMock;

  @SuppressWarnings("rawtypes")
  @Mock
  private WebClient.RequestHeadersUriSpec requestHeadersUriSpecMock;

  @Mock
  private WebClient.ResponseSpec responseSpecMock;

  @InjectMocks
  private MylinkApiService mylinkApiService;

  @BeforeEach
  void setup() {
    mylinkApiService.myLinkHost = "http://mock-host";
    mylinkApiService.myLinkApiToken = "mock-token";
    mylinkApiService.myLinkDoorOpeningReportUri = "/mock-uri";

    when(requestBuilderMock.baseUrl(anyString())).thenReturn(requestBuilderMock);
    when(requestBuilderMock.build()).thenReturn(webClientMock);
    when(webClientMock.get()).thenReturn(requestHeadersUriSpecMock);
    when(requestHeadersUriSpecMock.uri(any(Function.class))).thenReturn(requestHeadersSpecMock);
    when(requestHeadersSpecMock.retrieve()).thenReturn(responseSpecMock);
  }

  @Test
  void testLoadDoorOpeningLog_ReturnsResponse() {
    DoorOpeningLogRequest request = new DoorOpeningLogRequest();
    request.setLocationExtCommunityId(123456);
    request.setLocationExtCommunityUuid("uuid");
    request.setStartDateTime(LocalDateTime.now().minusDays(1));
    request.setEndDateTime(LocalDateTime.now());

    DoorOpeningLogResponse expected = new DoorOpeningLogResponse();
    expected.setMsg("Success");

    ResponseEntity<DoorOpeningLogResponse> entity = ResponseEntity.ok(expected);

    when(responseSpecMock.toEntity(DoorOpeningLogResponse.class)).thenReturn(Mono.just(entity));

    DoorOpeningLogResponse result = mylinkApiService.loadDoorOpeningLog(request);

    assertNotNull(result);
    assertEquals("Success", result.getMsg());
  }

  @Test
  void testLoadDoorOpeningLog_EmptyResponse() {
    DoorOpeningLogRequest request = new DoorOpeningLogRequest();
    request.setLocationExtCommunityId(654321);
    request.setLocationExtCommunityUuid("uuid");
    request.setStartDateTime(LocalDateTime.now().minusDays(1));
    request.setEndDateTime(LocalDateTime.now());

    when(responseSpecMock.toEntity(DoorOpeningLogResponse.class)).thenReturn(Mono.empty());

    DoorOpeningLogResponse result = mylinkApiService.loadDoorOpeningLog(request);

    assertNull(result);
  }
}
