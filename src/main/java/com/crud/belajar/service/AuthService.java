package com.crud.belajar.service;

import com.crud.belajar.dto.AuthResponse;
import com.crud.belajar.dto.LoginRequest;
import com.crud.belajar.dto.RegisterRequest;
import com.crud.belajar.model.User;
import com.crud.belajar.repository.UserRepository;
import com.crud.belajar.security.JwtUtil;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    public AuthService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            AuthenticationManager authenticationManager,
            JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
    }

    // 1. Registrasi User Baru
    public AuthResponse register(RegisterRequest request) {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email " + request.getEmail() + " sudah terdaftar!");
        }

        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole("ROLE_USER");

        userRepository.save(user);

        String token = jwtUtil.generateToken(user.getEmail());
        return new AuthResponse(token, user.getEmail(), user.getName(), user.getRole());
    }

    // 2. Login User & Generate JWT
    public AuthResponse login(LoginRequest request) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
            );

            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            User user = userRepository.findByEmail(userDetails.getUsername())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User tidak ditemukan"));

            String token = jwtUtil.generateToken(userDetails);
            return new AuthResponse(token, user.getEmail(), user.getName(), user.getRole());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Email atau password salah!");
        }
    }

    // 3. Ambil data profil user yang sedang login
    public User getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated() || "anonymousUser".equals(auth.getPrincipal())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Anda belum login!");
        }

        String email = auth.getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User tidak ditemukan"));
    }
}

