1) insert gym entries for gym table
INSERT INTO GYM VALUES (1, 'nellai barani fitness');
INSERT INTO GYM VALUES (2, 'king power gym');
INSERT INTO GYM VALUES (3, 'iron paradise');
INSERT INTO GYM VALUES (4, 'rajkamal gym');
INSERT INTO GYM VALUES (5, 'power gym');
INSERT INTO GYM VALUES (6, 'valimai gym');

2) insert branch entries for branch table
INSERT INTO BRANCH VALUES (102, 'T NAGAR BRANCH', 1, 'CHENNAI', 'USMAN ROAD', '600017');
INSERT INTO BRANCH VALUES (103, 'GANDHIPURAM BRANCH', 1, 'COIMBATORE', '100 FEET ROAD', '641012');
INSERT INTO BRANCH VALUES (104, 'KK NAGAR BRANCH', 1, 'MADURAI', 'LAKE VIEW STREET', '625020');
INSERT INTO BRANCH  VALUES (105, 'OMALUR ROAD BRANCH', 1, 'SALEM', 'OMALUR MAIN ROAD', '636004');
INSERT INTO BRANCH VALUES (106, 'PALAYAMKOTTAI BRANCH', 1, 'TIRUNELVELI', 'NGO COLONY', '627007');
INSERT INTO BRANCH VALUES (102, 'T NAGAR BRANCH', 1, 'CHENNAI', 'USMAN ROAD', '600017');

3)insert member entries for member table
INSERT INTO MEMBER VALUES (1002, 102, 'ARJUN', 'Male', 28, 'Mesoderm', 80, SYSDATE, 'MADURAI', 'POWER HOUSE GYM', '625001');
INSERT INTO MEMBER VALUES (1003, 103, 'PRIYA', 'Female', 24, 'Ectoderm', 55, SYSDATE, 'CHENNAI', 'FITNESS WORLD', '600028');
INSERT INTO MEMBER VALUES (1004, 104, 'KARTHIK', 'Male', 30, 'Endoderm', 90, SYSDATE, 'COIMBATORE', 'IRON TEMPLE GYM', '641001');
INSERT INTO MEMBER VALUES (1005, 105, 'DIVYA', 'Female', 27, 'Mesoderm', 61, SYSDATE, 'TRICHY', 'SHAPE UP FITNESS', '620017');
INSERT INTO MEMBER VALUES (1006, 106, 'VIGNESH', 'Male', 22, 'Ectoderm', 68, SYSDATE, 'SALEM', 'MUSCLE FACTORY', '636007');
COMMIT;
INSERT INTO MEMBER VALUES (9997, 103, 'rani', 'Female', 28, 'Unknown', 62.0, SYSDATE, 'Coimbatore', 'Lane', '641001');
INSERT INTO MEMBER VALUES (996, 110, 'q branch', 'Male', 30, 'Mesoderm', 80.0, SYSDATE, 'Salem', 'abc Road', '636001');

4)insert trainer entries for entries table
INSERT INTO TRAINER VALUES (201, 'ARUN', TO_DATE('15-06-1992','DD-MM-YYYY'), 32, SYSDATE, 7, 35000, 'CHENNAI', 'T NAGAR', '600017');
INSERT INTO TRAINER VALUES (202, 'KARTHIK', TO_DATE('20-03-1989','DD-MM-YYYY'), 35, SYSDATE, 9, 42000, 'COIMBATORE', 'GANDHIPURAM', '641012');
INSERT INTO TRAINER VALUES (203, 'PRIYA', TO_DATE('10-11-1995','DD-MM-YYYY'), 29, SYSDATE, 5, 30000, 'MADURAI', 'KK NAGAR', '625020');
INSERT INTO TRAINER VALUES (204, 'SURESH', TO_DATE('05-01-1986','DD-MM-YYYY'), 38, SYSDATE, 12, 50000, 'SALEM', 'OMALUR ROAD', '636004');
INSERT INTO TRAINER VALUES (205, 'DIVYA', TO_DATE('25-08-1993','DD-MM-YYYY'), 31, SYSDATE, 6, 32000, 'TIRUNELVELI', 'PALAYAMKOTTAI', '627007');
INSERT INTO TRAINER VALUES (202, 'KARTHIK', TO_DATE('20-03-1989','DD-MM-YYYY'), 35, SYSDATE, 9, 42000, 'COIMBATORE', 'GANDHIPURAM', '641012');
INSERT INTO TRAINER VALUES (205, 'DIVYA', TO_DATE('25-08-1993','DD-MM-YYYY'), 101, SYSDATE, 6, 32000, 'TIRUNELVELI', 'PALAYAMKOTTAI', '627007');

