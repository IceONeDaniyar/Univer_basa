package com.example.university.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.university.model.Student;

public interface StudentRepository extends JpaRepository<Student, Long> {

}
