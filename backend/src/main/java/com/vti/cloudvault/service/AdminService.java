package com.vti.cloudvault.service;

import com.vti.cloudvault.dto.AdminDashboardDTO;

public interface AdminService {
    AdminDashboardDTO getDashboardData();
    void cleanupDeletedFiles();
    AdminDashboardDTO.UserStorageStatsDTO getUserStorageStats(Long userId);
}
