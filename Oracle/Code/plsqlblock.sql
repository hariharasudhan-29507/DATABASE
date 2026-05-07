1) Update ARJUN's weight to 90 in the member table and save the change.

SQL> select member_name,weight from member;
SQL> set serveroutput on;
SQL> DECLARE
v_weight NUMBER;
BEGIN
v_weight := 90;
UPDATE member SET WEIGHT = v_weight WHERE MEMBER_NAME = 'ARJUN';
DBMS_OUTPUT.PUT_LINE('The weight of ARJUN has been updated to: ' || v_weight);
COMMIT;
END;
/
SQL> select member_name,weight from member;

2) Insert member 1007 into session 902 in the enrollment table and save it.

SQL> select member_id from enrollment_session where member_id=1007;
SQL> DECLARE
v_member_id member.MEMBER_ID%TYPE := 1007;
v_session_id training_session.SESSION_ID%TYPE := 902;
BEGIN
INSERT INTO enrollment_session (MEMBER_ID, SESSION_ID) VALUES (v_member_id, v_session_id);
DBMS_OUTPUT.PUT_LINE('Enrollment added for member ' || v_member_id || ' in session ' || v_session_id);
COMMIT;
END;
/
SQL> select member_id,session_id from enrollment_session ;

3) Add up all product rates in the machine table where branch ID is 103 and print the total.

SQL> DECLARE
v_total_rate NUMBER := 0;
BEGIN
SELECT SUM(PRODUCT_RATE) INTO v_total_rate FROM machine WHERE BRANCH_ID = 103;
DBMS_OUTPUT.PUT_LINE('Total product rate for branch 103: ' || v_total_rate);
COMMIT;
END;
/

4) Fetch all column values of ARJUN from the member table using a ROWTYPE variable and print them.

SQL> DECLARE
v_member member%ROWTYPE;
BEGIN
SELECT * INTO v_member FROM member WHERE member_name = 'ARJUN';
DBMS_OUTPUT.PUT_LINE('Member Name: ' || v_member.member_name);
DBMS_OUTPUT.PUT_LINE('City: ' || v_member.address_city);
DBMS_OUTPUT.PUT_LINE('Age: ' || v_member.age);
DBMS_OUTPUT.PUT_LINE('Body Type: ' || v_member.body_type);
DBMS_OUTPUT.PUT_LINE('Weight: ' || v_member.weight);
DBMS_OUTPUT.PUT_LINE('Status: ' || v_member.status);
END;
/

5) Same as Query 4 but using a custom record type instead of ROWTYPE.

SQL> DECLARE
TYPE member_record IS RECORD (
member_name   member.member_name%TYPE,
address_city  member.address_city%TYPE,
age           member.age%TYPE,
body_type     member.body_type%TYPE,
weight        member.weight%TYPE,
status        member.status%TYPE
);
v_member member_record;
BEGIN
SELECT member_name, address_city, age, body_type, weight, status INTO v_member FROM member WHERE member_name = 'ARJUN';
DBMS_OUTPUT.PUT_LINE('Member Name: ' || v_member.member_name);
DBMS_OUTPUT.PUT_LINE('City: ' || v_member.address_city);
DBMS_OUTPUT.PUT_LINE('Age: ' || v_member.age);
DBMS_OUTPUT.PUT_LINE('Body Type: ' || v_member.body_type);
DBMS_OUTPUT.PUT_LINE('Weight: ' || v_member.weight);
DBMS_OUTPUT.PUT_LINE('Status: ' || v_member.status);
END;
/

6) Use a cursor to join four tables and get the machine rate for ARJUN, then print it.

