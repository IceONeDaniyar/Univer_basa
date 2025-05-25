package com.example.university.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.stereotype.Controller;

@Controller
public class BaseController {

    @ModelAttribute("currentPath")
    public String getCurrentPath(HttpServletRequest request) {
        return request.getRequestURI();
    }
}
