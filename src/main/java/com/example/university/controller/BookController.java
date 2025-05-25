package com.example.university.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.List;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.university.model.Book;
import com.example.university.repository.BookRepository;
import com.example.university.service.FileStorageService;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;

@Controller
public class BookController {

    private final BookRepository bookRepository;
    private final FileStorageService fileStorageService;

    public BookController(BookRepository bookRepository, FileStorageService fileStorageService) {
        this.bookRepository = bookRepository;
        this.fileStorageService = fileStorageService;
    }

    @GetMapping("/reports")
    public String showReports(Model model) {
        List<Book> books = bookRepository.findAll();
        model.addAttribute("books", books);

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        boolean isAdmin = auth != null && auth.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"));
        model.addAttribute("isAdmin", isAdmin);

        return "reports"; // убедись, что есть reports.html в /resources/templates/
    }

    @GetMapping("/books/upload")
    public String showUploadForm(Model model) {
        return "books/upload";
    }

    @PostMapping("/books/upload")
    public String uploadBook(@RequestParam String title,
                             @RequestParam String uploader,
                             @RequestParam String category,
                             @RequestParam("file") MultipartFile file) {
        try {
            String filePath = fileStorageService.saveFile(file);

            Book book = new Book();
            book.setTitle(title);
            book.setUploader(uploader);
            book.setUploadDate(LocalDate.now());
            book.setFilePath(filePath);
            book.setCategory(category);

            bookRepository.save(book);
            return "redirect:/reports";
        } catch (IOException e) {
            e.printStackTrace();
            return "error";
        }
    }

    @GetMapping("/books/download/{id}")
    public void downloadFile(@PathVariable Long id, HttpServletResponse response) throws IOException {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new IOException("Book not found with id " + id));
        Path path = Paths.get(book.getFilePath());

        if (!Files.exists(path)) {
            throw new IOException("File not found: " + path.toString());
        }

        response.setContentType("application/octet-stream");
        response.setHeader("Content-Disposition", "attachment; filename=\"" + path.getFileName().toString() + "\"");

        Files.copy(path, response.getOutputStream());
        response.getOutputStream().flush();
    }

    @GetMapping("/books")
    public String listBooks(@RequestParam(value = "category", required = false) String category, Model model) {
        List<Book> books = (category == null || category.isEmpty())
                ? bookRepository.findAll()
                : bookRepository.findByCategory(category);

        model.addAttribute("books", books);
        model.addAttribute("categories", bookRepository.findDistinctCategories());
        return "books";
    }
    @GetMapping("/books/delete/{id}")
    public String deleteBook(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            bookRepository.deleteById(id);
            redirectAttributes.addFlashAttribute("message", "Книга успешно удалена.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Ошибка при удалении книги.");
        }
        return "redirect:/reports"; // или на другую страницу, где отображается список книг
    }
}

