package com.crud.belajar.controller;

import com.crud.belajar.dto.ApiResponse;
import com.crud.belajar.dto.ProductRequest;
import com.crud.belajar.model.Product;
import com.crud.belajar.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    // [GET] Ambil data produk (Mendukung Search, Pagination, dan Sorting)
    // Contoh pemanggilan:
    // - GET http://localhost:8080/api/products
    // - GET http://localhost:8080/api/products?search=asus
    // - GET http://localhost:8080/api/products?page=0&size=5&sortBy=price&sortDir=desc
    @GetMapping
    public ResponseEntity<ApiResponse<Page<Product>>> getProducts(
            @RequestParam(required = false) String search,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        Page<Product> products = productService.getProducts(search, page, size, sortBy, sortDir);
        return ResponseEntity.ok(ApiResponse.success("Berhasil mengambil data produk", products));
    }

    // [GET] Ambil detail produk berdasarkan ID
    // Endpoint: GET http://localhost:8080/api/products/{id}
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Product>> getProductById(@PathVariable Long id) {
        Product product = productService.getProductById(id);
        return ResponseEntity.ok(ApiResponse.success("Berhasil mengambil detail produk", product));
    }

    // [POST] Tambah produk baru (dengan validasi input)
    // Endpoint: POST http://localhost:8080/api/products
    @PostMapping
    public ResponseEntity<ApiResponse<Product>> createProduct(@Valid @RequestBody ProductRequest request) {
        Product createdProduct = productService.createProduct(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Produk berhasil ditambahkan", createdProduct));
    }

    // [PUT] Update produk berdasarkan ID
    // Endpoint: PUT http://localhost:8080/api/products/{id}
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Product>> updateProduct(
            @PathVariable Long id,
            @Valid @RequestBody ProductRequest request) {
        Product updatedProduct = productService.updateProduct(id, request);
        return ResponseEntity.ok(ApiResponse.success("Produk berhasil diperbarui", updatedProduct));
    }

    // [DELETE] Hapus produk berdasarkan ID
    // Endpoint: DELETE http://localhost:8080/api/products/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.ok(ApiResponse.success("Produk dengan ID " + id + " berhasil dihapus", null));
    }
}
