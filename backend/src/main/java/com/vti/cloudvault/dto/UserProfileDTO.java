package com.vti.cloudvault.dto;

import com.vti.cloudvault.repository.Entity.UserAccountEntity;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserProfileDTO {
    private Long id;
    private String username;
    private String email;
    private UserAccountEntity.Role role;
    private Long storageQuota;
    private Long usedStorage;
    private Long remainingStorage;
    private String usedStorageFormatted;
    private String quotaFormatted;
    private Integer storageUsagePercentage;
    private LocalDateTime createdAt;
    private Boolean isActive;
    private Long fileCount;
}
