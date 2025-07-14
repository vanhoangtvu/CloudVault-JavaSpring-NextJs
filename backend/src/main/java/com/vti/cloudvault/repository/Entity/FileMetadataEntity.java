package com.vti.cloudvault.repository.Entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "file_metadata")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FileMetadataEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String fileName;
    
    private String mimeType;
    
    @Column(unique = true, nullable = false)
    private String driveFileId;
    
    private String webViewLink;
    
    private String downloadLink;
    
    @Column(nullable = false)
    private Long size = 0L; // Dung lượng tính bằng byte

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserAccountEntity userAccount;

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    @Column(name = "is_deleted")
    private Boolean isDeleted = false;
    
    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
    
    // Method để kiểm tra quyền truy cập
    public boolean canAccess(UserAccountEntity user) {
        if (user.isAdmin()) {
            return true;
        }
        return this.userAccount.getId().equals(user.getId());
    }
}
