package com.example.demo.controller;

import com.example.demo.entity.Product;
import com.example.demo.repo.CategoryRepo;
import com.example.demo.repo.IncomeRepo;
import com.example.demo.repo.ProductRepo;
import com.example.demo.repo.SaleRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.UUID;

@Controller
@RequiredArgsConstructor
@RequestMapping("/")
public class HomeController {

    private final CategoryRepo categoryRepo;
    private final ProductRepo productRepo;
    private final SaleRepo saleRepo;
    private final IncomeRepo incomeRepo;

    @GetMapping
    public String home(
            Model model,
            @RequestParam(required = false) String categoryName,
            @RequestParam(required = false)UUID categoryId,
            @RequestParam(required = false) String productName,
            @RequestParam(required = false) UUID productId
            ){
        if (categoryName!=null){
            model.addAttribute("categoryName", categoryName);
            model.addAttribute("categories",categoryRepo.findAllByNameContainingIgnoreCase(categoryName));
        }else {
            model.addAttribute("categories",categoryRepo.findAll());
        }
        if (categoryId!=null){
            model.addAttribute("categoryId",categoryId);
            if(productName!=null){
                model.addAttribute("productName",productName);
                model.addAttribute("products",productRepo.findAllByCategoryIdAndNameContainingIgnoreCase(categoryId,productName));
            }else{
                model.addAttribute("products",productRepo.findAllByCategoryId(categoryId));
            }
            if (productId!=null){
                model.addAttribute("currentProduct",productRepo.findById(productId).get());
                model.addAttribute("productId",productId);
            }
        }
        return "home";
    }
    @GetMapping("alo")
    public String alo(
            Model model,
            @RequestParam(required = false) String categoryName,
            @RequestParam(required = false)UUID categoryId,
            @RequestParam(required = false) String productName,
            @RequestParam(required = false) UUID productId
    ){
        if (categoryName!=null){
            model.addAttribute("categoryName", categoryName);
            model.addAttribute("categories",categoryRepo.findAllByNameContainingIgnoreCase(categoryName));
        }else {
            model.addAttribute("categories",categoryRepo.findAll());
        }
        if (categoryId!=null){
            model.addAttribute("categoryId",categoryId);
            if(productName!=null){
                model.addAttribute("productName",productName);
                model.addAttribute("products",productRepo.findAllByCategoryIdAndNameContainingIgnoreCase(categoryId,productName));
            }else{
                model.addAttribute("products",productRepo.findAllByCategoryId(categoryId));
            }
            if (productId!=null){
                model.addAttribute("currentProduct",productRepo.findById(productId).get());
                model.addAttribute("productId",productId);
            }
        }
        return "sale";
    }

    @GetMapping("sales")
    public String salesList(Model model, @RequestParam UUID productId, @RequestParam UUID categoryId){
        Product product = productRepo.findById(productId).get();
        model.addAttribute("price",product.getPrice());
        model.addAttribute("products",productRepo.findAllByCategoryId(categoryId));
        model.addAttribute("categories",categoryRepo.findAll());
        model.addAttribute("sales",saleRepo.findAllByProductId(productId));
        model.addAttribute("productId",productId);
        model.addAttribute("categoryId",categoryId);
        return "/productSalesInfo";
    }
    @GetMapping("incomes")
    public String incomesList(Model model, @RequestParam UUID productId, @RequestParam UUID categoryId){
        Product product = productRepo.findById(productId).get();
        model.addAttribute("products",productRepo.findAllByCategoryId(categoryId));
        model.addAttribute("categories",categoryRepo.findAll());
        model.addAttribute("incomes",incomeRepo.findAllByProductId(productId));
        model.addAttribute("productId",productId);
        model.addAttribute("price",product.getPrice());
        model.addAttribute("categoryId",categoryId);
        return "/productIncomesInfo";
    }
}
