package ru.kpfu.itis;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import ru.kpfu.itis.models.Course;
import ru.kpfu.itis.models.Lesson;
import ru.kpfu.itis.models.Student;
import ru.kpfu.itis.models.Teacher;
import ru.kpfu.itis.repositories.CoursesRepository;
import ru.kpfu.itis.repositories.CoursesRepositoryJdbcTemplateImpl;
import ru.kpfu.itis.repositories.LessonsRepository;
import ru.kpfu.itis.repositories.LessonsRepositoryJdbcTemplateImpl;

import javax.sql.DataSource;
import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Properties;

public class Main {

    public static void main(String[] args) {

        Properties properties = new Properties();

        try {
            properties.load(ClassLoader.getSystemResourceAsStream("application.properties"));
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }

        HikariConfig config = new HikariConfig();
        config.setDriverClassName(properties.getProperty("db.driver"));
        config.setJdbcUrl(properties.getProperty("db.url"));
        config.setUsername(properties.getProperty("db.user"));
        config.setPassword(properties.getProperty("db.password"));
        config.setMaximumPoolSize(Integer.parseInt(properties.getProperty("db.hikari.pool-size")));

        DataSource dataSource = new HikariDataSource(config);

        checkWorkOfCoursesRep(dataSource);
        //checkWorkOfLessonsRep(dataSource);
    }

    public static void checkWorkOfCoursesRep(DataSource dataSource) {
        CoursesRepository coursesRep = new CoursesRepositoryJdbcTemplateImpl(dataSource);

        Teacher teacher1 = new Teacher(1, "Преподаватель1", "Фам1", 6);
        Student st1 = new Student(1,"Ученик1", "Ф1", "11-001");
        Student st2 = new Student(2,"Ученик2", "Ф2", "11-001");
        List<Student> listOfStudentsOnC5 = new ArrayList<>();
        listOfStudentsOnC5.add(st1);
        listOfStudentsOnC5.add(st2);

        Course course5 = new Course("Курс5", "09.02.21", "28.05.21", teacher1, listOfStudentsOnC5);
        coursesRep.save(course5);

        Teacher teacher2 = new Teacher(2, "Преподаватель2", "Фам2", 10);
        course5.setTeacher(teacher2);
        coursesRep.update(course5);

        System.out.println("Find by id = 5: ");
        System.out.println(coursesRep.findById(5).get());

        System.out.println("Find all by name = Курс4: ");
        for (Course course : coursesRep.findAllByName("Курс4")) {
            System.out.println(course);
        }

        System.out.println("Find all: ");
        for (Course course : coursesRep.findAll()) {
            System.out.println(course);
        }

        coursesRep.deleteById(9);
    }

    public static void checkWorkOfLessonsRep(DataSource dataSource) {
        LessonsRepository lesRep = new LessonsRepositoryJdbcTemplateImpl(dataSource);

        Teacher teacher1 = new Teacher(1, "Преподаватель1", "Фам1", 6);
        Course c4 = new Course(4, "Курс4", "29.06.21", "12.07.21", teacher1);
        Lesson l1 = new Lesson("Урок4 тема45", "СР 07.07.21", c4);

        lesRep.save(l1);

        l1.setTime("ЧТ 08.07.21");
        lesRep.update(l1);

        System.out.println("Find by id = 1: ");
        System.out.println(lesRep.findById(1).get());

        System.out.println("Find all by name = Урок1 тема1: ");
        for (Lesson lesson : lesRep.findAllByName("Урок1 тема1")) {
            System.out.println(lesson);
        }

        System.out.println("Find all: ");
        for (Lesson lesson : lesRep.findAll()) {
            System.out.println(lesson);
        }

        lesRep.deleteById(6);
    }

}
