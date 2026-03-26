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
1. Add DTOs under `dto/request` and `dto/response`.
2. Add entity (extend `BaseEntity` if it needs auditing) and repository (`JpaRepository`). Prefer JPQL projection for list endpoints.
3. Implement `@Service` class (constructor injection, throw `ServiceException` for errors).
4. Add `@RestController` with `@RequestMapping("/api/<name>")`, OpenAPI annotations, and return `ResponseEntity<BaseResponse<?>>`.
5. Add configuration properties to `application.properties` if required and default with `@Value("${...:default}")` in code.

Tests & verification
- Unit tests live under `test/java/...` — name test classes `*Tests.java`.
- After builds, smoke-test endpoints by running jar and calling endpoints (use curl or Postman). Example upload (Windows cmd):
  curl -v -F "file=@C:\\path\\to\\file.png" http://localhost:7000/api/file/v1/upload

When to ask a maintainer
- DB schema changes or migrations required.
- Need production secrets (SMTP, DB credentials, JWT secret) or changes to security rules.

If anything here is unclear or you want examples added (unit-test template, common repository projection examples), tell me which section to expand.
This repo is a Spring Boot (Java) backend for an English learning service. These instructions give targeted guidance for coding agents to be productive in this codebase.

High level architecture (quick):
- Spring Boot REST API, single module (packaged as english-0.0.1-SNAPSHOT.jar).
- Packages: `controller` (REST endpoints), `service` (business logic), `repository` (Spring Data JPA), `entity` (JPA entities), `dto` (request/response shapes), `config` and `filter` (security), `util` (helpers).
- Security: JWT-based auth implemented in `service.JwtService` + `filter.JwtAuthenticationFilter` and `config.SecurityConfig`.
- Persistence: MySQL with Spring Data JPA. Entities extend `BaseEntity` which provides auditing fields (createdBy/createdAt/modifiedBy/modifiedAt) and `status` integer.

Project-specific patterns and conventions:
- Controllers always return `ResponseEntity<BaseResponse<?>>` where `BaseResponse` is the project's envelope (see `dto/response/BaseResponse.java`). Wrap results with `BaseResponse.success(...)`.
- Errors are signalled by throwing `ServiceException(HttpStatus, String)` found in `exception/ServiceException.java`. Global handler maps them to HTTP responses (`GlobalExceptionHandler`).
- Repositories use Spring Data JPA and often return projection DTOs via JPQL `new` expressions (see `UserRepository.findAllByKeyword`). Prefer this style for paginated list endpoints.
- Entities use primitive `int` for `id` in `BaseEntity` but some entities like `User` use String id — check the entity. Always inspect the specific entity before reusing id types.
- Auditing is enabled via `@EnableJpaAuditing` in `config.SecurityConfig` — `createdBy`/`modifiedBy` are set through `SpringSecurityAuditorAware`.

Build / run / test workflows (how devs run stuff):
- Build and run with Maven wrappers present: use `mvnw.cmd` on Windows or `./mvnw` on *nix. Typical commands:
  ```instructions
  ## Copilot / AI agent instructions — english (backend)

  Short: single-module Spring Boot REST backend. The goal: give an AI agent the minimal, actionable knowledge to be productive.

  Quick facts
  - Main class: `site.viosmash.english.EnglishApplication` (single Maven module).
  - Layering: `controller` (REST) -> `service` (business) -> `repository` (Spring Data JPA) -> `entity` (JPA models). `dto`, `config`, `filter`, `util` exist for cross-cutting concerns.

  Must-know conventions (follow exactly)
  - Controllers return ResponseEntity<BaseResponse<?>>. Use `dto/response/BaseResponse.java` and `BaseResponse.success(...)` for payloads.
  - Signal business errors by throwing `exception.ServiceException(HttpStatus, String)`; `exception.GlobalExceptionHandler` maps them to HTTP responses.
  - Repositories prefer JPQL projection DTOs: look for `UserRepository.findAllByKeyword` for the pattern `select new ...Dto(...)` — use this for pageable list endpoints.
  - Use constructor injection with Lombok `@RequiredArgsConstructor` in services/controllers.
  - Entities usually extend `entity.BaseEntity` (auditing fields + int `status`). Check individual entity id types (most `int`, `User` uses String) before copying code.
  - Auditing enabled via `@EnableJpaAuditing` in `config.SecurityConfig`; `SpringSecurityAuditorAware` supplies createdBy/modifiedBy.

  Security & auth
  - JWT auth implemented in `service.JwtService`, enforced by `filter.JwtAuthenticationFilter` and configured in `config.SecurityConfig`.
  - If you add endpoints that require auth, update security rules in `SecurityConfig.java` (find the permit/antMatchers block).

  Integration points
  - Database: MySQL configured in `src/main/resources/application.properties`. No migrations in repo — JPA uses `hibernate.ddl-auto=update`.
  - Mail: `service.MailService` uses SMTP props in `application.properties` (treat credentials as secrets).
  - File uploads: `controller.FileController` + `service.FileService` write files to `file.storage.path` (property) and persist metadata in `entity/FileEntity` / `repository/FileRepository`.

  Developer workflows (Windows cmd)
  - Build: `mvnw.cmd clean package`
  - Run tests: `mvnw.cmd test`
  - Run app: `mvnw.cmd spring-boot:run` or `java -jar target/english-0.0.1-SNAPSHOT.jar` after packaging

  Adding a new REST feature (minimal checklist)
  1. Add request/response DTOs under `dto/request` and `dto/response`.
  2. Add JPA `entity` (extend `BaseEntity` if you want auditing) and `repository` (extend `JpaRepository`). For list endpoints prefer JPQL projection DTOs.
  3. Add `@Service` (throw `ServiceException` for domain errors; use `@RequiredArgsConstructor`).
  4. Add `@RestController` with `@RequestMapping("/api/<name>")`. Use OpenAPI annotations (existing controllers do) and return `ResponseEntity<BaseResponse<?>>`.
  5. Add properties to `application.properties` if needed and read them with `@Value("${...:default}")`.

  Quick verification
  - Unit tests: `test/java/...` (tests named `*Tests.java`). Run `mvnw.cmd test`.
  - Smoke: build jar and call an endpoint. Example (Windows cmd): curl -v -F "file=@C:\\path\\to\\file.png" http://localhost:7000/api/file/v1/upload

  When to ask a maintainer
  - Any DB schema migrations or production secret changes (DB, SMTP, JWT secret).
  - Changes to global security rules or auditor behavior.

  Key files to inspect (examples)
  - `controller/AuthController.java`, `service/AuthService` (auth flows)
  - `controller/UserController.java` (controller pattern + OpenAPI)
  - `service/JwtService.java`, `filter/JwtAuthenticationFilter.java`, `config/SecurityConfig.java` (security)
  - `entity/BaseEntity.java`, `config/SpringSecurityAuditorAware.java` (auditing)
  - `dto/response/BaseResponse.java`, `exception/ServiceException.java`, `exception/GlobalExceptionHandler.java` (error handling)

  If anything's unclear or you want a short unit-test template / example projection query, tell me which section to expand.
  ```
