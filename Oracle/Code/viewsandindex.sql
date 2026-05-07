1) to create a simple view with member names and cities

SQL> CREATE VIEW member_city_view AS
SELECT MEMBER_NAME, ADDRESS_CITY FROM MEMBER;
SQL> SELECT * FROM member_city_view;

2) to create view with member name ,city where city is Chennai

SQL> CREATE VIEW member_chennai AS
SELECT MEMBER_NAME, ADDRESS_CITY, WEIGHT
FROM MEMBER WHERE ADDRESS_CITY = 'CHENNAI';
SQL> SELECT * FROM member_chennai;

3) to create view branch into member name ,branch name along with gym using join

SQL> CREATE VIEW member_branch_gym_info AS
SELECT m.MEMBER_NAME, m.WEIGHT, b.BRANCH_NAME, g.GYM_NAME
FROM MEMBER m
JOIN BRANCH b ON m.BRANCH_ID = b.BRANCH_ID
JOIN GYM g ON b.GYM_ID = g.GYM_ID;
SQL> SELECT * FROM member_branch_gym_info;

4) to create view payment with aggregate functions along with join

SQL> CREATE VIEW payment_details_view AS
SELECT p.PAYMENT_ID, p.STATUS, SUM(p.AMOUNT) AS TOTAL_AMOUNT
FROM PAYMENT p
JOIN BRANCH b ON p.BRANCH_ID = b.BRANCH_ID
GROUP BY p.PAYMENT_ID, p.STATUS, b.BRANCH_NAME;
SQL> select * from payment_details_view;

5) to drop a view

SQL> drop view payment_details_view;
SQL> rollback;

6) to create view high value branch from another view high payment

SQL> CREATE VIEW branch_payment_total AS
SELECT b.BRANCH_NAME, SUM(p.AMOUNT) AS BRANCH_TOTAL
FROM PAYMENT p
JOIN BRANCH b ON p.BRANCH_ID = b.BRANCH_ID
GROUP BY b.BRANCH_NAME;
SQL> CREATE VIEW high_revenue_branches AS
SELECT BRANCH_NAME, BRANCH_TOTAL
FROM branch_payment_total
WHERE BRANCH_TOTAL > 1000;
SQL> SELECT * FROM high_revenue_branches;

7) to create members using a view active members

SQL> CREATE VIEW active_members AS
SELECT MEMBER_ID, MEMBER_NAME, WEIGHT
FROM MEMBER
WHERE AGE > 18;
SQL> INSERT INTO active_members VALUES (109, 'Tamil', 75.5);
SQL> SELECT * FROM active_members;
SQL> commit;
SQL> update active_members set weight=67 where member_id=1035;
SQL> create view city_group_view as select address_city,count(*) as city_count from member_city_view group by address_city;
SQL> update city_group_view set address_city ='MADURAI' where address_city='madurai';
SQL> select * from city_group_view;
SQL> insert into city_group_view(address_city) values('Madurai');

8) setting timer on

SQL> set timing on

9) to display branch details who are not from sivakasi and from gym id 1

SQL> select * from branch where gym_id=1 and address_city<>'sivakasi';

10) to create an index on address_city from branch

SQL> CREATE INDEX idx_branch_city ON BRANCH(ADDRESS_CITY);
SQL> select * from branch where gym_id=1 and address_city<>'sivakasi';

11) to create a composite index

SQL> CREATE INDEX idx_member_city_age ON MEMBER(ADDRESS_CITY, AGE);
SQL> select member_id,address_city from member where age<30 ;

12) to create a index with upper case of member name

SQL> CREATE INDEX idx_member_name_upper ON MEMBER(UPPER(MEMBER_NAME));
SQL> select member_id,upper(member_name) from member ;

13) to create a index with lower case of member name

SQL> CREATE INDEX idx_member_name_lower ON MEMBER(LOWER(MEMBER_NAME));
SQL> select member_id,lower(member_name) from member ;

14) to create a bitmap index

SQL> select member_id from member where gender='Male';
SQL> CREATE BITMAP INDEX idx_member_gender ON MEMBER(GENDER);
SQL> select member_id from member where gender='Male';

15) to create a unique index

SQL> select payment_id,status from payment;
SQL> CREATE UNIQUE INDEX idx_payment_status ON PAYMENT(PAYMENT_ID, STATUS);
SQL> select payment_id,status from payment;

16) 

SQL> SELECT member_id, member_name, weight,
CASE
WHEN weight > 80 THEN 'HEAVY'
WHEN weight >= 60 THEN 'MEDIUM'
ELSE 'LIGHT'
END AS weight_category
FROM member;

17) 

SQL> UPDATE member
SET weight = CASE
WHEN member_id = 1002 THEN 88
WHEN address_city = 'CHENNAI' THEN weight + 2
ELSE weight
END
WHERE member_id IN (1002, 1003);
SQL> select member_id,address_city,weight from member;

18) 

SQL> select row_number() over ( order by experience desc) as senior_rank, trainer_id,trainer_name from trainer;

19) 

SQL> select trainer_id,trainer_name,age from trainer
fetch first 4 rows only;

20) 

SQL> select member_id ,member_name,weight from copy_member;
SQL> DELETE FROM copy_member
WHERE CASE
WHEN member_name<>'Rahul' THEN 1
WHEN address_city = 'CHENNAI' THEN 1
WHEN weight < 50 THEN 1
ELSE 0
END = 1;
SQL> select * from copy_member;
