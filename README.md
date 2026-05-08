# DATABASE

A comprehensive collection of database exercises, SQL queries, and programming examples spanning multiple database technologies. This repository covers Oracle SQL, MongoDB, HackerRank SQL challenges, and LeetCode SQL problems — all built around a **Gym Management System** domain.

---

## Repository Structure

```
DATABASE/
├── HackerRank/          # SQL solutions to HackerRank database challenges
├── LeetCode/            # SQL solutions to LeetCode database problems
├── MongoDB/             # MongoDB CRUD and aggregation exercises
└── Oracle/
    ├── Code/            # Oracle SQL and PL/SQL scripts
    ├── Console-Outputs/ # SQL*Plus session transcripts with outputs
    └── ODBC/            # Java applications using ODBC/JDBC connectivity
```

---

## Contents

| Section | Technology | Description |
|---|---|---|
| [HackerRank](./HackerRank) | SQL (MySQL / Oracle) | Solutions to HackerRank SQL challenges |
| [LeetCode](./LeetCode) | SQL (MySQL) | Solutions to LeetCode SQL problems |
| [MongoDB](./MongoDB) | MongoDB Shell | CRUD and aggregation on a GymDB collection |
| [Oracle](./Oracle) | Oracle SQL & PL/SQL | Full DDL/DML/PL/SQL exercise set with ODBC integration |

---

## Setup

### Prerequisites

- [Oracle Database 21c+](https://www.oracle.com/database/technologies/) or Oracle XE with SQL\*Plus
- [MongoDB 6.0+](https://www.mongodb.com/try/download/community) with `mongosh` on macOS, Ubuntu, or Windows
- [JDK 11+](https://adoptium.net/) for the ODBC/JDBC examples
- Any MySQL-compatible client (e.g., [MySQL 8.0+](https://dev.mysql.com/downloads/)) for HackerRank / LeetCode queries

---

### Oracle Setup

1. **Install Oracle Database** (XE edition is free):
   ```
   https://www.oracle.com/database/technologies/xe-downloads.html
   ```

2. **Launch SQL\*Plus**:
   ```bash
   sqlplus username/password@localhost:1521/XE
   ```

3. **Run the DDL script** to create all tables:
   ```sql
   @Oracle/Code/ddl.sql
   ```

4. **Populate data** with the DML script:
   ```sql
   @Oracle/Code/dml.sql
   ```

5. **Execute further scripts** as needed (views, subqueries, PL/SQL blocks, etc.):
   ```sql
   @Oracle/Code/viewsandindex.sql
   @Oracle/Code/subqueries.sql
   @Oracle/Code/setsandjoins.sql
   @Oracle/Code/plsqlblock.sql
   @Oracle/Code/plsqlfunctionsandprocedures.sql
   @Oracle/Code/plsqltriggers.sql
   ```

> Console outputs for every script are available in `Oracle/Console-Outputs/` for reference.

---

### MongoDB Setup

1. **Install MongoDB** and start `mongod`:
   ```bash
   # macOS (Homebrew)
   brew install mongodb-community
   brew services start mongodb-community

   # Ubuntu
   sudo systemctl start mongod

   # Windows (PowerShell, after installing MongoDB Community Server)
   net start MongoDB
   ```

2. **Open the MongoDB shell**:
   ```bash
   mongosh
   ```

3. **Switch to the gym database**:
   ```js
   use gymdb
   ```

4. **Run the DDL file** to create the collection with validation rules and sample data:
   ```bash
   mongosh < MongoDB/"DDL(Data Definition Language).json"
   ```

---

### Java ODBC / JDBC Setup

1. **Add the Oracle JDBC driver** (`ojdbc11.jar`) to your classpath. Download from:
   ```
   https://www.oracle.com/database/technologies/appdev/jdbc-downloads.html
   ```

2. **Compile** a Java source file:
   ```bash
   javac -cp .:ojdbc11.jar Oracle/ODBC/GymMemberForm.java
   ```
   On Windows, use `;` instead of `:` in the classpath:
   ```bat
   javac -cp .;ojdbc11.jar Oracle/ODBC/GymMemberForm.java
   ```

3. **Run** the compiled class:
   ```bash
   java -cp .:ojdbc11.jar GymMemberForm
   ```
   On Windows:
   ```bat
   java -cp .;ojdbc11.jar GymMemberForm
   ```

---

### HackerRank / LeetCode

These are standalone SQL files. Paste the contents of each file directly into the relevant online judge editor:

- **HackerRank SQL**: [https://www.hackerrank.com/domains/sql](https://www.hackerrank.com/domains/sql)
- **LeetCode SQL**: [https://leetcode.com/problemset/database/](https://leetcode.com/problemset/database/)

Alternatively, run them against a local MySQL instance:
```bash
mysql -u root -p < HackerRank/JapaneseCities.sql
```

---

## Domain: Gym Management System

Most exercises in this repository are modelled around a **Gym Management System** database containing the following core entities:

- **GYM** – Top-level gym organisation
- **BRANCH** – Individual gym branches
- **MEMBER** – Gym members with personal and physical details
- **TRAINER** – Trainers assigned to branches
- **STAFF** – Non-trainer staff at branches
- **MEMBERSHIP** – Member subscription plans
- **WORKOUT_PLAN** – Exercise plans assigned to members
- **TRAINING_SESSION** – Scheduled training sessions
- **PAYMENT / INVOICE** – Billing and payment records
- **SUPPLIER / MACHINE** – Equipment and supply management

---

## License

This repository is intended for educational purposes.
