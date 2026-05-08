# LeetCode — Database Problems

This folder contains SQL solutions to selected **LeetCode** database problems. The queries are written for MySQL unless otherwise stated.

---

## Problem List

| File | Problem # | Problem Name | Difficulty | Key Concepts |
|---|---|---|---|---|
| `175_CombineTwoTables.sql` | 175 | Combine Two Tables | Easy | `LEFT JOIN` |
| `180_ConsecutiveNumbers.sql` | 180 | Consecutive Numbers | Medium | Self-`JOIN`, `DISTINCT` |

---

## Problem Descriptions

### `175_CombineTwoTables.sql` — Combine Two Tables (Easy)

**Goal:** Report the first name, last name, city, and state for every person in the `Person` table, regardless of whether they have a corresponding address in the `Address` table.

**Approach:** A `LEFT JOIN` from `Person` to `Address` on `personId` ensures that all persons are included in the result even when no address record exists — in which case `city` and `state` are returned as `NULL`.

**Schema:**
```
Person  (personId PK, lastName, firstName)
Address (addressId PK, personId FK, city, state)
```

---

### `180_ConsecutiveNumbers.sql` — Consecutive Numbers (Medium)

**Goal:** Find all numbers that appear at least three times consecutively in the `Logs` table (rows are ordered by ascending `id`).

**Approach:** The `Logs` table is self-joined twice — `l1`, `l2` (id + 1), and `l3` (id + 2) — and only rows where all three consecutive `num` values are equal are kept. `DISTINCT` removes duplicate values from the result.

**Schema:**
```
Logs (id PK AUTO_INCREMENT, num VARCHAR)
```

---

## How to Run

Paste the contents of any file directly into the LeetCode online editor at:

```
https://leetcode.com/problemset/database/
```

Or run against a local MySQL instance after creating the relevant tables:

```bash
mysql -u root -p < 175_CombineTwoTables.sql
```
