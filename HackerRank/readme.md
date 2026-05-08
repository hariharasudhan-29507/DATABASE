# HackerRank — SQL Challenges

This folder contains SQL solutions to selected **HackerRank** database challenges. All queries are written in standard SQL and are compatible with both MySQL and Oracle unless otherwise noted.

---

## Problem List

| File | Problem | Key Concepts |
|---|---|---|
| `JapaneseCities.sql` | Japanese Cities | `WHERE`, string filter |
| `NewCompanies.sql` | New Companies | Multi-table `JOIN`, `COUNT DISTINCT`, `GROUP BY`, `ORDER BY` |
| `PopulatuionCensus.sql` | Population Census | `JOIN`, aggregate `SUM` |
| `TopCompetitors.sql` | Top Competitors | Multi-table `JOIN`, `GROUP BY`, `HAVING`, `ORDER BY` |
| `TypesOfTriange.sql` | Type of Triangle | `CASE WHEN` conditional logic |

---

## Problem Descriptions

### `JapaneseCities.sql`
Retrieves all columns from the `CITY` table for cities located in Japan (`COUNTRYCODE = 'JPN'`).  
Demonstrates basic `SELECT … WHERE` filtering on a string column.

---

### `NewCompanies.sql`
Lists each company's code and founder, together with distinct counts of lead managers, senior managers, managers, and employees across the corporate hierarchy.  
Joins the `Company` and `Employee` tables and uses `COUNT(DISTINCT …)` to avoid duplicates introduced by the denormalised schema. Results are ordered by `company_code`.

---

### `PopulatuionCensus.sql`
Calculates the total population of all cities located on the Asian continent by joining the `CITY` and `COUNTRY` tables on their shared country code and filtering by `CONTINENT = 'Asia'`.  
Demonstrates `JOIN` combined with `SUM` aggregation.

---

### `TopCompetitors.sql`
Identifies hackers who have scored full marks on more than one challenge.  
Joins four tables — `Submissions`, `Challenges`, `Difficulty`, and `Hackers` — matching each submission's score against the maximum possible score for its difficulty level. Results are ordered by submission count (descending) and then hacker ID (ascending).

---

### `TypesOfTriange.sql`
Classifies each row in the `TRIANGLES` table as *Equilateral*, *Isosceles*, *Scalene*, or *Not A Triangle* using a `CASE WHEN` expression that checks both the triangle inequality and side equality conditions.

---

## How to Run

Paste any file's contents into the HackerRank SQL editor for the corresponding problem, or execute locally against a MySQL instance:

```bash
mysql -u root -p < JapaneseCities.sql
```
