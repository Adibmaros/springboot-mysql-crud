package com.crud.belajar.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Schema(description = "Data transfer object untuk membuat atau memperbarui user")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserRequest {

    @NotBlank(message = "Nama user tidak boleh kosong")
    @Schema(description = "Nama lengkap user", example = "Jane Doe")
    private String name;

    @NotBlank(message = "Email tidak boleh kosong")
    @Email(message = "Format email tidak valid")
    @Schema(description = "Alamat email unik user", example = "jane.doe@example.com")
    private String email;

    @Schema(description = "Kata sandi akun (opsional saat update)", example = "password123")
    private String password;

    @Schema(description = "Role pengguna (default: ROLE_USER)", example = "ROLE_USER")
    private String role;
}
