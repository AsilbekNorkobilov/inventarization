package com.example.demo.controller;


import com.example.demo.entity.Category;
import com.example.demo.entity.Income;
import com.example.demo.entity.Product;
import com.example.demo.repo.CategoryRepo;
import com.example.demo.repo.IncomeRepo;
import com.example.demo.repo.ProductRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
@RequestMapping("product")
public class ProductController {

    private final CategoryRepo categoryRepo;
    private final ProductRepo productRepo;
    private final IncomeRepo incomeRepo;

    @PostMapping("add")
    public String  addProduct(@RequestParam UUID categoryId, @RequestParam Integer price, @RequestParam String name, @RequestParam MultipartFile photo) throws IOException {
        Category category = categoryRepo.findById(categoryId).get();
        Product product=new Product();
        product.setName(name);
        product.setPrice(price);
        product.setCategory(category);
        product.setPhoto(photo.getBytes());
        productRepo.save(product);
        return "redirect:/";
    }
    @PostMapping("edit")
    public String  editProduct(
            @RequestParam Integer price,
            @RequestParam String name,
            @RequestParam MultipartFile photo,
            @RequestParam UUID productId
    ) throws IOException {
        Product product = productRepo.findById(productId).get();
        product.setName(name);
        product.setPrice(price);
        if (photo.getBytes().length!=0){
            product.setPhoto(photo.getBytes());
        }
        productRepo.save(product);
        return "redirect:/";
    }

    @GetMapping("add")
    private String addPage(Model model){
        model.addAttribute("categories",categoryRepo.findAll());
        return "addProduct";
    }

    @GetMapping("income")
    private String incomePage(Model model,@RequestParam UUID productId){
        Product product = productRepo.findById(productId).get();
        model.addAttribute("product", product);
        return "/incomeProduct";
    }
    @PostMapping("income")
    private String income(
            @RequestParam Integer amount,
            @RequestParam Integer price,
            @RequestParam UUID productId
    ){
        Product product = productRepo.findById(productId).get();
        Income income=new Income(amount,price,product);
        incomeRepo.save(income);
        return "redirect:/";
    }
}
