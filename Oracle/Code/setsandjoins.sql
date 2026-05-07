1) to display all unique member id from member and takes_workout_plan

SQL> select member_id from member union select member_id from takes_workout_plan;

2) to display all supplier id from member and takes workout plan

SQL> select member_id from member union all select member_id from takes_workout_plan;

3) to display common member id from member and takes workout plan

SQL> select member_id from member intersect select member_id from takes_workout_plan;

4) to display all common member id from member and takes workout plan

SQL> select member_id from member intersect all select member_id from takes_workout_plan;

5) to display cities from member but not trainers

SQL> select address_city from member minus select address_city from trainer;

6) to display cities from member but not trainers using except

SQL> select address_city from member except select address_city from trainer;

7) to display cross join between member and trainer

SQL> select * from member,trainer;

10) to display cross join between trainer and member using keyword

11) to display trainer and member belongs to same city

SQL> select t.trainer_name as trainer_name,m.member_name as member_name from trainer t,member m where t.address_city=m.address_city;

12) to display cross join among trainer,member and staff

13) display all payment id from branches along branch details

SQL> select p.payment_id,b.branch_id,b.branch_name from payment p inner join branch b on p.branch_id=b.branch_id;

14) display maintenance record along with machine details

SQL> select * from member natural join branch;

15) display all staff and their managers

SQL> select s.staff_name,s.staff_id,s1.staff_name as manager from staff s left outer join staff s1 on s.staff_id=s1.staff_id;

16) display all members without membership by left outer join

SQL> select m.member_name,ms.type from member m left outer join membership ms on m.member_id=ms.member_id;

17) display all memberships even member details are missing right outer join

SQL> select m.member_name,ms.type from member m right outer join membership ms on m.member_id=ms.member_id;

18) display all members and membership

SQL> select m.member_name,ms.type from member m full outer join membership ms on m.member_id=ms.member_id;

19) display only members belonging to same branch

SQL> select a.member_name as member1,b.member_name as member2,b.branch_id,a.branch_id from member a join member b on a.branch_id=b.branch_id and a.member_id<>b.branch_id;

20) display members along with workout plans and trainers

SQL> select m.member_name,w.workout_plan_name, t.trainer_name from workout_plan w join member m on w.member_id=m.member_id join trainer t on w.trainer_id=t.trainer_id;

21) display members with branches

SQL> select m.member_name,w.workout_plan_name, t.trainer_name from workout_plan w join member m on w.member_id=m.member_id join trainer t on w.trainer_id=t.trainer_id;
SQL> select m.member_name , b.branch_name from member m,branch b where m.branch_id=b.branch_id;

22) display members with membership between 1000 and 2000

SQL> select m.member_name,ms.amount from member m join membership ms on ms.amount between 1000 and 2000;

23) display cities where active members or staffs live

SQL> select address_city from member where status='active' union select address_city from staff where status='active';

24) display only members or staff with age above 25 and salary above 20000

SQL> select member_name from member where age>25 union all select staff_name from staff where salary>20000;

25) to display members with existing memberships using left outer join

SQL> SELECT m.MEMBER_NAME, ms.TYPE FROM MEMBER m left OUTER JOIN MEMBERSHIP ms ON m.MEMBER_ID = ms.MEMBER_ID;

26) to display members with existing memberships using right outer join

SQL> SELECT m.MEMBER_NAME, ms.TYPE FROM MEMBER m RIGHT OUTER JOIN MEMBERSHIP ms ON m.MEMBER_ID = ms.MEMBER_ID;
