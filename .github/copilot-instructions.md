# Copilot Instructions for online-shop-ghetze

## Commands

- Compile: `mvn compile -q`
- Run all tests: `mvn test`
- Run single test: `mvn test -Dtest=ClassName`
- Run app: `mvn spring-boot:run "-Dspring-boot.run.profiles=local"`

## Architecture

Package root: `ro.msg.learning.shop`

- `entity/` — JPA entities; `entity/base/` holds `AbstractBaseEntity`; `entity/embeddable/` holds `@Embeddable` types
- `enums/` — Java enums mapped as `VARCHAR` with DB `CHECK` constraints
- `repository/` — Spring Data JPA repositories (no service/controller layer yet)

Domain model: `Product` → `ProductCategory`; `Stock` (product × location × quantity); `Order` → `OrderDetail` (line items with `shippedFrom` location); `User` with `UserRole` enum.

`Address` is `@Embeddable`, reused in both `Order` and `Location` with prefixed columns (`address_country`, `address_city`, `address_county`, `address_street_address`).

All tables live in the `online_shop` PostgreSQL schema. `spring.jpa.hibernate.ddl-auto=validate` — Hibernate validates; Flyway owns DDL.

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
- Enum fields map to `VARCHAR` columns backed by a DB `CHECK` constraint (see `UserRole`)
- `@Embeddable` types use column-name prefixes matching the DB schema (e.g., `address_`)
- `stocks.quantity` and `order_details.quantity` have `CHECK (quantity > 0)` — delete rows instead of zeroing

## Database Migrations
- Migrations: `V{N}__description.sql` in `src/main/resources/db/migration/`; testdata: `V{N}.{minor}__description.sql` in `src/main/resources/db/testdata/` (local profile only)
- Never use `CREATE TABLE IF NOT EXISTS` in Flyway migrations — let Flyway handle idempotency
- Always name constraints: `fk_`, `uq_`, `chk_` prefixes
- Always add indexes on FK columns
- Use `TIMESTAMPTZ` (not `TIMESTAMP`) for timestamp columns

## General
- Always validate changes compile before committing (`mvn compile -q`)
- Never commit secrets or plain-text passwords
- Always ask me before commiting and pushing changes.

# ULTRA LOW TOKEN MODE

## CORE

- Minimal output only.
- Code only.
- No prose.
- No explanations.
- No summaries.
- No repetition.
- No context restating.
- No markdown unless required.
- No alternatives.
- No suggestions.
- No examples.
- No docs.
- No TODOs.

## OUTPUT

- Output only changed code.
- Output only changed methods.
- Never output full files.
- Never output unchanged lines.
- Never output imports unless changed.
- Never output package statements unless changed.
- Prefer unified diff format.
- Keep output under 50 lines.
- Stop immediately after solution.

## HARD RULES

- Max 30 lines preferred.
- Max 80 lines absolute.
- 0 explanations by default.
- 0 duplicate code.
- 0 unchanged code.
- 0 boilerplate.

## QUALITY GUARDRAILS

- Keep output minimal, but do not sacrifice correctness.
- Preserve existing project style, architecture, and naming conventions.
- Prefer small, focused changes over broad refactors.
- Keep security and data safety rules intact.
- Run validation only when risk is non-trivial (build/tests as needed).
- Report only critical blockers or risks in 1-2 lines.
- Add code comments only for non-obvious logic.
