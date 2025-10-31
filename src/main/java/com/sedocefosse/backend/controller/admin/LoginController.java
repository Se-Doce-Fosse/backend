package com.sedocefosse.backend.controller.admin;

import com.sedocefosse.backend.configs.exceptions.LoginException;
import com.sedocefosse.backend.model.admin.request.LoginRequest;
import com.sedocefosse.backend.model.admin.request.AdminDTO;
import com.sedocefosse.backend.model.admin.response.auth.AuthResponse;
import com.sedocefosse.backend.service.login.LoginService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class LoginController {

    private final LoginService loginService;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
        try{
            return ResponseEntity.ok(loginService.login(loginRequest));
        } catch (LoginException e) {
            throw new LoginException(e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @PostMapping("/signup")
    public ResponseEntity<AuthResponse> signup(@Valid @RequestBody AdminDTO adminDTO){
        try{
            return ResponseEntity.ok(loginService.signup(adminDTO));
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
