# 🚀 springboot-mysql-crud

Proyek ini adalah contoh implementasi **RESTful API CRUD (Create, Read, Update, Delete)** produk yang sederhana, bersih, dan modular menggunakan **Spring Boot** dan **MySQL**, dibangun tanpa autentikasi sebagai pondasi awal belajar *backend development*. 

Proyek ini telah menerapkan standar *best practices* arsitektur berlapis (*layered architecture*), penanganan error global (*global exception handling*), validasi input, paginasi, pencarian, dan *database seeding*.

---

## 🛠️ Tech Stack & Dependencies

* **Java**: Versi 17
* **Framework**: Spring Boot
* **Dependencies**:
  * **Spring Web**: Membangun RESTful API menggunakan arsitektur MVC.
  * **Spring Data JPA**: Mempermudah interaksi database menggunakan Hibernate ORM.
  * **MySQL Driver**: Konektor JDBC untuk database MySQL.
  * **Validation (Hibernate Validator)**: Validasi data payload request menggunakan anotasi standar Java Bean Validation.
  * **Springdoc OpenAPI (Swagger UI)**: Dokumentasi interaktif dan visualisasi RESTful API berbasis browser.
* **Build Tool**: Apache Maven (Maven Wrapper disertakan)

---

## 📁 Struktur Arsitektur Proyek

Kode diorganisasi mengikuti pola *Separation of Concerns* (pemisahan tanggung jawab):

```text
src/main/java/com/crud/belajar/
├── BelajarApplication.java        # Main class aplikasi (@SpringBootApplication, @EnableJpaAuditing)
│
├── model/
│   └── Product.java               # Entity JPA: Pemetaan tabel 'products' & audit timestamp
│
├── repository/
│   └── ProductRepository.java     # Data Access Layer: Query JPA & pencarian (extends JpaRepository)
│
├── dto/
│   ├── ProductRequest.java        # Request Data Transfer Object dengan aturan validasi (@NotBlank, @Min, dll)
│   └── ApiResponse.java           # Format respon API standar & konsisten (success, message, data, timestamp)
│
├── service/
│   └── ProductService.java        # Business Logic Layer: Pengolahan data sebelum disimpan/diambil
│
├── controller/
│   └── ProductController.java     # Presentation Layer: Menangani HTTP Request (GET, POST, PUT, DELETE)
│
├── exception/
│   └── GlobalExceptionHandler.java# Menangani error terpusat (@RestControllerAdvice)
│
└── config/
    └── DataSeeder.java            # Otomatis mengisi data awal jika database masih kosong (CommandLineRunner)
```

---

## ✨ Fitur Utama

1. **Operasi CRUD Lengkap**:
   * Menambahkan data produk baru.
   * Menampilkan semua produk atau detail per ID.
   * Mengubah informasi produk.
   * Menghapus produk.
2. **Validasi Input Ketat**:
   * Nama produk tidak boleh kosong (`@NotBlank`).
   * Harga harus ada dan bernilai positif atau 0 (`@NotNull`, `@PositiveOrZero`).
   * Stok tidak boleh negatif (`@Min(0)`).
3. **Respon JSON Terstandarisasi (`ApiResponse<T>`)**:
   * Semua endpoint mengembalikan format JSON yang konsisten (`success`, `message`, `data`, `timestamp`).
4. **Paginasi & Pengurutan Dinamis (*Pagination & Dynamic Sorting*)**:
   * Mendukung navigasi per halaman (`page`, `size`) dan pengurutan dinamis (`sortBy`, `sortDir`).
5. **Pencarian Nama Produk**:
   * Filter pencarian fleksibel dan *case-insensitive* (`?search=nama`).
6. **Penanganan Error Terpusat (*Global Exception Handling*)**:
   * Menghasilkan pesan error validasi yang ramah pembaca ketika input salah (HTTP 400).
   * Menangani data tidak ditemukan secara elegan (HTTP 404).
7. **JPA Auditing**:
   * Kolom `createdAt` dan `updatedAt` otomatis terisi saat data dibuat atau dimodifikasi.
8. **Automated Data Seeder**:
   * Saat pertama kali dijalankan, sistem otomatis memasukkan 5 data dummy produk jika tabel kosong.

---

## 📋 Prasyarat Sistem

Sebelum menjalankan proyek, pastikan perangkat kamu telah terpasang:
* **Java Development Kit (JDK)**: Versi 17 atau lebih baru (`java -version`).
* **MySQL Server**: Versi 8.0+ (bisa melalui XAMPP, Laragon, MySQL Community Server, atau Docker).

---

## ⚙️ Langkah Instalasi & Menjalankan

### 1. Clone Repositori
```bash
git clone https://github.com/Adibmaros/springboot-mysql-crud.git
cd springboot-mysql-crud
```

### 2. Siapkan Database MySQL
Buka MySQL client (terminal / phpMyAdmin / DBeaver / Workbench) dan jalankan:
```sql
CREATE DATABASE belajar_db;
```

