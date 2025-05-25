package com.example.university.model;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String uploader;      // Кто загрузил книгу
    private LocalDate uploadDate; // Дата загрузки
    private String filePath;      // Путь к файлу книги
    private String category;

    public Book() {
        // Конструктор без аргументов
    }

    public Book(Long id, String title, String uploader, LocalDate uploadDate, String filePath, String category) {
        this.id = id;
        this.title = title;
        this.uploader = uploader;
        this.uploadDate = uploadDate;
        this.filePath = filePath;
        this.category = category;
    }

    // Геттеры и сеттеры

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUploader() {
        return uploader;
    }

    public void setUploader(String uploader) {
        this.uploader = uploader;
    }

    public LocalDate getUploadDate() {
        return uploadDate;
    }

    public void setUploadDate(LocalDate uploadDate) {
        this.uploadDate = uploadDate;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getCategory() {
    return category;
}

    public void setCategory(String category) {
    this.category = category;
}

   
}
