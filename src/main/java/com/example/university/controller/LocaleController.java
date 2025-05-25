package com.example.university.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.LocaleResolver;

import java.util.Locale;

@Controller
public class LocaleController {

    private final LocaleResolver localeResolver;

    public LocaleController(LocaleResolver localeResolver) {
        this.localeResolver = localeResolver;
    }

    @GetMapping("/change-lang")
    public String changeLanguage(HttpServletRequest request, HttpServletResponse response, String lang) {
        Locale locale = Locale.forLanguageTag(lang); // lang = "ru" или "en"
        localeResolver.setLocale(request, response, locale);

        // Возврат на предыдущую страницу после смены языка
        String referer = request.getHeader("Referer");
        return "redirect:" + (referer != null ? referer : "/");
    }
}
