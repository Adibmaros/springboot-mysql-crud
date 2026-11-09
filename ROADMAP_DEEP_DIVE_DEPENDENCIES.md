# 🧭 Roadmap Bedah Mendalam: Arsitektur & Alur Dependensi Spring Boot

> **Target Pembaca:** Developer dengan latar belakang **Golang (Gin, GORM, database/sql)** yang ingin memahami cara kerja internal (*under the hood*) ekosistem **Java & Spring Boot** hingga ke tingkat *bytecode*, *threading*, dan *lifecycle*.

---

## 🎯 Tujuan Belajar
1. Tidak hanya sekadar bisa memakai (*copy-paste* anotasi), tetapi memahami **apa yang sebenarnya terjadi di balik layar**.
2. Membangun **mental model komparatif** antara paradigma Golang (eksplisit & kompilasi langsung) vs Spring Boot (deklaratif, *IoC Container*, *reflection*, & *runtime proxies*).
3. Memahami siklus hidup (*lifecycle*) request dari port jaringan hingga ke basis data MySQL.

---

## 🗺️ Gambaran Alur Request di Proyek Ini

```
[HTTP Request dari Client / REST Client / Swagger]
       │
       ▼
┌─────────────────────────────────────────────────────────────┐
│ 1. Embedded Tomcat (Port 8080)                              │
│    └─ Socket Listener & Worker Thread Pool                  │
└──────────────────────────────┬──────────────────────────────┘
                               │
                               ▼
┌─────────────────────────────────────────────────────────────┐
│ 2. Spring Security Filter Chain                             │
│    ├─ JwtAuthenticationFilter (Cek Header Bearer JWT)       │
│    │    └─ JJWT: Validasi Signature & Parse Claims          │
│    │    └─ Simpan User ke SecurityContext (ThreadLocal)     │
│    └─ FilterSecurityInterceptor (Cek role/otorisasi URL)    │
└──────────────────────────────┬──────────────────────────────┘
                               │
                               ▼
┌─────────────────────────────────────────────────────────────┐
│ 3. DispatcherServlet (Front Controller Spring Web MVC)      │
│    ├─ HandlerMapping: Cari Controller yang cocok            │
│    ├─ HandlerAdapter: Siapkan argument & data binding       │
│    │    └─ Jackson: Parse JSON -> DTO                       │
│    │    └─ Jakarta Validation: Cek @NotBlank, @Min, dll     │
│    └─ GlobalExceptionHandler (Jika validasi/request error)  │
└──────────────────────────────┬──────────────────────────────┘
                               │
                               ▼
┌─────────────────────────────────────────────────────────────┐
│ 4. Controller Layer (ProductController / AuthController)     │
└──────────────────────────────┬──────────────────────────────┘
                               │
                               ▼
┌─────────────────────────────────────────────────────────────┐
│ 5. Service Layer (ProductService / AuthService)             │
│    └─ Business Logic, Password Hashing (BCrypt)             │
└──────────────────────────────┬──────────────────────────────┘
                               │
                               ▼
┌─────────────────────────────────────────────────────────────┐
│ 6. Spring Data JPA & Hibernate                              │
│    ├─ JpaRepository (Dynamic Proxy Invocation)              │
│    ├─ EntityManager / Hibernate Session                     │
│    │    └─ First-Level Cache, Dirty Checking, SQL Gen       │
│    └─ HikariCP (Connection Pool) -> MySQL Socket           │
└──────────────────────────────┬──────────────────────────────┘
                               │
                               ▼
┌─────────────────────────────────────────────────────────────┐
│ 7. MySQL Database 8 (Skema dimigrasikan oleh Flyway)        │
└─────────────────────────────────────────────────────────────┘
```

---

## 📚 Silabus & Modul Pembelajaran

---

### 📦 Modul 1: Spring Web MVC & Embedded Tomcat (`spring-boot-starter-webmvc`)
> *Jantung penanganan HTTP, Routing, Serialization, dan Exception Handling.*

* **Topik Utama:**
  1. **Embedded Tomcat vs Golang HTTP Server:** Bagaimana Spring Boot memaketkan server servlet tanpa file `.war` eksternal terpisah.
  2. **`DispatcherServlet`:** Pola *Front Controller* yang menerima semua request HTTP dan mendistribusikannya ke controller yang tepat (`HandlerMapping`).
  3. **Jackson JSON Engine:** Proses *Serialization* (Java Object $\rightarrow$ JSON) dan *Deserialization* (JSON $\rightarrow$ Java Object).
  4. **Pola Global Exception Handling:** Bagaimana `@RestControllerAdvice` dan `@ExceptionHandler` mencegat error sebelum keluar ke client.
