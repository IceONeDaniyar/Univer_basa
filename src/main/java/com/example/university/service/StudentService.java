package com.example.university.service;

import com.example.university.model.Student;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface StudentService {
    List<Student> getAllStudents();
    void saveStudent(Student student);
    void importFromExcel(MultipartFile file);
    void exportToExcel(HttpServletResponse response) throws IOException;
}
