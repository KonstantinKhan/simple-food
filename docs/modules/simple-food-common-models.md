---
type: module-doc
scope: shared-models
language: Kotlin
related:
  - repo-in-memory
  - repo-postgresql
---

# Module `simple-food-common-models`

## Purpose

Defines the **domain model layer** of the project.  
Provides:

- immutable **business entities (`Be*`)** and **repository entities (`Db*`)**,
- type-safe **value objects** for core nutrition metrics,
- and **repository contracts (`IRepo*`)** for data abstraction.

> 💡 **LLM Note:**  
> Use this file as a reference for understanding shared data structures and value objects.  
> If you need repository implementation details, refer to `simple-food-repo-*` modules.
---

## Path

[simple-food-common-models](../../simple-food-common-models)

---

## 🔑 Key Components

> 🧩 **Hint for LLM:**  
> The folder structure defines semantic grouping of models.  
> Each subpackage maps directly to a domain layer or a database DTO set.

### Interfaces

| Name             | Description                                    |
|------------------|------------------------------------------------|
| `IRepoProduct`   | CRUD contract for product repositories         |
| `IDbResponse<T>` | Generic wrapper for database operation results |

### Business Models (`Be*` prefix)

| Entity      | Description                       |
|-------------|-----------------------------------|
| `BeProduct` | Product with nutritional data     |
| `BeDish`    | Dish composed of product portions |
| `BeAuthor`  | Metadata about author/user        |
| `BeError`   | Business layer Error model        |

### Value Objects (`Be*` prefix)

Type-safe, immutable inline classes for domain primitives:

| Name                                                                    | Description           |
|-------------------------------------------------------------------------|-----------------------|
| `BeId`                                                                  | UUID-based identifier |
| `BeWeight` / `BeCalories` / `BeProteins` / `BeFats` / `BeCarbohydrates` | Nutritional metrics   |
| `BeCategories` / `BeCategory`                                           | Category model set    |

### Repository DTOs (`Db*` prefix)

Models for repository interaction layer:

- `DbProduct*Request` — CRUD and filter requests
- `DbProduct*Response` — Single or multiple product responses

---

## 🧩 Design Principles

| Principle                  | Explanation                                                        |
|----------------------------|--------------------------------------------------------------------|
| **Type Safety**            | Inline value classes prevent primitive obsession                   |
| **Immutability**           | All entities are `data class` with `val` properties                |
| **Separation of Concerns** | Distinction between business (`Be*`) and repository (`Db*`) models |
| **Companion Objects**      | Each model defines `NONE` constant for default value               |

---

## 🔗 Related Modules

- [simple-food-product-repo-memory](../../docs/modules/simple-food-product-repo-memory.md) — in-memory repository (data source)
- [simple-food-product-repo-postgresql](../../docs/modules/simple-food-product-repo-postgresql.md) — PostgreSQL repository (persistent
  layer)

> 💬 **LLM Navigation Tip:**  
> Use these links to find where models are instantiated or mapped.  
> For DB mappings, check the corresponding repository module.
---

## ⚙️ Summary for Agents

| Agent            | Usage                                                                   |
|------------------|-------------------------------------------------------------------------|
| `ArchitectAgent` | Understands data flow and model dependencies                            |
| `DevAgent`       | Uses model definitions to generate code and serialization logic         |
| `TestAgent`      | Reads `Be*` and `Db*` definitions to create test fixtures and mock data |

> ⚠️ When generating new models — follow naming and pattern rules defined here.  
> Avoid redefining value classes; reuse existing `Be*` primitives.