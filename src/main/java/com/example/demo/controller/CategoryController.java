package com.example.demo.controller;

import com.example.demo.entity.Category;
import com.example.demo.repo.CategoryRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("category")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryRepo categoryRepo;

    @PostMapping("add")
    public String  addCategory(@ModelAttribute Category category){
        categoryRepo.save(category);
        return "redirect:/";
    }

    @GetMapping("add")
    public String addCategoryPage(){
        return "addCategory";
    }
}
