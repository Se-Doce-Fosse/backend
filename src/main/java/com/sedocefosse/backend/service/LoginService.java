package com.sedocefosse.backend.service;

import com.sedocefosse.backend.configs.exceptions.LoginException;
import com.sedocefosse.backend.configs.exceptions.SignupException;
import com.sedocefosse.backend.configs.security.TokenService;
import com.sedocefosse.backend.model.admin.request.AdminDTO;
import com.sedocefosse.backend.model.admin.request.LoginRequest;
import com.sedocefosse.backend.model.admin.response.auth.AuthResponse;
import com.sedocefosse.backend.repository.AdminRepository;
import com.sedocefosse.backend.repository.model.AdminEntity;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class LoginService {

    private static final String LOGIN_FAILURE_MSG = "Invalid username or password";
    private static final String SIGNUP_FAILURE_MSG = "This user already exists";
    private final AdminRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;

    public AuthResponse login(LoginRequest loginRequest) {
        AdminEntity user = this.repository.findByEmail(loginRequest.getEmail())
                .orElseThrow(() -> new LoginException("User not found"));

        if(passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            String token = this.tokenService.generateToken(user);
            return new AuthResponse(user.getUsername(), token);
        }
        else throw new LoginException(LOGIN_FAILURE_MSG);
    }

    public AuthResponse signup(AdminDTO signupRequest) {
        Optional<AdminEntity> user = this.repository.findByEmail(signupRequest.getEmail());

        if(user.isEmpty()) {
            var newUser = mapToEntity(signupRequest);
            this.repository.save(newUser);

            return new AuthResponse(newUser.getUsername(), "User created");
        }
        else throw new SignupException(SIGNUP_FAILURE_MSG);
    }

    private AdminEntity mapToEntity(AdminDTO dto) {
        return AdminEntity.builder()
                .email(dto.getEmail())
                .password(dto.getPassword())
                .role(dto.getRole())
                .username(dto.getUsername())
                .password(passwordEncoder.encode(dto.getPassword()))
                .build();
    }
}
