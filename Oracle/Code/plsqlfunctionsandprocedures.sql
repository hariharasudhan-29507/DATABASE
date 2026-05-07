1) FUNCTION TO GET TOTAL NUMBER OF MEMBERS (NO PARAMETER)

SQL> CREATE OR REPLACE FUNCTION get_total_members
RETURN NUMBER
IS
v_total NUMBER;
BEGIN
SELECT COUNT(*) INTO v_total
FROM MEMBER;
RETURN v_total;
EXCEPTION
WHEN OTHERS THEN
RETURN 0;
END get_total_members;
/
SQL> BEGIN
DBMS_OUTPUT.PUT_LINE('Total Members: ' || get_total_members);
END;
/

2) FUNCTION TO GET AGE OF A MEMBER (SINGLE IN PARAMETER)

SQL> CREATE OR REPLACE FUNCTION get_member_age(p_member_id IN NUMBER)
RETURN NUMBER
IS
v_age NUMBER;
BEGIN
SELECT AGE INTO v_age
FROM MEMBER
WHERE MEMBER_ID = p_member_id;
RETURN v_age;
EXCEPTION
WHEN NO_DATA_FOUND THEN
RETURN NULL;
WHEN OTHERS THEN
RETURN NULL;
END get_member_age;
/
SQL> DECLARE
n NUMBER;
BEGIN
n :=&n;
DBMS_OUTPUT.PUT_LINE('AGE OF MEMBER ' || n || ' IS ' || get_member_age(n));
END;
/

3) FUNCTION TO COUNT MEMBERS BY BRANCH AND GENDER (MULTIPLE IN PARAMETERS)

SQL> CREATE OR REPLACE FUNCTION get_members_by_branch_gender(
p_branch_id IN NUMBER,
p_gender    IN VARCHAR2
) RETURN NUMBER
IS
v_count NUMBER;
BEGIN
SELECT COUNT(*) INTO v_count
FROM MEMBER
WHERE BRANCH_ID = p_branch_id
AND GENDER = p_gender;
RETURN v_count;
END get_members_by_branch_gender;
/
SQL> DECLARE
bid NUMBER;
gen VARCHAR2(10);
BEGIN
bid :=&bid;
gen :=&gen;
DBMS_OUTPUT.PUT_LINE('COUNT: ' || get_members_by_branch_gender(bid, gen));
END;
/

4) FUNCTION TO RETURN FULL TRAINER RECORD (RETURN ROWTYPE)

SQL> CREATE OR REPLACE FUNCTION get_trainer_record(p_trainer_id IN NUMBER)
RETURN TRAINER%ROWTYPE
IS
v_trainer TRAINER%ROWTYPE;
BEGIN
SELECT * INTO v_trainer
FROM TRAINER
WHERE TRAINER_ID = p_trainer_id;
RETURN v_trainer;
EXCEPTION
WHEN NO_DATA_FOUND THEN RETURN NULL;
WHEN OTHERS THEN RETURN NULL;
END get_trainer_record;
/
SQL> DECLARE
n     NUMBER;
v_rec TRAINER%ROWTYPE;
BEGIN
n :=&n;
v_rec := get_trainer_record(n);
IF v_rec.TRAINER_ID IS NOT NULL THEN
DBMS_OUTPUT.PUT_LINE('NAME   : ' || v_rec.TRAINER_NAME);
DBMS_OUTPUT.PUT_LINE('SALARY : ' || v_rec.SALARY);
DBMS_OUTPUT.PUT_LINE('EXP    : ' || v_rec.EXPERIENCE);
END IF;
END;
/

5) FUNCTION TO RETURN ALL ACTIVE MEMBERSHIPS (RETURN REF CURSOR)

