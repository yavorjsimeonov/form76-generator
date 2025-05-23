package com.form76.generator.service;

import com.form76.generator.rest.model.ReportDownloadResponse;
import com.google.auth.Credentials;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class UploadReportServiceTest {

  private UploadReportService uploadReportService;
  private Storage mockStorage;

  @BeforeEach
  void setup() {
    uploadReportService = Mockito.spy(new UploadReportService());
    mockStorage = mock(Storage.class);

    uploadReportService.projectId = "mock-project";
    uploadReportService.bucketName = "mock-bucket";
  }

  @Test
  void testUploadFile_CreatesBlob() throws Exception {
    // Create a dummy file
    String fileName = "test-upload.txt";
    Path tmpFile = Path.of("/tmp/" + fileName);
    Files.writeString(tmpFile, "sample data");

    doReturn(mockStorage).when(uploadReportService).getReportsBucket();

    uploadReportService.uploadFile(fileName);

    verify(mockStorage).createFrom(any(BlobInfo.class), eq(tmpFile));
    Files.deleteIfExists(tmpFile); // Cleanup
  }

  @Test
  void testDownloadFile_ReadsBlobAndReturnsData() throws Exception {
    String fileName = "test-download.txt";
    Path tmpFile = Path.of("/tmp/" + fileName);
    byte[] content = "file contents".getBytes();
    Files.write(tmpFile, content);

    doReturn(mockStorage).when(uploadReportService).getReportsBucket();

    ReportDownloadResponse response = uploadReportService.downloadFile(fileName);

    assertEquals(fileName, response.fileName());
    assertArrayEquals(content, response.content());
    Files.deleteIfExists(tmpFile); // Cleanup
  }

  @Test
  void testBlobInfoStructure() {
    // Reflection test to confirm blob path composition logic
    BlobInfo blobInfo = uploadReportService.getBlobInfo("file1.pdf");

    assertEquals("file1.pdf", blobInfo.getBlobId().getName());
    assertEquals("mock-bucket", blobInfo.getBlobId().getBucket());
  }
}
