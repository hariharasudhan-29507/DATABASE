# Oracle — Gym Management System

This folder contains a complete Oracle SQL and PL/SQL exercise set modelled around a **Gym Management System** database. It is organised into three sub-folders covering raw SQL/PL/SQL scripts, SQL\*Plus console transcripts, and Java ODBC/JDBC applications.

---

## Folder Structure

```
Oracle/
├── Code/                   # SQL and PL/SQL source scripts
├── Console-Outputs/        # SQL*Plus session transcripts (commands + outputs)
├── docs/                   # Markdown guides covering advanced database topics
└── ODBC/                   # Java applications connecting via ODBC/JDBC
```

| Sub-folder | Contents | Reference Guides |
|---|---|---|
| [`Code/`](./Code) | Runnable `.sql` scripts covering DDL, DML, Views, Joins, Subqueries, and PL/SQL | • [Schema Design](./docs/schema_design.md)<br>• [PL/SQL Developer's Guide](./docs/plsql_guide.md) |
| [`Console-Outputs/`](./Console-Outputs) | `.txt` transcripts showing SQL\*Plus prompts and query results | • [Console README](./Console-Outputs/README.md) |
| [`docs/`](./docs) | Fully formatted developer and system administration guides | • [Queries, Joins, and Views](./docs/queries_and_views.md) |
| [`ODBC/`](./ODBC) | Java source files demonstrating JDBC connectivity to Oracle | • [ODBC README](./ODBC/README.md) |

---

## Domain: Gym Management System

The database schema models a multi-branch gym organisation with the following core tables:

| Table | Description |
|---|---|
| `GYM` | Top-level gym entity |
| `BRANCH` | Individual gym locations |
| `MEMBER` | Gym members (personal and physical details) |
| `TRAINER` | Trainers assigned to branches |
| `STAFF` | Non-trainer staff |
| `MEMBERSHIP` | Member subscription plans |
| `WORKOUT_PLAN` | Exercise plans linked to members and trainers |
| `TRAINING_SESSION` | Scheduled training sessions |
| `ENROLLMENT_SESSION` | Member–session enrolment records |
| `MACHINE` | Gym equipment and associated rates |
| `PAYMENT` | Branch payment records |
| `INVOICE / INVOICE_LINE` | Invoice header and line-item details |
| `CARD / CASH` | Payment modes |
| `SUPPLIER` | Supplier records per branch |

For a complete and deep explanation of keys, constraints, and relational design, see the [Schema Design Guide](./docs/schema_design.md).

---

## Getting Started

1. Start SQL\*Plus and connect to your Oracle instance:
   ```bash
   sqlplus username/password@localhost:1521/XE
   ```

2. Run scripts in the following recommended order:
   ```sql
   @Code/ddl.sql
   @Code/dml.sql
   @Code/viewsandindex.sql
   @Code/subqueries.sql
   @Code/setsandjoins.sql
   @Code/plsqlblock.sql
   @Code/plsqlfunctionsandprocedures.sql
   @Code/plsqltriggers.sql
   ```

3. Compare your output against the reference transcripts in `Console-Outputs/`.
