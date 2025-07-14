package com.vti.cloudvault.api;

import com.vti.cloudvault.dto.FileResponseDTO;
import com.vti.cloudvault.dto.UploadResponseDTO;
import com.vti.cloudvault.dto.UserProfileDTO;
import com.vti.cloudvault.service.FileService;
import com.vti.cloudvault.service.UserService;
import com.vti.cloudvault.repository.Entity.UserAccountEntity;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/drive")
@RequiredArgsConstructor
@PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
@Tag(name = "File Management", description = "Endpoints for file upload, download, and management")
@SecurityRequirement(name = "Bearer Authentication")
public class DriveController {

    private final FileService fileService;
    private final UserService userService;

    /**
     * Upload file - chỉ user có thể upload vào thư mục của mình
     */
    @PostMapping("/upload")
    @Operation(summary = "Upload file", description = "Upload a file to user's personal folder on Google Drive")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "File uploaded successfully"),
        @ApiResponse(responseCode = "400", description = "Storage quota exceeded"),
        @ApiResponse(responseCode = "500", description = "Upload failed")
    })
    public ResponseEntity<?> uploadFile(
            @Parameter(description = "File to upload") @RequestParam("file") MultipartFile file) {
        try {
            Long userId = getCurrentUserId();
            
            // Kiểm tra quota trước khi upload
            if (!fileService.checkStorageQuota(userId, file.getSize())) {
                return ResponseEntity.badRequest().body("Storage quota exceeded");
            }
            
            UploadResponseDTO response = fileService.uploadFile(file, userId);
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Upload failed: " + e.getMessage());
            return ResponseEntity.status(500).body(errorResponse);
        }
    }

    /**
     * Danh sách file của user hiện tại
     */
    @GetMapping("/my-files")
    @Operation(summary = "Get my files", description = "Get list of files uploaded by current user")
    @ApiResponse(responseCode = "200", description = "Files retrieved successfully")
    public ResponseEntity<List<FileResponseDTO>> getMyFiles() {
        try {
            Long userId = getCurrentUserId();
            List<FileResponseDTO> files = fileService.getMyFiles(userId);
            return ResponseEntity.ok(files);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }
    
    /**
     * Lấy thông tin chi tiết một file
     */
    @GetMapping("/files/{fileId}")
    public ResponseEntity<FileResponseDTO> getFileDetails(@PathVariable Long fileId) {
        try {
            Long userId = getCurrentUserId();
            FileResponseDTO file = fileService.getFileById(fileId, userId);
            return ResponseEntity.ok(file);
        } catch (Exception e) {
            return ResponseEntity.status(404).body(null);
        }
    }
    
    /**
     * Lấy link download file
     */
    @GetMapping("/files/{fileId}/download")
    public ResponseEntity<?> getDownloadLink(@PathVariable Long fileId) {
        try {
            Long userId = getCurrentUserId();
            String downloadLink = fileService.getDownloadLink(fileId, userId);
            
            Map<String, String> response = new HashMap<>();
            response.put("downloadLink", downloadLink);
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Cannot get download link: " + e.getMessage());
            return ResponseEntity.status(404).body(errorResponse);
        }
    }

    /**
     * Xóa file - chỉ user có thể xóa file của mình
     */
    @DeleteMapping("/files/{fileId}")
    public ResponseEntity<?> deleteFile(@PathVariable Long fileId) {
        try {
            Long userId = getCurrentUserId();
            fileService.deleteFile(fileId, userId);
            
            Map<String, String> response = new HashMap<>();
            response.put("message", "File deleted successfully");
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Delete failed: " + e.getMessage());
            return ResponseEntity.status(500).body(errorResponse);
        }
    }
    
    /**
     * Cập nhật file - thay thế file cũ bằng file mới
     */
    @PutMapping("/files/{fileId}")
    public ResponseEntity<?> updateFile(@PathVariable Long fileId, @RequestParam("file") MultipartFile file) {
        try {
            Long userId = getCurrentUserId();
            
            // Kiểm tra quota trước khi update
            if (!fileService.checkStorageQuota(userId, file.getSize())) {
                return ResponseEntity.badRequest().body("Storage quota exceeded");
            }
            
            UploadResponseDTO response = fileService.updateFile(fileId, file, userId);
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Update failed: " + e.getMessage());
            return ResponseEntity.status(500).body(errorResponse);
        }
    }
    
    /**
     * Lấy thông tin profile và storage của user hiện tại
     */
    @GetMapping("/profile")
    public ResponseEntity<UserProfileDTO> getUserProfile() {
        try {
            Long userId = getCurrentUserId();
            UserProfileDTO profile = userService.getUserProfile(userId);
            return ResponseEntity.ok(profile);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    /**
     * Helper method để lấy ID của user hiện tại từ JWT token
     */
    private Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        UserAccountEntity user = userService.findByUsername(username);
        return user.getId();
    }
}