SQL> DECLARE
v_member_name member.member_name%TYPE;
v_product_rate machine.product_rate%TYPE;
CURSOR machine_cursor IS
SELECT m.member_name, mc.product_rate FROM member m JOIN enrollment_session e ON m.member_id = e.member_id JOIN training_session t ON e.session_id = t.session_id JOIN machine mc ON t.branch_id = mc.branch_id WHERE m.member_name = 'ARJUN';
BEGIN
OPEN machine_cursor;
FETCH machine_cursor INTO v_member_name, v_product_rate;
DBMS_OUTPUT.PUT_LINE('Member: ' || v_member_name);
DBMS_OUTPUT.PUT_LINE('Machine Rate: ' || v_product_rate);
CLOSE machine_cursor;
END;
/

7) Use a cursor to join four tables and get the machine rate for ARJUN, then print it.

SQL> DECLARE
v_member_name member.member_name%TYPE;
v_product_rate machine.product_rate%TYPE;
BEGIN
SELECT m.member_name, mc.product_rate INTO v_member_name, v_product_rate FROM member m JOIN enrollment_session e ON m.member_id = e.member_id JOIN training_session t ON e.session_id = t.session_id JOIN machine mc ON t.branch_id = mc.branch_id WHERE m.member_name = 'ARJUN';
DBMS_OUTPUT.PUT_LINE('Member: ' || v_member_name);
DBMS_OUTPUT.PUT_LINE('Machine Rate: ' || v_product_rate);
END;
/

8) Open a cursor, fetch the first two members one by one, and check cursor attributes like FOUND, ROWCOUNT, and ISOPEN.

SQL> DECLARE
v_member_name member.member_name%TYPE;
v_weight member.weight%TYPE;
CURSOR member_cursor IS
SELECT member_name, weight FROM member;
BEGIN
OPEN member_cursor;
FETCH member_cursor INTO v_member_name, v_weight;
IF member_cursor%FOUND THEN
DBMS_OUTPUT.PUT_LINE('First row fetched - Member: ' || v_member_name);
DBMS_OUTPUT.PUT_LINE(' Weight: ' || v_weight);
ELSE
DBMS_OUTPUT.PUT_LINE('No rows found');
END IF;
FETCH member_cursor INTO v_member_name, v_weight;
IF member_cursor%FOUND THEN
DBMS_OUTPUT.PUT_LINE('Second row fetched - Member: ' || v_member_name);
DBMS_OUTPUT.PUT_LINE('Weight: ' || v_weight);
ELSE
DBMS_OUTPUT.PUT_LINE('No more rows available');
END IF;
DBMS_OUTPUT.PUT_LINE('Number of rows fetched so far: ' || member_cursor%ROWCOUNT);
IF member_cursor%ISOPEN THEN
DBMS_OUTPUT.PUT_LINE('Cursor is still open');
ELSE
DBMS_OUTPUT.PUT_LINE('Cursor is closed');
END IF;
CLOSE member_cursor;
END;
/

9) Fetch ARJUN's weight and print whether he is underweight, healthy, or overweight using IF ELSIF conditions.

SQL> DECLARE
v_member_name member.member_name%TYPE;
v_weight member.weight%TYPE;
BEGIN
-- Select the member's name and weight for a specific member (example: ARJUN)
SELECT member_name, weight
INTO v_member_name, v_weight
FROM member
WHERE member_name = 'ARJUN';
IF v_weight < 60 THEN
DBMS_OUTPUT.PUT_LINE(v_member_name || ' is Underweight.');
ELSIF v_weight >= 60 AND v_weight <= 85 THEN
DBMS_OUTPUT.PUT_LINE(v_member_name || ' is Healthy.');
ELSE
DBMS_OUTPUT.PUT_LINE(v_member_name || ' is Overweight.');
END IF;
END;
/

10) Print numbers 1 to 5 using a basic LOOP with an EXIT condition.

SQL> DECLARE
v_num NUMBER := 1;
BEGIN
LOOP
DBMS_OUTPUT.PUT_LINE('Number: ' || v_num);
v_num := v_num + 1;
EXIT WHEN v_num > 5;
END LOOP;
END;
/

