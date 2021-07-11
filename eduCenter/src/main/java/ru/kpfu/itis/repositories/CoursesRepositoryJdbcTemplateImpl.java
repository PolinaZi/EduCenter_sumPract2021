package ru.kpfu.itis.repositories;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import ru.kpfu.itis.models.Course;
import ru.kpfu.itis.models.Student;
import ru.kpfu.itis.models.Teacher;
import ru.kpfu.itis.repositories.CoursesRepository;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.util.*;

public class CoursesRepositoryJdbcTemplateImpl implements CoursesRepository {

    //language=SQL
    private static final String SQL_INSERT_WITH_TEACHER = "insert into course(course_name, start_date, end_date, teacher) values (?, ?, ?, ?)";

    //language=SQL
    private static final String SQL_INSERT = "insert into course(course_name, start_date, end_date) values (?, ?, ?)";

    //language=SQL
    private static final String SQL_INSERT_STUDENTS_OF_COURSE = "insert into courses_students(course_id, student_id) values (?, ?)";

    //language=SQL
    private static final String SQL_UPDATE_BY_ID = "update course set course_name = ?, start_date = ?, end_date = ? where id = ?";

    //language=SQL
    private static final String SQL_UPDATE_BY_ID_WITH_T = "update course set course_name = ?, start_date = ?, end_date = ?, " +
                                                            "teacher = ? where id = ?";

    //language=SQL
    private static final String SQL_SELECT_BY_ID = "select c_id, course_name, start_date, end_date, teacher, t.first_name as " +
            "teacher_first_name, t.last_name as teacher_last_name,experience, student_id, student_first_name, student_last_name, group_number " +
            "from (select c.id as c_id, course_name, start_date, end_date, teacher, student_id, n1.first_name as student_first_name, " +
            "n1.last_name as student_last_name, group_number from (select * from courses_students cs left join student s on s.id = cs.student_id) n1 " +
            "right join course c on c.id = course_id where c.id = ?) n2 inner join teacher t on t.id = n2.teacher order by c_id";

    //language=SQL
    private static final String SQL_SELECT_ALL_BY_NAME = "select c_id, course_name, start_date, end_date, teacher, t.first_name as " +
            "teacher_first_name, t.last_name as teacher_last_name,experience, student_id, student_first_name, student_last_name, group_number " +
            "from (select c.id as c_id, course_name, start_date, end_date, teacher, student_id, n1.first_name as student_first_name, " +
            "n1.last_name as student_last_name, group_number from (select * from courses_students cs left join student s on s.id = cs.student_id) n1 " +
            "right join course c on c.id = course_id where course_name = ?) n2 inner join teacher t on t.id = n2.teacher order by c_id";

    //language=SQL
    private static final String SQL_SELECT_ALL = "select c_id, course_name, start_date, end_date, teacher, t.first_name as " +
            "teacher_first_name, t.last_name as teacher_last_name,experience, student_id, student_first_name, student_last_name, group_number " +
            "from (select c.id as c_id, course_name, start_date, end_date, teacher, student_id, n1.first_name as student_first_name, " +
            "n1.last_name as student_last_name, group_number from (select * from courses_students cs left join student s on s.id = cs.student_id) n1 " +
            "right join course c on c.id = course_id) n2 inner join teacher t on t.id = n2.teacher order by c_id";

    //language=SQL
    private static final String SQL_DELETE_BY_ID = " delete from lesson where course = ?; delete from courses_students " +
            "where course_id = ?;delete from course c where c.id = ?;";

    //language=SQL
    private static final String SQL_DELETE_STUDENTS_OF_COURSES = "delete from courses_students where course_id = ?";

    private JdbcTemplate jdbcTemplate;