### 3. Konfigurasi `application.properties`
Buka file `src/main/resources/application.properties` dan sesuaikan kredensial MySQL kamu:
```properties
spring.application.name=belajar

# Koneksi Database
spring.datasource.url=jdbc:mysql://localhost:3306/belajar_db?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true
spring.datasource.username=root
spring.datasource.password=isi_password_mysql_kamu_disini

# Hibernate / JPA
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
```

### 4. Jalankan Aplikasi
Gunakan Maven Wrapper bawaan proyek tanpa perlu menginstal Maven secara global:

* **Windows (Command Prompt / PowerShell)**:
  ```bash
  .\mvnw.cmd spring-boot:run
  ```
* **macOS / Linux**:
  ```bash
  chmod +x mvnw
  ./mvnw spring-boot:run
  ```

Aplikasi akan aktif di: `http://localhost:8080`.

---

## 📡 Dokumentasi Endpoint API

| Method | Endpoint | Deskripsi | Query Parameters (Opsional) |
| :--- | :--- | :--- | :--- |
| **GET** | `/api/products` | Ambil semua produk (Paging & Sort) | `page`, `size`, `sortBy`, `sortDir`, `search` |
| **GET** | `/api/products/{id}` | Ambil detail satu produk | - |
| **POST** | `/api/products` | Tambah produk baru | - |
| **PUT** | `/api/products/{id}` | Perbarui data produk | - |
| **DELETE** | `/api/products/{id}` | Hapus data produk | - |

---

## 🧪 Contoh Request & Response

### 1. Tambah Produk Baru (`POST /api/products`)

**Request Body (JSON):**
```json
{
  "name": "Headset Sony WH-1000XM5",
  "price": 4999000.0,
  "stock": 15,
  "description": "Headphone wireless noise-cancelling"
}
```

**Response Sukses (HTTP 201 Created):**
```json
{
  "success": true,
  "message": "Produk berhasil ditambahkan",
  "data": {
    "id": 6,
    "name": "Headset Sony WH-1000XM5",
    "price": 4999000.0,
    "stock": 15,
    "description": "Headphone wireless noise-cancelling",
    "createdAt": "2026-11-08T12:50:00",
    "updatedAt": "2026-11-08T12:50:00"
  },
  "timestamp": "2026-11-08T12:50:00"
}
```

---

### 2. Contoh Respon Error Validasi (HTTP 400 Bad Request)

Jika mengirimkan data tidak valid (misal nama kosong dan harga negatif):
```json
{
  "name": "",
  "price": -1000
}
```

**Response Error:**
```json
{
  "success": false,
  "message": "Validasi data gagal, mohon periksa kembali input Anda",
  "data": {
    "name": "Nama produk tidak boleh kosong",
    "price": "Harga harus bernilai positif atau 0",
    "stock": "Stok tidak boleh kosong"
  },
  "timestamp": "2026-11-08T12:50:05"
}
```

---

### 3. Paginasi & Pengurutan (`GET /api/products`)

* Mengurutkan berdasarkan harga termahal:
  `GET http://localhost:8080/api/products?sortBy=price&sortDir=desc`
* Filter pencarian nama:
  `GET http://localhost:8080/api/products?search=asus`

---

## 📖 Dokumentasi Interaktif (Swagger UI)

Proyek ini telah dilengkapi dengan antarmuka web interaktif **Swagger UI (OpenAPI 3)**. Kamu bisa langsung melihat semua endpoint, skema request/response DTO, serta melakukan uji coba (klik tombol **"Try it out"**) langsung dari web browser tanpa aplikasi tambahan!

* **URL Swagger UI**: `http://localhost:8080/swagger-ui.html`
* **URL OpenAPI JSON**: `http://localhost:8080/api-docs`

---

## 📑 File Pengujian Cepat (`api-test.http`)

Proyek ini telah dilengkapi file pengujian siap pakai: [`api-test.http`](api-test.http).  
Jika kamu menggunakan **Antigravity / VS Code** dengan ekstensi **REST Client**, kamu cukup membuka file tersebut dan menekan tombol **`Send Request`** untuk menguji setiap endpoint secara interaktif tanpa memerlukan Postman.

---

## 🗺️ Roadmap Selanjutnya (Next Steps)

Proyek ini sengaja dibuat tanpa autentikasi sebagai dasar pembelajaran CRUD. Tahap pengembangan berikutnya:
- [x] Dokumentasi Interaktif menggunakan **Swagger / OpenAPI (springdoc-openapi)**.
- [ ] Integrasi Autentikasi & Otorisasi (**Spring Security + JWT Token**).
- [ ] Manajemen Akun & Hak Akses User/Admin (Role-Based Access Control).
- [ ] Penulisan Unit Test dan Integration Test (**JUnit 5 & Mockito / MockMvc**).
- [ ] Containerization menggunakan **Docker & Docker Compose**.

