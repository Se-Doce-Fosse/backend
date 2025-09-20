package com.sedocefosse.backend.model.admin.request;

import com.sedocefosse.backend.utils.ConstrainsMessages;
import com.sedocefosse.backend.utils.RoleEnum;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequest {

    @Email(message = ConstrainsMessages.EMAIL_INVALID)
    private String email;

    private String password;
}
