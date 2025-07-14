package com.vti.cloudvault.repository.Entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "user_account")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserAccountEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(unique = true, nullable = false)
    private String username;
    
    @Column(nullable = false)
    private String password;
    
    @Column(unique = true, nullable = false)
    private String email;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role = Role.USER;
    
    @Column(name = "drive_folder_id")
    private String driveFolderId; // ID thư mục riêng trên Google Drive
    
    @Column(name = "storage_quota")
    private Long storageQuota = 1073741824L; // 1GB mặc định
    
    @Column(name = "used_storage")
    private Long usedStorage = 0L;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();
    
    @Column(name = "is_active")
    private Boolean isActive = true;
    
    @OneToMany(mappedBy = "userAccount", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<FileMetadataEntity> files = new ArrayList<>();
    
    public enum Role {
        USER, ADMIN
    }
    
    // Method để kiểm tra có phải admin không
    public boolean isAdmin() {
        return Role.ADMIN.equals(this.role);
    }
    
    // Method để kiểm tra dung lượng còn lại
    public Long getRemainingStorage() {
        return storageQuota - usedStorage;
    }
    
    // Method để cập nhật dung lượng đã sử dụng
    public void updateUsedStorage(Long additionalSize) {
        this.usedStorage += additionalSize;
    }
}