* **Komparasi dengan Golang:**
  - `DispatcherServlet` $\approx$ `http.Handler` / Router `gin.Engine`.
  - Jackson $\approx$ `encoding/json` atau `jsoniter`.
  - `@RestControllerAdvice` $\approx$ `gin.CustomRecovery()` / Error Handler Middleware.
* **Titik Eksplorasi Kode:**
  - [`ProductController.java`](file:///c:/Users/user/Desktop/belajar/src/main/java/com/crud/belajar/controller/ProductController.java)
  - [`GlobalExceptionHandler.java`](file:///c:/Users/user/Desktop/belajar/src/main/java/com/crud/belajar/exception/GlobalExceptionHandler.java)
  - [`ApiResponse.java`](file:///c:/Users/user/Desktop/belajar/src/main/java/com/crud/belajar/dto/ApiResponse.java)
* **Eksperimen Praktik:**
  - Tambahkan breakpoint di method Controller dan inspect `Thread.currentThread().getName()` untuk melihat pool worker Tomcat (`http-nio-8080-exec-*`).

---

### 📦 Modul 2: Project Lombok (`lombok`)
> *Sihir di tahap kompilasi untuk memangkas ribuan baris boilerplate code.*

* **Topik Utama:**
  1. **Bukan Reflection, tapi Kompilasi:** Mengapa Lombok sama sekali tidak memperlambat aplikasi saat dijalankan (*Zero Runtime Overhead*).
  2. **Java Annotation Processing API (JSR 269):** Bagaimana Lombok memodifikasi *Abstract Syntax Tree* (AST) Java sebelum diubah menjadi file `.class`.
  3. **Anotasi Esensial:** `@Getter`, `@Setter`, `@NoArgsConstructor`, `@AllArgsConstructor`, `@Builder`, `@RequiredArgsConstructor`.
  4. **Delombok:** Cara melihat kode Java murni hasil translasi Lombok.
* **Komparasi dengan Golang:**
  - Lombok $\approx$ `go generate` (misalnya tools generator *stringer* atau *easyjson*), tetapi Lombok berjalan otomatis saat proses *compile* tanpa perlu perintah manual terpisah.
* **Titik Eksplorasi Kode:**
  - [`User.java`](file:///c:/Users/user/Desktop/belajar/src/main/java/com/crud/belajar/entity/User.java)
  - [`Product.java`](file:///c:/Users/user/Desktop/belajar/src/main/java/com/crud/belajar/entity/Product.java)
* **Eksperimen Praktik:**
  - Buka folder `target/classes/com/crud/belajar/entity/User.class` menggunakan decompiler IDE atau perintah `javap -c User.class` untuk membuktikan method `getId()`, `getName()`, dan constructor benar-benar ada di *bytecode*.

---

### 📦 Modul 3: Jakarta Validation & Hibernate Validator (`spring-boot-starter-validation`)
> *Standardisasi validasi input DTO sebelum masuk ke logika bisnis.*

* **Topik Utama:**
  1. **JSR-380 Specification:** Standar Bean Validation pada ekosistem Java.
  2. **Anotasi Validasi:** `@NotBlank`, `@Size`, `@Min`, `@Email`, `@NotNull`.
  3. **Peran `@Valid`:** Memicu interceptor validasi pada parameter `@RequestBody`.
  4. **`MethodArgumentNotValidException`:** Membedah struktur error `BindingResult` dan memformatnya menjadi response JSON yang rapi (*field-by-field*).
* **Komparasi dengan Golang:**
  - Jakarta Validation $\approx$ `go-playground/validator/v10` (`binding:"required,min=3"` di Gin).
* **Titik Eksplorasi Kode:**
  - [`ProductRequest.java`](file:///c:/Users/user/Desktop/belajar/src/main/java/com/crud/belajar/dto/ProductRequest.java)
  - [`RegisterRequest.java`](file:///c:/Users/user/Desktop/belajar/src/main/java/com/crud/belajar/dto/RegisterRequest.java)
  - [`GlobalExceptionHandler.java#L21-L35`](file:///c:/Users/user/Desktop/belajar/src/main/java/com/crud/belajar/exception/GlobalExceptionHandler.java#L21-L35)
* **Eksperimen Praktik:**
  - Buat custom validation annotation (misalnya `@ValidPhoneNumber` atau `@NoBadWords`) dengan mengimplementasikan `ConstraintValidator<A, T>`.

---

### 📦 Modul 4: HikariCP, MySQL Driver & Spring Data JPA (`spring-boot-starter-data-jpa`)
> *Mesin ORM, Connection Pooling, dan Dynamic Proxy Repository.*

* **Topik Utama:**
  1. **HikariCP Connection Pool:** Mengapa sangat cepat? Manajemen koneksi aktif, idle, timeout, dan *thread borrowing*.
  2. **Hibernate ORM Core:**
     - Pemetaan Relasi: `@OneToMany`, `@ManyToOne`, `@JoinColumn`, `mappedBy`.
     - *Cascade Types* (`CascadeType.ALL`, `orphanRemoval`).
     - Hibernate Session, *First-Level Cache* (Persistence Context), dan *Dirty Checking* (otomatis update SQL jika setter dipanggil dalam transaksi).
  3. **Spring Data JPA Dynamic Proxy:** Bagaimana interface seperti `ProductRepository extends JpaRepository` bisa bekerja tanpa ada implementasi *class* konkretnya (menggunakan Java Dynamic Proxies `java.lang.reflect.Proxy`).
  4. **Paginasi & Sorting di Level Database:** Membedah bagaimana `Pageable` diterjemahkan menjadi klausa `LIMIT` dan `OFFSET` di MySQL.
* **Komparasi dengan Golang:**
  - HikariCP $\approx$ Connection Pool bawaan `sql.DB` (`SetMaxOpenConns`, `SetMaxIdleConns`).
  - Hibernate $\approx$ GORM / Ent / SQLBoiler.
  - `JpaRepository` $\approx$ Generic Repository pattern di Go.
* **Titik Eksplorasi Kode:**
  - [`ProductRepository.java`](file:///c:/Users/user/Desktop/belajar/src/main/java/com/crud/belajar/repository/ProductRepository.java)
  - [`UserRepository.java`](file:///c:/Users/user/Desktop/belajar/src/main/java/com/crud/belajar/repository/UserRepository.java)
  - [`ProductService.java`](file:///c:/Users/user/Desktop/belajar/src/main/java/com/crud/belajar/service/ProductService.java)
* **Eksperimen Praktik:**
  - Cek log SQL di terminal saat memanggil `GET /api/products?page=0&size=5` untuk melihat 2 query yang dihasilkan: query data (`LIMIT ? OFFSET ?`) dan query total (`SELECT COUNT(*) FROM products`).

---

### 📦 Modul 5: Flyway Database Migration (`flyway-core`, `flyway-mysql`)
> *Manajemen skema database berbasis kode berversi untuk konsistensi antar lingkungan.*

* **Topik Utama:**
  1. **Siklus Hidup Startup Flyway:** Bagaimana Flyway berjalan sebelum Hibernate dan Spring Data aktif.
  2. **Tabel Metadata `flyway_schema_history`:** Kolom `installed_rank`, `version`, `checksum`, dan `installed_on`.
  3. **Aturan Penamaan Script:** Format `V<Nomor>__<Deskripsi>.sql` vs `R__<Deskripsi>.sql` (Repeatable).
  4. **Validasi Checksum (CRC32):** Mengapa mengubah file migrasi lama yang sudah dijalankan akan menyebabkan startup gagal (*checksum mismatch*), dan bagaimana cara yang benar jika ingin mengubah skema.
  5. **Sinergi dengan Hibernate:** Mengapa `spring.jpa.hibernate.ddl-auto` harus diubah dari `update` menjadi `validate` saat menggunakan Flyway.
* **Komparasi dengan Golang:**
  - Flyway $\approx$ `golang-migrate/migrate` atau `pressly/goose`.
* **Titik Eksplorasi Kode:**
  - [`V1__create_users_table.sql`](file:///c:/Users/user/Desktop/belajar/src/main/resources/db/migration/V1__create_users_table.sql)
  - [`V2__create_products_table.sql`](file:///c:/Users/user/Desktop/belajar/src/main/resources/db/migration/V2__create_products_table.sql)
  - [`application.properties#L11-L15`](file:///c:/Users/user/Desktop/belajar/src/main/resources/application.properties#L11-L15)
* **Eksperimen Praktik:**
  - Buka database MySQL dan jalankan query: `SELECT * FROM flyway_schema_history;`. Perhatikan nilai hash `checksum`-nya.

---

### 📦 Modul 6: Spring Security 6 & JJWT (`spring-boot-starter-security`, `jjwt`)
> *Arsitektur keamanan modern, Servlet Filter, Stateless Session, dan Token HMAC.*

* **Topik Utama:**
  1. **Servlet Filter Chain vs Spring Interceptor:** Mengapa Spring Security bekerja di lapisan Filter (sebelum Servlet) bukan di Interceptor (setelah Servlet).
  2. **SecurityFilterChain Configuration:**
     - Penonaktifan CSRF untuk REST API (`csrf.disable()`).
     - Pengaturan `SessionCreationPolicy.STATELESS`.
     - Konfigurasi `authorizeHttpRequests` (mana yang publik, mana yang butuh autentikasi).
  3. **`JwtAuthenticationFilter` (`OncePerRequestFilter`):**
     - Membaca header `Authorization: Bearer <token>`.
     - Validasi keabsahan token melalui `JwtTokenProvider`.
  4. **`SecurityContextHolder` & `ThreadLocal`:**
     - Bagaimana data user login bisa diakses di sembarang tempat tanpa harus di-oper sebagai parameter method (`SecurityContextHolder.getContext().getAuthentication()`).
  5. **Library JJWT (Java JWT):**
     - Pembagian modul: `jjwt-api`, `jjwt-impl`, `jjwt-jackson`.
     - Struktur Token JWT: Header, Payload (Claims), Signature (HMAC-SHA256).
  6. **`JwtAuthenticationEntryPoint`:** Menangani error 401 Unauthorized dalam bentuk JSON kustom jika token tidak valid atau tidak dikirim.
* **Komparasi dengan Golang:**
  - `SecurityFilterChain` $\approx$ Rantai Middleware Gin (`router.Use(AuthMiddleware())`).
  - `SecurityContextHolder` $\approx$ `c.Set("currentUser", user)` dan `c.Get("currentUser")` pada Gin Context.
  - JJWT $\approx$ `golang-jwt/jwt/v5`.
* **Titik Eksplorasi Kode:**
  - [`SecurityConfig.java`](file:///c:/Users/user/Desktop/belajar/src/main/java/com/crud/belajar/security/SecurityConfig.java)
  - [`JwtAuthenticationFilter.java`](file:///c:/Users/user/Desktop/belajar/src/main/java/com/crud/belajar/security/JwtAuthenticationFilter.java)
  - [`JwtTokenProvider.java`](file:///c:/Users/user/Desktop/belajar/src/main/java/com/crud/belajar/security/JwtTokenProvider.java)
  - [`JwtAuthenticationEntryPoint.java`](file:///c:/Users/user/Desktop/belajar/src/main/java/com/crud/belajar/security/JwtAuthenticationEntryPoint.java)
* **Eksperimen Praktik:**
  - Dekode token dari response login di web `jwt.io` untuk memeriksa isi payload (claims: subject email, role, expired time).

---

### 📦 Modul 7: SpringDoc OpenAPI 3 / Swagger UI (`springdoc-openapi-starter-webmvc-ui`)
> *Dokumentasi API interaktif otomatis berbasis refleksi kode.*

* **Topik Utama:**
  1. **Runtime Reflection Scanning:** Bagaimana SpringDoc memindai semua `@RestController`, `@GetMapping`, `@PostMapping`, serta anotasi validasi (`@NotBlank`) untuk menghasilkan spesifikasi OpenAPI 3 secara dinamis saat aplikasi berjalan.
  2. **Konfigurasi Global OpenAPI:** Mendaftarkan Security Scheme (Bearer JWT) agar tombol **Authorize** muncul di Swagger UI.
  3. **Anotasi OpenAPI:** `@Tag`, `@Operation`, `@ApiResponse`, `@Schema`.
* **Komparasi dengan Golang:**
  - SpringDoc $\approx$ `swaggo/swag`, perbedaannya `swaggo` membaca komentar kode sebelum compile (*static analysis*), sedangkan SpringDoc membaca refleksi kode dan anotasi secara *runtime*.
* **Titik Eksplorasi Kode:**
  - [`OpenApiConfig.java`](file:///c:/Users/user/Desktop/belajar/src/main/java/com/crud/belajar/config/OpenApiConfig.java)
  - Swagger UI: `http://localhost:8080/swagger-ui.html`
  - OpenAPI JSON Spec: `http://localhost:8080/api-docs`

---

### 📦 Modul 8: Spring Boot Actuator (`spring-boot-starter-actuator`)
> *Observability siap produksi: Health checks, Metrics, dan Monitoring.*

* **Topik Utama:**
  1. **Tujuan Actuator:** Menghubungkan aplikasi dengan sistem monitoring modern (Kubernetes probes, Prometheus, Grafana, Datadog).
  2. **Endpoint Esensial:**
     - `/actuator/health`: Cek kesehatan menyeluruh (disk space, koneksi MySQL).
     - `/actuator/metrics`: Cek metrik JVM (heap memory, GC pause, connection pool stats).
  3. **Konsep `HealthIndicator`:** Bagaimana Spring Boot secara otomatis membuat MySQL Health Indicator dari datasource yang ada.
  4. **Keamanan Endpoint Actuator:** Membatasi akses endpoint sensitif agar tidak terekspos ke publik di lingkungan production.
* **Komparasi dengan Golang:**
  - Actuator $\approx$ Handler `/healthz` + library Prometheus `prometheus/client_golang` untuk mengekspos metrik sistem.
* **Titik Eksplorasi Kode:**
  - [`application.properties#L25-L29`](file:///c:/Users/user/Desktop/belajar/src/main/resources/application.properties#L25-L29)
* **Eksperimen Praktik:**
  - Akses `http://localhost:8080/actuator/metrics/jvm.memory.used` untuk melihat konsumsi RAM Java secara *real-time*.

---

### 📦 Modul 9: Spring Boot DevTools (`spring-boot-devtools`)
> *Meningkatkan kecepatan iterasi pengembang lokal.*

* **Topik Utama:**
  1. **Dual ClassLoader Strategy:**
     - *Base ClassLoader*: Menyimpan pustaka dependensi pihak ketiga (tidak pernah di-reload).
     - *Restart ClassLoader*: Menyimpan kode aplikasi kita sendiri (di-reload seketika saat ada perubahan file `.class`).
  2. **LiveReload Server:** Mengirim sinyal reload otomatis ke browser (jika terpasang ekstensi LiveReload).
  3. **Auto-Disable Cache:** Otomatis menonaktifkan cache template saat pengembangan.
* **Komparasi dengan Golang:**
  - DevTools $\approx$ Tool CLI `air` atau `gow` di Golang yang memonitor file `.go` dan melakukan compile ulang secara otomatis.
* **Titik Eksplorasi Kode:**
  - [`pom.xml#L90-L94`](file:///c:/Users/user/Desktop/belajar/pom.xml#L90-L94)

---

## 📝 Checklist Progres Belajar

Tandai `[x]` jika modul sudah kamu pelajari dan kuasai:

- [ ] **Modul 1**: Spring Web MVC & Embedded Tomcat (Routing, Jackson, Global Exception)
- [ ] **Modul 2**: Lombok (Kompilasi JSR-269, AST Manipulation, Bytecode Inspection)
- [ ] **Modul 3**: Jakarta Validation (JSR-380, Anotasi Validasi, Custom Constraint)
- [ ] **Modul 4**: HikariCP, MySQL Driver & Spring Data JPA (Proxy, EntityManager, Paginasi)
- [ ] **Modul 5**: Flyway Database Migration (Checksum, Schema History, DDL Auto Validate)
- [ ] **Modul 6**: Spring Security 6 & JJWT (Filter Chain, ThreadLocal SecurityContext, HMAC Token)
- [ ] **Modul 7**: SpringDoc OpenAPI / Swagger UI (Runtime Reflection, Security Scheme)
- [ ] **Modul 8**: Spring Boot Actuator (Health Indicator, Metrik JVM, Observability)
- [ ] **Modul 9**: Spring Boot DevTools (Dual ClassLoader, Fast Restart)

---

> **💡 Tips Memulai:**
> Kamu bisa mulai dari **Modul 1 (Spring Web MVC)** atau modul mana saja yang paling membuatmu penasaran. Cukup sebutkan: *"Yuk bahas Modul X!"* dan kita akan bedah sampai tuntas!
