package com.vti.cloudvault.service.impl;

import com.vti.cloudvault.repository.Entity.FileMetadataEntity;
import com.vti.cloudvault.repository.FileMetadataRepository;
import com.vti.cloudvault.repository.UserAccountRepository;
import com.vti.cloudvault.service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class FileServiceImpl implements FileService {

    private final FileMetadataRepository fileMetadataRepository;
    private final UserAccountRepository userAccountRepository;
    private final DriveHelper driveHelper;

    @Override
    public FileResponse uploadFile(MultipartFile file, Long userId) throws IOException {
        String driveFileId = driveHelper.uploadFileToDrive(file);
        String link = driveHelper.getWebViewLink(driveFileId);

        FileMetadataEntity metadata = new FileMetadataEntity();
        metadata.setFileName(file.getOriginalFilename());
        metadata.setMimeType(file.getContentType());
        metadata.setDriveFileId(driveFileId);
        metadata.setWebViewLink(link);
        metadata.setSize(file.getSize());
        metadata.setCreatedAt(LocalDateTime.now());
        metadata.setUserAccount(userAccountRepository.findById(userId).orElseThrow());

        fileMetadataRepository.save(metadata);

        return new FileResponse(metadata.getId(), metadata.getFileName(), metadata.getWebViewLink());
    }

    @Override
    public List<FileResponse> getMyFiles(Long userId) {
        return fileMetadataRepository.findByUserAccountId(userId)
                .stream()
                .map(f -> new FileResponse(f.getId(), f.getFileName(), f.getWebViewLink()))
                .toList();
    }

    @Override
    public void deleteFile(Long fileId, Long userId) {
        FileMetadataEntity file = fileMetadataRepository.findById(fileId)
                .orElseThrow(() -> new RuntimeException("File not found"));
        if (!file.getUserAccount().getId().equals(userId)) {
            throw new AccessDeniedException("Không có quyền xoá file này");
        }
        driveHelper.deleteFileFromDrive(file.getDriveFileId());
        fileMetadataRepository.delete(file);
    }
}
