# Oracle/Console-Outputs — SQL\*Plus Session Transcripts

This folder contains full SQL\*Plus session transcripts for every topic covered in the [`Code/`](../Code) folder. Each file records the exact commands entered and the output produced by the Oracle database engine, making it straightforward to verify expected results without running the scripts yourself.

---

## File Overview

| File | Corresponding Script | Contents |
|---|---|---|
| `DDL(Data Definition Language).txt` | `Code/ddl.sql` | Table creation, constraint additions, ALTER and DROP operations with SQL\*Plus feedback |
| `DML(Data Manipulation Language).txt` | `Code/dml.sql` | INSERT, UPDATE, DELETE statements with row-count confirmations and SELECT verification queries |
| `ViewsAndIndex.txt` | `Code/viewsandindex.sql` | View and index creation messages, plus SELECT results from the created views |
| `SubQueries.txt` | `Code/subqueries.sql` | Subquery execution with full result sets |
| `SetsAndJoins.txt` | `Code/setsandjoins.sql` | Set operation and JOIN query results showing combined and filtered row sets |
| `PLSQLBlock.txt` | `Code/plsqlblock.sql` | PL/SQL anonymous block executions with `DBMS_OUTPUT` printed values and exception messages |
| `PLSQLFunctionsAndProcedures.txt` | `Code/plsqlfunctionsandprocedures.sql` | Stored procedure and function compilation confirmations and invocation outputs |
| `PLSLQLTriggers.txt` | `Code/plsqltriggers.sql` | Trigger creation confirmations and DML outputs demonstrating trigger firing |

---

## How to Use These Files

These transcripts serve two purposes:

1. **Reference** — Compare your own SQL\*Plus output against the expected results when working through the exercises.
2. **Study** — Read through the output alongside the source scripts to understand what each statement produces without needing an active database connection.

### Format

Each file follows the SQL\*Plus session format:

```
SQL> <command entered by the user>
<output produced by Oracle>
```

PL/SQL blocks appear as multi-line input terminated by `/`, followed by any `DBMS_OUTPUT` lines and the `PL/SQL procedure successfully completed.` confirmation message.

---

## Notes

- **`set serveroutput on`** must be active in the session for `DBMS_OUTPUT.PUT_LINE` calls to appear; this command is included at the start of each PL/SQL transcript.
- Row counts (e.g., `1 row created.`) confirm successful DML execution.
- ORA- error messages, where present, are intentional and demonstrate exception-handling scenarios.
