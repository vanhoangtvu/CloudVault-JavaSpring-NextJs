package com.vti.cloudvault.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminDashboardDTO {
    private Long totalUsers;
    private Long activeUsers;
    private Long totalFiles;
    private Long totalStorageUsed;
    private String totalStorageUsedFormatted;
    private Long totalStorageQuota;
    private String totalStorageQuotaFormatted;
    private Double averageStorageUsagePercentage;
    private List<UserStorageStatsDTO> topStorageUsers;
    private List<FileStatsDTO> recentFiles;
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UserStorageStatsDTO {
        private String username;
        private Long fileCount;
        private Long usedStorage;
        private String usedStorageFormatted;
        private Double usagePercentage;
    }
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class FileStatsDTO {
        private String fileName;
        private String username;
        private Long size;
        private String sizeFormatted;
        private String uploadDate;
    }
}
