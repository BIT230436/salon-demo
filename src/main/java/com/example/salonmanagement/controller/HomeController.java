package com.example.salonmanagement.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping({"/", "/index"})
    public String index() {
        return "index";
    }

    @GetMapping("/promotions")
    public String promotionsList() {
        return "promotion/list";
    }

    @GetMapping("/promotions/new")
    public String promotionsCreateForm() {
        return "promotion/form";
    }

    @GetMapping("/promotions/edit")
    public String promotionsEditForm() {
        // form page reads id from query param ?id=
        return "promotion/form";
    }
}