5)insert staff values in staff table
INSERT INTO STAFF VALUES (301, 102, 'RAJESH KUMAR', TO_DATE('12-05-1990','DD-MM-YYYY'), 34, SYSDATE, 8, 25000, 'CHENNAI', 'USMAN ROAD', '600017');
INSERT INTO STAFF VALUES (302, 103, 'ANITHA DEVI', TO_DATE('22-09-1994','DD-MM-YYYY'), 30, SYSDATE, 5, 22000, 'COIMBATORE', '100 FEET ROAD', '641012');
INSERT INTO STAFF VALUES (303, 104, 'PRAVEEN KUMAR', TO_DATE('18-01-1988','DD-MM-YYYY'), 36, SYSDATE, 10, 27000, 'MADURAI', 'LAKE VIEW STREET', '625020');
INSERT INTO STAFF VALUES (304, 105, 'KEERTHANA S', TO_DATE('07-07-1996','DD-MM-YYYY'), 28, SYSDATE, 4, 21000, 'SALEM', 'OMALUR MAIN ROAD', '636004');
INSERT INTO STAFF VALUES (305, 106, 'MOHAN RAJ', TO_DATE('30-03-1992','DD-MM-YYYY'), 32, SYSDATE, 6, 23000, 'TIRUNELVELI', 'NGO COLONY', '627007');
INSERT INTO STAFF VALUES (305, 106, 'MOHAN RAJ', TO_DATE('30-03-1992','DD-MM-YYYY'), 32, SYSDATE, 6, 23000, 'TIRUNELVELI', 'NGO COLONY', '627007');
INSERT INTO STAFF VALUES (305, 106, 'MOHAN RAJ', TO_DATE('30-03-1992','DD-MM-YYYY'), 3, SYSDATE, 6, 23000, 'TIRUNELVELI', 'NGO COLONY', '627007');

6)insert supplier entries for supplier table
INSERT INTO SUPPLIER VALUES (401, 'FITNESS EQUIP MART', 102, 150000, '33ABCDE1234F1Z5');
INSERT INTO SUPPLIER VALUES (402, 'POWER GYM SUPPLIES', 103, 120000, '33PQRSX5678K1Z2');
INSERT INTO SUPPLIER VALUES (403, 'IRON WORLD DISTRIBUTORS', 104, 175000, '33LMNOP9012Q1Z8');
INSERT INTO SUPPLIER VALUES (404, 'HEALTH PRO EQUIPMENT', 105, 98000, '33UVWXY3456R1Z3');
INSERT INTO SUPPLIER VALUES (405, 'MUSCLE BUILDERS HUB', 106, 210000, '33HJKLA7890T1Z6');
INSERT INTO SUPPLIER VALUES (402, 'POWER GYM SUPPLIES', 13, 120000, '33PQRSX5678K1Z2');

