package com.example.university.repository;

import com.example.university.model.Course;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseRepository extends JpaRepository<Course, Long> {
     Optional<Course> findByName(String name);
     
}
