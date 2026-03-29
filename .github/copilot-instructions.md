## Copilot / AI agent instructions — english (backend)

Short: single-module Spring Boot REST backend. This doc gives concise, actionable rules an AI agent can rely on immediately.

Key architecture (what to know fast)
- Single Maven module; main class `EnglishApplication` (package `site.viosmash.english`).
- Layers: `controller` (REST), `service` (business), `repository` (Spring Data JPA), `entity` (JPA models), `dto` (request/response), `config` and `filter` (security), `util` (helpers).
- Auth flow: JWTs are created/validated in `service.JwtService` and enforced by `filter.JwtAuthenticationFilter`; security rules are in `config.SecurityConfig`.
- Persistence: MySQL via Spring Data JPA. Entities commonly extend `BaseEntity` (auditing fields + int `status`).

Project conventions (must follow)
- Controllers always return `ResponseEntity<BaseResponse<?>>` (see `dto/response/BaseResponse.java`). Use `BaseResponse.success(...)` for successful payloads.
- Throw `ServiceException(HttpStatus, String)` for business errors; global handler `GlobalExceptionHandler` converts to HTTP responses.
- Repositories prefer JPQL projections using `new` DTO expressions for list endpoints (example: `UserRepository.findAllByKeyword`). Use projections for pageable APIs.
- Constructor injection via Lombok `@RequiredArgsConstructor` is standard in `service` and `controller` classes.
- Check entity id types per entity (most use `int` in `BaseEntity`, but some like `User` may use `String`). Inspect the specific entity before reusing id handling.
- Auditing is enabled (`@EnableJpaAuditing` in `config.SecurityConfig`) and `SpringSecurityAuditorAware` sets `createdBy`/`modifiedBy`.

Build / run / debug (practical commands for Windows cmd)
- Build: `mvnw.cmd clean package`
- Run tests: `mvnw.cmd test`
- Run locally: `mvnw.cmd spring-boot:run` (or `java -jar target/english-0.0.1-SNAPSHOT.jar` after package)
- Config: edit `src/main/resources/application.properties` for DB, JWT, SMTP, and `file.storage.path` used by `FileController`/`FileService`.

Integration points & secrets
- MySQL: configured in `application.properties`; no migrations present — JPA uses `hibernate.ddl-auto=update`.
- SMTP: `MailService` relies on properties in `application.properties`. Treat credentials as secrets; ask maintainers for real values.
- File storage: uploaded files are written to `file.storage.path` and metadata stored in `entity/FileEntity.java` + `repository/FileRepository.java`.

Quick examples / files to inspect
- Auth endpoints: `controller/AuthController.java` and `service/AuthService`.
- Example controller pattern: `controller/UserController.java` (OpenAPI annotations, `@RequiredArgsConstructor`, returns `BaseResponse`).
- Exception pattern: `exception/ServiceException.java` and `exception/GlobalExceptionHandler.java`.
- Auditing: `config/SpringSecurityAuditorAware.java` and `entity/BaseEntity.java`.

Adding a new REST feature (minimal checklist)
## Copilot / AI agent instructions — english (backend)

Short (1 line): single-module Spring Boot REST backend. This file gives the minimal, immediately actionable rules for an AI contributor.

What to know fast
- Single Maven module; main class `site.viosmash.english.EnglishApplication`.
- Typical layers: `controller` -> `service` -> `repository` (Spring Data JPA) -> `entity`. Cross-cutting: `dto`, `config`, `filter`, `util`.
- Auth: JWT tokens created/validated in `service/JwtService.java` and enforced by `filter/JwtAuthenticationFilter.java`; security rules live in `config/SecurityConfig.java`.
- Persistence: MySQL (configured in `src/main/resources/application.properties`). There are no DB migrations; JPA uses `hibernate.ddl-auto=update`.

Project conventions (do these exactly)
- Controllers always return `ResponseEntity<BaseResponse<?>>` — see `dto/response/BaseResponse.java`. Use `BaseResponse.success(...)` for payloads.
- Signal business errors by throwing `exception.ServiceException(HttpStatus, String)`; `exception/GlobalExceptionHandler` maps these to HTTP responses.
- Repositories prefer JPQL projection DTOs (example: `UserRepository.findAllByKeyword` uses `select new ...Dto(...)`) for pageable/list endpoints.
- Use constructor injection with Lombok `@RequiredArgsConstructor` in `@Service` and `@RestController` classes.
- Most entities extend `entity/BaseEntity` (auditing fields + `int status`); check individual entity id types (many use `int`, `User` uses `String`).
- Auditing is enabled (`@EnableJpaAuditing` in `config/SecurityConfig`) and `SpringSecurityAuditorAware` provides `createdBy/modifiedBy`.

Build / run / test (Windows cmd)
- Build: `mvnw.cmd clean package`
- Run tests: `mvnw.cmd test`
- Run locally: `mvnw.cmd spring-boot:run` or after package: `java -jar target/english-0.0.1-SNAPSHOT.jar`

Important integration points
- File uploads: `controller/FileController.java` + `service/FileService` write files to the path configured by `file.storage.path` and persist metadata in `entity/FileEntity.java` / `repository/FileRepository.java`.
- Mail: `service/MailService` uses SMTP properties in `application.properties` — treat credentials as secrets.

Quick examples / files to read
- Auth flow: `controller/AuthController.java`, `service/AuthService.java`, `service/JwtService.java`, `filter/JwtAuthenticationFilter.java`.
- Error handling: `exception/ServiceException.java`, `exception/GlobalExceptionHandler.java`.
- Example controller + OpenAPI usage: `controller/UserController.java`.
- Auditing: `entity/BaseEntity.java`, `config/SpringSecurityAuditorAware.java`.

Adding a new REST feature (minimal, follow this order)
1. Add DTOs under `dto/request` and `dto/response`.
2. Add JPA `entity` (extend `BaseEntity` if you need auditing) and `repository` (extend `JpaRepository`). For list endpoints prefer JPQL projection DTOs.
3. Implement a `@Service` (use `@RequiredArgsConstructor`, throw `ServiceException` for domain errors).
4. Add a `@RestController` with `@RequestMapping("/api/<name>")`, OpenAPI annotations, and return `ResponseEntity<BaseResponse<?>>`.
5. Add properties to `application.properties` if needed and read them via `@Value("${...:default}")`.

When to ask a maintainer
- Any DB schema migration or change to production secrets (DB, SMTP, JWT secret).
- Changes to global security / CORS rules in `config/SecurityConfig.java`.

If you want, I can add a one-file unit-test template, a sample JPQL projection DTO, or a quick-start checklist for running locally behind a MySQL container. Tell me which to add.
  - Repositories prefer JPQL projection DTOs: look for `UserRepository.findAllByKeyword` for the pattern `select new ...Dto(...)` — use this for pageable list endpoints.
