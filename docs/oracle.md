# Oracle Setup and Explanation Guide

This guide provides an exhaustive walkthrough for setting up Oracle Database, executing SQL scripts, implementing PL/SQL programming constructs, and establishing Java JDBC connectivity for the **Gym Management System**.

---

## Table of Contents
1. [Prerequisites & Installation](#1-prerequisites--installation)
   - [Oracle Database XE](#oracle-database-xe)
   - [SQL*Plus & Connection](#sqlplus--connection)
2. [Schema Architecture (DDL & DML)](#2-schema-architecture-ddl--dml)
   - [Entity Relationship Breakdown](#entity-relationship-breakdown)
   - [Executing Setup Scripts](#executing-setup-scripts)
3. [Advanced Querying Constructs](#3-advanced-querying-constructs)
   - [Views & Indexes](#views--indexes)
   - [Subqueries](#subqueries)
   - [Sets & Joins](#sets--joins)
4. [PL/SQL Programming](#4-plsql-programming)
   - [PL/SQL Blocks](#plsql-blocks)
   - [Functions & Procedures](#functions--procedures)
   - [Triggers](#triggers)
5. [Java JDBC Connectivity (ODBC/JDBC)](#5-java-jdbc-connectivity-odbcjdbc)
   - [JDBC Driver Selection](#jdbc-driver-selection)
   - [Compilation & Run Guidelines](#compilation--run-guidelines)

---

## 1. Prerequisites & Installation

To run the Oracle SQL and PL/SQL scripts, you need an Oracle Database instance. The free Express Edition (XE) is recommended.

### Oracle Database XE
1. Download **Oracle Database Express Edition (XE)** for your operating system from the [Oracle XE Downloads Page](https://www.oracle.com/database/technologies/xe-downloads.html).
2. Follow the setup wizard instructions. During installation, you will define password credentials for administrative accounts (`SYS`, `SYSTEM`, `PDBADMIN`).
3. Take note of the default port (typically `1521`) and system identifier (SID) or service name (typically `XE` or `XEPDB1`).

### SQL*Plus & Connection
SQL\*Plus is the command-line utility bundled with Oracle Database used for executing SQL statements and PL/SQL blocks.

Launch SQL\*Plus from your terminal or command prompt:
```bash
# Connect using SYSTEM or administrative accounts
sqlplus system/your_password@localhost:1521/XE
```
If you are connecting on a local Windows server with environment variables set up, you can connect as sysdba:
```bash
sqlplus / as sysdba
```

---

## 2. Schema Architecture (DDL & DML)

The schema models a multi-facility **Gym Management System** (`GYMDB`) utilizing Oracle's relational engine.

### Entity Relationship Breakdown

The system models several interacting real-world entities:
- **`GYM`**: Represents the brand or organization.
- **`BRANCH`**: Individual geographical gym branches, linked to a parent `GYM`.
- **`MEMBER`**: Active users of the gym services, having unique attributes (height, weight, body type) and associated with a specific branch.
- **`TRAINER`**: Dedicated professionals who coach members and are associated with a branch.
- **`STAFF`**: Administrative and maintenance support employees.
- **`MEMBERSHIP`**: Subscriptions or membership schemes (e.g. Monthly, Annual).
- **`WORKOUT_PLAN`**: Custom training regimes created by trainers and allocated to members.
- **`TRAINING_SESSION`**: Scheduled activities led by a trainer (e.g. Cardio, Yoga, CrossFit).
- **`ENROLLMENT_SESSION`**: Bridge table tracking which members attend which training sessions.
- **`MACHINE`**: Gym apparatus tracking unit acquisition costs and maintenance rates.
- **`PAYMENT` / `INVOICE`**: Billing management, line items, and transaction logs.
- **`CARD` / `CASH`**: Modality-specific payment details.
- **`SUPPLIER`**: Procurement partners supplying gym branches with equipment and dietary supplements.

### Executing Setup Scripts

To initialize the schema from scratch, run the scripts in sequence inside SQL\*Plus:

```sql
-- 1. Create all Tables and Constraints (DDL)
@Oracle/Code/ddl.sql

-- 2. Populate the Database with Sample Data (DML)
@Oracle/Code/dml.sql
```

*Verification transcripts are provided in `Oracle/Console-Outputs/` as a historical reference to cross-examine output lines and verify that all objects compile/execute without errors.*

---

## 3. Advanced Querying Constructs

### Views & Indexes
Views provide logical abstraction layers over database queries, and indexes speed up query processing.

- **`viewsandindex.sql`**: This script defines virtual tables to simplify standard operations (e.g., combining member details with their enrolled programs) and creates indexes on frequently used filtering attributes (like `MEMBER.member_name` or `BRANCH.branch_id`) to optimize join speeds.

```sql
-- To run Views and Indexes configuration:
@Oracle/Code/viewsandindex.sql
```

### Subqueries
Subqueries allow nesting querying logic to calculate aggregate thresholds dynamically.

- **`subqueries.sql`**: Contains examples of correlated subqueries, nested subqueries in SELECT/WHERE/FROM clauses, and operations with quantified comparison operators (`ANY`, `ALL`, `EXISTS`). For example, finding members who pay more than the average membership plan of their branch.

```sql
-- To run subqueries:
@Oracle/Code/subqueries.sql
```

### Sets & Joins
Retrieving data across highly normalized tables requires multiple join conditions and set operations.

- **`setsandjoins.sql`**: Demonstrates inner joins, outer joins (left, right, full), cross joins, self-joins, and set operators (`UNION`, `UNION ALL`, `INTERSECT`, `MINUS`). Use this script to trace how branch records intersect with payment entries and trainer schedules.

```sql
-- To run joins and set operations:
@Oracle/Code/setsandjoins.sql
```

---

## 4. PL/SQL Programming

Oracle PL/SQL (Procedural Language/Structured Query Language) extends SQL by adding structured constructs, variables, exception handlers, and loops.

### PL/SQL Blocks
Anonymous procedural blocks executed on-demand.

- **`plsqlblock.sql`**: Contains basic PL/SQL syntax showing variable declarations, control loops (`LOOP`, `WHILE`, `FOR`), conditional statements (`IF-THEN-ELSE`), and basic error handling blocks.

```sql
-- To run the anonymous blocks:
@Oracle/Code/plsqlblock.sql
```

### Functions & Procedures
Stored procedures and functions allow you to encapsulate and reuse query logic in the database.

- **`plsqlfunctionsandprocedures.sql`**:
  * **Procedures**: Execute transactional logic (such as updating members' subscription status, recording a transaction, or scheduling a new session).
  * **Functions**: Perform mathematical calculations or status checks (e.g. computing BMI based on height/weight variables, or returning the total active members at a branch) and return a value.

```sql
-- To compile and run functions and procedures:
@Oracle/Code/plsqlfunctionsandprocedures.sql
```

### Triggers
Triggers are stored PL/SQL blocks executed automatically when specific events occur (such as `BEFORE` or `AFTER` an `INSERT`, `UPDATE`, or `DELETE` operation).

- **`plsqltriggers.sql`**: Implements custom business validation rules. Examples:
  * Audit logging: Inserting a record to an audit table when member profiles are modified.
  * Auto-generation sequences: Auto-populating primary IDs.
  * Validation: Preventing member registration if the maximum branch capacity has been reached.

```sql
-- To compile and test triggers:
@Oracle/Code/plsqltriggers.sql
```

---

## 5. Java JDBC Connectivity (ODBC/JDBC)

The `Oracle/ODBC/` directory includes sample Java applications demonstrating direct database connectivity.

### JDBC Driver Selection
For modern JDK environments (JDK 11+), you need the **Oracle Database JDBC Driver (`ojdbc11.jar`)**.
- Download from: [Oracle JDBC Downloads](https://www.oracle.com/database/technologies/appdev/jdbc-downloads.html).
- Keep this driver in your working directory or register it in your environment's classpath.

### Compilation & Run Guidelines

To run the JDBC interfaces:

1. **Verify Connection String**: Ensure the connection string in the Java file matches your local Oracle service (e.g., `jdbc:oracle:thin:@localhost:1521/XE`).
2. **Compile the program**:
   ```bash
   # Unix/macOS
   javac -cp .:ojdbc11.jar Oracle/ODBC/GymMemberForm.java

   # Windows (use semicolon instead)
   javac -cp .;ojdbc11.jar Oracle/ODBC/GymMemberForm.java
   ```
3. **Run the executable class**:
   ```bash
   # Unix/macOS
   java -cp .:ojdbc11.jar GymMemberForm

   # Windows
   java -cp .;ojdbc11.jar GymMemberForm
   ```
