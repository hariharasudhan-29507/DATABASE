# Oracle Schema Design

This document details the database schema for the **Gym Management System**, implemented in Oracle SQL. The schema is highly modular and covers gyms, branches, members, trainers, staffs, memberships, training sessions, invoicing, payments, and equipment suppliers.

---

## Entity-Relationship & Table Schema

### 1. GYM
The top-level entity representing the gym brand or organization.
- `GYM_ID` (INTEGER, Primary Key): Unique identifier for the gym brand.
- `GYM_NAME` (VARCHAR2(50)): Name of the gym.

### 2. BRANCH
Represents specific physical gym locations owned by a gym brand.
- `BRANCH_ID` (INTEGER, Primary Key): Unique identifier for the branch.
- `BRANCH_NAME` (VARCHAR2(50)): Name/Location description of the branch.
- `GYM_ID` (INTEGER, Foreign Key): References `GYM(GYM_ID)`.
- `ADDRESS_CITY` (VARCHAR2(30)): City where the branch is located.
- `ADDRESS_STREET` (VARCHAR2(30)): Street name of the branch.
- `ADDRESS_PINCODE` (VARCHAR2(10)): Pincode/Zip of the branch.

### 3. MEMBER
Represents registered gym members with their personal and physical attributes.
- `MEMBER_ID` (INTEGER, Primary Key): Unique member ID.
- `BRANCH_ID` (INTEGER, Foreign Key): References `BRANCH(BRANCH_ID)`.
- `MEMBER_NAME` (VARCHAR2(50)): Full name of the member.
- `GENDER` (VARCHAR2(10)): Must be one of `'MALE'`, `'FEMALE'`, or `'OTHER'`.
- `AGE` (INTEGER): Must be greater than 0.
- `BODY_TYPE` (VARCHAR2(20)): Must be one of `'MESODERM'`, `'ENDODERM'`, or `'ECTODERM'`.
- `WEIGHT` (FLOAT): Weight of the member (altered from INTEGER to FLOAT).
- `DATE_OF_JOIN` (DATE): Joining date of the member (renamed from `JOINING_DATE`).
- `ADDRESS_CITY` (VARCHAR2(30)): City address.
- `ADDRESS_STREET` (VARCHAR2(30)): Street address.
- `ADDRESS_PINCODE` (VARCHAR2(10)): Pincode.

### 4. TRAINER
Contains information about fitness trainers employed by the system.
- `TRAINER_ID` (INTEGER, Primary Key): Unique trainer ID.
- `TRAINER_NAME` (VARCHAR2(50)): Full name.
- `DATE_OF_BIRTH` (DATE): DOB.
- `AGE` (INTEGER): Calculated age.
- `JOINING_DATE` (DATE): Employee hire date.
- `EXPERIENCE` (INTEGER): Years of experience.
- `SALARY` (FLOAT): Monthly salary package.
- `ADDRESS_CITY` (VARCHAR2(30)), `ADDRESS_STREET` (VARCHAR2(30)), `ADDRESS_PINCODE` (VARCHAR2(10))

### 5. STAFF
Non-trainer operational staff working at specific branches (e.g., receptionists, floor managers).
- `STAFF_ID` (INTEGER, Primary Key): Unique staff ID.
- `BRANCH_ID` (INTEGER, Foreign Key): References `BRANCH(BRANCH_ID)`.
- `STAFF_NAME` (VARCHAR2(50)): Full name.
- `DATE_OF_BIRTH` (DATE): DOB.
- `AGE` (INTEGER): Staff age; must be at least 10 years old (`CHK_STAFF_AGE`).
- `JOINING_DATE` (DATE): Employee hire date.
- `EXPERIENCE` (INTEGER): Years of experience.
- `SALARY` (FLOAT): Monthly salary.
- `ADDRESS_CITY` (VARCHAR2(30)), `ADDRESS_STREET` (VARCHAR2(30)), `ADDRESS_PINCODE` (VARCHAR2(10))

### 6. SUPPLIER
Details about third-party suppliers supplying equipment or supplements to branches.
- `SUPPLIER_ID` (INTEGER, Primary Key): Unique supplier ID.
- `SUPPLIER_NAME` (VARCHAR2(50)): Company/Supplier name.
- `BRANCH_ID` (INTEGER, Foreign Key): References `BRANCH(BRANCH_ID)`.
- `AMOUNT` (FLOAT): Transaction amount.
- `GST_NUMBER` (VARCHAR2(20)): Tax/GST identification number.

