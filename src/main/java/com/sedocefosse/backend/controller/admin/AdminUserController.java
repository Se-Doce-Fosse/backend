package com.sedocefosse.backend.controller.admin;

import com.sedocefosse.backend.model.admin.request.UpdateAdminUserRequest;
import com.sedocefosse.backend.model.admin.response.AdminUserResponse;
import com.sedocefosse.backend.service.admin.AdminUserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/users")
@RequiredArgsConstructor
public class AdminUserController {

    private final AdminUserService adminUserService;

    @GetMapping
    public ResponseEntity<List<AdminUserResponse>> listUsers() {
        return ResponseEntity.ok(adminUserService.listAllUsers());
    }

    @PutMapping("/{userId}")
    public ResponseEntity<AdminUserResponse> updateUser(
            @PathVariable Long userId,
            @Valid @RequestBody UpdateAdminUserRequest request
    ) {
        return adminUserService.updateUser(userId, request)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long userId) {
        boolean deleted = adminUserService.deleteUser(userId);
        if (!deleted) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.noContent().build();
    }
}
