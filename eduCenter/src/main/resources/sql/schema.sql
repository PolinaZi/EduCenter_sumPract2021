create table teacher (
    id serial primary key,
    first_name varchar(20) not null,
    last_name varchar(20) not null,
    experience integer check ( experience >=0 and experience <=100 ) default 0
);

create table course (
    id serial primary key,
    course_name varchar(35) not null,
    start_date varchar(8),
    end_date varchar(8),
    teacher integer,
    foreign key (teacher) references teacher (id)
);


-- drop table lesson;
-- drop table courses_students;
-- drop table course;
-- drop table teacher;
--  drop table student;

create table lesson (
    id serial primary key,
    lesson_name varchar(20) not null,
    time varchar(11),
    course integer not null,
    foreign key (course) references course (id)
);


create table student (
    id serial primary key,
    first_name varchar(20) not null,
    last_name varchar(20) not null,
    group_number varchar(10) not null
);

create table courses_students (
    course_id integer not null,
    foreign key (course_id) references course (id),
    student_id integer not null,
    foreign key (student_id) references student (id)
)