7)insert payment entries for payment table
INSERT INTO PAYMENT VALUES (501, SYSDATE, 'PAID', 102, 5000);
INSERT INTO PAYMENT VALUES (502, SYSDATE, 'PENDING', 103, 7500);
INSERT INTO PAYMENT VALUES (503, SYSDATE, 'PAID', 104, 6200);
INSERT INTO PAYMENT VALUES (504, SYSDATE, 'FAILED', 105, 4500);
INSERT INTO PAYMENT VALUES (505, SYSDATE, 'PAID', 106, 8100);
INSERT INTO PAYMENT VALUES (599, SYSDATE, 'CANCELLED', 105, 3000.0);

8)insert entries for card table
INSERT INTO CARD VALUES (601, 'CREDIT CARD', 501);
INSERT INTO CARD VALUES (602, 'DEBIT CARD', 502);
INSERT INTO CARD VALUES (603, 'CREDIT CARD', 503);
INSERT INTO CARD VALUES (604, 'DEBIT CARD', 504);
INSERT INTO CARD VALUES (605, 'CREDIT CARD', 505);

9)insert entries for cash table
INSERT INTO CASH VALUES (501, 0, 5000);
INSERT INTO CASH VALUES (502, 500, 7000);
INSERT INTO CASH VALUES (503, 0, 6200);
INSERT INTO CASH VALUES (504, 1000, 3500);
INSERT INTO CASH VALUES (505, 0, 8100);

10)insert entries for invoice table
INSERT INTO INVOICE VALUES (701, 5, SYSDATE, 5250, 501);
INSERT INTO INVOICE VALUES (702, 5, SYSDATE, 7875, 502);
INSERT INTO INVOICE VALUES (703, 5, SYSDATE, 6510, 503);
INSERT INTO INVOICE VALUES (704, 5, SYSDATE, 4725, 504);
INSERT INTO INVOICE VALUES (705, 5, SYSDATE, 8505, 505);

11)insert  entries for invoice line table
INSERT INTO INVOICE_LINE VALUES (801, 701, 2, 'PROTEIN POWDER', 2500);
INSERT INTO INVOICE_LINE VALUES (802, 702, 1, 'DUMBBELL SET', 7500);
INSERT INTO INVOICE_LINE VALUES (803, 703, 3, 'YOGA MAT', 2170);
INSERT INTO INVOICE_LINE VALUES (804, 704, 1, 'FITNESS ACCESSORIES', 4500);
INSERT INTO INVOICE_LINE VALUES (805, 705, 1, 'TREADMILL MAINTENANCE', 8100);

12)insert entries for workout table using insert all
INSERT ALL
INTO WORKOUT_PLAN VALUES (901, 'BEGINNER STRENGTH', 30, 'PUSHUPS, SQUATS, PLANK', 201, 1002)
INTO WORKOUT_PLAN VALUES (902, 'FAT LOSS PROGRAM', 45, 'BURPEES, JUMP ROPE, MOUNTAIN CLIMBERS', 202, 1003)
INTO WORKOUT_PLAN VALUES (903, 'MUSCLE BUILDING', 60, 'BENCH PRESS, DEADLIFT, SQUATS', 203, 1004)
INTO WORKOUT_PLAN VALUES (904, 'CARDIO BOOST', 40, 'TREADMILL, CYCLING, ROWING', 204, 1005)
INTO WORKOUT_PLAN VALUES (905, 'CORE STABILITY', 35, 'PLANK, LEG RAISES, RUSSIAN TWIST', 205, 1006)
SELECT * FROM DUAL;