11) Print numbers 1 to 5 using a FOR loop.

SQL> BEGIN
FOR v_num IN 1..5 LOOP
DBMS_OUTPUT.PUT_LINE('Number: ' || v_num);
END LOOP;
END;
/
SQL> BEGIN
FOR v_num IN REVERSE 1..5 LOOP
DBMS_OUTPUT.PUT_LINE('Number: ' || v_num);
END LOOP;
END;
/

12) Print odd numbers from 1 to 9 using a FOR loop with a step of 2.

SQL> BEGIN
FOR i IN 1..10 BY 2 LOOP
DBMS_OUTPUT.PUT_LINE('Value of i: ' || i);
END LOOP;
END;
/

13) Print numbers 1 to 5 using a WHILE loop.

SQL> DECLARE
v_num NUMBER := 1;
BEGIN
WHILE v_num <= 5 LOOP
DBMS_OUTPUT.PUT_LINE('Number: ' || v_num);
v_num := v_num + 1;
END LOOP;
END;
/

14) Print all member names by looping through a declared cursor using a FOR loop.

SQL> DECLARE
CURSOR member_cursor IS
SELECT member_name FROM member;
BEGIN
FOR member_record IN member_cursor LOOP
DBMS_OUTPUT.PUT_LINE('Member Name: ' || member_record.member_name);
END LOOP;
END;
/

15) Print all member names by looping without declared cursor using a FOR loop.

SQL> BEGIN
FOR member_record IN (SELECT member_name FROM member) LOOP
DBMS_OUTPUT.PUT_LINE('Member Name: ' || member_record.member_name);
END LOOP;
END;
/

16) Try to fetch a member that does not exist and handle the NO DATA FOUND exception.

SQL> DECLARE
v_weight member.weight%TYPE;
BEGIN
SELECT weight INTO v_weight FROM member WHERE member_id = 99999;
EXCEPTION
WHEN NO_DATA_FOUND THEN
DBMS_OUTPUT.PUT_LINE('Error: No member found with the provided member id.');
END;
/

17) Try to fetch one row but get multiple rows back and handle the TOO MANY ROWS exception.

SQL> select * from machine;
SQL> DECLARE
v_rate machine.product_rate%TYPE;
BEGIN
SELECT product_rate INTO v_rate FROM machine WHERE branch_id = 103;
EXCEPTION
WHEN TOO_MANY_ROWS THEN
DBMS_OUTPUT.PUT_LINE('Error: More than one machine found for this branch.');
END;
/

19) Try to divide by zero and handle the ZERO DIVIDE exception.

SQL> DECLARE
v_total_rate NUMBER := 29800;
v_machine_count NUMBER := 0;
v_average_rate NUMBER;
BEGIN
v_average_rate := v_total_rate / v_machine_count;
DBMS_OUTPUT.PUT_LINE('Average Rate: ' || v_average_rate);
EXCEPTION
WHEN ZERO_DIVIDE THEN
DBMS_OUTPUT.PUT_LINE('Error: Division by zero, machine count is zero.');
END;
/

20) ry to convert the varchar to a number and handle the VALUE ERROR exception.

SQL> DECLARE
v_weight VARCHAR2(10);
v_result NUMBER;
BEGIN
v_weight := 'ABC';
v_result := TO_NUMBER(v_weight);
EXCEPTION
WHEN VALUE_ERROR THEN
DBMS_OUTPUT.PUT_LINE('Error: Invalid value for number.');
END;
/

21) Fetch a non-existent member and handle it using both NO DATA FOUND and WHEN OTHERS exceptions together.

SQL> DECLARE
v_weight member.weight%TYPE;
BEGIN
SELECT weight
INTO v_weight
FROM member
WHERE member_id = 99999;
EXCEPTION
WHEN NO_DATA_FOUND THEN
DBMS_OUTPUT.PUT_LINE('No member found with this id.');
WHEN OTHERS THEN
DBMS_OUTPUT.PUT_LINE('An unexpected error occurred: ' || SQLERRM);
END;
/