SQL> CREATE OR REPLACE FUNCTION get_active_memberships_cursor
RETURN SYS_REFCURSOR
IS
v_cursor SYS_REFCURSOR;
BEGIN
OPEN v_cursor FOR
SELECT * FROM MEMBERSHIP
WHERE END_DATE >= SYSDATE;
RETURN v_cursor;
END get_active_memberships_cursor;
/
SQL> DECLARE
v_cur    SYS_REFCURSOR;
v_mid    MEMBERSHIP.MEMBERSHIP_ID%TYPE;
v_memid  MEMBERSHIP.MEMBER_ID%TYPE;
v_branch MEMBERSHIP.BRANCH_ID%TYPE;
v_dur    MEMBERSHIP.DURATION%TYPE;
v_type   MEMBERSHIP.TYPE%TYPE;
v_amount MEMBERSHIP.AMOUNT%TYPE;
v_start  MEMBERSHIP.START_DATE%TYPE;
v_end    MEMBERSHIP.END_DATE%TYPE;
BEGIN
v_cur := get_active_memberships_cursor;
DBMS_OUTPUT.PUT_LINE('MEMBERSHIP_ID | MEMBER_ID | TYPE        | AMOUNT');
DBMS_OUTPUT.PUT_LINE('------------------------------------------------');
LOOP
FETCH v_cur INTO v_mid, v_memid, v_branch, v_dur, v_type, v_amount, v_start, v_end;
EXIT WHEN v_cur%NOTFOUND;
DBMS_OUTPUT.PUT_LINE(v_mid || ' | ' || v_memid || ' | ' || v_type || ' | ' || v_amount);
END LOOP;
CLOSE v_cur;
END;
/

6) FUNCTION TO GET TOTAL TRAINER SALARY EXPENSE (NO PARAMETER)

SQL> CREATE OR REPLACE FUNCTION get_total_salary_expense
RETURN FLOAT
IS
v_total FLOAT;
BEGIN
SELECT SUM(SALARY) INTO v_total
FROM TRAINER;
RETURN NVL(v_total, 0);
EXCEPTION
WHEN OTHERS THEN
RETURN 0;
END get_total_salary_expense;
/
SQL> BEGIN
DBMS_OUTPUT.PUT_LINE('Total Salary Expense: ' || get_total_salary_expense);
END;
/

7) FUNCTION TO GET MEMBERSHIP AMOUNT BY ID (SINGLE IN PARAMETER)

SQL> CREATE OR REPLACE FUNCTION get_membership_amount(p_membership_id IN NUMBER)
RETURN FLOAT
IS
v_amount FLOAT;
BEGIN
SELECT AMOUNT INTO v_amount
FROM MEMBERSHIP
WHERE MEMBERSHIP_ID = p_membership_id;
RETURN v_amount;
EXCEPTION
WHEN NO_DATA_FOUND THEN RETURN 0;
WHEN OTHERS THEN RETURN NULL;
END get_membership_amount;
/
SQL> DECLARE
n NUMBER;
BEGIN
n :=&n;
DBMS_OUTPUT.PUT_LINE('MEMBERSHIP FEE FOR ID ' || n || ' IS ' || get_membership_amount(n));
END;
/

8) FUNCTION TO COUNT PAID PAYMENTS ABOVE AMOUNT IN A BRANCH (MULTIPLE IN PARAMETERS)

SQL> CREATE OR REPLACE FUNCTION get_payments_above_amount(
p_branch_id  IN NUMBER,
p_min_amount IN FLOAT
) RETURN NUMBER
IS
v_count NUMBER;
BEGIN
SELECT COUNT(*) INTO v_count
FROM PAYMENT
WHERE BRANCH_ID = p_branch_id
AND AMOUNT > p_min_amount
AND STATUS = 'PAID';
RETURN v_count;
EXCEPTION
WHEN OTHERS THEN
RETURN 0;
END get_payments_above_amount;
/
SQL> DECLARE
bid NUMBER;
amt FLOAT;
BEGIN
bid :=&bid;
amt :=&amt;
DBMS_OUTPUT.PUT_LINE('PAYMENTS ABOVE ' || amt || ' IN BRANCH ' || bid || ' : ' || get_payments_above_amount(bid, amt));
END;
/

9) FUNCTION TO RETURN FULL STAFF RECORD (RETURN ROWTYPE)