13)insert entries for membership table using insert table
INSERT ALL
INTO MEMBERSHIP VALUES (10001, 1002, 102, 3, 'MONTHLY', 3000, SYSDATE, ADD_MONTHS(SYSDATE, 3))
INTO MEMBERSHIP VALUES (10002, 1003, 103, 6, 'HALF-YEARLY', 5500, SYSDATE, ADD_MONTHS(SYSDATE, 6))
INTO MEMBERSHIP VALUES (10003, 1004, 104, 12, 'YEARLY', 10000, SYSDATE, ADD_MONTHS(SYSDATE, 12))
INTO MEMBERSHIP VALUES (10004, 1005, 105, 1, 'MONTHLY', 1200, SYSDATE, ADD_MONTHS(SYSDATE, 1))
INTO MEMBERSHIP VALUES (10005, 1006, 106, 3, 'QUARTERLY', 3200, SYSDATE, ADD_MONTHS(SYSDATE, 3))
SELECT * FROM DUAL;
INSERT INTO MEMBERSHIP VALUES (100000, 1000, 106, 3, 'MONTHLY', 2500.0, SYSDATE, SYSDATE + 90);
INSERT INTO MEMBERSHIP VALUES (10001, 1002, 102, 3, 'MONTHLY', 3000.0, SYSDATE, SYSDATE + 90);
INSERT INTO MEMBERSHIP VALUES (99998, 1003, 103, 6, 'HALF-YEARLY', 5500.0, SYSDATE + 180, SYSDATE);

14)insert entries for takes_workout_plan table
INSERT INTO TAKES_WORKOUT_PLAN VALUES (1002, 901, SYSDATE, ADD_MONTHS(SYSDATE, 1));
INSERT INTO TAKES_WORKOUT_PLAN VALUES (1003, 902, SYSDATE, ADD_MONTHS(SYSDATE, 2));
INSERT INTO TAKES_WORKOUT_PLAN VALUES (1004, 903, SYSDATE, ADD_MONTHS(SYSDATE, 3));
INSERT INTO TAKES_WORKOUT_PLAN VALUES (1005, 904, SYSDATE, ADD_MONTHS(SYSDATE, 1));
INSERT INTO TAKES_WORKOUT_PLAN VALUES (1006, 905, SYSDATE, ADD_MONTHS(SYSDATE, 2));

15)insert entries for trainer specialisation table
INSERT INTO TRAINER_SPECIALISATION (TRAINER_ID, SPECIALISATION)
VALUES (201, 'STRENGTH TRAINING');
INSERT INTO TRAINER_SPECIALISATION (TRAINER_ID, SPECIALISATION)
VALUES (202, 'WEIGHT LOSS');
INSERT INTO TRAINER_SPECIALISATION (TRAINER_ID, SPECIALISATION)
VALUES (203, 'BODYBUILDING');
INSERT INTO TRAINER_SPECIALISATION (TRAINER_ID, SPECIALISATION)
VALUES (204, 'CARDIO FITNESS');
INSERT INTO TRAINER_SPECIALISATION (TRAINER_ID, SPECIALISATION)
VALUES (205, 'CORE TRAINING');
INSERT INTO TRAINER_SPECIALISATION (TRAINER_ID, SPECIALISATION)
VALUES (201, 'FUNCTIONAL TRAINING');
INSERT INTO TRAINER_SPECIALISATION (TRAINER_ID, SPECIALISATION)
VALUES (202, 'HIIT');

16)to update member weight increased by 5 kg
UPDATE MEMBER SET WEIGHT=WEIGHT+5;
SELECT * FROM MEMBER;

17)to update salary of trainer with experience>2
select experience,salary from trainer;
update trainer set salary = salary+6000 where experience>=2;
select experience,salary from trainer;

18)to update multiple columns in staff table
select age,salary from staff;
update staff set age=age+1,salary=salary-1000;
select age,salary from staff;

19)to delete member with age<12
select age from member;
delete from member where age<12;
select age from member;

20)to delete payment records with falied status
delete from payment where status='FAILED';
DELETE FROM INVOICE_LINE
WHERE INVOICE_ID IN (
SELECT INVOICE_ID FROM INVOICE
WHERE PAYMENT_ID IN (
SELECT PAYMENT_ID FROM PAYMENT WHERE STATUS = 'FAILED'
)
);
DELETE FROM INVOICE
WHERE PAYMENT_ID IN (
SELECT PAYMENT_ID FROM PAYMENT WHERE STATUS = 'FAILED'
);
DELETE FROM CARD
WHERE PAYMENT_ID IN (
SELECT PAYMENT_ID FROM PAYMENT WHERE STATUS = 'FAILED'
);
DELETE FROM CASH
WHERE PAYMENT_ID IN (
SELECT PAYMENT_ID FROM PAYMENT WHERE STATUS = 'FAILED'
);
DELETE FROM PAYMENT
WHERE STATUS = 'FAILED';
ROLLBACK;

