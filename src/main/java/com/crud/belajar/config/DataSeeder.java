package com.crud.belajar.config;

import com.crud.belajar.model.Product;
import com.crud.belajar.repository.ProductRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DataSeeder implements CommandLineRunner {

    private final ProductRepository productRepository;

    public DataSeeder(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public void run(String... args) {
        // Hanya isi data dummy jika tabel masih kosong
        if (productRepository.count() == 0) {
            List<Product> dummyProducts = List.of(
                    new Product("Laptop Asus TUF Gaming", 14500000.0, 15, "Laptop gaming dengan prosesor Ryzen 7 dan RTX 3050"),
                    new Product("MacBook Air M2", 16999000.0, 8, "Laptop Apple ultra-tipis dengan chip M2"),
                    new Product("Mouse Wireless Logitech M330", 250000.0, 50, "Mouse silent click ergonomis"),
                    new Product("Keyboard Mechanical Keychron K2", 1250000.0, 20, "Keyboard mechanical wireless 75% layout"),
                    new Product("Monitor LG 24 Inch IPS", 1850000.0, 12, "Monitor Full HD 75Hz bezel-less")
            );

            productRepository.saveAll(dummyProducts);
            System.out.println(">> [DataSeeder] 5 data produk dummy berhasil dimasukkan ke database!");
        }
    }
}

