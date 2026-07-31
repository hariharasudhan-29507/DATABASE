# Oracle PL/SQL Developer's Guide

This document catalogs the PL/SQL programming solutions implemented in the Gym Management System. It highlights standard anonymous blocks, custom stored procedures, functions, and active triggers designed to enforce automation, logging, and data integrity.

---

## 1. Anonymous PL/SQL Blocks

Anonymous PL/SQL blocks are used for direct scripting and one-off workflows inside SQL\*Plus.

### Member Count & Summary Script (`plsqlblock.sql`)
This script queries the active member base to fetch and output gym demographics:
- Retrieves specific information like members over a certain age or from specific regions.
- Uses local PL/SQL variables, cursor operations, and `DBMS_OUTPUT.PUT_LINE` to format output.

---

## 2. Stored Procedures

Stored procedures are compiled directly into the Oracle Schema for reuse, encapsulating database updates or reports.

### Get Member Count by Gender
- **Location**: `plsqlfunctionsandprocedures.sql`
- **Objective**: Dynamically fetch count details filtered by gender.
- **Parameters**: `GENDER_IN` (IN), `MEMBER_COUNT` (OUT)
- **Signature**:
  ```sql
  CREATE OR REPLACE PROCEDURE GET_MEMBER_COUNT_BY_GENDER(
      p_gender IN VARCHAR2,
      p_count OUT INTEGER
  );
  ```

### Update Staff Salary
- **Location**: `plsqlfunctionsandprocedures.sql`
- **Objective**: Modifies monthly staff packages based on cumulative experience metrics.
- **Parameters**: `p_staff_id`, `p_new_salary`

---

## 3. Custom Functions

User-defined functions (UDFs) accept arguments, perform complex queries, and return single computations.

### Calculate Monthly Revenue
- **Location**: `plsqlfunctionsandprocedures.sql`
- **Objective**: Calculates total branch income from active subscription payments.
- **Signature**:
  ```sql
  CREATE OR REPLACE FUNCTION CALCULATE_BRANCH_REVENUE(
      p_branch_id IN INTEGER
  ) RETURN FLOAT;
  ```

### Get Trainer Years of Service
- **Objective**: Analyzes training staff durations based on the hire dates.

---

## 4. Active Database Triggers

Triggers automate auditing, cascade operations, and reinforce business validation when rows are modified.

### Salary Level Validation Trigger (`plsqltriggers.sql`)
- **Event**: `BEFORE INSERT OR UPDATE ON STAFF`
- **Objective**: Automatically checks whether a newly entered salary package is valid based on the minimum requirements of roles, preventing underpayment.

### Audit Trial / Membership Tracking Trigger (`plsqltriggers.sql`)
- **Event**: `AFTER UPDATE ON MEMBERSHIP`
- **Objective**: Logs subscription updates, changes in fees, or plan modifications into an archive table for tracking purposes.
