package ru.kpfu.itis.repositories;

import ru.kpfu.itis.models.Course;

import java.util.List;
import java.util.Optional;

public interface CoursesRepository {
    void save(Course course);
    void update(Course course);
    void deleteById(Integer id);
    Optional<Course> findById(Integer id);
    List<Course> findAllByName(String name);
    List<Course> findAll();
}
