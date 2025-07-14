package com.vti.cloudvault.api;

import com.vti.cloudvault.dto.AdminDashboardDTO;
import com.vti.cloudvault.dto.UserProfileDTO;
import com.vti.cloudvault.dto.FileResponseDTO;
import com.vti.cloudvault.service.AdminService;
import com.vti.cloudvault.service.UserService;
import com.vti.cloudvault.service.FileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
@Tag(name = "Admin Management", description = "Admin endpoints for user and system management")
@SecurityRequirement(name = "Bearer Authentication")
public class AdminController {
    
    private final AdminService adminService;
    private final UserService userService;
    private final FileService fileService;
    
    /**
     * Dashboard - Thống kê tổng quan
     */
    @GetMapping("/dashboard")
    public ResponseEntity<AdminDashboardDTO> getDashboard() {
        AdminDashboardDTO dashboard = adminService.getDashboardData();
        return ResponseEntity.ok(dashboard);
    }
    
    /**
     * Quản lý người dùng - Xem danh sách tất cả user
     */
    @GetMapping("/users")
    public ResponseEntity<List<UserProfileDTO>> getAllUsers() {
        List<UserProfileDTO> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }
    
    /**
     * Quản lý người dùng - Xem chi tiết một user
     */
    @GetMapping("/users/{userId}")
    public ResponseEntity<UserProfileDTO> getUserDetails(@PathVariable Long userId) {
        UserProfileDTO user = userService.getUserProfile(userId);
        return ResponseEntity.ok(user);
    }
    
    /**
     * Quản lý người dùng - Cập nhật quota storage cho user
     */
    @PutMapping("/users/{userId}/quota")
    public ResponseEntity<?> updateUserQuota(@PathVariable Long userId, @RequestBody Map<String, Long> request) {
        Long newQuota = request.get("quota");
        userService.updateUserQuota(userId, newQuota);
        
        Map<String, Object> response = new HashMap<>();
        response.put("message", "User quota updated successfully");
        response.put("newQuota", newQuota);
        
        return ResponseEntity.ok(response);
    }
    
    /**
     * Quản lý người dùng - Kích hoạt/vô hiệu hóa user
     */
    @PutMapping("/users/{userId}/toggle-status")
    public ResponseEntity<?> toggleUserStatus(@PathVariable Long userId) {
        userService.toggleUserStatus(userId);
        
        Map<String, Object> response = new HashMap<>();
        response.put("message", "User status updated successfully");
        
        return ResponseEntity.ok(response);
    }
    
    /**
     * Quản lý người dùng - Xóa user
     */
    @DeleteMapping("/users/{userId}")
    public ResponseEntity<?> deleteUser(@PathVariable Long userId) {
        userService.deleteUser(userId);
        
        Map<String, Object> response = new HashMap<>();
        response.put("message", "User deleted successfully");
        
        return ResponseEntity.ok(response);
    }
    
    /**
     * Quản lý file - Xem tất cả file của tất cả user
     */
    @GetMapping("/files")
    public ResponseEntity<List<FileResponseDTO>> getAllFiles() {
        List<FileResponseDTO> files = fileService.getAllFiles();
        return ResponseEntity.ok(files);
    }
    
    /**
     * Quản lý file - Xóa file của bất kỳ user nào
     */
    @DeleteMapping("/files/{fileId}")
    public ResponseEntity<?> deleteFile(@PathVariable Long fileId) {
        fileService.deleteFileByAdmin(fileId);
        
        Map<String, Object> response = new HashMap<>();
        response.put("message", "File deleted successfully");
        
        return ResponseEntity.ok(response);
    }
    
    /**
     * Thống kê storage của một user cụ thể
     */
    @GetMapping("/users/{userId}/storage-stats")
    public ResponseEntity<AdminDashboardDTO.UserStorageStatsDTO> getUserStorageStats(@PathVariable Long userId) {
        AdminDashboardDTO.UserStorageStatsDTO stats = adminService.getUserStorageStats(userId);
        return ResponseEntity.ok(stats);
    }
    
    /**
     * Dọn dẹp file đã xóa
     */
    @PostMapping("/cleanup")
    public ResponseEntity<?> cleanupDeletedFiles() {
        adminService.cleanupDeletedFiles();
        
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Cleanup completed successfully");
        
        return ResponseEntity.ok(response);
    }
}
