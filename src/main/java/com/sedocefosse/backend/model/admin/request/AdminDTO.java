package com.sedocefosse.backend.model.admin.request;

import com.sedocefosse.backend.repository.model.AdminEntity;
import com.sedocefosse.backend.utils.ConstrainsMessages;
import com.sedocefosse.backend.utils.RoleEnum;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdminDTO {

    @NotBlank(message = ConstrainsMessages.USERNAME_INVALID)
    private String username;

    @Email(message = ConstrainsMessages.EMAIL_INVALID)
    private String email;

    @NotNull(message = ConstrainsMessages.ROLE_INVALID)
    private RoleEnum role;

    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$", message = ConstrainsMessages.PASSWORD_INVALID)
    private String password;

    public static AdminDTO fromEntity(AdminEntity entity) {
        return AdminDTO.builder()
                .username(entity.getUsername())
                .email(entity.getEmail())
                .role(entity.getRole())
                .password(entity.getPassword())
                .build();
    }
}
