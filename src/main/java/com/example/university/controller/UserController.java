package com.example.university.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;

@Controller
public class UserController {

    @GetMapping("/profile")
    public String profile(Principal principal, Model model) {
        String username = principal.getName();
        model.addAttribute("username", username);
        return "profile"; // шаблон profile.html должен существовать
    }
}
