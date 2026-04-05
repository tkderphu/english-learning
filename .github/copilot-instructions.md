## Copilot / AI agent instructions — english (backend)

Short: single-module Spring Boot REST backend. This doc gives concise, actionable rules an AI agent can rely on immediately.

Key architecture (what to know fast)

Project conventions (must follow)

Build / run / debug (practical commands for Windows cmd)

Integration points & secrets

Quick examples / files to inspect

Adding a new REST feature (minimal checklist)
- Single Maven module; main class `site.viosmash.english.EnglishApplication`.
- Typical layers: `controller` -> `service` -> `repository` (Spring Data JPA) -> `entity`. Cross-cutting: `dto`, `config`, `filter`, `util`.

Project conventions (do these exactly)
## Copilot / AI agent instructions — english (backend)
- Signal business errors by throwing `exception.ServiceException(HttpStatus, String)`; `exception/GlobalExceptionHandler` maps these to HTTP responses.
Short: single-module Spring Boot REST backend (Maven). This file explains the minimal, immediately-actionable knowledge an AI contributor needs to be productive.
- Repositories prefer JPQL projection DTOs (example: `UserRepository.findAllByKeyword` uses `select new ...Dto(...)`) for pageable/list endpoints.
What to know fast
- Main class: `site.viosmash.english.EnglishApplication` (single Maven module).
- Layers and layout: controllers -> services -> repositories -> entities; cross-cutting folders: `dto`, `config`, `filter`, `exception`, `util`, `mapper`.
- Security / auth: JWT creation/validation lives in `service/JwtService.java`, authentication is enforced by `filter/JwtAuthenticationFilter.java`, and rules are configured in `config/SecurityConfig.java`.
- Persistence: Spring Data JPA to MySQL. Many entities extend `entity/BaseEntity` (auditing fields + common `int status`). Check the concrete entity for id type (most use `int`, `User` uses `String`).
- Use constructor injection with Lombok `@RequiredArgsConstructor` in `@Service` and `@RestController` classes.
Controller / service conventions (must follow)
- All REST controllers return `ResponseEntity<BaseResponse<?>>`. Use `dto/response/BaseResponse.java` and `BaseResponse.success(...)` for normal responses.
- Business errors are signalled by throwing `exception/ServiceException(HttpStatus, String)`; handled globally by `exception/GlobalExceptionHandler`.
- Use constructor injection with Lombok `@RequiredArgsConstructor` in `@Service` and `@RestController` classes.
- For pageable/list endpoints prefer JPQL projection DTOs (example: `repository/UserRepository.findAllByKeyword` uses `select new ...Dto(...)`). This reduces entity fetching and avoids unnecessary joins.
- Most entities extend `entity/BaseEntity` (auditing fields + `int status`); check individual entity id types (many use `int`, `User` uses `String`).
Common integration points
- File uploads: controllers `controller/FileController.java` and `controller/AudioController.java` call `service/FileService` — files are written to the path configured by `file.storage.path` in `src/main/resources/application.properties`. Metadata stored in `entity/FileEntity` / `repository/FileRepository`.
- Email: `service/MailService` reads SMTP properties from `application.properties` — treat these as secrets.
- JWT and security: `service/JwtService`, `filter/JwtAuthenticationFilter`, `config/SecurityConfig` form the auth flow. See `controller/AuthController.java` and `service/AuthService` for typical usage.
- Auditing is enabled (`@EnableJpaAuditing` in `config/SecurityConfig`) and `SpringSecurityAuditorAware` provides `createdBy/modifiedBy`.
Build / run / test (Windows cmd)
- Build package: mvnw.cmd clean package
- Run tests: mvnw.cmd test
- Run locally: mvnw.cmd spring-boot:run  (or after packaging: java -jar target/english-0.0.1-SNAPSHOT.jar)
- Config edits: change `src/main/resources/application.properties` for DB, JWT, SMTP, and `file.storage.path`.

Project-specific patterns and gotchas
- Entities: most extend `BaseEntity` which supplies auditing (`createdBy`, `modifiedBy`) and `int status`. Use these fields consistently.
- ID types: don't assume `int` for every entity; inspect `entity/User.java` and similar before using IDs in code.
- Responses: never return raw entities from controllers — always wrap with `BaseResponse` DTOs or projection DTOs.
- Repositories: prefer JPQL projection DTOs for list endpoints and pageable APIs to keep queries efficient and stable. Search for `select new` usages in `repository/`.
- Error handling: throw `ServiceException` for domain errors; the global handler converts them to proper HTTP status + payload.
- Auditing: enabled in `config/SecurityConfig` via `@EnableJpaAuditing` and `SpringSecurityAuditorAware` sets the current user.
Build / run / test (Windows cmd)
Adding a new REST feature (practical checklist)
1. Add request/response DTOs under `dto/request` and `dto/response` (prefer projection DTO for list endpoints).
2. Add JPA `entity` (extend `BaseEntity` if you need auditing) and `repository` (extend `JpaRepository`).
3. Implement a `@Service` (use `@RequiredArgsConstructor`, throw `ServiceException` on domain errors).
4. Add a `@RestController` with `@RequestMapping("/api/<name>")`, use OpenAPI annotations where present, and return `ResponseEntity<BaseResponse<?>>`.
5. Add config properties to `application.properties` if required and read them via `@Value("${...:default}")`.
- Build: `mvnw.cmd clean package`
When to ask a maintainer (must consult)
- Any production secret changes (DB credentials, SMTP, JWT secret).
- DB schema migrations or changes that need migration scripts (the project currently uses `hibernate.ddl-auto=update`, but do not change schema in prod without discussion).
- Changes to global security/CORS rules in `config/SecurityConfig.java`.
- Run tests: `mvnw.cmd test`
Where to look for examples
- Auth flow: `controller/AuthController.java`, `service/AuthService.java`, `service/JwtService.java`, `filter/JwtAuthenticationFilter.java`.
- Controller pattern: `controller/UserController.java`, `controller/BookController.java` (OpenAPI + BaseResponse usage).
- File upload: `controller/FileController.java`, `service/FileService.java`, `entity/FileEntity.java`, `repository/FileRepository.java`.
- Error handling and DTOs: `exception/ServiceException.java`, `exception/GlobalExceptionHandler.java`, `dto/response/BaseResponse.java`.
- Run locally: `mvnw.cmd spring-boot:run` or after package: `java -jar target/english-0.0.1-SNAPSHOT.jar`
Quick quality gates for edits
- Build: run `mvnw.cmd -DskipTests=false clean package` (fail fast if compilation/tests fail).
- Tests: run `mvnw.cmd test` and include at least one unit test for new service logic.
- Keep changes minimal and prefer adding tests for public behavior.

If anything is unclear or you'd like me to add a one-file unit-test template, a sample JPQL projection DTO, or a tiny quick-start README for running with MySQL docker-compose, tell me which and I'll add it.
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
