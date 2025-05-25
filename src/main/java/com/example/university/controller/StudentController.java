package com.example.university.controller;

import com.example.university.model.Course;
import com.example.university.model.Student;
import com.example.university.repository.CourseRepository;
import com.example.university.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Controller
public class StudentController {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private CourseRepository courseRepository;

    @GetMapping("/")
    public String showList(Model model, Authentication auth) {
        model.addAttribute("students", studentRepository.findAll());
        model.addAttribute("student", new Student());

        List<Course> allCourses = courseRepository.findAll();
        model.addAttribute("allCourses", allCourses);

        boolean isAdmin = auth != null && auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
        model.addAttribute("isAdmin", isAdmin);

        return "index";
    }

    @PostMapping("/students")
    public String saveStudent(@ModelAttribute Student student,
                              @RequestParam(value = "courseIds", required = false) List<Long> courseIds) {
        if (courseIds != null) {
            Set<Course> courses = new HashSet<>(courseRepository.findAllById(courseIds));
            student.setCourses(courses);
        }
        studentRepository.save(student);
        return "redirect:/";
    }

    @GetMapping("/students/edit/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String editStudent(@PathVariable Long id, Model model) {
        Student student = studentRepository.findById(id).orElseThrow();
        model.addAttribute("student", student);
        model.addAttribute("students", studentRepository.findAll());
        model.addAttribute("allCourses", courseRepository.findAll());
        model.addAttribute("isAdmin", true);
        return "index";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/students/delete/{id}")
    public String deleteStudent(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        if (studentRepository.existsById(id)) {
            Student student = studentRepository.findById(id).orElse(null);
            if (student != null) {
                student.getCourses().forEach(c -> c.getStudents().remove(student));
                student.getCourses().clear();
                studentRepository.save(student);
            }
            studentRepository.deleteById(id);
            redirectAttributes.addFlashAttribute("message", "Студент успешно удалён.");
        } else {
            redirectAttributes.addFlashAttribute("error", "Студент не найден.");
        }
        return "redirect:/";
    }

    @GetMapping("/students/export/excel")
    public void exportToExcel(HttpServletResponse response) throws IOException {
        response.setContentType("application/octet-stream");
        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=students.xlsx";
        response.setHeader(headerKey, headerValue);

        List<Student> students = studentRepository.findAll();
        new com.example.university.service.ExcelExporter(students).export(response);
    }

    @PostMapping("/students/import/excel")
    public String importFromExcel(@RequestParam("file") MultipartFile file) {
        try {
            List<Student> students = new com.example.university.service.ExcelImporter().importStudents(file.getInputStream());
            studentRepository.saveAll(students);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "redirect:/";
    }

    @GetMapping("/student-reports")
    public String showReports(Model model) {
        model.addAttribute("totalStudents", 120);
        model.addAttribute("averageGrade", "B+");
        model.addAttribute("activeCourses", 8);
        model.addAttribute("recentGrades", List.of());
        return "reports";
    }
}