SQL> CREATE OR REPLACE FUNCTION get_staff_record(p_staff_id IN NUMBER)
RETURN STAFF%ROWTYPE
IS
v_staff STAFF%ROWTYPE;
BEGIN
SELECT * INTO v_staff
FROM STAFF
WHERE STAFF_ID = p_staff_id;
RETURN v_staff;
EXCEPTION
WHEN NO_DATA_FOUND THEN RETURN NULL;
WHEN OTHERS THEN RETURN NULL;
END get_staff_record;
/
SQL> DECLARE
n     NUMBER;
v_rec STAFF%ROWTYPE;
BEGIN
n :=&n;
v_rec := get_staff_record(n);
IF v_rec.STAFF_ID IS NOT NULL THEN
DBMS_OUTPUT.PUT_LINE('NAME   : ' || v_rec.STAFF_NAME);
DBMS_OUTPUT.PUT_LINE('SALARY : ' || v_rec.SALARY);
DBMS_OUTPUT.PUT_LINE('CITY   : ' || v_rec.ADDRESS_CITY);
END IF;
END;
/

10) FUNCTION TO RETURN PAYMENTS OF A BRANCH (RETURN REF CURSOR)

SQL> CREATE OR REPLACE FUNCTION get_branch_payments_cursor(p_branch_id IN NUMBER)
RETURN SYS_REFCURSOR
IS
v_cursor SYS_REFCURSOR;
BEGIN
OPEN v_cursor FOR
SELECT PAYMENT_ID, PAYMENT_DATE, STATUS, AMOUNT
FROM PAYMENT
WHERE BRANCH_ID = p_branch_id;
RETURN v_cursor;
END get_branch_payments_cursor;
/
SQL> DECLARE
n        NUMBER;
v_cur    SYS_REFCURSOR;
v_pid    PAYMENT.PAYMENT_ID%TYPE;
v_date   PAYMENT.PAYMENT_DATE%TYPE;
v_status PAYMENT.STATUS%TYPE;
v_amt    PAYMENT.AMOUNT%TYPE;
BEGIN
n :=&n;
v_cur := get_branch_payments_cursor(n);
DBMS_OUTPUT.PUT_LINE('PAYMENT_ID | DATE      | STATUS  | AMOUNT');
DBMS_OUTPUT.PUT_LINE('------------------------------------------');
LOOP
FETCH v_cur INTO v_pid, v_date, v_status, v_amt;
EXIT WHEN v_cur%NOTFOUND;
DBMS_OUTPUT.PUT_LINE(v_pid || ' | ' || TO_CHAR(v_date,'DD-MON-YY') || ' | ' || v_status || ' | ' || v_amt);
END LOOP;
CLOSE v_cur;
END;
/

11) PROCEDURE TO DISPLAY TOTAL MEMBER COUNT (NO PARAMETER)

SQL> CREATE OR REPLACE PROCEDURE show_total_members
IS
v_count NUMBER;
BEGIN
SELECT COUNT(*) INTO v_count FROM MEMBER;
DBMS_OUTPUT.PUT_LINE('TOTAL MEMBERS IN GYM: ' || v_count);
END show_total_members;
/
SQL> EXEC show_total_members;

12) PROCEDURE TO INSERT A NEW MEMBER (IN PARAMETERS)

SQL> CREATE OR REPLACE PROCEDURE add_member(
p_member_id IN NUMBER,
p_branch_id IN NUMBER,
p_name      IN VARCHAR2,
p_gender    IN VARCHAR2,
p_age       IN NUMBER,
p_body_type IN VARCHAR2,
p_weight    IN FLOAT
)
IS
BEGIN
INSERT INTO MEMBER
(MEMBER_ID, BRANCH_ID, MEMBER_NAME, GENDER, AGE, BODY_TYPE, WEIGHT, DATE_OF_JOIN)
VALUES
(p_member_id, p_branch_id, p_name, p_gender, p_age, p_body_type, p_weight, SYSDATE);
COMMIT;
DBMS_OUTPUT.PUT_LINE('Member ' || p_name || ' added successfully.');
END add_member;
/
SQL> EXEC add_member(2001, 102, 'RAVI', 'Male', 26, 'Mesoderm', 72);
SQL> EXEC add_member(2002, 103, 'KAVYA', 'Female', 23, 'Ectoderm', 54);

