package com.example.university.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.university.model.Book;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    List<Book> findByCategory(String category);

    @Query("SELECT DISTINCT b.category FROM Book b")
    List<String> findDistinctCategories();
}