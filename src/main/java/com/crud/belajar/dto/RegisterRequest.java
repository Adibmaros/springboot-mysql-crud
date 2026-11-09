package com.crud.belajar.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Schema(description = "Data transfer object untuk registrasi user baru")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequest {

    @NotBlank(message = "Nama tidak boleh kosong")
    @Schema(description = "Nama lengkap pengguna", example = "Adib Maros")
    private String name;

    @NotBlank(message = "Email tidak boleh kosong")
    @Email(message = "Format email tidak valid")
    @Schema(description = "Alamat email unik", example = "adib@example.com")
    private String email;

    @NotBlank(message = "Password tidak boleh kosong")
    @Size(min = 6, message = "Password minimal 6 karakter")
    @Schema(description = "Kata sandi akun", example = "password123")
    private String password;
}