13) PROCEDURE TO GET MEMBER NAME BY ID (OUT PARAMETER)

SQL> CREATE OR REPLACE PROCEDURE get_member_name(
p_member_id IN  NUMBER,
p_name      OUT VARCHAR2
)
IS
BEGIN
SELECT MEMBER_NAME INTO p_name
FROM MEMBER
WHERE MEMBER_ID = p_member_id;
END get_member_name;
/
SQL> DECLARE
id   NUMBER;
name VARCHAR2(50);
BEGIN
id :=&id;
get_member_name(id, name);
DBMS_OUTPUT.PUT_LINE('NAME: ' || name);
END;
/

14) PROCEDURE TO UPDATE MEMBER WEIGHT (IN OUT PARAMETER)

SQL> CREATE OR REPLACE PROCEDURE update_member_weight(
p_member_id IN     NUMBER,
p_weight    IN OUT VARCHAR2
)
IS
v_new_weight FLOAT := TO_NUMBER(p_weight);
BEGIN
UPDATE MEMBER
SET WEIGHT = v_new_weight
WHERE MEMBER_ID = p_member_id;
SELECT TO_CHAR(WEIGHT) INTO p_weight
FROM MEMBER
WHERE MEMBER_ID = p_member_id;
COMMIT;
END update_member_weight;
/
SQL> DECLARE
id NUMBER;
wt VARCHAR2(20);
BEGIN
id :=&id;
wt :=&wt;
update_member_weight(id, wt);
DBMS_OUTPUT.PUT_LINE('UPDATED WEIGHT FOR MEMBER ' || id || ': ' || wt || ' kg');
END;
/

15) PROCEDURE TO DISPLAY ALL MEMBERS USING CURSOR (OUT REF CURSOR)

SQL> CREATE OR REPLACE PROCEDURE get_members_cursor(
p_cursor OUT SYS_REFCURSOR
)
IS
BEGIN
OPEN p_cursor FOR
SELECT MEMBER_ID, MEMBER_NAME, GENDER, AGE, BODY_TYPE
FROM MEMBER;
END get_members_cursor;
/
SQL> DECLARE
v_cur    SYS_REFCURSOR;
v_id     MEMBER.MEMBER_ID%TYPE;
v_name   MEMBER.MEMBER_NAME%TYPE;
v_gender MEMBER.GENDER%TYPE;
v_age    MEMBER.AGE%TYPE;
v_btype  MEMBER.BODY_TYPE%TYPE;
BEGIN
get_members_cursor(v_cur);
LOOP
FETCH v_cur INTO v_id, v_name, v_gender, v_age, v_btype;
EXIT WHEN v_cur%NOTFOUND;
DBMS_OUTPUT.PUT_LINE(v_id || ' | ' || v_name || ' | ' || v_gender || ' | AGE:' || v_age);
END LOOP;
CLOSE v_cur;
END;
/

16) PROCEDURE TO ADD A SUPPLIER WITH EXCEPTION HANDLING

SQL> CREATE OR REPLACE PROCEDURE add_supplier(
p_supplier_id IN NUMBER,
p_name        IN VARCHAR2,
p_branch_id   IN NUMBER,
p_amount      IN FLOAT,
p_gst         IN VARCHAR2
)
IS
BEGIN
INSERT INTO SUPPLIER
(SUPPLIER_ID, SUPPLIER_NAME, BRANCH_ID, AMOUNT, GST_NUMBER)
VALUES
(p_supplier_id, p_name, p_branch_id, p_amount, p_gst);
COMMIT;
DBMS_OUTPUT.PUT_LINE('Supplier ' || p_name || ' registered.');
EXCEPTION
WHEN DUP_VAL_ON_INDEX THEN
DBMS_OUTPUT.PUT_LINE('Error: Supplier ID already exists.');
WHEN OTHERS THEN
DBMS_OUTPUT.PUT_LINE('Error: ' || SQLERRM);
END add_supplier;
/
SQL> EXEC add_supplier(501, 'FitGear Pvt Ltd', 102, 85000, '27AABCU9603R1ZM');
SQL> EXEC add_supplier(501, 'Duplicate Supplier', 103, 5000, '27AABCU0000R1ZX');

