# Gym Management System — Multi-Database Repository

A comprehensive collection of database exercises, SQL queries, relational schemas, NoSQL documents, and programming examples spanning multiple database technologies. This repository covers Oracle SQL/PL-SQL, MongoDB, HackerRank SQL challenges, and LeetCode SQL problems — all unified around a **Gym Management System** domain.

---

## Repository Structure

```
DATABASE/
├── docs/                # Comprehensive setup & explanation documentation
│   ├── mongodb.md       # Detailed guide for MongoDB setup and operations
│   └── oracle.md        # Detailed guide for Oracle schema, PL/SQL and JDBC
├── HackerRank/          # SQL solutions to HackerRank database challenges
├── LeetCode/            # SQL solutions to LeetCode database problems
├── MongoDB/             # MongoDB CRUD and aggregation scripts (JSON logs)
└── Oracle/
    ├── Code/            # Oracle SQL and PL/SQL source scripts
    ├── Console-Outputs/ # SQL*Plus execution transcripts (logs & outputs)
    └── ODBC/            # Java applications connecting via JDBC
```

---

## Contents & Quick Navigation

To help you get the most out of this repository, we have prepared detailed documentation for each database engine. Click on the guides below for extensive explanations and setup steps.

| Technology / Section | Documentation Guide | Description |
|---|---|---|
| 🍃 **MongoDB** | [**MongoDB Setup & Explanation Guide**](./docs/mongodb.md) | JSON Schema validation, CRUD operations, comparison operators, and aggregation pipelines on the `MEMBER` collection. |
| 🔴 **Oracle SQL & PL/SQL** | [**Oracle Setup & Explanation Guide**](./docs/oracle.md) | Relational design, advanced queries (Joins, Subqueries), PL/SQL logic (Stored Functions, Procedures, Triggers), and Java JDBC integration. |
| 🏆 **HackerRank** | [HackerRank Solutions](./HackerRank) | Solutions to intermediate/advanced SQL challenges (MySQL/Oracle dialect) hosted on HackerRank. |
| 💻 **LeetCode** | [LeetCode Solutions](./LeetCode) | Structured SQL queries resolving complex business logic queries on LeetCode. |

---

## Getting Started (Quick Start)

### 1. Oracle SQL & PL/SQL

For full detailed setup, refer to the [Oracle Setup & Explanation Guide](./docs/oracle.md).

1. Launch SQL*Plus and connect to your Oracle instance:
   ```bash
   sqlplus username/password@localhost:1521/XE
   ```
2. Build the relational schema:
   ```sql
   @Oracle/Code/ddl.sql
   ```
3. Populate with dummy records:
   ```sql
   @Oracle/Code/dml.sql
   ```
4. Run analytical queries and PL/SQL scripts:
   ```sql
   @Oracle/Code/viewsandindex.sql
   @Oracle/Code/subqueries.sql
   @Oracle/Code/setsandjoins.sql
   @Oracle/Code/plsqlblock.sql
   @Oracle/Code/plsqlfunctionsandprocedures.sql
   @Oracle/Code/plsqltriggers.sql
   ```

---

### 2. MongoDB Shell (`mongosh`)

For full detailed setup and JSON schema explanations, refer to the [MongoDB Setup & Explanation Guide](./docs/mongodb.md).

1. Connect to your local instance using the MongoDB shell:
   ```bash
   mongosh
   ```
2. Switch to the `gymdb` database:
   ```javascript
   use gymdb
   ```
3. Import the database schema validation rule and execute the CRUD log actions:
   ```bash
   mongosh gymdb < MongoDB/"DDL(Data Definition Language).json"
   ```

---

### 3. Java JDBC Setup

1. Add `ojdbc11.jar` to your classpath.
2. Compile and run the Java database interface:
   ```bash
   # Compile
   javac -cp .:ojdbc11.jar Oracle/ODBC/GymMemberForm.java

   # Run
   java -cp .:ojdbc11.jar GymMemberForm
   ```
   *(For Windows environments, use a semicolon `;` as path separator in place of `:`)*

---

### 4. HackerRank / LeetCode Standalone Queries

These SQL scripts are isolated query solutions. To execute:
- Copy/paste the queries directly to online platforms:
  - [HackerRank SQL Domain](https://www.hackerrank.com/domains/sql)
  - [LeetCode Database Problem Set](https://leetcode.com/problemset/database/)
- Alternatively, load them into a local MySQL/Oracle engine:
  ```bash
  mysql -u root -p < HackerRank/JapaneseCities.sql
  ```

---

## Domain Entity Model: Gym Management System

The core domain across the projects is a modern multi-facility **Gym Management System**. The unified business entity relations include:

* **GYM** – Brand details and corporate headquarters.
* **BRANCH** – Physical gym venues complete with location details and associated staff.
* **MEMBER** – Member registrations tracking profiles, somatic body types, and registered branches.
* **TRAINER** – Certified fitness professionals hosting personal/group sessions.
* **STAFF** – Supporting operations, front-desk, and management personnel.
* **MEMBERSHIP** – Subscription options (e.g. Bronze, Gold, Elite tiers).
* **WORKOUT_PLAN** – Tailored exercise routines mapped to members by trainers.
* **TRAINING_SESSION** – Group classes (Zumba, Powerlifting, HIIT) scheduled regularly.
* **PAYMENT / INVOICE** – Financial records, receipts, and line-item detail statements.
* **SUPPLIER / MACHINE** – Procurement records for equipment maintenance and vendor tracking.

---

## License & Contribution

This project is open-source and intended for educational training and developer portfolio reference. Contributions or suggestions are always welcome!
