package com.crud.belajar.controller;

import com.crud.belajar.dto.ApiResponse;
import com.crud.belajar.dto.AuthResponse;
import com.crud.belajar.dto.LoginRequest;
import com.crud.belajar.dto.RegisterRequest;
import com.crud.belajar.model.User;
import com.crud.belajar.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Authentication", description = "Endpoint untuk registrasi, login, dan autentikasi token JWT")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    @Operation(summary = "Registrasi Akun Baru", description = "Mendaftarkan user baru dengan kata sandi terenkripsi BCrypt dan mengembalikan token JWT.")
    public ResponseEntity<ApiResponse<AuthResponse>> register(@Valid @RequestBody RegisterRequest request) {
        AuthResponse response = authService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Registrasi akun berhasil!", response));
    }

    @PostMapping("/login")
    @Operation(summary = "Login Pengguna", description = "Autentikasi email & password untuk memperoleh token JWT.")
    public ResponseEntity<ApiResponse<AuthResponse>> login(@Valid @RequestBody LoginRequest request) {
        AuthResponse response = authService.login(request);
        return ResponseEntity.ok(ApiResponse.success("Login berhasil!", response));
    }

    @GetMapping("/me")
    @Operation(summary = "Profil Pengguna Sedang Login", description = "Mengambil detail profil akun yang sedang login dari header Authorization Bearer Token.")
    public ResponseEntity<ApiResponse<User>> getCurrentUser() {
        User user = authService.getCurrentUser();
        return ResponseEntity.ok(ApiResponse.success("Berhasil mengambil data profil user", user));
    }
}

