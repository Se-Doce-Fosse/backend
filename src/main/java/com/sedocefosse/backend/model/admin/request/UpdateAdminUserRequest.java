package com.sedocefosse.backend.model.admin.request;

import com.sedocefosse.backend.utils.RoleEnum;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UpdateAdminUserRequest {

    @NotBlank
    private String username;

    @Email
    @NotBlank
    private String email;

    @NotNull
    private RoleEnum role;

    private String password;
}
