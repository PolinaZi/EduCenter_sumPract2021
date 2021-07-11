package ru.kpfu.itis.repositories;

import ru.kpfu.itis.models.Course;
import ru.kpfu.itis.models.Lesson;

import java.util.List;
import java.util.Optional;

public interface LessonsRepository {
    void save(Lesson lesson);
    void update(Lesson lesson);
    void deleteById(Integer id);
    Optional<Lesson> findById(Integer id);
    List<Lesson> findAllByName(String name);
    List<Lesson> findAll();
}