21)to update membership amount as membership+500
update membership set amount=amount+500;
select amount from membership;

22)to update payment status of id '504'
SELECT PAYMENT_ID,STATUS FROM PAYMENT;
update payment set status='PAID' where payment_id=504;
SELECT PAYMENT_ID,STATUS FROM PAYMENT;

23)to update supplier amount by 10000
select amount from supplier;
update supplier set amount=amount+10000;
select amount from supplier;

24)to update gst number of supplier
update supplier set gst_number='gst0000draft011';
select gst_number from supplier;

25)to update trainer specialisation of trainer
select * from trainer_specialisation;
UPDATE trainer_specialisation
SET specialisation = 'yet_to_be_added'
WHERE trainer_id = 204
AND specialisation = 'CARDIO FITNESS';
select * from trainer_specialisation;

26)to update cash balance of payment_id  501
select * from cash;
update cash set balance=received_amount-500 where payment_id=501;
select * from cash;

27)to delete specialisation of all trainer
select specialisation from trainer_specialisation;
delete specialisation from trainer_specialisation;
delete from trainer_specialisation;
delete from invoice;
delete from workout_plan;

28)to delete memberships where enddate is less than today date
select * from membership;
delete from membership where end_date<sysdate;
delete from membership where end_date<sysdate+45;
select * from membership;

29)to rollback from here
rollback;

30)to commit from here
commit;

31)to delete branch city ends with I
delete from branch where address_city like '%I';

32)to delete the member backup
delete from member_backup;

33)to display all members
select * from member;

34)to see total from member
select count(*) from member;

35)to see average weight of members
select avg(weight) from member;

36)to see minimum age of membr
select min(age) from member;

37)to select maximum age from member
select max(age) from member;

38)to display body type and average weight of member grouping them by body type
select body_type,avg(weight) from member group by body_type;

39)to display average salary of trainer
select avg(salary) from trainer;

40)to display the experience and salary of trainer grouping them bye experience
select experience,avg(salary) from trainer group by experience;

41)to see total count of invoices generated
select count(*) from invoice;

42)to display the average of total amount from invoice
select avg(total_amount) from invoice;

43)to display status ,sum of amount from payment grouping by status
select status,sum(amount) from payment group by status;

44)to display the rounded values of weight of member
select round(weight) from member;

45)to display the truncated values of weight from member
select trunc(weight) from member;

46)to display square root of weight from member
select sqrt(weight) from member;

47)to display absolute value of weight from member
select abs(weight) from member;

48) to absolute balance from cash
select abs(balance) from cash;

49)to display modulus of weight by 10 of member
select mod(weight,10) from member;

50)to display maximum rounded value of salary from trainer
select ceil(salary) from trainer;

51) to display minimum rounded value of salary from trainer
select floor(salary) from trainer;

52)to display the truncated value of salary of trainer
select trunc(mod(salary,3)) from trainer;

53)to display the absolute value of amount for payment
select abs(mod(amount,3)) from payment;

54)to display modulus of amount with 500 from payment
select mod(amount,500) from payment;

55)to display total count of workout plan
select count(*) from workout_plan;

56)to display the current date
select sysdate from dual;

57)to display members name , joining date of member
select member_name,date_of_join from member;

58)to  display members name , truncated value of joining date of member
select member_name,trunc(date_of_join) from member;

59)to display member name along with move month 6 from current month
select member_name,add_months(date_of_join,6) from member;

