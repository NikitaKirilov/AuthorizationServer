package org.example.backend.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginUiController {

    @GetMapping("/login")
    public String getLoginPage() {
        return "index.html";
    }
}
