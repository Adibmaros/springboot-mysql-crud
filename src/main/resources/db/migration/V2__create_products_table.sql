-- ========================================================
-- Migrasi V2: Pembuatan Tabel 'products'
-- Menyimpan data produk dan relasi Many-to-One ke tabel 'users'
-- ========================================================

CREATE TABLE IF NOT EXISTS products (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    price DOUBLE NOT NULL,
    stock INT,
    description VARCHAR(255),
    user_id BIGINT,
    created_at DATETIME NOT NULL,
    updated_at DATETIME NOT NULL,
    CONSTRAINT fk_products_user FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
);

