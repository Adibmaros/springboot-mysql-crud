package com.crud.belajar.service;

import com.crud.belajar.dto.UserRequest;
import com.crud.belajar.model.User;
import com.crud.belajar.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // 1. Ambil semua user
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // 2. Ambil user berdasarkan ID
    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User dengan ID " + id + " tidak ditemukan"));
    }

    // 3. Tambah user baru
    public User createUser(UserRequest request) {
        userRepository.findByEmail(request.getEmail()).ifPresent(u -> {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email " + request.getEmail() + " sudah terdaftar");
        });

        String rawPassword = (request.getPassword() != null && !request.getPassword().isBlank()) 
                ? request.getPassword() 
                : "password123";
        String encodedPassword = passwordEncoder.encode(rawPassword);

        String role = (request.getRole() != null && !request.getRole().isBlank()) 
                ? request.getRole() 
                : "ROLE_USER";

        User user = new User(request.getName(), request.getEmail(), encodedPassword, role);
        return userRepository.save(user);
    }

    // 4. Update data user
    public User updateUser(Long id, UserRequest request) {
        User user = getUserById(id);

        if (!user.getEmail().equalsIgnoreCase(request.getEmail())) {
            userRepository.findByEmail(request.getEmail()).ifPresent(u -> {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email " + request.getEmail() + " sudah digunakan user lain");
            });
        }

        user.setName(request.getName());
        user.setEmail(request.getEmail());

        if (request.getPassword() != null && !request.getPassword().isBlank()) {
            user.setPassword(passwordEncoder.encode(request.getPassword()));
        }

        if (request.getRole() != null && !request.getRole().isBlank()) {
            user.setRole(request.getRole());
        }

        return userRepository.save(user);
    }

    // 5. Hapus user (akan cascade menghapus produk milik user terkait)
    public void deleteUser(Long id) {
        User user = getUserById(id);
        userRepository.delete(user);
    }
}
