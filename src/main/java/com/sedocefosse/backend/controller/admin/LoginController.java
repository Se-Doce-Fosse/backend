package com.sedocefosse.backend.controller.admin;

import com.sedocefosse.backend.configs.security.TokenService;
import com.sedocefosse.backend.model.admin.request.LoginRequest;
import com.sedocefosse.backend.model.admin.request.AdminDTO;
import com.sedocefosse.backend.model.admin.response.auth.AuthResponse;
import com.sedocefosse.backend.repository.AdminRepository;
import com.sedocefosse.backend.repository.model.AdminEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController("/auth")
@RequiredArgsConstructor
public class LoginController {

    private final AdminRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;

    @GetMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest loginRequest) {
        AdminEntity user = this.repository.findByEmail(loginRequest.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if(passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            String token = this.tokenService.generateToken(user);
            return ResponseEntity.ok(new AuthResponse(user.getUsername(), token));
        }
        return ResponseEntity.badRequest().build();
    }

    @GetMapping("/signup")
    public ResponseEntity<AuthResponse> signup(@RequestBody AdminDTO adminDTO){
        Optional<AdminEntity> user = this.repository.findByEmail(adminDTO.getEmail());

        if(user.isEmpty()) {
            AdminEntity newUser = new AdminEntity();
            newUser.setPassword(passwordEncoder.encode(adminDTO.getPassword()));
            newUser.setEmail(adminDTO.getEmail());
            newUser.setUsername(adminDTO.getUsername());
            this.repository.save(newUser);

            String token = this.tokenService.generateToken(newUser);
            return ResponseEntity.ok(new AuthResponse(newUser.getUsername(), token));
        }
        return ResponseEntity.badRequest().build();
    }
}
