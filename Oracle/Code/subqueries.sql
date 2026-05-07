1) to display the oldest member

SQL> select member_name,age from member where age=(select max(age) from member);

2) to display member older than average age

SQL> select member_name,age from member where age>(select avg(age) from member);

3) to display members in branches of gym with id 1

SQL> select member_name from member where branch_id in (select branch_id from branch where gym_id=1);

4) to display member younger than any trainer age>30

SQL> select member_name,age from member where age<any(select age from trainer where age>30);

5) to display member older than any trainer age>30

SQL> select member_name,age from member where age>any(select age from trainer where age<30);

6) to display member same age with any trainer age=30

SQL> select member_name,age from member where age=any(select age from trainer where age<30);

7) to display member younger than all trainer age>30

SQL> select member_name,age from member where age<all(select age from trainer where age>30);

8) to display member older than all trainer age<30

SQL> select member_name,age from member where age>all(select age from trainer where age<30);

9) to display member with same age of trainer age=30

SQL> select member_name,age from member where age=all(select age from trainer where age=30);

8) to display members younger than average age in their branch

SQL> select m1.member_name,m1.age from member m1 where m1.age<(select avg(m2.age) from member m2 where m2.branch_id=m1.branch_id)
;

9) to display members matching branch city of supplier amount >15000

SQL> select m1.member_name,m1.branch_id,b1.address_city from member m1 join branch b1 on m1.branch_id=b1.branch_id where (m1.branch_id,b1.address_city) in (select s.branch_id,b2.address_city from supplier s join branch b2 on s.branch_id=b2.branch_id where s.amount>15000);

10) to display members younger than average age in their branch

SQL> select m1.member_name,m1.age from member m1 where m1.age<(select avg(m2.age) from member m2 where m2.branch_id=m1.branch_id);

11) to increase salary for experienced trainers

SQL> select * from trainer;
SQL> update trainer set salary=salary+5000 where experience>(select avg(experience) from trainer);
SQL> select * from trainer;

12) display members count per body type

SQL> select body_type,count(*) as number_count from(select distinct body_type from member) group by body_type;

13) branches with average member age greater than overall age

SQL> select b.branch_name,avg(m.age) as avg_age from member m join branch b on m.branch_id=b.branch_id group by b.branch_name having avg(m.age)>(select avg(age) from member);

14) to display members with membership

SQL> select m.member_name from member m where exists (select 1 from membership ms where ms.member_id=m.member_id);

15) to display members without membership

SQL> select m.member_name from member m where not exists (select 1 from membership ms where ms.member_id=m.member_id);

16) to display members from branchs with salary above average salary as highpaying branches

SQL> with highpaying_branches as (select branch_id from payment group by branch_id having sum(amount)>(select avg(amount) from payment )) select m.member_name,m.age from member m join highpaying_branches h on m.branch_id=h.branch_id;

17) to display members above average age from branches with salary above average salary

SQL> with highpaying_branches as (select branch_id from payment group by branch_id having sum(amount)>(select avg(amount) from payment )),
average_age as (select avg(age) as avgage from member) select m.member_name,m.age
from member m join highpaying_branches h on m.branch_id=h.branch_id join average_age a on m.age>a.avgage;

18) to display member id and their trainer

SQL> select member_id,(select trainer_id from workout_plan where trainer_id = 202) as assigned_trainer_id from member where branch_id=102;

19) to display member id ,plan name ,trainer id with existing plan name

SQL> select m1.member_id,m1.workout_plan_name,m1.trainer_id from workout_plan m1 where (m1.workout_plan_name,m1.trainer_id) in (select m2.workout_plan_name,m2.trainer_id from workout_plan m2 where m1.workout_plan_name=m2.workout_plan_name);

20) to display trainer name ,address_city not in city with salary above 60000

SQL> select trainer_name,address_city from trainer where address_city not in (select address_city from trainer where salary>60000);
