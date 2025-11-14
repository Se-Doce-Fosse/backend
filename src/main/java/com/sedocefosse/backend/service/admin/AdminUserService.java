package com.sedocefosse.backend.service.admin;

import com.sedocefosse.backend.model.admin.request.UpdateAdminUserRequest;
import com.sedocefosse.backend.model.admin.response.AdminUserResponse;
import com.sedocefosse.backend.repository.admin.AdminRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AdminUserService {

    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional(readOnly = true)
    public List<AdminUserResponse> listAllUsers() {
        return adminRepository.findAll()
                .stream()
                .map(AdminUserResponse::fromEntity)
                .toList();
    }

    @Transactional
    public Optional<AdminUserResponse> updateUser(Long userId, UpdateAdminUserRequest request) {
        return adminRepository.findById(userId)
                .map(user -> {
                    user.setUsername(request.getUsername());
                    user.setEmail(request.getEmail());
                    user.setRole(request.getRole());
                    if (request.getPassword() != null && !request.getPassword().isBlank()) {
                        user.setPassword(passwordEncoder.encode(request.getPassword()));
                    }
                    return AdminUserResponse.fromEntity(adminRepository.save(user));
                });
    }

    @Transactional
    public boolean deleteUser(Long userId) {
        if (!adminRepository.existsById(userId)) {
            return false;
        }
        adminRepository.deleteById(userId);
        return true;
    }
}
