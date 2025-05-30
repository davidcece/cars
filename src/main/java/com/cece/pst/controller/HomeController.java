package com.cece.pst.controller;

import com.cece.pst.dto.Brand;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;


@Controller
@RequestMapping("/")
@RequiredArgsConstructor
public class HomeController {

    private final List<Brand> brands;

    @GetMapping
    public String index(Model model) {
        model.addAttribute("brands", brands);
        return "index";
    }

}