### 7. PAYMENT
The primary record for client billing and incoming funds.
- `PAYMENT_ID` (INTEGER, Primary Key): Unique payment ID.
- `PAYMENT_DATE` (DATE): Date of payment.
- `STATUS` (VARCHAR2(20)): Must be one of `'PAID'`, `'PENDING'`, or `'FAILED'`.
- `BRANCH_ID` (INTEGER, Foreign Key): References `BRANCH(BRANCH_ID)`.
- `AMOUNT` (FLOAT): Total payment amount.

### 8. CARD & CASH
Specialized modes of payment utilizing table inheritance relationships with `PAYMENT`.
- **CARD**:
  - `CARD_ID` (INTEGER, Primary Key).
  - `MODES` (VARCHAR2(20)).
  - `PAYMENT_ID` (INTEGER, Foreign Key): References `PAYMENT(PAYMENT_ID)`.
- **CASH**:
  - `PAYMENT_ID` (INTEGER, Primary Key & Foreign Key): References `PAYMENT(PAYMENT_ID)`.
  - `BALANCE` (FLOAT).
  - `RECEIVED_AMOUNT` (FLOAT).

### 9. INVOICE & INVOICE_LINE
Handles structured invoicing for records.
- **INVOICE**:
  - `INVOICE_ID` (INTEGER, Primary Key).
  - `TAX` (FLOAT).
  - `INVOICE_DATE` (DATE).
  - `TOTAL_AMOUNT` (FLOAT).
  - `PAYMENT_ID` (INTEGER, Foreign Key): References `PAYMENT(PAYMENT_ID)`.
- **INVOICE_LINE**:
  - `LINE_ID` (INTEGER).
  - `INVOICE_ID` (INTEGER, NOT NULL, Foreign Key): References `INVOICE(INVOICE_ID)`.
  - `QUANTITY` (INTEGER).
  - `LINE` (VARCHAR2(50)).
  - `UNIT_PRICE` (FLOAT).

### 10. MEMBERSHIP
Subscription levels indicating a member's valid period and plan type at a branch.
- `MEMBERSHIP_ID` (INTEGER, Part of Composite PK).
- `MEMBER_ID` (INTEGER, Part of Composite PK, Foreign Key): References `MEMBER(MEMBER_ID)`.
- `BRANCH_ID` (INTEGER, Foreign Key): References `BRANCH(BRANCH_ID)`.
- `DURATION` (INTEGER): Number of months.
- `TYPE` (VARCHAR2(20)): Plan class.
- `AMOUNT` (FLOAT): Registration/Subscription fee.
- `START_DATE` (DATE).
- `END_DATE` (DATE): Check constraint enforces `END_DATE > START_DATE`.

### 11. WORKOUT_PLAN & TAKES_WORKOUT_PLAN
Defines customized routine plans.
- **TAKES_WORKOUT_PLAN**:
  - `MEMBER_ID` (INTEGER, Foreign Key): References `MEMBER(MEMBER_ID)`.
  - `PLAN_ID` (INTEGER, Foreign Key): References `WORKOUT_PLAN(PLAN_ID)`.
  - `START_DATE` (DATE).
  - `END_DATE` (DATE).

### 12. TRAINER_SPECIALISATION
Captures one-to-many professional certificates or expertises per trainer.
- `TRAINER_ID` (INTEGER, Foreign Key): References `TRAINER(TRAINER_ID)`.
- `SPECIALISATION` (VARCHAR2(30)): Expertise topic.

---

## Referential Integrity and Check Constraints
1. **`FK_BRANCH_GYM`**: Ensures branch links to a valid Gym.
2. **`CHK_MEMBER_GENDER`**: Restricts `GENDER` to `('MALE', 'FEMALE', 'OTHER')`.
3. **`CHK_MEMBER_AGE`**: Guarantees member age is positive.
4. **`CHK_MEMBER_BODYTYPE`**: Restricts `BODY_TYPE` to `('MESODERM', 'ENDODERM', 'ECTODERM')`.
5. **`CHK_STAFF_AGE`**: Operational staff must be at least 10 years old.
6. **`CHK_PAYMENT_STATUS`**: Billing statuses must strictly be `('PAID', 'PENDING', 'FAILED')`.
7. **`CHK_MEMBERSHIP_DATE`**: Subscription start must precede the end date (`END_DATE > START_DATE`).
