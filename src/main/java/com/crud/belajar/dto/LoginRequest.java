package com.crud.belajar.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Schema(description = "Data transfer object untuk login user")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequest {

    @NotBlank(message = "Email tidak boleh kosong")
    @Email(message = "Format email tidak valid")
    @Schema(description = "Alamat email terdaftar", example = "adib@example.com")
    private String email;

    @NotBlank(message = "Password tidak boleh kosong")
    @Schema(description = "Kata sandi akun", example = "password123")
    private String password;
}