22) Declare a custom exception, raise it when weight is below 20, and handle it with a message.

SQL> DECLARE
-- Declare a custom exception
e_low_weight EXCEPTION;
v_weight NUMBER := 10;
BEGIN
IF v_weight < 20 THEN
RAISE e_low_weight;
END IF;
EXCEPTION
WHEN e_low_weight THEN
DBMS_OUTPUT.PUT_LINE('Error: The weight value is too low to be valid.');
END;
/
SQL> set serveroutput on;
SQL> DECLARE
v_name   MEMBER.MEMBER_NAME%TYPE;
v_age    MEMBER.AGE%TYPE;
CURSOR c_cursor IS
SELECT MEMBER_NAME, AGE
FROM MEMBER
WHERE MEMBER_NAME LIKE 'A%';
BEGIN
OPEN c_cursor;
FETCH c_cursor INTO v_name, v_age;
IF c_cursor%FOUND THEN
DBMS_OUTPUT.PUT_LINE('First row fetched: ' || v_name);
ELSE
DBMS_OUTPUT.PUT_LINE('No row fetched');
END IF;
FETCH c_cursor INTO v_name, v_age;
IF c_cursor%FOUND THEN
DBMS_OUTPUT.PUT_LINE('Second row fetched: ' || v_name);
ELSE
DBMS_OUTPUT.PUT_LINE('No second row found');
END IF;
IF c_cursor%ISOPEN THEN
DBMS_OUTPUT.PUT_LINE('Cursor is open');
ELSE
DBMS_OUTPUT.PUT_LINE('Cursor is not open');
END IF;
DBMS_OUTPUT.PUT_LINE('Number of rows fetched: ' || c_cursor%ROWCOUNT);
CLOSE c_cursor;
IF c_cursor%ISOPEN THEN
DBMS_OUTPUT.PUT_LINE('Cursor is open');
ELSE
DBMS_OUTPUT.PUT_LINE('Cursor is not open');
END IF;
END;
/
SQL> DECLARE
c_count      NUMBER;
cursor_open  EXCEPTION;
CURSOR c_cursor IS
SELECT COUNT(*)
FROM MEMBER
WHERE AGE > (
SELECT AVG(AGE)
FROM MEMBER
GROUP BY ADDRESS_CITY
HAVING ADDRESS_CITY = 'SIVAKASI'
)
AND ADDRESS_CITY = 'SIVAKASI';
BEGIN
OPEN c_cursor;
FETCH c_cursor INTO c_count;
IF c_cursor%ISOPEN THEN
DBMS_OUTPUT.PUT_LINE('Members in SIVAKASI above average age: ' || c_count);
ELSE
RAISE cursor_open;
END IF;
CLOSE c_cursor;
IF c_cursor%ISOPEN THEN
DBMS_OUTPUT.PUT_LINE('Members in SIVAKASI above average age: ' || c_count);
ELSE
RAISE cursor_open;
END IF;
EXCEPTION
WHEN cursor_open THEN
DBMS_OUTPUT.PUT_LINE('Error: Cursor is closed');
END;
/
SQL> DECLARE
A NUMBER :=-1 ;
B NUMBER :=1;
TEMP NUMBER :=0;
NUM NUMBER;
BEGIN
NUM :=& NUM;
FOR I IN 1..NUM LOOP
TEMP :=A+B;
DBMS_OUTPUT.PUT_LINE(TEMP||' ');
A :=B;
B :=TEMP;
END LOOP;
END;
/
SQL> declare
n number;
flag number :=1;
begin
n :=&n;
for i in 2..n/2 loop
if mod(n,i)=0 then
flag :=0;
else
flag :=1;
end if;
end loop;
if flag=1 then
dbms_output.put_line('prime');
else
dbms_output.put_line(' not prime');
end if;
end;
/