60)to display months between current date and joining date along member name
select member_name,months_between(sysdate,add_months(date_of_join,6)) from member;

61)to select only members who joined 1 month before
select member_name,date_of_join from member where date_of_join<sysdate;

62)to select member with joining date between 3 months from current date
SELECT member_name, date_of_join FROM member WHERE date_of_join BETWEEN SYSDATE-90 AND SYSDATE;

63)to display only year from joining date
select extract(year from date_of_join) from member;

64)to display only month from joining date
select extract(month from date_of_join) from member;

65)to display only day from joining date
select extract(day from date_of_join) from member;

66)to display trainer name along with date of birth
select trainer_name,date_of_birth from trainer;

67)to display only year from date of birth of trainer
select extract(year from date_of_birth) from trainer;

68)to display only year from date of birth of staff
select extract(year from date_of_birth) from staff;

69)to display payment id ,payment date from payment table
select payment_id,payment_date from payment;

70)to display only payments made on current date
select payment_id,payment_date from payment where payment_date=sysdate;

71)to display payment id , along with payment date by adding 1 month
select payment_id,add_months(payment_date,1) from payment;

72)to display invoice id and invoice date from invoice
select invoice_id,invoice_date from invoice;

73)to display the months between current date and invoice date
select months_between(sysdate,invoice_date) from invoice;

74)to display duration of membership
select membership_id,(end_date-start_date) as duration from membership;

75)to display only the membership id where end date is less than current date
select membership_id from membership where end_date<sysdate;

76)to display only the membership id where end date is greater than current date
select membership_id from membership where end_date>sysdate;

77)to display plan_id along with start date and end date
select plan_id ,start_date,end_date from takes_workout_plan;

78)to display plan id along with months between start and end date
select plan_id ,months_between(start_date,end_date) from takes_workout_plan;

79)to display current date as character
select to_char(sysdate,'dd/mm/yyyy') from dual;

80)to display weight as a character
select member_name,to_char(weight) from member;

81)to display the day from invoice date as character
select invoice_id,to_char(invoice_date,'day') from invoice;

82)to display the salary of staff with increment by to number()
select salary+to_number('5000') from staff;

83)to display members joined at 1.1.26
select member_name,member_id from member where date_of_join=to_date('01-01-2026','dd-mm-yyyy');

84)to display member_name,gender ,average of weight from member where body type is mesoderm and group them by gender with average weight>50 also order them by average weight
SELECT gender,avg(weight) AS average_weight
FROM member
WHERE body_type='Mesoderm'
GROUP BY gender;
SELECT gender,avg(weight) AS average_weight
FROM member
WHERE body_type='Mesoderm'
GROUP BY gender
HAVING AVG(weight) > 50;
SELECT gender,avg(weight) AS average_weight
FROM member
WHERE body_type='Mesoderm'
GROUP BY gender
HAVING AVG(weight) > 50
ORDER BY average_weight;

85)to display the total collection on a day with no pemding status order them in descending order
select payment_id,sum(amount) as total_collection from payment where status='PAID' group by payment_id order by total_collection desc;

86)to display members with gender male
select * from member where gender='male';
select * from member where gender='Male';

87)to display member not from branch 101,102
select * from member where branch_id not in(101,102);

88)to display salary of staff with details where salary should exceed 10000 and upto 20000
select * from staff where salary not between 10000 and 20000;

89)to display payment_id if status is not null
select payment_id from payment where status is not null;

90)to get last day of joining date from member
select member_id,member_name ,last_day(date_of_join) from member;

91)to convert text to upper case
select upper(member_name),weight from member;

92)to display text in lower case
select lower(member_name),weight from member;

93)to capitalize first letter of supplier name
select initcap(supplier_name) from supplier;

94)to find length of body type

95)to find position of R in address street
select address_street,instr(address_street,'R') from trainer;

96)to concatenate member addresses
select concat(concat(address_street,address_city),address_pincode) from member;

