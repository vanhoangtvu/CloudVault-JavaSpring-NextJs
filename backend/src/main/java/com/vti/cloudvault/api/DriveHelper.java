package com.vti.cloudvault.api;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.FileContent;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.drive.model.*;
import com.google.auth.http.HttpCredentialsAdapter;
import com.google.auth.oauth2.GoogleCredentials;

import java.io.File;
import java.io.FileInputStream;
import java.util.Collections;
import java.util.List;

public class DriveHelper {

    private static final String APPLICATION_NAME = "CloudNet DriveHub";
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    private static final String CREDENTIALS_FILE_PATH = "src/main/resources/credentials.json";

    private static Drive getDriveService() throws Exception {
        final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        GoogleCredentials credentials = GoogleCredentials.fromStream(new FileInputStream(CREDENTIALS_FILE_PATH))
                .createScoped(Collections.singleton(DriveScopes.DRIVE));
        return new Drive.Builder(HTTP_TRANSPORT, JSON_FACTORY, new HttpCredentialsAdapter(credentials))
                .setApplicationName(APPLICATION_NAME)
                .build();
    }

    public static String uploadFile(String filePath, String mimeType, String folderId) throws Exception {
        Drive service = getDriveService();
        File fileMetaData = new File();
        fileMetaData.setName(new java.io.File(filePath).getName());
        if (folderId != null) {
            fileMetaData.setParents(List.of(folderId));
        }

        FileContent mediaContent = new FileContent(mimeType, new java.io.File(filePath));
        File file = service.files().create(fileMetaData, mediaContent)
                .setFields("id, name, parents")
                .execute();
        System.out.println("File Uploaded ID: " + file.getId());
        return file.getId();
    }

    public static void listFiles() throws Exception {
        Drive service = getDriveService();
        FileList result = service.files().list()
                .setPageSize(10)
                .setFields("files(id, name)")
                .execute();
        List<File> files = result.getFiles();
        if (files == null || files.isEmpty()) {
            System.out.println("No files found.");
        } else {
            System.out.println("Files:");
            for (File file : files) {
                System.out.printf("%s (%s)\n", file.getName(), file.getId());
            }
        }
    }

    public static void deleteFile(String fileId) throws Exception {
        Drive service = getDriveService();
        service.files().delete(fileId).execute();
        System.out.println("File deleted: " + fileId);
    }
}