17) PROCEDURE TO RECORD A PAYMENT (IN PARAMETERS)

SQL> CREATE OR REPLACE PROCEDURE record_payment(
p_payment_id IN NUMBER,
p_branch_id  IN NUMBER,
p_amount     IN FLOAT,
p_status     IN VARCHAR2
)
IS
BEGIN
INSERT INTO PAYMENT
(PAYMENT_ID, PAYMENT_DATE, STATUS, BRANCH_ID, AMOUNT)
VALUES
(p_payment_id, SYSDATE, p_status, p_branch_id, p_amount);
COMMIT;
DBMS_OUTPUT.PUT_LINE('Payment ID ' || p_payment_id || ' recorded.');
END record_payment;
/
SQL> EXEC record_payment(5001, 102, 12000, 'PAID');
SQL> EXEC record_payment(5002, 103, 8500, 'PENDING');

18) PROCEDURE TO GET MEMBER COUNT OF A BRANCH (OUT PARAMETER)

SQL> CREATE OR REPLACE PROCEDURE get_branch_member_count(
p_branch_id IN  NUMBER,
p_count     OUT NUMBER
)
IS
BEGIN
SELECT COUNT(*) INTO p_count
FROM MEMBER
WHERE BRANCH_ID = p_branch_id;
END get_branch_member_count;
/
SQL> DECLARE
bid     NUMBER;
v_count NUMBER;
BEGIN
bid :=&bid;
get_branch_member_count(bid, v_count);
DBMS_OUTPUT.PUT_LINE('MEMBERS IN BRANCH ' || bid || ': ' || v_count);
END;
/

19) PROCEDURE TO RENEW MEMBERSHIP BY EXTENDING MONTHS (IN OUT PARAMETER)

SQL> CREATE OR REPLACE PROCEDURE renew_membership(
p_membership_id IN     NUMBER,
p_extra_months  IN OUT VARCHAR2
)
IS
v_months NUMBER := TO_NUMBER(p_extra_months);
BEGIN
UPDATE MEMBERSHIP
SET END_DATE = ADD_MONTHS(END_DATE, v_months)
WHERE MEMBERSHIP_ID = p_membership_id;
SELECT TO_CHAR(END_DATE, 'DD-MON-YYYY')
INTO p_extra_months
FROM MEMBERSHIP
WHERE MEMBERSHIP_ID = p_membership_id;
COMMIT;
END renew_membership;
/
SQL> DECLARE
v_val VARCHAR2(20) := '3';
BEGIN
renew_membership(10001, v_val);
DBMS_OUTPUT.PUT_LINE('New End Date: ' || v_val);
END;
/
SQL> DECLARE
v_val VARCHAR2(20) := '6';
BEGIN
renew_membership(10002, v_val);
DBMS_OUTPUT.PUT_LINE('New End Date: ' || v_val);
END;
/

20) PROCEDURE TO LIST TRAINERS OF A BRANCH USING CURSOR (OUT REF CURSOR)

SQL> CREATE OR REPLACE PROCEDURE get_trainers_by_branch_cursor(
p_branch_id IN  NUMBER,
p_cursor    OUT SYS_REFCURSOR
)
IS
BEGIN
OPEN p_cursor FOR
SELECT T.TRAINER_ID, T.TRAINER_NAME, T.EXPERIENCE, T.SALARY
FROM TRAINER T
JOIN STAFF S ON S.BRANCH_ID = p_branch_id
WHERE T.ADDRESS_CITY = (
SELECT ADDRESS_CITY FROM BRANCH WHERE BRANCH_ID = p_branch_id
);
END get_trainers_by_branch_cursor;
/
SQL> DECLARE
bid    NUMBER;
v_cur  SYS_REFCURSOR;
v_tid  TRAINER.TRAINER_ID%TYPE;
v_name TRAINER.TRAINER_NAME%TYPE;
v_exp  TRAINER.EXPERIENCE%TYPE;
v_sal  TRAINER.SALARY%TYPE;
BEGIN
bid :=&bid;
get_trainers_by_branch_cursor(bid, v_cur);
DBMS_OUTPUT.PUT_LINE('ID  | NAME   | EXP | SALARY');
DBMS_OUTPUT.PUT_LINE('--------------------------------');
LOOP
FETCH v_cur INTO v_tid, v_name, v_exp, v_sal;
EXIT WHEN v_cur%NOTFOUND;
DBMS_OUTPUT.PUT_LINE(v_tid || ' | ' || v_name || ' | Exp: ' || v_exp || ' | ' || v_sal);
END LOOP;
CLOSE v_cur;
END;
/