97)to trim leading spaces from trainer name
select ltrim(trainer_name) from trainer;

98)to trim spaces from both sides of trainer name
select trim(trainer_name) from trainer;

99)to replace character of member name has a to i
select member_name,replace(member_name,'A','I') as replaced_name from member;

100)to display staff with salary not between 20000 and 24000
select staff_name,staff_id from staff where salary not between 20000 and 24000;

101)to display trainer with name starting with A
select trainer_name from trainer where trainer_name like 'A%';

102)to display trainer with name not ending with A
select trainer_name from trainer where trainer_name not like '%A';

103)to create commit command
commit;

104)to select staffs where staff id is not 301,304,307
select staff_id,staff_name from staff where staff_id not in(301,304,307);

105)to display member_id,body_type from member but group them by according to both
select member_id,body_type from member group by member_id,body_type;

106)to display member_id,weight from member order them accordingly descending
select member_id,weight from member order by member_id,weight desc;

107)to display only the 1st 5 letters of members name
SELECT MEMBER_NAME, WEIGHT FROM MEMBER;
SELECT SUBSTR(MEMBER_NAME, 1, 3) AS SHORT_NAME, WEIGHT FROM MEMBER;

108)display branch name and address but sort branches by city ascending, then assets descending:
SELECT BRANCH_NAME, ADDRESS_CITY, ADDRESS_PINCODE FROM BRANCH
ORDER BY ADDRESS_CITY ASC, ADDRESS_PINCODE DESC;

109)to display the member name and weight (using truncate command)
INSERT INTO MEMBER VALUES (99, 102, 'Vash', 'Male', 25, 'Mesoderm', -23.6, SYSDATE,'CHENNAI', 'KEELAMBAKAM ROAD', 600017, 'ACTIVE');
SELECT MEMBER_NAME, WEIGHT, TRUNC(WEIGHT, 0) AS TRUNC_WEIGHT FROM MEMBER;

110)to display the member name and weight (using absolute command)
SELECT MEMBER_NAME, WEIGHT from member;
SELECT MEMBER_NAME, WEIGHT, ABS(WEIGHT) AS ABS_WEIGHT FROM MEMBER;

111)to get details from user and insert them in member table
ACCEPT p_member_id NUMBER PROMPT 'MEMBER_ID: '
ACCEPT p_branch_id NUMBER PROMPT 'BRANCH_ID: '
ACCEPT p_name CHAR PROMPT 'MEMBER_NAME: '
ACCEPT p_gender CHAR PROMPT 'GENDER: '
ACCEPT p_age NUMBER PROMPT 'AGE: '
ACCEPT p_body CHAR PROMPT 'BODY_TYPE: '
ACCEPT p_weight NUMBER PROMPT 'WEIGHT: '
ACCEPT p_city CHAR PROMPT 'ADDRESS_CITY: '
ACCEPT p_street CHAR PROMPT 'ADDRESS_STREET: '
ACCEPT p_pin CHAR PROMPT 'ADDRESS_PINCODE: '
ACCEPT p_status CHAR PROMPT 'STATUS: '
INSERT INTO MEMBER VALUES (&p_member_id, &p_branch_id, '&p_name', '&p_gender', &p_age,
'&p_body', &p_weight, SYSDATE, '&p_city', '&p_street',
'&p_pin', '&p_status');
select * from member where member_id=100;

112)to display member name , weight and apply round() on weight
SELECT MEMBER_NAME, WEIGHT FROM MEMBER;
SELECT MEMBER_NAME, WEIGHT, ROUND(WEIGHT) AS ROUND_WEIGHT FROM MEMBER;

113)to display count of members with particular age on a particular branch
select address_city,age,count(*) from member group by address_city,age;
select address_city,age,count(*) from member group by address_city,age order by address_city;

114)to display count of members with particular age on a particular branch and order by multiple columns
select address_city,age,count(*) from member group by address_city,age order by address_city,age;
