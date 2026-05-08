# Oracle/Code — SQL & PL/SQL Scripts

This folder contains the runnable Oracle SQL and PL/SQL source scripts for the **Gym Management System** database. Execute them in SQL\*Plus or any Oracle-compatible IDE (e.g., SQL Developer, DBeaver).

---

## Script Overview

| File | Category | Description |
|---|---|---|
| `ddl.sql` | DDL | Creates all tables, constraints, and schema alterations |
| `dml.sql` | DML | Inserts, updates, deletes, and transaction control |
| `viewsandindex.sql` | Views & Indexes | Creates views and indexes for common query patterns |
| `subqueries.sql` | Subqueries | Correlated and non-correlated subquery examples |
| `setsandjoins.sql` | Sets & Joins | UNION, INTERSECT, MINUS, and all JOIN types |
| `plsqlblock.sql` | PL/SQL | Anonymous PL/SQL blocks with variables, cursors, loops, and exceptions |
| `plsqlfunctionsandprocedures.sql` | PL/SQL | Stored functions and procedures |
| `plsqltriggers.sql` | PL/SQL | DML triggers (BEFORE / AFTER INSERT, UPDATE, DELETE) |

---

## Detailed Descriptions

### `ddl.sql` — Data Definition Language
Creates the full Gym Management System schema including:

- **Tables:** `GYM`, `BRANCH`, `MEMBER`, `TRAINER`, `STAFF`, `MEMBERSHIP`, `WORKOUT_PLAN`, `TAKES_WORKOUT_PLAN`, `TRAINER_SPECIALISATION`, `PAYMENT`, `INVOICE`, `INVOICE_LINE`, `CARD`, `CASH`, `SUPPLIER`
- **Constraints:** `PRIMARY KEY`, `FOREIGN KEY`, `CHECK` (gender, age, body type, payment status, membership dates)
- **ALTER TABLE:** Adding/dropping columns, renaming columns, modifying data types, adding/dropping constraints
- **Utility DDL:** `TRUNCATE`, `DROP TABLE`, `RENAME`, `CREATE TABLE … AS SELECT`

---

### `dml.sql` — Data Manipulation Language
Populates and maintains the database with:

- **INSERT** statements seeding all tables with realistic gym data
- **UPDATE** statements modifying member, trainer, and payment records
- **DELETE** statements removing specific records
- **COMMIT / ROLLBACK** demonstrating transaction control

---

### `viewsandindex.sql` — Views & Indexes
Demonstrates database object creation for performance and abstraction:

- **Views:** Virtual tables that simplify frequently queried joins (e.g., member–branch view, member–membership summary)
- **Indexes:** B-tree indexes on commonly filtered columns to improve query performance
- **Querying views:** SELECT statements run against the created views

---

### `subqueries.sql` — Subqueries
Covers all major subquery patterns:

- **Scalar subqueries** in `SELECT` and `WHERE` clauses
- **Correlated subqueries** referencing the outer query's row
- **Subqueries with `IN` / `NOT IN`**
- **Subqueries with `EXISTS` / `NOT EXISTS`**
- **Subqueries in `FROM`** (inline views / derived tables)

---

### `setsandjoins.sql` — Set Operations & Joins
Comprehensive coverage of multi-table query techniques:

**Set Operations:**
- `UNION` — distinct rows from two queries
- `UNION ALL` — all rows including duplicates
- `INTERSECT` / `INTERSECT ALL` — common rows
- `MINUS` / `EXCEPT` — rows in the first query not in the second

**Join Types:**
- `CROSS JOIN` — Cartesian product
- `INNER JOIN` (implicit and explicit syntax)
- `NATURAL JOIN`
- `LEFT OUTER JOIN`
- `RIGHT OUTER JOIN`
- `FULL OUTER JOIN`
- **Self-join** — joining a table to itself

---

### `plsqlblock.sql` — PL/SQL Anonymous Blocks
Introduces procedural database programming through anonymous blocks covering:

- **Variable declaration** — scalar (`%TYPE`) and record (`%ROWTYPE`, custom `RECORD`) types
- **DML inside PL/SQL** — `UPDATE` and `INSERT` with `COMMIT`
- **Explicit cursors** — `OPEN`, `FETCH`, `CLOSE`, cursor attributes (`%FOUND`, `%NOTFOUND`, `%ROWCOUNT`, `%ISOPEN`)
- **Implicit cursors** — cursor `FOR` loops without explicit declaration
- **Control flow** — `IF / ELSIF / ELSE`, basic `LOOP … EXIT WHEN`, `FOR` loop, `WHILE` loop
- **Exception handling** — `NO_DATA_FOUND`, `TOO_MANY_ROWS`, `ZERO_DIVIDE`, `VALUE_ERROR`, `OTHERS`, and user-defined exceptions

---

### `plsqlfunctionsandprocedures.sql` — Stored Subprograms
Demonstrates reusable named PL/SQL units:

- **Stored procedures** — `CREATE OR REPLACE PROCEDURE` with `IN`, `OUT`, and `IN OUT` parameters
- **Stored functions** — `CREATE OR REPLACE FUNCTION` returning computed values
- **Calling subprograms** — invoking procedures and functions from anonymous blocks and SQL

---

### `plsqltriggers.sql` — Triggers
Implements automatic database-level logic through:

- **Row-level triggers** — `FOR EACH ROW` firing once per affected row
- **Statement-level triggers** — firing once per DML statement
- **`BEFORE` and `AFTER` triggers** — auditing, validation, and default-value assignment
- **`:NEW` and `:OLD` pseudo-records** — accessing row data before and after modification

---

## Recommended Execution Order

```sql
-- 1. Create schema
@ddl.sql

-- 2. Seed data
@dml.sql

-- 3. Query objects
@viewsandindex.sql
@subqueries.sql
@setsandjoins.sql

-- 4. Procedural extensions
@plsqlblock.sql
@plsqlfunctionsandprocedures.sql
@plsqltriggers.sql
```
