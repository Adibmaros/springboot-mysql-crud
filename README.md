# 🚀 springboot-mysql-crud

Proyek ini adalah implementasi **RESTful API CRUD (Create, Read, Update, Delete) Produk dan Manajemen User** yang bersih, aman, dan modular menggunakan **Spring Boot**, **Spring Security**, **JWT (JSON Web Token)**, dan **MySQL**.

Proyek ini telah menerapkan standar *best practices* arsitektur berlapis (*layered architecture*), autentikasi berbasis token (*stateless JWT*), proteksi endpoint berbasis peran, relasi entitas basis data (*One-to-Many*), penanganan error global (*global exception handling*), validasi input, paginasi, pencarian, dan *database seeding*.

---

## 🛠️ Tech Stack & Dependencies

Proyek ini dibangun menggunakan **Java 17** dan ekosistem **Spring Boot** dengan deretan dependensi produksi berikut:

| Dependensi (Artifact) | Kategori | Fungsi & Peran dalam Aplikasi |
| :--- | :--- | :--- |
| **`spring-boot-starter-webmvc`** | Core Web Framework | Membangun RESTful API berbasis arsitektur MVC, menangani HTTP Request/Response, routing URL, dan menyediakan Embedded Apache Tomcat Server. |
| **`spring-boot-starter-data-jpa`** | Database / ORM | Mengelola persistensi data dan interaksi database tanpa query manual melalui Hibernate ORM dan interface `JpaRepository`. |
| **`spring-boot-starter-security`** | Keamanan / Auth | Kerangka keamanan terpusat untuk autentikasi user, proteksi endpoint, enkripsi password (BCrypt), dan pengaturan filter request. |
| **`jjwt-api` / `jjwt-impl` / `jjwt-jackson`** | Keamanan / Token | Pustaka pembuatan, penandatanganan digital (*digital signature* HMAC-SHA256), dan validasi token stateless JSON Web Token (JWT). |
| **`mysql-connector-j`** | Driver Database | Driver JDBC resmi MySQL untuk menjembatani komunikasi data antara aplikasi Java dengan server MySQL (port 3306). |
| **`flyway-core` & `flyway-mysql`** | Database Migration | Manajemen versi struktur database (*Database Migration / Version Control*). Menjalankan skrip SQL berurut (`V1__`, `V2__`) dan melacak riwayat perubahan skema. |
| **`spring-boot-starter-validation`** | Validasi Input | Memvalidasi integritas data payload request JSON menggunakan anotasi standar Java Bean Validation (`@NotBlank`, `@Email`, `@Min`, `@NotNull`). |
| **`springdoc-openapi-starter-webmvc-ui`** | Dokumentasi API | Meng-generate visualisasi dokumentasi interaktif Swagger UI (OpenAPI 3) berbasis browser dengan dukungan *Authorize Bearer JWT*. |
| **`spring-boot-starter-actuator`** | Observability / Monitoring | Menyediakan endpoint bawaan untuk memantau kesehatan server (`/health`), koneksi database MySQL, metrik penggunaan memori JVM, dan status aplikasi. |
| **`spring-boot-devtools`** | Developer Productivity | Meningkatkan kenyamanan *development* dengan fitur *Automatic Restart* (LiveReload) ketika berkas kode Java disimpan (`Ctrl + S`). |
| **`lombok`** | Developer Tool | Menghilangkan penulisan kode berulang (*boilerplate code*) dengan men-generate otomatis getter, setter, dan constructor pada tahap kompilasi. |
| **`spring-boot-starter-test`** | Pengujian | Framework pengujian unit dan integrasi (JUnit 5, Mockito, Spring TestContext). |

---

## 📁 Struktur Arsitektur Proyek

Kode diorganisasi mengikuti pola *Separation of Concerns* (pemisahan tanggung jawab):

