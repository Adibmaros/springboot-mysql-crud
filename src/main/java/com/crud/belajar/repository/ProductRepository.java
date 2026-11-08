package com.crud.belajar.repository;

import com.crud.belajar.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    // Cari berdasarkan nama (tanpa paging)
    List<Product> findByNameContainingIgnoreCase(String name);

    // Cari berdasarkan nama dengan Pagination & Sorting
    Page<Product> findByNameContainingIgnoreCase(String name, Pageable pageable);
}