    public CoursesRepositoryJdbcTemplateImpl(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    private final ResultSetExtractor<List<Course>> courseResultSetExtractor = resultSet -> {
        List<Course> courses = new ArrayList<>();

        Set<Integer> processedCourses = new HashSet<>();
        Course currentCourse = null;

        while (resultSet.next()) {
            if (!processedCourses.contains(resultSet.getInt("c_id"))) {

                Integer courseId = resultSet.getInt("c_id");
                String courseName = resultSet.getString("course_name");
                String startDate = resultSet.getString("start_date");
                String endDate = resultSet.getString("end_date");

                Integer teacherId = resultSet.getInt("teacher");
                String teacher_first_name = resultSet.getString("teacher_first_name");
                String teacher_last_name = resultSet.getString("teacher_last_name");
                Integer experience = resultSet.getInt("experience");

                Teacher teacher = new Teacher(teacherId, teacher_first_name, teacher_last_name, experience);
                currentCourse = new Course(courseId, courseName, startDate, endDate, teacher, new ArrayList<>());

                courses.add(currentCourse);
            }

            Integer studentId = resultSet.getObject("student_id", Integer.class);

            if (studentId != null) {
                String firstName = resultSet.getString("student_first_name");
                String lastName = resultSet.getString("student_last_name");
                String groupNumber = resultSet.getString("group_number");

                Student student = new Student(studentId, firstName, lastName, groupNumber, new ArrayList<>());
                student.getCourses().add(currentCourse);

                currentCourse.getStudents().add(student);
            }
            processedCourses.add(currentCourse.getId());
        }
        return courses;
    };

    @Override
    public void save(Course course) {
        KeyHolder keyHolder = new GeneratedKeyHolder();

        String query = course.getTeacher() == null ? SQL_INSERT : SQL_INSERT_WITH_TEACHER;

        jdbcTemplate.update(connection -> {
            PreparedStatement statement = connection.prepareStatement(query, new String[] {"id"});

            statement.setString(1, course.getName());
            statement.setString(2, course.getStartDate());
            statement.setString(3, course.getEndDate());

            if (course.getTeacher() != null) {
                statement.setInt(4,course.getTeacher().getId());
            }

            return statement;
        }, keyHolder);

        course.setId(keyHolder.getKey().intValue());

        insertStudentsOfCourse(course);
    }

    private void insertStudentsOfCourse(Course course) {
        for (Student student : course.getStudents()) {

            if (student.getId() == null) {
                throw new IllegalArgumentException();
            }

            jdbcTemplate.update(SQL_INSERT_STUDENTS_OF_COURSE, course.getId(), student.getId());
        }
    }

    @Override
    public void update(Course course) {
        if (course == null || course.getId() == null) {
            throw new IllegalArgumentException();
        }

        if (course.getTeacher() == null) {
            jdbcTemplate.update(SQL_UPDATE_BY_ID, course.getName(), course.getStartDate(), course.getEndDate(),  course.getId());
        } else {
            if (course.getTeacher().getId() == null) {
                throw new IllegalArgumentException();
            }
            jdbcTemplate.update(SQL_UPDATE_BY_ID_WITH_T, course.getName(), course.getStartDate(), course.getEndDate(), course.getTeacher().getId(),course.getId());
        }

        jdbcTemplate.update(SQL_DELETE_STUDENTS_OF_COURSES, course.getId());
        insertStudentsOfCourse(course);
    }

    @Override
    public Optional<Course> findById(Integer id) {
        try {
            return Optional.of(jdbcTemplate.query(SQL_SELECT_BY_ID, courseResultSetExtractor, id).get(0));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<Course> findAllByName(String name) {
        return jdbcTemplate.query(SQL_SELECT_ALL_BY_NAME, courseResultSetExtractor, name);
    }

    @Override
    public List<Course> findAll() {
        return jdbcTemplate.query(SQL_SELECT_ALL, courseResultSetExtractor);
    }

    @Override
    public void deleteById(Integer id) {
        jdbcTemplate.update(SQL_DELETE_BY_ID, id, id, id);
    }
}
