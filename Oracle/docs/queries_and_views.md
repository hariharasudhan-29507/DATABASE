# Oracle Queries, Joins, and Views

This document details the complex data retrieval strategies, relational joins, subqueries, indexes, and database views used in the Gym Management System.

---

## 1. Relational Joins & Set Operations

Gym reporting requires blending multiple tables to extract branch metrics, member subscriptions, and class scheduling details.

### Joins Used
- **INNER JOIN**: Joins `MEMBER`, `MEMBERSHIP`, and `BRANCH` to retrieve active memberships per location.
- **LEFT OUTER JOIN**: Displays all `TRAINER` information, linking their scheduled `WORKOUT_PLAN` details (even if some trainers have no active assignments).
- **RIGHT OUTER JOIN**: Displays invoice information alongside corresponding receipt files.
- **FULL OUTER JOIN**: Synthesizes payment and invoice profiles to identify mismatched financial records.

### Set Operations (`setsandjoins.sql`)
- **UNION**: Merges address/contact lists of `TRAINER` and `STAFF` into a unified directory of personnel.
- **INTERSECT**: Finds members who both have an active workout plan and are enrolled in a specialty training session.
- **MINUS**: Lists trainers who have not been assigned any specialization or classes yet.

---

## 2. Subqueries

Subqueries are used in various forms to calculate performance thresholds, filter statistics, and perform nested queries.

### Nested & Correlated Subqueries (`subqueries.sql`)
- **Single-Row Subquery**: Selects all members whose weight exceeds the average weight of the entire gym.
- **Multi-Row Subquery (using `IN` / `ANY` / `ALL`)**: Finds branches containing staff salaries higher than any trainer's salary.
- **Correlated Subquery**: Lists staff members who earn more than the average salary of their specific branch:
  ```sql
  SELECT S.STAFF_NAME, S.SALARY, S.BRANCH_ID
  FROM STAFF S
  WHERE S.SALARY > (
      SELECT AVG(SUB.SALARY)
      FROM STAFF SUB
      WHERE SUB.BRANCH_ID = S.BRANCH_ID
  );
  ```

---

## 3. Database Views and Indexes

To optimize frequent queries and simplify report generation, several views and indexes are created.

### Views (`viewsandindex.sql`)
- **`MEMBER_PLAN_DETAILS`**: Combines member, branch, and active workout plans into a virtual table. This hides the complex JOIN logic from the application tier.
- **`BRANCH_REVENUE_SUMMARY`**: Aggregates payments and membership dues grouped by branch, allowing management to track sales.

### Optimization via Indexes
Indexes improve search response times on high-frequency filter paths:
- **Single-Column Index**: Created on `MEMBER_NAME` for fast lookups by name.
- **Composite Index**: Created on `(ADDRESS_CITY, ADDRESS_STREET)` for quickly retrieving region-specific demographics.
- **Unique Index**: Ensures clean, non-duplicate entries for external registration tokens.
