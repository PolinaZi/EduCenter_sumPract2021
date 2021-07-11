insert into teacher(first_name, last_name, experience)
values ('Преподаватель1', 'Фам1', 6);
insert into teacher(first_name, last_name, experience)
values ('Преподаватель2', 'Фам2', 10);
insert into teacher(first_name, last_name, experience)
values ('Преподаватель3', 'Фам3', 2);
insert into teacher(first_name, last_name, experience)
values ('Преподаватель4', 'Фам4', 3);

insert into course(course_name, start_date, end_date, teacher)
values ('Курс1', '09.02.21','05.06.21', 1);
insert into course(course_name, start_date, end_date, teacher)
values ('Курс2', '09.02.21','28.05.21', 3);
insert into course(course_name, start_date, end_date, teacher)
values ('Курс3', '09.02.21','28.05.21', 2);
insert into course(course_name, start_date, end_date, teacher)
values ('Курс4', '29.06.21','12.07.21', 1);

insert into lesson(lesson_name, time, course)
values ('Урок1 тема123', 'ВТ 09.02.21', 1);
insert into lesson(lesson_name, time, course)
values ('Урок1 тема1', 'ВТ 29.06.21', 4);
insert into lesson(lesson_name, time, course)
values ('Урок2 тема345', 'СБ 03.07.21', 4);
insert into lesson(lesson_name, time, course)
values ('Урок3 тема456', 'ПН 05.07.21', 4);

insert into student(first_name, last_name, group_number)
values ('Ученик1', 'Ф1', '11-001');
insert into student(first_name, last_name, group_number)
values ('Ученик2', 'Ф2', '11-001');
insert into student(first_name, last_name, group_number)
values ('Ученик3', 'Ф3', '11-006');
insert into student(first_name, last_name, group_number)
values ('Ученик4', 'Ф4', '11-004');
insert into student(first_name, last_name, group_number)
values ('Ученик5', 'Ф5', '11-002');
insert into student(first_name, last_name, group_number)
values ('Ученик6', 'Ф6', '11-001');
insert into student(first_name, last_name, group_number)
values ('Ученик7', 'Ф7', '11-001');
insert into student(first_name, last_name, group_number)
values ('Ученик8', 'Ф8', '11-002');

insert into courses_students(course_id, student_id)
values (4, 2);
insert into courses_students(course_id, student_id)
values (4, 6);
insert into courses_students(course_id, student_id)
values (4, 7);
insert into courses_students(course_id, student_id)
values (1, 1);
insert into courses_students(course_id, student_id)
values (4, 8);

