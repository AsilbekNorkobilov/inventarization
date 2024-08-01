package com.example.demo.controller;

import com.example.demo.dao.Basket;
import com.example.demo.dao.BasketProduct;
import com.example.demo.repo.CategoryRepo;
import com.example.demo.repo.IncomeRepo;
import com.example.demo.repo.ProductRepo;
import com.example.demo.repo.SaleRepo;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
@RequestMapping("sale")
public class SaleController {
    private final CategoryRepo categoryRepo;
    private final ProductRepo productRepo;
    private final IncomeRepo incomeRepo;
    private final SaleRepo saleRepo;

    @GetMapping()
    public String sale(
            HttpSession session,
            Model model,
            @RequestParam(required = false) String categoryName,
            @RequestParam(required = false) UUID categoryId,
            @RequestParam(required = false) String productName,
            @RequestParam(required = false) UUID productId
    ){
        Basket basket= Objects.requireNonNullElse((Basket) session.getAttribute("basket"),new Basket());
        List<BasketProduct> basketProducts = basket.getBasketProducts();
        double sum = basketProducts.stream().mapToDouble(item -> item.getProduct().getPrice() * item.getAmount()).sum();
        model.addAttribute("basketProducts", basketProducts);
        model.addAttribute("sum",sum);
        if (categoryName!=null){
            model.addAttribute("categories",categoryRepo.findAllByNameContainingIgnoreCase(categoryName));
            model.addAttribute("categoryName",categoryName);
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
                Integer incomeCount=Objects.requireNonNullElse(incomeRepo.ProductIncomeCount(productId),0);
                Integer saleCount=Objects.requireNonNullElse(saleRepo.ProductSaleCount(productId),0);
                model.addAttribute("productCount",incomeCount-saleCount);
                model.addAttribute("currentProduct",productRepo.findById(productId).get());
                model.addAttribute("productId",productId);
                Optional<BasketProduct> opt = basketProducts.stream()
                        .filter(item -> item.getProduct().getId().equals(productId)).findFirst();
                if(opt.isPresent()){
                    BasketProduct basketProduct = opt.get();
                    model.addAttribute("count",basketProduct.getAmount());
                }else{
                    model.addAttribute("count",0);
                }
            }
        }
        return "/sale";
    }
}
