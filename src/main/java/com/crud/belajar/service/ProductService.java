package com.crud.belajar.service;

import com.crud.belajar.dto.ProductRequest;
import com.crud.belajar.model.Product;
import com.crud.belajar.repository.ProductRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    // Dependency Injection via Constructor
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    // 1. Ambil data produk dengan fitur Pagination, Sorting, dan Search
    public Page<Product> getProducts(String search, int page, int size, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);

        if (search != null && !search.trim().isEmpty()) {
            return productRepository.findByNameContainingIgnoreCase(search, pageable);
        }
        return productRepository.findAll(pageable);
    }

    // 2. Ambil 1 produk berdasarkan ID
    public Product getProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Produk dengan ID " + id + " tidak ditemukan"));
    }

    // 3. Tambah produk baru
    public Product createProduct(ProductRequest request) {
        Product product = new Product();
        product.setName(request.getName());
        product.setPrice(request.getPrice());
        product.setStock(request.getStock());
        product.setDescription(request.getDescription());

        return productRepository.save(product);
    }

    // 4. Update data produk
    public Product updateProduct(Long id, ProductRequest request) {
        Product product = getProductById(id);

        product.setName(request.getName());
        product.setPrice(request.getPrice());
        product.setStock(request.getStock());
        product.setDescription(request.getDescription());

        return productRepository.save(product);
    }

    // 5. Hapus produk
    public void deleteProduct(Long id) {
        Product product = getProductById(id);
        productRepository.delete(product);
    }
}