```text
src/main/java/com/crud/belajar/
├── BelajarApplication.java        # Main class aplikasi (@SpringBootApplication, @EnableJpaAuditing)
│
├── model/
│   ├── User.java                  # Entity JPA: Tabel 'users' (password BCrypt, role, relasi @OneToMany)
│   └── Product.java               # Entity JPA: Tabel 'products' & relasi @ManyToOne ke User
│
├── repository/
│   ├── UserRepository.java        # Data Access Layer untuk User
│   └── ProductRepository.java     # Data Access Layer: Query JPA & pencarian (extends JpaRepository)
│
├── security/
│   ├── SecurityConfig.java        # Konfigurasi filter security, CORS/CSRF disable, Stateless Session
│   ├── JwtUtil.java               # Helper pembangkitan, ekstraksi, dan validasi token JWT
│   ├── JwtAuthenticationFilter.java# Filter pencegat request untuk validasi Bearer Token
│   ├── JwtAuthenticationEntryPoint.java# Handler kustom respon JSON 401 Unauthorized
│   └── CustomUserDetailsService.java# Adaptor UserDetailsService untuk autentikasi User JPA
│
├── dto/
│   ├── RegisterRequest.java       # Request DTO untuk registrasi akun baru
│   ├── LoginRequest.java          # Request DTO untuk login user
│   ├── AuthResponse.java          # Response DTO berisi token JWT dan info akun
│   ├── UserRequest.java           # Request DTO untuk manajemen User
│   ├── ProductRequest.java        # Request DTO untuk produk (@NotBlank, @Min, userId)
│   └── ApiResponse.java           # Format respon API standar & konsisten
│
├── service/
│   ├── AuthService.java           # Business Logic: Registrasi, login, dan validasi sesi
│   ├── UserService.java           # Business Logic: Pengolahan data User & hash password
│   └── ProductService.java        # Business Logic: Pengolahan data Produk
│
├── controller/
│   ├── AuthController.java        # Endpoint Autentikasi (/api/auth/register, /login, /me)
│   ├── UserController.java        # Endpoint CRUD User (/api/users)
│   └── ProductController.java     # Endpoint CRUD Produk (/api/products)
│
├── exception/
│   └── GlobalExceptionHandler.java# Menangani error terpusat (@RestControllerAdvice)
│
└── config/
    ├── OpenApiConfig.java         # Konfigurasi Swagger UI dengan tombol "Authorize" Bearer JWT
    └── DataSeeder.java            # Otomatis mengisi akun admin & 5 produk dummy jika database kosong

src/main/resources/
├── application.properties         # Konfigurasi MySQL, Hibernate (validate), Flyway, dan JWT
└── db/migration/
    ├── V1__create_users_table.sql    # Skrip SQL Flyway: pembuatan tabel 'users'
    └── V2__create_products_table.sql # Skrip SQL Flyway: pembuatan tabel 'products' & foreign key user_id
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
   * Saat pertama kali dijalankan, sistem otomatis memasukkan 1 akun admin default (`john.doe@example.com` / `password123`) dan 5 data dummy produk yang saling terhubung jika database kosong.
9. **Relasi Database JPA (One-to-Many & Many-to-One)**:
   * Mengintegrasikan relasi antara entitas `User` dan `Product` (1 User dapat memiliki banyak Produk) menggunakan foreign key `user_id`.
10. **Autentikasi & Otorisasi (Spring Security + JWT)**:
   * Mengamankan endpoint write/update/delete menggunakan token JSON Web Token (`Bearer <token>`).
   * Password di-hash menggunakan algoritma **BCrypt**.
   * Endpoint katalog produk (GET) tetap terbuka untuk publik.
   * Swagger UI dilengkapi tombol **Authorize** untuk menguji endpoint ber-token langsung dari browser.
11. **Database Migration Berversi (Flyway)**:
   * Struktur dan perubahan skema database dikelola melalui skrip SQL terversi (`V1__...`, `V2__...`).
   * Hibernate disetel ke mode `validate`, mencegah manipulasi skema liar dan memastikan kepatuhan standar *production*.
12. **Monitoring Kesehatan & Metrik Sistem (Spring Boot Actuator)**:
   * Endpoint siap pakai untuk memeriksa status kesehatan server & MySQL (`/actuator/health`) serta metrik penggunaan memori JVM (`/actuator/metrics`).
13. **Pengalaman Developer Cepat (Spring Boot DevTools)**:
   * Fitur *LiveReload* dan *Automatic Restart* saat kode diubah tanpa mematikan server secara manual.

---

## 🔑 Akun Default (Seeder)

Setelah aplikasi pertama kali dijalankan, kamu dapat langsung login menggunakan akun seeder berikut:
* **Email**: `john.doe@example.com`
* **Password**: `password123`
* **Role**: `ROLE_ADMIN`

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

# Konfigurasi JWT (JSON Web Token)
jwt.secret=belajarCrudSpringBootSecretKeyMustBeAtLeast256BitsLongForHMACSHA256Security1234567890
jwt.expiration-ms=86400000
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

### 1. Autentikasi (`/api/auth`) — Publik
| Method | Endpoint | Deskripsi | Akses |
| :--- | :--- | :--- | :--- |
| **POST** | `/api/auth/register` | Mendaftarkan akun user baru & langsung mendapatkan token JWT | Publik |
| **POST** | `/api/auth/login` | Login menggunakan email & password untuk memperoleh token JWT | Publik |
| **GET** | `/api/auth/me` | Mengambil detail profil user yang sedang aktif berdasarkan token | Bearer Token |

### 2. Manajemen Produk (`/api/products`)
| Method | Endpoint | Deskripsi | Akses |
| :--- | :--- | :--- | :--- |
| **GET** | `/api/products` | Ambil semua produk (Mendukung `page`, `size`, `sortBy`, `sortDir`, `search`) | Publik |
| **GET** | `/api/products/{id}` | Ambil detail satu produk | Publik |
| **POST** | `/api/products` | Tambah produk baru (bisa menautkan `userId`) | Bearer Token |
| **PUT** | `/api/products/{id}` | Perbarui data produk | Bearer Token |
| **DELETE** | `/api/products/{id}` | Hapus data produk | Bearer Token |

### 3. Manajemen User (`/api/users`) — Terproteksi
| Method | Endpoint | Deskripsi | Akses |
| :--- | :--- | :--- | :--- |
| **GET** | `/api/users` | Ambil semua user beserta daftar produk miliknya | Bearer Token |
| **GET** | `/api/users/{id}` | Ambil detail satu user dan daftar produknya | Bearer Token |
| **POST** | `/api/users` | Tambah user baru | Bearer Token |
| **PUT** | `/api/users/{id}` | Perbarui data nama/email user | Bearer Token |
| **DELETE** | `/api/users/{id}` | Hapus user (otomatis menghapus produk miliknya via *cascade*) | Bearer Token |

### 4. Monitoring Server (`/actuator`) — Publik / Observability
| Method | Endpoint | Deskripsi | Akses |
| :--- | :--- | :--- | :--- |
| **GET** | `/actuator/health` | Cek status kesehatan aplikasi & database MySQL (UP / DOWN) | Publik |
| **GET** | `/actuator/info` | Informasi lingkungan aplikasi | Publik |
| **GET** | `/actuator/metrics` | Daftar metrik sistem yang dapat dipantau | Publik |
| **GET** | `/actuator/metrics/{name}` | Cek metrik spesifik (misal `/actuator/metrics/jvm.memory.used`) | Publik |

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

Proyek ini telah dilengkapi dengan antarmuka web interaktif **Swagger UI (OpenAPI 3)** dengan fitur **Authorize Bearer JWT**. Kamu bisa langsung melihat semua endpoint, skema request/response DTO, serta melakukan uji coba langsung dari browser:

* **URL Swagger UI**: `http://localhost:8080/swagger-ui.html`
* **URL OpenAPI JSON**: `http://localhost:8080/api-docs`

