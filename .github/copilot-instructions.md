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
  - Build: `mvnw.cmd clean package` (Windows)
  - Run tests only: `mvnw.cmd test`
  - Run locally: `mvnw.cmd spring-boot:run` or run produced jar `java -jar target/english-0.0.1-SNAPSHOT.jar`.
- The application uses `application.properties` (MySQL configuration). For local dev, ensure MySQL is running or change datasource to an in-memory DB for quick iteration.

Important files to inspect when editing or adding features:
- `controller/*` — follow patterns of `AuthController` and `UserController` (OpenAPI annotations, `@RequiredArgsConstructor`).
- `service/*` — business logic, use constructor injection (Lombok `@RequiredArgsConstructor`). Throw `ServiceException` for business errors.
- `repository/*` — prefer Spring Data interfaces with JPQL projections for paginated queries.
- `dto/response/BaseResponse.java` and `dto/request` — conform to request/response DTO shapes; controllers should return `BaseResponse` wrapped responses.
- `config/SecurityConfig.java`, `filter/JwtAuthenticationFilter.java`, `service/JwtService.java` — any endpoint that needs authentication must be added to security config if necessary.

Integration points and external dependencies:
- MySQL database configured in `application.properties` — migrations are not present; JPA uses `hibernate.ddl-auto=update`.
- SMTP mail sender is used by `MailService` and configured in `application.properties` — credentials in this repo are placeholder and should be updated in secrets.
- JWT configuration keys are in `application.properties` (secret and token durations).

Adding a new REST feature (practical checklist):
1. Add DTOs under `dto/request` and `dto/response`.
2. Add JPA `entity` (extend `BaseEntity` if it needs auditing fields). Choose id type to match other entities.
3. Add `repository` interface extending `JpaRepository` and prefer JPQL projection for listing endpoints.
4. Add `service` class with `@Service` + `@RequiredArgsConstructor`. Throw `ServiceException` for error cases.
5. Add `controller` class with `@RestController`, `@RequestMapping("/api/<name>")` and OpenAPI `@Tag` and `@Operation` annotations. Return `ResponseEntity<BaseResponse<?>>`.
6. Add configuration property to `application.properties` if new behavior requires it. Use `@Value("${...:default}")` to provide sensible defaults.

File-storage quick example:
- New endpoints should follow `FileController` at `/api/file/v1/upload` (POST, `MultipartFile`) and `/api/file/v1/{id}` (DELETE). Implementation persists file to `file.storage.path` (configurable) and stores metadata in `entity/FileEntity.java` + `repository/FileRepository.java`.

Testing and verification tips:
- Unit: project currently uses standard Maven test setup; add unit tests under `test/java/...` and name test classes `*Tests.java`.
- Smoke test: after a build, run the jar and call endpoints with curl or Postman. Example upload (Windows cmd):
  curl -v -F "file=@C:\\path\\to\\file.png" http://localhost:7000/api/file/v1/upload

When to ask the maintainer:
- If adding persistent DB schema changes that require migration scripts.
- If you need access to secrets (SMTP, production DB) or want to change security rules.

If anything in these instructions is unclear or incomplete, point to the area you'd like expanded (security rules, DTO conventions, example unit tests) and I'll update this file.
