package com.crud.belajar.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Schema(description = "Data transfer object untuk membuat atau memperbarui produk")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductRequest {

    @NotBlank(message = "Nama produk tidak boleh kosong")
    @Schema(description = "Nama produk", example = "MacBook Air M2")
    private String name;

    @NotNull(message = "Harga tidak boleh kosong")
    @PositiveOrZero(message = "Harga harus bernilai positif atau 0")
    @Schema(description = "Harga produk dalam Rupiah", example = "16999000.0")
    private Double price;

    @NotNull(message = "Stok tidak boleh kosong")
    @Min(value = 0, message = "Stok minimal 0")
    @Schema(description = "Jumlah stok produk", example = "10")
    private Integer stock;

    @Schema(description = "Deskripsi detail produk", example = "Laptop Apple ultra-tipis dengan chip M2")
    private String description;

    @Schema(description = "ID User pemilik/penjual produk (opsional)", example = "1")
    private Long userId;

    public ProductRequest(String name, Double price, Integer stock, String description) {
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.description = description;
    }
}
