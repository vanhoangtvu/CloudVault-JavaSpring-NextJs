package com.vti.cloudvault.api;

import com.vti.cloudvault.dto.UserProfileDTO;
import com.vti.cloudvault.service.UserService;
import com.vti.cloudvault.repository.Entity.UserAccountEntity;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
@PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
@Tag(name = "User Profile", description = "User profile management endpoints")
@SecurityRequirement(name = "Bearer Authentication")
public class UserController {
    
    private final UserService userService;
    
    /**
     * Lấy thông tin profile của user hiện tại
     */
    @GetMapping("/profile")
    @Operation(summary = "Get user profile", description = "Get current user's profile information")
    @ApiResponse(responseCode = "200", description = "Profile retrieved successfully")
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
     * Lấy thông tin storage usage của user hiện tại
     */
    @GetMapping("/storage-info")
    @Operation(summary = "Get storage information", description = "Get current user's storage usage information")
    @ApiResponse(responseCode = "200", description = "Storage info retrieved successfully")
    public ResponseEntity<?> getStorageInfo() {
        try {
            Long userId = getCurrentUserId();
            UserProfileDTO profile = userService.getUserProfile(userId);
            
            Map<String, Object> storageInfo = new HashMap<>();
            storageInfo.put("usedStorage", profile.getUsedStorage());
            storageInfo.put("usedStorageFormatted", profile.getUsedStorageFormatted());
            storageInfo.put("storageQuota", profile.getStorageQuota());
            storageInfo.put("quotaFormatted", profile.getQuotaFormatted());
            storageInfo.put("remainingStorage", profile.getRemainingStorage());
            storageInfo.put("usagePercentage", profile.getStorageUsagePercentage());
            storageInfo.put("fileCount", profile.getFileCount());
            
            return ResponseEntity.ok(storageInfo);
        } catch (Exception e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Cannot get storage info: " + e.getMessage());
            return ResponseEntity.status(500).body(errorResponse);
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
