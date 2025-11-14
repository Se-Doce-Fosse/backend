package com.sedocefosse.backend.model.admin.response;

import com.sedocefosse.backend.model.admin.AdminEntity;
import com.sedocefosse.backend.utils.RoleEnum;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class AdminUserResponse {
    Long id;
    String username;
    String email;
    RoleEnum role;

    public static AdminUserResponse fromEntity(AdminEntity entity) {
        return AdminUserResponse.builder()
                .id(entity.getId())
                .username(entity.getUsername())
                .email(entity.getEmail())
                .role(entity.getRole())
                .build();
    }
}
