package ru.kpfu.itis.models;

import java.util.Objects;

public class Lesson {
    private Integer id;
    private String name;
    private String time;
    private Course course;

    public Lesson(Integer id, String name, String time, Course course) {
        this.id = id;
        this.name = name;
        this.time = time;
        this.course = course;
    }

    public Lesson(String name, String  time, Course course) {
        this.name = name;
        this.time = time;
        this.course = course;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Lesson lesson = (Lesson) o;
        return Objects.equals(id, lesson.id) &&
                Objects.equals(name, lesson.name) &&
                Objects.equals(time, lesson.time) &&
                Objects.equals(course, lesson.course);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, time, course);
    }

    @Override
    public String toString() {
        return "Lesson{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", time='" + time + '\'' +
                ", course=" + course +
                '}';
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }
}