> **💡 Cara Otorisasi di Swagger UI:**
> 1. Panggil endpoint `POST /api/auth/login` (atau `/register`) di Swagger UI.
> 2. Salin nilai string `token` dari hasil respon JSON.
> 3. Klik tombol hijau **Authorize 🔓** di pojok kanan atas Swagger UI.
> 4. Tempel token ke dalam kotak input (tanpa perlu mengetik kata `Bearer `) lalu klik **Authorize**.
> 5. Semua endpoint bertanda gembok kini bisa kamu uji langsung!

---

## 📑 File Pengujian Cepat (`api-test.http`)

Proyek ini telah dilengkapi file pengujian siap pakai: [`api-test.http`](api-test.http).  
Jika kamu menggunakan **Antigravity / VS Code** dengan ekstensi **REST Client**, kamu cukup membuka file tersebut dan menekan tombol **`Send Request`** untuk menguji setiap endpoint secara interaktif (termasuk penyimpanan token otomatis dari response login).

---

## 🗺️ Status Pengembangan (Roadmap)

> 💡 **Panduan Bedah Internal Dependensi**: Untuk memahami cara kerja di balik layar (*under the hood*) dari setiap dependensi beserta komparasi konsepnya dengan Golang, silakan baca: **[`ROADMAP_DEEP_DIVE_DEPENDENCIES.md`](ROADMAP_DEEP_DIVE_DEPENDENCIES.md)**.

