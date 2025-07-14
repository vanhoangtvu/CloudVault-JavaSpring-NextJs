package com.vti.cloudvault.repository;

import com.vti.cloudvault.repository.Entity.UserAccountEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserAccountRepository extends JpaRepository<UserAccountEntity, Long> {
    
    Optional<UserAccountEntity> findByUsername(String username);
    
    Optional<UserAccountEntity> findByEmail(String email);
    
    boolean existsByUsername(String username);
    
    boolean existsByEmail(String email);
    
    List<UserAccountEntity> findByRole(UserAccountEntity.Role role);
    
    List<UserAccountEntity> findByIsActiveTrue();
    
    @Query("SELECT u FROM UserAccountEntity u WHERE u.role = 'USER' ORDER BY u.usedStorage DESC")
    List<UserAccountEntity> findUsersOrderByStorageUsage();
    
    @Query("SELECT SUM(u.usedStorage) FROM UserAccountEntity u WHERE u.role = 'USER'")
    Long getTotalUsedStorage();
    
    @Query("SELECT COUNT(u) FROM UserAccountEntity u WHERE u.role = 'USER' AND u.isActive = true")
    Long getActiveUserCount();
    
    @Query("SELECT u FROM UserAccountEntity u WHERE u.usedStorage > :threshold")
    List<UserAccountEntity> findUsersExceedingStorageThreshold(@Param("threshold") Long threshold);
}

