package com.crud.belajar.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Schema(description = "Respon autentikasi yang memuat token JWT")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponse {

    @Schema(description = "JSON Web Token (JWT)", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
    private String token;

    @Schema(description = "Tipe Token", example = "Bearer")
    private String tokenType = "Bearer";

    @Schema(description = "Email user", example = "adib@example.com")
    private String email;

    @Schema(description = "Nama user", example = "Adib Maros")
    private String name;

    @Schema(description = "Peran / Role user", example = "ROLE_USER")
    private String role;

    public AuthResponse(String token, String email, String name, String role) {
        this.token = token;
        this.tokenType = "Bearer";
        this.email = email;
        this.name = name;
        this.role = role;
    }
}

