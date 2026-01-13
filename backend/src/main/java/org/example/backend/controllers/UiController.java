package org.example.backend.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/app")
public class UiController {

    @GetMapping("**")
    public String getLoginPage() {
        return "index.html";
    }
}
