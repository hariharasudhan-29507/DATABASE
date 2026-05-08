# Oracle/ODBC — Java Database Connectivity (JDBC) Applications

This folder contains Java applications that connect to the Oracle Gym Management System database using the **Oracle JDBC Thin driver**. They demonstrate how to perform database operations programmatically from a Java front-end.

---

## Files

| File | Application | Description |
|---|---|---|
| `GymMemberForm.java` | Gym Member Registration Form | A Java application for inserting, querying, and managing gym member records via JDBC |
| `OnlineShoppingApp.java` | Online Shopping Application | A Java application demonstrating JDBC operations for an e-commerce/shopping workflow |

---

## Application Descriptions

### `GymMemberForm.java` — Gym Member Registration Form

A Java Swing / console application that interfaces with the `MEMBER` table (and related tables) in the Oracle Gym Management System database. Core features include:

- **JDBC connection setup** using the Oracle Thin driver (`jdbc:oracle:thin:@host:port:SID`)
- **Prepared statements** for parameterised INSERT and UPDATE queries to prevent SQL injection
- **ResultSet handling** for displaying member records retrieved from the database
- **Transaction management** — explicit `commit()` and `rollback()` calls
- **Exception handling** — `SQLException` catch blocks with meaningful error reporting

---

### `OnlineShoppingApp.java` — Online Shopping Application

A Java application demonstrating JDBC connectivity in an e-commerce context. Core features include:

- **Connection pooling fundamentals** using a single `Connection` object lifecycle
- **CRUD operations** — creating, reading, updating, and deleting product or order records
- **Batch updates** — using `addBatch()` and `executeBatch()` for efficient multi-row inserts
- **Metadata queries** — using `DatabaseMetaData` and `ResultSetMetaData` to inspect schema information dynamically

---

## Prerequisites

| Requirement | Details |
|---|---|
| JDK | 11 or higher |
| Oracle JDBC Driver | `ojdbc11.jar` — download from [Oracle JDBC Downloads](https://www.oracle.com/database/technologies/appdev/jdbc-downloads.html) |
| Oracle Database | 21c XE or compatible instance running on `localhost:1521` |

---

## Setup & Compilation

1. **Download** `ojdbc11.jar` and place it in the project directory (or add it to your IDE's classpath).

2. **Update the connection string** inside the Java file to match your Oracle instance:
   ```java
   String url = "jdbc:oracle:thin:@localhost:1521:XE";
   String user = "your_username";
   String password = "your_password";
   ```

3. **Compile**:
   ```bash
   javac -cp .:ojdbc11.jar GymMemberForm.java
   ```

4. **Run**:
   ```bash
   java -cp .:ojdbc11.jar GymMemberForm
   ```

   On Windows, replace `:` with `;` in the classpath:
   ```cmd
   javac -cp .;ojdbc11.jar GymMemberForm.java
   java  -cp .;ojdbc11.jar GymMemberForm
   ```

---

## Key JDBC Concepts Demonstrated

| Concept | API Used |
|---|---|
| Establishing a connection | `DriverManager.getConnection()` |
| Executing static SQL | `Statement.executeQuery()` / `executeUpdate()` |
| Parameterised queries | `PreparedStatement` |
| Iterating result rows | `ResultSet.next()`, `getString()`, `getInt()` |
| Transaction control | `Connection.setAutoCommit(false)`, `commit()`, `rollback()` |
| Releasing resources | `ResultSet.close()`, `Statement.close()`, `Connection.close()` |