21) PROCEDURE TO DISPLAY ALL MEMBER IDs, NAMES, GENDER AND MEMBERSHIP TYPE

SQL> CREATE OR REPLACE PROCEDURE member_details(c_cursor OUT SYS_REFCURSOR)
IS
BEGIN
OPEN c_cursor FOR
SELECT M.MEMBER_ID, M.MEMBER_NAME, M.GENDER, MS.TYPE AS MEMBERSHIP_TYPE
FROM MEMBER M
JOIN MEMBERSHIP MS ON M.MEMBER_ID = MS.MEMBER_ID;
END member_details;
/
SQL> DECLARE
v_cursor        SYS_REFCURSOR;
v_member_id     MEMBER.MEMBER_ID%TYPE;
v_member_name   MEMBER.MEMBER_NAME%TYPE;
v_gender        MEMBER.GENDER%TYPE;
v_membership    MEMBERSHIP.TYPE%TYPE;
BEGIN
member_details(v_cursor);
LOOP
FETCH v_cursor INTO v_member_id, v_member_name, v_gender, v_membership;
EXIT WHEN v_cursor%NOTFOUND;
DBMS_OUTPUT.PUT_LINE('ID:'||v_member_id||' NAME:'||v_member_name||' GENDER:'||v_gender||' MEMBERSHIP:'||v_membership);
END LOOP;
END;
/

22) FUNCTION TO DISPLAY MEMBERSHIP TYPE OF ALL MEMBERS

SQL> CREATE OR REPLACE FUNCTION get_membership_types RETURN SYS_REFCURSOR
IS
c_cursor SYS_REFCURSOR;
BEGIN
OPEN c_cursor FOR
SELECT MEMBER_ID, TYPE AS MEMBERSHIP_TYPE, AMOUNT
FROM MEMBERSHIP
WHERE TYPE IS NOT NULL;
RETURN c_cursor;
END get_membership_types;
/
SQL> DECLARE
v_cursor SYS_REFCURSOR;
v_mid    MEMBERSHIP.MEMBER_ID%TYPE;
v_type   MEMBERSHIP.TYPE%TYPE;
v_amount MEMBERSHIP.AMOUNT%TYPE;
BEGIN
v_cursor := get_membership_types;
DBMS_OUTPUT.PUT_LINE('MEMBER_ID   MEMBERSHIP_TYPE   AMOUNT');
LOOP
FETCH v_cursor INTO v_mid, v_type, v_amount;
EXIT WHEN v_cursor%NOTFOUND;
DBMS_OUTPUT.PUT_LINE(v_mid || '        ' || v_type || '        ' || v_amount);
END LOOP;
END;
/

23) FUNCTION TO DISPLAY AVERAGE SALARY FOR EACH CITY

SQL> CREATE OR REPLACE FUNCTION avg_salary_by_city RETURN SYS_REFCURSOR
IS
c_cursor SYS_REFCURSOR;
BEGIN
OPEN c_cursor FOR
SELECT ADDRESS_CITY, AVG(SALARY) AS AVG_SALARY
FROM TRAINER
GROUP BY ADDRESS_CITY;
RETURN c_cursor;
END avg_salary_by_city;
/
SQL> DECLARE
v_cursor SYS_REFCURSOR;
v_salary NUMBER;
v_city   VARCHAR2(30);
BEGIN
v_cursor := avg_salary_by_city;
DBMS_OUTPUT.PUT_LINE('AVG_SALARY    CITY');
LOOP
FETCH v_cursor INTO v_city, v_salary;
EXIT WHEN v_cursor%NOTFOUND;
DBMS_OUTPUT.PUT_LINE(v_salary || '        ' || v_city);
END LOOP;
END;
/

