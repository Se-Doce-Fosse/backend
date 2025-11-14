package com.sedocefosse.backend.service.admin;

import com.sedocefosse.backend.model.admin.response.AdminUserResponse;
import com.sedocefosse.backend.repository.admin.AdminRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminUserService {

    private final AdminRepository adminRepository;

    @Transactional(readOnly = true)
    public List<AdminUserResponse> listAllUsers() {
        return adminRepository.findAll()
                .stream()
                .map(AdminUserResponse::fromEntity)
                .toList();
    }
}
