package com.crud.belajar.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

public class ProductRequest {

    @NotBlank(message = "Nama produk tidak boleh kosong")
    private String name;

    @NotNull(message = "Harga tidak boleh kosong")
    @PositiveOrZero(message = "Harga harus bernilai positif atau 0")
    private Double price;

    @NotNull(message = "Stok tidak boleh kosong")
    @Min(value = 0, message = "Stok minimal 0")
    private Integer stock;

    private String description;

    public ProductRequest() {
    }

    public ProductRequest(String name, Double price, Integer stock, String description) {
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}

