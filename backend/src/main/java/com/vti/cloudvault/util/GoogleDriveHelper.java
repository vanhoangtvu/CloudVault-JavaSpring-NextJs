package com.vti.cloudvault.util;

import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;
import com.google.api.services.drive.model.Permission;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;

@Component
@RequiredArgsConstructor
public class GoogleDriveHelper {
    
    private final Drive driveService;
    
    /**
     * Tạo thư mục riêng cho user trên Google Drive
     */
    public String createUserFolder(String username) throws IOException {
        File folderMetadata = new File();
        folderMetadata.setName("CloudVault_" + username);
        folderMetadata.setMimeType("application/vnd.google-apps.folder");
        
        File folder = driveService.files().create(folderMetadata)
                .setFields("id, name")
                .execute();
        
        // Đặt quyền chỉ cho service account có thể truy cập
        setFolderPermissions(folder.getId());
        
        return folder.getId();
    }
    
    /**
     * Upload file vào thư mục riêng của user
     */
    public String uploadFileToDrive(MultipartFile file, String userFolderId) throws IOException {
        File fileMetadata = new File();
        fileMetadata.setName(file.getOriginalFilename());
        fileMetadata.setParents(Collections.singletonList(userFolderId));

        InputStream inputStream = file.getInputStream();
        com.google.api.client.http.InputStreamContent mediaContent =
                new com.google.api.client.http.InputStreamContent(file.getContentType(), inputStream);

        File uploadedFile = driveService.files().create(fileMetadata, mediaContent)
                .setFields("id, name, size, webViewLink, webContentLink")
                .execute();
                
        return uploadedFile.getId();
    }
    
    /**
     * Lấy thông tin file từ Google Drive
     */
    public File getFileInfo(String fileId) throws IOException {
        return driveService.files().get(fileId)
                .setFields("id, name, size, webViewLink, webContentLink, mimeType")
                .execute();
    }
    
    /**
     * Lấy link xem file
     */
    public String getWebViewLink(String fileId) throws IOException {
        File file = driveService.files().get(fileId)
                .setFields("webViewLink")
                .execute();
        return file.getWebViewLink();
    }
    
    /**
     * Lấy link download file
     */
    public String getDownloadLink(String fileId) throws IOException {
        File file = driveService.files().get(fileId)
                .setFields("webContentLink")
                .execute();
        return file.getWebContentLink();
    }
    
    /**
     * Lấy danh sách file trong thư mục của user
     */
    public List<File> listFilesInFolder(String folderId) throws IOException {
        FileList result = driveService.files().list()
                .setQ("'" + folderId + "' in parents and trashed=false")
                .setFields("files(id, name, mimeType, size, createdTime, webViewLink)")
                .execute();
        return result.getFiles();
    }
    
    /**
     * Xóa file khỏi Google Drive
     */
    public void deleteFileFromDrive(String fileId) throws IOException {
        driveService.files().delete(fileId).execute();
    }
    
    /**
     * Cập nhật file trên Google Drive
     */
    public String updateFile(String fileId, MultipartFile newFile) throws IOException {
        File fileMetadata = new File();
        fileMetadata.setName(newFile.getOriginalFilename());

        InputStream inputStream = newFile.getInputStream();
        com.google.api.client.http.InputStreamContent mediaContent =
                new com.google.api.client.http.InputStreamContent(newFile.getContentType(), inputStream);

        File updatedFile = driveService.files().update(fileId, fileMetadata, mediaContent)
                .setFields("id, name, size, webViewLink, webContentLink")
                .execute();
                
        return updatedFile.getId();
    }
    
    /**
     * Kiểm tra file có tồn tại không
     */
    public boolean fileExists(String fileId) {
        try {
            driveService.files().get(fileId).execute();
            return true;
        } catch (IOException e) {
            return false;
        }
    }
    
    /**
     * Đặt quyền cho thư mục (private)
     */
    private void setFolderPermissions(String folderId) throws IOException {
        // Thư mục sẽ chỉ có thể truy cập bởi service account
        // Không cần set thêm permission vì mặc định đã private
    }
    
    /**
     * Lấy tổng dung lượng sử dụng của thư mục
     */
    public Long getFolderSize(String folderId) throws IOException {
        List<File> files = listFilesInFolder(folderId);
        return files.stream()
                .mapToLong(file -> file.getSize() != null ? file.getSize() : 0L)
                .sum();
    }
}
