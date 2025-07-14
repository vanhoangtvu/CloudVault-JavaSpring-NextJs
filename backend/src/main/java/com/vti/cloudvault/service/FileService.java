package com.vti.cloudvault.service;

import com.vti.cloudvault.dto.FileResponseDTO;
import com.vti.cloudvault.dto.UploadResponseDTO;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface FileService {
    UploadResponseDTO uploadFile(MultipartFile file, Long userId) throws IOException;
    List<FileResponseDTO> getMyFiles(Long userId);
    List<FileResponseDTO> getAllFiles(); // For admin
    FileResponseDTO getFileById(Long fileId, Long userId);
    void deleteFile(Long fileId, Long userId);
    void deleteFileByAdmin(Long fileId); // Admin can delete any file
    UploadResponseDTO updateFile(Long fileId, MultipartFile file, Long userId) throws IOException;
    boolean checkStorageQuota(Long userId, Long fileSize);
    String getDownloadLink(Long fileId, Long userId);
}
