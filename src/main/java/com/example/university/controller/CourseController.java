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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Controller
@RequestMapping("/courses")
public class CourseController {

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private StudentRepository studentRepository;

    @GetMapping
    public String listCourses(Model model, Authentication auth) {
        List<Course> courses = courseRepository.findAll();
        model.addAttribute("courses", courses);

        boolean isAdmin = auth != null && auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
        model.addAttribute("isAdmin", isAdmin);

        // Для формы добавления нового курса (пустой объект)
        model.addAttribute("course", new Course());

        // Список всех студентов для выбора при создании курса
        List<Student> allStudents = studentRepository.findAll();
        model.addAttribute("allStudents", allStudents);

        return "courses";
    }

  @PostMapping
@PreAuthorize("hasRole('ADMIN')")
public String saveCourse(@ModelAttribute Course course, 
                         @RequestParam(required = false) List<Long> studentIds,
                         RedirectAttributes redirectAttributes) {
    if (studentIds != null && !studentIds.isEmpty()) {
        Set<Student> students = new HashSet<>(studentRepository.findAllById(studentIds));
        course.getStudents().clear();
        for (Student student : students) {
            course.addStudent(student);  // добавляем студента с обновлением двунаправленной связи
        }
    } else {
        course.getStudents().clear();
    }

    courseRepository.save(course);
    redirectAttributes.addFlashAttribute("message", "Курс сохранён.");
    return "redirect:/courses";
}


    @GetMapping("/delete/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String deleteCourse(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        courseRepository.deleteById(id);
        redirectAttributes.addFlashAttribute("message", "Курс удалён.");
        return "redirect:/courses";
    }
    @GetMapping("/edit/{id}")
@PreAuthorize("hasRole('ADMIN')")
public String editCourse(@PathVariable Long id, Model model, Authentication auth) {
    Course course = courseRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Неверный ID курса"));
    model.addAttribute("course", course);

    boolean isAdmin = auth != null && auth.getAuthorities().stream()
            .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
    model.addAttribute("isAdmin", isAdmin);

    List<Student> allStudents = studentRepository.findAll();
    model.addAttribute("allStudents", allStudents);
    model.addAttribute("course", course);

    return "courses"; // возвращаем ту же страницу с формой редактирования
}

@PostMapping("/update")
@PreAuthorize("hasRole('ADMIN')")
public String updateCourse(@ModelAttribute("course") Course updatedCourse,
                           @RequestParam(required = false) List<Long> studentIds,
                           RedirectAttributes redirectAttributes) {
    Course course = courseRepository.findById(updatedCourse.getId())
            .orElseThrow(() -> new IllegalArgumentException("Курс не найден"));

    course.setName(updatedCourse.getName());

    if (studentIds != null) {
        Set<Student> students = new HashSet<>(studentRepository.findAllById(studentIds));
        course.getStudents().clear();
        for (Student student : students) {
            course.addStudent(student); // обновляем двунаправленную связь
        }
    } else {
        course.getStudents().clear();
    }

    courseRepository.save(course);
    redirectAttributes.addFlashAttribute("message", "Курс успешно обновлён.");
    return "redirect:/courses";
}

}