### ✅ Milestone Selesai (Completed)
- [x] **Dokumentasi Interaktif**: Integrasi Swagger UI (OpenAPI 3) dengan fitur *Authorize Bearer JWT*.
- [x] **Relasi Antar Tabel Basis Data**: Pemetaan *One-to-Many* dan *Many-to-One* antara entitas `User` dan `Product`.
- [x] **Autentikasi & Otorisasi**: Implementasi Spring Security 6 dan stateless JSON Web Token (JWT).
- [x] **Pengurangan Boilerplate Code**: Pemanfaatan Project Lombok (`@Getter`, `@Setter`, `@NoArgsConstructor`).
- [x] **Database Migration Berversi**: Manajemen skema database menggunakan Flyway (`V1__`, `V2__`) dan Hibernate mode `validate`.
- [x] **Observability & Health Monitoring**: Pemantauan kesehatan server dan metrik memori menggunakan Spring Boot Actuator (`/health`, `/metrics`).
- [x] **Developer Productivity**: Fitur auto-restart dan LiveReload menggunakan Spring Boot DevTools.

### 🚀 Roadmap Selanjutnya (Next Milestones)
1. **Tahap 1: Pengujian Otomatis (Automated Testing)**
   - [ ] Unit Test pada lapisan Service menggunakan **JUnit 5** dan **Mockito**.
   - [ ] Integration Test pada endpoint Controller menggunakan **MockMvc** dan `@WithMockUser`.
2. **Tahap 2: Kontainerisasi & Lingkungan Mandiri (Docker & Docker Compose)**
   - [ ] Pembuatan `Dockerfile` multi-stage untuk image Spring Boot yang efisien.
   - [ ] Pembuatan `docker-compose.yml` untuk orkestrasi otomatis Spring Boot + MySQL.
3. **Tahap 3: Caching, Performa & Otomasi CI/CD**
   - [ ] Implementasi Caching katalog produk menggunakan **Spring Cache** (Redis / Caffeine).
   - [ ] Otomasi Continuous Integration (CI) menggunakan **GitHub Actions** (`mvn clean test` on push).
4. **Tahap 4: Proyek Hybrid Microservices (Spring Boot + Golang)**
   - [ ] Arsitektur multi-layanan: Core Business & Transaksi (Spring Boot) + Real-time Notification / Streaming (Golang Gin/Fiber via WebSocket atau Message Broker).

