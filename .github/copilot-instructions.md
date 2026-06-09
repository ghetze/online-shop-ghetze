# Copilot Instructions for online-shop-ghetze

## Git Commits
- Always commit using the repository owner's identity — do NOT add `Co-authored-by: Copilot` trailers
- Write commit messages in imperative mood (e.g. "Add entity classes", not "Added entity classes")
- Keep the subject line under 72 characters
- Add a blank line between subject and body
- List key changes as bullet points in the body

## Project Stack
- Java 25, Spring Boot 4, JPA / Hibernate 7, Flyway, PostgreSQL, Lombok
- Target database: `shopdb_springboot_learning` (PostgreSQL on localhost:5433)
- Run app locally: `mvn spring-boot:run "-Dspring-boot.run.profiles=local"`

## Code Style
- All JPA entities extend `AbstractBaseEntity` (UUID primary key) — except composite-key entities
- Always use `FetchType.LAZY` on `@ManyToOne` and `@ManyToMany`
- Use `@Getter @Setter @NoArgsConstructor` on entities — never `@Data` (causes JPA issues)
- Use `@ToString(exclude = "...")` on bidirectional relationships to avoid infinite loops
- Use primitives (`int`, `double`) for non-nullable numeric fields instead of boxed types
- Explicit `@Table`, `@Column`, `@JoinColumn` names everywhere — never rely on implicit naming

## Database Migrations
- Flyway migrations go in `src/main/resources/db/migration/`
- Dev/mock data goes in `src/main/resources/db/testdata/` (only active with `local` profile)
- Never use `CREATE TABLE IF NOT EXISTS` in Flyway migrations — let Flyway handle idempotency
- Always name constraints: `fk_`, `uq_`, `chk_` prefixes
- Always add indexes on FK columns
- Use `TIMESTAMPTZ` (not `TIMESTAMP`) for timestamp columns

## General
- Always validate changes compile before committing (`mvn compile -q`)
- Never commit secrets or plain-text passwords
- Always ask me before commiting and pushing changes.
