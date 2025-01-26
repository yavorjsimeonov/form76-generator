package com.form76.generator.service;

//import com.google.auth.ApiKeyCredentials;
//import com.google.auth.Credentials;
//import com.google.auth.oauth2.AccessToken;
import com.form76.generator.rest.model.ReportDownloadResponse;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.file.Paths;

@Service
public class UploadReportService {

  @Value("${google.cloud.project-id}")
  private String projectId;
  @Value("${google.cloud.bucket.name}")
  private String bucketName;
//  @Value("${google.cloud.project.api.key}")
//  private String apiKey;


  public void uploadFile(String fileName) throws IOException {
//    Storage storage = StorageOptions.newBuilder()
//        .setProjectId(projectId)
//        .setCredentials(ApiKeyCredentials.create(apiKey))
//        .build().getService();

    Storage storage = getReportsBucket();
    BlobInfo blobInfo = getBlobInfo(fileName);
    String filePath = "/tmp/" + fileName;

    storage.createFrom(blobInfo, Paths.get(filePath));
  }


  public ReportDownloadResponse downloadFile(String fileName) throws IOException {
    Storage storage = getReportsBucket();
    BlobInfo blobInfo = getBlobInfo(fileName);
    String filePath = "/tmp/" + fileName;

    storage.downloadTo(blobInfo.getBlobId(), Paths.get(filePath));

    try (FileInputStream byteArrayInputStream = new FileInputStream(filePath)) {
      return new ReportDownloadResponse(fileName, byteArrayInputStream.readAllBytes());
    }
  }

  private Storage getReportsBucket() throws IOException {
    StorageOptions storageOptions = StorageOptions.newBuilder()
        .setProjectId(projectId)
        .setCredentials(GoogleCredentials.fromStream(
            new FileInputStream("/app/nimble-theme-448917-u5-c5df7d240535.json"))).build();
    return storageOptions.getService();
  }

  private BlobInfo getBlobInfo(String fileName) {
    BlobId blobId = BlobId.of(bucketName, fileName);
    return BlobInfo.newBuilder(blobId).build();
  }
}
