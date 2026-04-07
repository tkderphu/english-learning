## Copilot / AI agent instructions — english (backend)

Single-module Spring Boot REST (`site.viosmash.english.EnglishApplication`). Layers: `controller` → `service` → `repository` → `entity`; `dto`, `config`, `filter`, `exception`, `mapper`, `util`.

### Auth & persistence
- JWT: `service/JwtService`, `filter/JwtAuthenticationFilter`, `config/SecurityConfig`.
- MySQL + JPA; `hibernate.ddl-auto=update` (no Flyway). Many entities extend `BaseEntity` (`created_user`, `created_at`, `modified_by`, `modified_at`, `status`). Check id type per entity (`User` uses `String`, most others `int`).

### Conventions
- Controllers return `ResponseEntity<BaseResponse<?>>`; use `BaseResponse.success(...)`.
- Business errors: throw `ServiceException(HttpStatus, String)`; handled by `GlobalExceptionHandler`.
- `@RequiredArgsConstructor` constructor injection in services/controllers.
- List/page APIs: prefer JPQL `select new ...Dto(...)` projections (see `UserRepository.findAllByKeyword`).
- Auditing: `@EnableJpaAuditing` + `SpringSecurityAuditorAware`.

### Integrations
- Files: `FileController` / `FileService`, path `file.storage.path` in `application.properties`.
- Mail: `MailService`; credentials via env, not committed.
- AI chat: Groq/OpenAI-compatible client, `AiChatServiceImpl`, prompts in `AiPromptBuilderServiceImpl`.

### Build (Windows)
- `mvnw.cmd clean package` / `mvnw.cmd test` / `mvnw.cmd spring-boot:run`

### Adding a feature
1. DTOs in `dto/request` & `dto/response`.
2. Entity + repository (extend `BaseEntity` if needed).
3. `@Service` with `ServiceException` for domain errors.
4. `@RestController`, OpenAPI annotations where used, `BaseResponse` responses.
5. New config keys in `application.properties` or env vars.

Ask maintainers before changing production secrets, DB migrations, or global security/CORS.
