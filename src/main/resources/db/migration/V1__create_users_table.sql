-- ========================================================
-- Migrasi V1: Pembuatan Tabel 'users'
-- Menyimpan data akun pengguna, password terenkripsi, dan role
-- ========================================================

CREATE TABLE IF NOT EXISTS users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(50) NOT NULL DEFAULT 'ROLE_USER',
    created_at DATETIME NOT NULL,
    updated_at DATETIME NOT NULL
);
