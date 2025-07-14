package com.vti.cloudvault.service;

import com.vti.cloudvault.dto.UserRegistrationDTO;
import com.vti.cloudvault.dto.UserProfileDTO;
import com.vti.cloudvault.repository.Entity.UserAccountEntity;

import java.util.List;

public interface UserService {
    UserAccountEntity registerUser(UserRegistrationDTO registrationDTO);
    UserAccountEntity findByUsername(String username);
    UserAccountEntity findById(Long id);
    UserProfileDTO getUserProfile(Long userId);
    List<UserProfileDTO> getAllUsers();
    UserAccountEntity updateUserQuota(Long userId, Long newQuota);
    UserAccountEntity toggleUserStatus(Long userId);
    void deleteUser(Long userId);
    String createUserFolderOnDrive(String username);
    void updateUserStorageUsage(Long userId, Long additionalSize);
}
