//package com.form76.generator.service;
//
//import com.form76.generator.service.model.DoorOpeningLogRequest;
//import com.form76.generator.service.model.DoorOpeningLogResponse;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.reactive.function.client.WebClient;
//import reactor.core.publisher.Mono;
//
//import java.time.LocalDateTime;
//import java.util.function.Function;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.*;
//
//@ExtendWith(MockitoExtension.class)
//class MylinkApiServiceTest {
//
//  @Mock
//  private WebClient.Builder webClientBuilder;
//
//  @Mock
//  private WebClient webClient;
//
//  @Mock
//  private WebClient.RequestHeadersUriSpec<?> uriSpec;
//
//  @Mock
//  private WebClient.RequestHeadersSpec<?> headersSpec;
//
//  @Mock
//  private WebClient.ResponseSpec responseSpec;
//
//  @InjectMocks
//  private MylinkApiService mylinkApiService;
//
//  @BeforeEach
//  void setup() {
//    // Inject test values into @Value fields
//    mylinkApiService.myLinkHost = "http://mock-host";
//    mylinkApiService.myLinkApiToken = "mock-token";
//    mylinkApiService.myLinkDoorOpeningReportUri = "/mock-uri";
//
//    // Set up WebClient chain
//    when(webClientBuilder.baseUrl(anyString())).thenReturn(webClientBuilder);
//    when(webClientBuilder.build()).thenReturn(webClient);
//    when(webClient.get()).thenReturn(uriSpec);
//    when(uriSpec.uri(any(Function.class))).thenReturn(headersSpec);
//    when(headersSpec.retrieve()).thenReturn(responseSpec);
//  }
//
//  @Test
//  void testLoadDoorOpeningLog_ReturnsResponse() {
//    DoorOpeningLogRequest request = new DoorOpeningLogRequest();
//    request.setLocationExtCommunityId(123456);
//    request.setLocationExtCommunityUuid("uuid");
//    request.setStartDateTime(LocalDateTime.now().minusDays(1));
//    request.setEndDateTime(LocalDateTime.now());
//
//    DoorOpeningLogResponse expected = new DoorOpeningLogResponse();
//    expected.setMsg("Success");
//
//    ResponseEntity<DoorOpeningLogResponse> entity = ResponseEntity.ok(expected);
//
//    when(responseSpec.toEntity(DoorOpeningLogResponse.class)).thenReturn(Mono.just(entity));
//
//    DoorOpeningLogResponse result = mylinkApiService.loadDoorOpeningLog(request);
//
//    assertNotNull(result);
//    assertEquals("Success", result.getMsg());
//  }
//
//  @Test
//  void testLoadDoorOpeningLog_NullResponse() {
//    DoorOpeningLogRequest request = new DoorOpeningLogRequest();
//    request.setLocationExtCommunityId(654321);
//    request.setLocationExtCommunityUuid("uuid");
//    request.setStartDateTime(LocalDateTime.now().minusDays(1));
//    request.setEndDateTime(LocalDateTime.now());
//
//    when(responseSpec.toEntity(DoorOpeningLogResponse.class)).thenReturn(Mono.just(null));
//
//    DoorOpeningLogResponse result = mylinkApiService.loadDoorOpeningLog(request);
//
//    assertNull(result);
//  }
//}
