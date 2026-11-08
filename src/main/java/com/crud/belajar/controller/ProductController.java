package com.crud.belajar.controller;

import com.crud.belajar.dto.ApiResponse;
import com.crud.belajar.dto.ProductRequest;
import com.crud.belajar.model.Product;
import com.crud.belajar.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/products")
@Tag(name = "Product", description = "Endpoint untuk manajemen CRUD data produk")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    // [GET] Ambil data produk (Mendukung Search, Pagination, dan Sorting)
    @GetMapping
    @Operation(summary = "Ambil semua produk", description = "Mengambil data produk dengan dukungan pencarian nama, paginasi, dan pengurutan dinamis.")
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
    @GetMapping("/{id}")
    @Operation(summary = "Ambil detail produk", description = "Mengambil data satu produk berdasarkan ID.")
    public ResponseEntity<ApiResponse<Product>> getProductById(@PathVariable Long id) {
        Product product = productService.getProductById(id);
        return ResponseEntity.ok(ApiResponse.success("Berhasil mengambil detail produk", product));
    }

    // [POST] Tambah produk baru (dengan validasi input)
    @PostMapping
    @Operation(summary = "Tambah produk baru", description = "Menyimpan produk baru ke database dengan validasi nama, harga, dan stok.")
    public ResponseEntity<ApiResponse<Product>> createProduct(@Valid @RequestBody ProductRequest request) {
        Product createdProduct = productService.createProduct(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Produk berhasil ditambahkan", createdProduct));
    }

    // [PUT] Update produk berdasarkan ID
    @PutMapping("/{id}")
    @Operation(summary = "Perbarui produk", description = "Mengubah data produk yang sudah ada berdasarkan ID.")
    public ResponseEntity<ApiResponse<Product>> updateProduct(
            @PathVariable Long id,
            @Valid @RequestBody ProductRequest request) {
        Product updatedProduct = productService.updateProduct(id, request);
        return ResponseEntity.ok(ApiResponse.success("Produk berhasil diperbarui", updatedProduct));
    }

    // [DELETE] Hapus produk berdasarkan ID
    @DeleteMapping("/{id}")
    @Operation(summary = "Hapus produk", description = "Menghapus data produk dari database berdasarkan ID.")
    public ResponseEntity<ApiResponse<Void>> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.ok(ApiResponse.success("Produk dengan ID " + id + " berhasil dihapus", null));
    }
}
