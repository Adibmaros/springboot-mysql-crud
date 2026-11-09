package com.crud.belajar.config;

import com.crud.belajar.model.Product;
import com.crud.belajar.model.User;
import com.crud.belajar.repository.ProductRepository;
import com.crud.belajar.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DataSeeder implements CommandLineRunner {

    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public DataSeeder(
            ProductRepository productRepository,
            UserRepository userRepository,
            PasswordEncoder passwordEncoder) {
        this.productRepository = productRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        // 1. Buat user dummy jika tabel users masih kosong
        User defaultUser;
        if (userRepository.count() == 0) {
            defaultUser = new User(
                    "John Doe",
                    "john.doe@example.com",
                    passwordEncoder.encode("password123"),
                    "ROLE_ADMIN"
            );
            defaultUser = userRepository.save(defaultUser);
            System.out.println(">> [DataSeeder] 1 data user dummy berhasil dimasukkan (email: john.doe@example.com / password: password123)");
        } else {
            defaultUser = userRepository.findAll().get(0);
        }

        // 2. Isi data dummy produk dan hubungkan ke defaultUser
        if (productRepository.count() == 0) {
            List<Product> dummyProducts = List.of(
                    new Product("Laptop Asus TUF Gaming", 14500000.0, 15, "Laptop gaming dengan prosesor Ryzen 7 dan RTX 3050", defaultUser),
                    new Product("MacBook Air M2", 16999000.0, 8, "Laptop Apple ultra-tipis dengan chip M2", defaultUser),
                    new Product("Mouse Wireless Logitech M330", 250000.0, 50, "Mouse silent click ergonomis", defaultUser),
                    new Product("Keyboard Mechanical Keychron K2", 1250000.0, 20, "Keyboard mechanical wireless 75% layout", defaultUser),
                    new Product("Monitor LG 24 Inch IPS", 1850000.0, 12, "Monitor Full HD 75Hz bezel-less", defaultUser)
            );

            productRepository.saveAll(dummyProducts);
            System.out.println(">> [DataSeeder] 5 data produk dummy berhasil dimasukkan ke database dan terhubung ke user: " + defaultUser.getName());
        }
    }
}
