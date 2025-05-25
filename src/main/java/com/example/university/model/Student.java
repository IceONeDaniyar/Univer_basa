package com.example.university.model;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String email;
    private String groupName;
    private String grade;  // Добавлено

    @ManyToMany(mappedBy = "students")
    private Set<Course> courses = new HashSet<>();

    public Set<Course> getCourse() {
    return courses;
}

    public void setCourse(Set<Course> courses) {
    this.courses = courses;
}
@ManyToMany
@JoinTable(
    name = "course_student",
    joinColumns = @JoinColumn(name = "course_id"),
    inverseJoinColumns = @JoinColumn(name = "student_id")
)
private Set<Student> students = new HashSet<>();



    // Геттеры и сеттеры...

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getGroupName() { return groupName; }
    public void setGroupName(String groupName) { this.groupName = groupName; }

    public String getGrade() { return grade; }
    public void setGrade(String grade) { this.grade = grade; }

    public Set<Course> getCourses() { return courses; }
    public void setCourses(Set<Course> courses) { this.courses = courses; }
}
