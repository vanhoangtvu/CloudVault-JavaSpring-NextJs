package com.vti.cloudvault.repository;

import com.vti.cloudvault.repository.Entity.FileMetadataEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FileMetadataRepository extends JpaRepository<FileMetadataEntity, Long> {
    
    List<FileMetadataEntity> findByUserAccountId(Long userId);
    
    List<FileMetadataEntity> findByUserAccountIdAndIsDeletedFalse(Long userId);
    
    Optional<FileMetadataEntity> findByIdAndUserAccountId(Long fileId, Long userId);
    
    Optional<FileMetadataEntity> findByDriveFileId(String driveFileId);
    
    @Query("SELECT f FROM FileMetadataEntity f WHERE f.isDeleted = false ORDER BY f.createdAt DESC")
    List<FileMetadataEntity> findAllActiveFiles();
    
    @Query("SELECT f FROM FileMetadataEntity f JOIN f.userAccount u WHERE u.username = :username AND f.isDeleted = false")
    List<FileMetadataEntity> findByUsername(@Param("username") String username);
    
    @Query("SELECT SUM(f.size) FROM FileMetadataEntity f WHERE f.userAccount.id = :userId AND f.isDeleted = false")
    Long getTotalSizeByUserId(@Param("userId") Long userId);
    
    @Query("SELECT COUNT(f) FROM FileMetadataEntity f WHERE f.userAccount.id = :userId AND f.isDeleted = false")
    Long getFileCountByUserId(@Param("userId") Long userId);
    
    @Query("SELECT f FROM FileMetadataEntity f WHERE f.size > :sizeThreshold AND f.isDeleted = false")
    List<FileMetadataEntity> findLargeFiles(@Param("sizeThreshold") Long sizeThreshold);
    
    @Query("SELECT u.username, COUNT(f), COALESCE(SUM(f.size), 0) FROM FileMetadataEntity f " +
           "RIGHT JOIN f.userAccount u WHERE f.isDeleted = false OR f.isDeleted IS NULL " +
           "GROUP BY u.id, u.username ORDER BY SUM(f.size) DESC")
    List<Object[]> getUserStorageStatistics();
}