24) PROCEDURE TO DISPLAY THE MEMBER WITH HIGHEST AGE IN EACH CITY

SQL> CREATE OR REPLACE PROCEDURE max_age_per_city
IS
CURSOR city_cur IS
SELECT DISTINCT ADDRESS_CITY FROM MEMBER WHERE ADDRESS_CITY IS NOT NULL;
v_city     MEMBER.ADDRESS_CITY%TYPE;
v_max_age  NUMBER;
v_mem_name MEMBER.MEMBER_NAME%TYPE;
BEGIN
OPEN city_cur;
FETCH city_cur INTO v_city;
-- Outer loop: iterate through each city
WHILE city_cur%FOUND LOOP
v_max_age := 0;
-- Find max age for current city
SELECT MAX(AGE) INTO v_max_age
FROM MEMBER
WHERE ADDRESS_CITY = v_city;
DECLARE
CURSOR mem_cur IS
SELECT MEMBER_NAME FROM MEMBER
WHERE ADDRESS_CITY = v_city AND AGE = v_max_age;
BEGIN
OPEN mem_cur;
FETCH mem_cur INTO v_mem_name;
-- Inner loop: print all members with max age in this city
WHILE mem_cur%FOUND LOOP
DBMS_OUTPUT.PUT_LINE('CITY: ' || v_city || ' | NAME: ' || v_mem_name || ' | AGE: ' || v_max_age);
FETCH mem_cur INTO v_mem_name;
END LOOP;
CLOSE mem_cur;
END;
FETCH city_cur INTO v_city;
END LOOP;
CLOSE city_cur;
END;
/
SQL> EXEC max_age_per_city;

25) PROCEDURE TO RETURN THE BRANCH WITH THE MAXIMUM NUMBER OF MEMBERS

SQL> CREATE OR REPLACE PROCEDURE max_members_branch(
v_out_branch OUT VARCHAR2,
v_out_count  OUT NUMBER
) IS
BEGIN
SELECT ADDRESS_CITY, count_val
INTO v_out_branch, v_out_count
FROM (
SELECT ADDRESS_CITY, COUNT(*) AS count_val
FROM MEMBER
WHERE ADDRESS_CITY IS NOT NULL
GROUP BY ADDRESS_CITY
ORDER BY count_val DESC
)
WHERE ROWNUM = 1;
END;
/
SQL> DECLARE
branch_name VARCHAR2(50);
mem_count   NUMBER;
BEGIN
max_members_branch(branch_name, mem_count);
DBMS_OUTPUT.PUT_LINE('Branch city with most members: ' || branch_name || ' (Count: ' || mem_count || ')');
END;
/

26) FUNCTION TO RETURN DETAILS OF MEMBERS YOUNGER THAN THE AVERAGE AGE

SQL> CREATE OR REPLACE FUNCTION members_below_avg_karthik RETURN SYS_REFCURSOR
IS
v_cursor SYS_REFCURSOR;
BEGIN
OPEN v_cursor FOR
SELECT MEMBER_NAME, AGE, ADDRESS_CITY
FROM MEMBER
WHERE AGE < (
-- Subquery: average age of members in the same city as KARTHIK
SELECT AVG(AGE)
FROM MEMBER
WHERE ADDRESS_CITY = (
SELECT ADDRESS_CITY FROM MEMBER
WHERE MEMBER_NAME = 'KARTHIK' AND ROWNUM = 1
)
);
RETURN v_cursor;
END;
/
SQL> DECLARE
v_res    SYS_REFCURSOR;
v_name   MEMBER.MEMBER_NAME%TYPE;
v_age    MEMBER.AGE%TYPE;
v_city   MEMBER.ADDRESS_CITY%TYPE;
BEGIN
v_res := members_below_avg_karthik;
LOOP
FETCH v_res INTO v_name, v_age, v_city;
EXIT WHEN v_res%NOTFOUND;
DBMS_OUTPUT.PUT_LINE('NAME: ' || v_name || ' | AGE: ' || v_age || ' | CITY: ' || v_city);
END LOOP;
CLOSE v_res;
END;
/
