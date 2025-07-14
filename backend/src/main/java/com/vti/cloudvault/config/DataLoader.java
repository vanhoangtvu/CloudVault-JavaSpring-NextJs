package com.vti.cloudvault.config;

import com.vti.cloudvault.repository.Entity.UserAccountEntity;
import com.vti.cloudvault.repository.UserAccountRepository;
import com.vti.cloudvault.util.GoogleDriveHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataLoader implements CommandLineRunner {
    
    private final UserAccountRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final GoogleDriveHelper googleDriveHelper;
    
    @Override
    public void run(String... args) throws Exception {
        createDefaultAdminUser();
    }
    
    private void createDefaultAdminUser() {
        String adminUsername = "admin";
        String adminPassword = "hoangadmin@11";
        String adminEmail = "admin@cloudvault.com";
        
        // Kiểm tra xem admin đã tồn tại chưa
        if (!userRepository.existsByUsername(adminUsername)) {
            try {
                // Tạo thư mục cho admin trên Google Drive
                String adminFolderId = googleDriveHelper.createUserFolder(adminUsername);
                
                // Tạo tài khoản admin
                UserAccountEntity admin = new UserAccountEntity();
                admin.setUsername(adminUsername);
                admin.setPassword(passwordEncoder.encode(adminPassword));
                admin.setEmail(adminEmail);
                admin.setRole(UserAccountEntity.Role.ADMIN);
                admin.setDriveFolderId(adminFolderId);
                admin.setStorageQuota(10737418240L); // 10GB cho admin
                admin.setUsedStorage(0L);
                admin.setIsActive(true);
                
                userRepository.save(admin);
                
                log.info("=================================");
                log.info("ADMIN ACCOUNT CREATED SUCCESSFULLY!");
                log.info("Username: {}", adminUsername);
                log.info("Password: {}", adminPassword);
                log.info("Email: {}", adminEmail);
                log.info("Storage Quota: 10GB");
                log.info("=================================");
                
            } catch (Exception e) {
                log.error("Error creating default admin user: {}", e.getMessage());
                
                // Nếu lỗi tạo folder trên Drive, vẫn tạo admin nhưng không có folderId
                UserAccountEntity admin = new UserAccountEntity();
                admin.setUsername(adminUsername);
                admin.setPassword(passwordEncoder.encode(adminPassword));
                admin.setEmail(adminEmail);
                admin.setRole(UserAccountEntity.Role.ADMIN);
                admin.setStorageQuota(10737418240L); // 10GB cho admin
                admin.setUsedStorage(0L);
                admin.setIsActive(true);
                
                userRepository.save(admin);
                
                log.warn("Admin created without Google Drive folder. Folder ID will be created on first file upload.");
                log.info("=================================");
                log.info("ADMIN ACCOUNT CREATED (LIMITED)!");
                log.info("Username: {}", adminUsername);
                log.info("Password: {}", adminPassword);
                log.info("Email: {}", adminEmail);
                log.info("=================================");
            }
        } else {
            log.info("Default admin user '{}' already exists", adminUsername);
        }
    }
}
