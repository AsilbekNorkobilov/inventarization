package com.example.demo.controller;

import com.example.demo.entity.Category;
import com.example.demo.entity.Role;
import com.example.demo.entity.User;
import com.example.demo.repo.CategoryRepo;
import com.example.demo.repo.RoleRepo;
import com.example.demo.repo.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("manager")
@RequiredArgsConstructor
public class ManagerController {

    private final UserRepo userRepo;
    private final RoleRepo roleRepo;

    @PostMapping("add")
    public String  addCategory(@ModelAttribute User user){
        List<Role> roleManager = roleRepo.findAllByRole("ROLE_MANAGER");
        user.setRoles(roleManager);
        userRepo.save(user);
        return "redirect:/";
    }

    @GetMapping("add")
    public String addCategoryPage(){
        return "addManager";
    }
}
