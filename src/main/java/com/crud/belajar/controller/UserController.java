package com.crud.belajar.controller;

import com.crud.belajar.dto.ApiResponse;
import com.crud.belajar.dto.UserRequest;
import com.crud.belajar.model.User;
import com.crud.belajar.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@Tag(name = "User", description = "Endpoint untuk manajemen CRUD data user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // [GET] Ambil semua user
    @GetMapping
    @Operation(summary = "Ambil semua user", description = "Mengambil seluruh data user beserta daftar produk yang dimilikinya.")
    public ResponseEntity<ApiResponse<List<User>>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        return ResponseEntity.ok(ApiResponse.success("Berhasil mengambil data user", users));
    }

    // [GET] Ambil detail user berdasarkan ID
    @GetMapping("/{id}")
    @Operation(summary = "Ambil detail user", description = "Mengambil data satu user beserta daftar produknya berdasarkan ID.")
    public ResponseEntity<ApiResponse<User>> getUserById(@PathVariable Long id) {
        User user = userService.getUserById(id);
        return ResponseEntity.ok(ApiResponse.success("Berhasil mengambil detail user", user));
    }

    // [POST] Tambah user baru
    @PostMapping
    @Operation(summary = "Tambah user baru", description = "Menyimpan user baru ke database.")
    public ResponseEntity<ApiResponse<User>> createUser(@Valid @RequestBody UserRequest request) {
        User createdUser = userService.createUser(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("User berhasil ditambahkan", createdUser));
    }

    // [PUT] Update user berdasarkan ID
    @PutMapping("/{id}")
    @Operation(summary = "Perbarui user", description = "Mengubah nama atau email user berdasarkan ID.")
    public ResponseEntity<ApiResponse<User>> updateUser(
            @PathVariable Long id,
            @Valid @RequestBody UserRequest request) {
        User updatedUser = userService.updateUser(id, request);
        return ResponseEntity.ok(ApiResponse.success("User berhasil diperbarui", updatedUser));
    }

    // [DELETE] Hapus user berdasarkan ID
    @DeleteMapping("/{id}")
    @Operation(summary = "Hapus user", description = "Menghapus data user dan semua produk terkait (cascade) dari database.")
    public ResponseEntity<ApiResponse<Void>> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.ok(ApiResponse.success("User dengan ID " + id + " berhasil dihapus", null));
    }
}
