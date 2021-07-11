package ru.kpfu.itis.repositories;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import ru.kpfu.itis.models.Course;
import ru.kpfu.itis.models.Lesson;
import ru.kpfu.itis.repositories.CoursesRepositoryJdbcTemplateImpl;
import ru.kpfu.itis.repositories.LessonsRepository;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.util.List;
import java.util.Optional;

public class LessonsRepositoryJdbcTemplateImpl implements LessonsRepository {

    //language=SQL
    private static final String SQL_INSERT = "insert into lesson(lesson_name, time, course) values (?, ?, ?)";

    //language=SQL
    private static final String SQL_UPDATE_BY_ID = "update lesson set lesson_name = ?, time = ?, course = ? where id = ?";

    //language=SQL
    private static final String SQL_SELECT_BY_ID = "select id, lesson_name, time, course from lesson where id = ?";

    //language=SQL
    private static final String SQL_SELECT_ALL_BY_NAME = "select id, lesson_name, time, course from lesson where lesson_name = ?";

    //language=SQL
    private static final String SQL_SELECT_ALL = "select id, lesson_name, time, course from lesson";

    //language=SQL
    private static final String SQL_DELETE_BY_ID = "delete from lesson where id = ?";

    private JdbcTemplate jdbcTemplate;

    public LessonsRepositoryJdbcTemplateImpl(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    private final RowMapper<Lesson> lessonRowMapper = (row, rowNumber) -> {

        int id = row.getInt("id");
        String name = row.getString("lesson_name");
        String time = row.getString("time");
        Integer courseId = row.getInt("course");

        Course course = new CoursesRepositoryJdbcTemplateImpl(jdbcTemplate.getDataSource()).findById(courseId).get();

        return new Lesson(id, name, time, course);
    };

    @Override
    public void save(Lesson lesson) {
        KeyHolder keyHolder = new GeneratedKeyHolder();

        if (lesson.getCourse() == null || lesson.getCourse().getId() == null) {
            throw new IllegalArgumentException();
        }

        jdbcTemplate.update(connection -> {
            PreparedStatement statement = connection.prepareStatement(SQL_INSERT, new String[] {"id"});

            statement.setString(1, lesson.getName());
            statement.setString(2, lesson.getTime());
            statement.setInt(3, lesson.getCourse().getId());

            return statement;
        }, keyHolder);

        lesson.setId(keyHolder.getKey().intValue());
    }

    @Override
    public void update(Lesson lesson) {
        if (lesson.getCourse() == null || lesson.getCourse().getId() == null) {
            throw new IllegalArgumentException();
        }

        jdbcTemplate.update(SQL_UPDATE_BY_ID, lesson.getName(), lesson.getTime(), lesson.getCourse().getId(), lesson.getId());
    }

    @Override
    public Optional<Lesson> findById(Integer id) {
        try {
            return Optional.ofNullable( jdbcTemplate.queryForObject(SQL_SELECT_BY_ID, lessonRowMapper, id));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<Lesson> findAllByName(String name) {
        return jdbcTemplate.query(SQL_SELECT_ALL_BY_NAME, lessonRowMapper, name);
    }

    @Override
    public List<Lesson> findAll() {
        return jdbcTemplate.query(SQL_SELECT_ALL, lessonRowMapper);
    }

    @Override
    public void deleteById(Integer id) {
        jdbcTemplate.update(SQL_DELETE_BY_ID, id);
    }
}
