# MongoDB — Gym Management System

This folder contains MongoDB shell exercises built around a **Gym Management System** (`gymdb`) database. The exercises cover collection creation with schema validation, CRUD operations, sorting, filtering, and aggregation pipelines.

---

## Folder Structure

```
MongoDB/
├── docs/                             # Exhaustive guides for schema rules & operations
│   ├── operations_guide.md           # Full CRUD operations & aggregation guide
│   └── schema_validation.md          # Schema validator configuration and rules
└── DDL(Data Definition Language).json # Full MongoDB shell session setup & queries
```

---

## Files and Guides

| Path | Description | Documentation Links |
|---|---|---|
| `DDL(Data Definition Language).json` | Full MongoDB shell session — collection setup, inserts, updates, deletes, queries, and aggregations | • [How to Run](#how-to-run) |
| `docs/schema_validation.md` | Deep dive into `$jsonSchema` rules, types, mandatory fields, and enum constraints | • [Schema Validation Guide](./docs/schema_validation.md) |
| `docs/operations_guide.md` | Complete reference for CRUD operations, query operators, and aggregation pipelines | • [Operations Guide](./docs/operations_guide.md) |

---

## Database: `gymdb`

All operations run against the `gymdb` database and its `MEMBER` collection.

---

## Collection: `MEMBER`

The `MEMBER` collection is created with a **JSON Schema validator** enforcing the following rules:

| Field | Type | Constraints |
|---|---|---|
| `member_id` | int | Required |
| `member_name` | string | Required |
| `gender` | string | Required; one of `Male`, `Female`, `Other` |
| `age` | int | Required; minimum value `1` |
| `body_type` | string | Required; one of `Mesoderm`, `Endoderm`, `Ectoderm` |
| `branch_id` | int | Optional |
| `weight` | double | Optional |
| `date_of_join` | date | Optional |
| `address_city` | string | Optional |
| `address_street` | string | Optional |
| `address_pincode` | string | Optional |

To read more about specific schema constraints, see the [Schema Validation Guide](./docs/schema_validation.md).

---

## Topics Covered

### Collection Management
- Creating a collection with `$jsonSchema` validation rules using `db.createCollection()`

### Insert Operations
- **`insertOne()`** — Insert a single member document
- **`insertMany()`** — Insert multiple member documents in one call

### Update Operations
- **`updateOne()` with `$inc`** — Increment a numeric field (e.g., increase weight by 5)
- **`updateOne()` with `$set`** — Replace a field value (e.g., change body type)
- **`updateOne()` with `$rename`** — Rename a field key
- **`updateMany()` with `$inc`** — Apply an increment to all documents

### Replace Operations
- **`replaceOne()`** — Replace an entire document while keeping its `_id`

### Query Operations
- **`find()`** — Retrieve all documents
- **`find({ field: value })`** — Filter by exact match
- **`$or`** — Match documents satisfying at least one condition
- **`$gt` / `$lt`** — Range comparisons (greater than / less than)
- **`$in` / `$nin`** — Match (or exclude) documents where a field value is within a list
- **`sort()`** — Sort results ascending (`1`) or descending (`-1`)

### Delete Operations
- **`deleteOne()`** — Remove the first document matching a filter
- **`deleteMany()`** — Remove all documents matching a filter (including all documents when passed an empty filter `{}`)

### Aggregation Pipeline
- **`$group` with `$sum`** — Count members grouped by `body_type`
- **`$group` with `$max` / `$min`** — Find maximum and minimum weight per gender

To view precise examples of execution and usage commands, see the [Operations Guide](./docs/operations_guide.md).

---

## How to Run

1. Start `mongosh` and switch to the gym database:
   ```bash
   mongosh
   use gymdb
   ```

2. Copy and paste commands from `DDL(Data Definition Language).json` directly into the shell, or load the file:
   ```bash
   mongosh gymdb < "DDL(Data Definition Language).json"
   ```
