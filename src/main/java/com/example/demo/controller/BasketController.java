package com.example.demo.controller;

import com.example.demo.dao.Basket;
import com.example.demo.dao.BasketProduct;
import com.example.demo.entity.Product;
import com.example.demo.entity.Sale;
import com.example.demo.repo.CategoryRepo;
import com.example.demo.repo.IncomeRepo;
import com.example.demo.repo.ProductRepo;
import com.example.demo.repo.SaleRepo;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
@RequestMapping("basket")
public class BasketController {

    private final ProductRepo productRepo;
    private final IncomeRepo incomeRepo;
    private final SaleRepo saleRepo;
    private final CategoryRepo categoryRepo;

    @GetMapping("delete")
    public String deleteFromBasket(@RequestParam UUID productId,
                                   @RequestParam UUID categoryId,
                                   Model model,
                                   HttpSession session){
        Basket basket =(Basket) session.getAttribute("basket");
        List<BasketProduct> update=basket.getBasketProducts().stream()
                .filter(item->!item.getProduct().getId().equals(productId)).collect(Collectors.toList());
        basket.setBasketProducts(update);
        session.setAttribute("basket",basket);
        model.addAttribute("productId",productId);
        model.addAttribute("categoryId",categoryId);
        model.addAttribute("products",productRepo.findAllByCategoryId(categoryId));
        model.addAttribute("categories",categoryRepo.findAll());
        double sum = update.stream().mapToDouble(item -> item.getProduct().getPrice() * item.getAmount()).sum();
        model.addAttribute("sum",sum);
        Integer incomeCount= Objects.requireNonNullElse(incomeRepo.ProductIncomeCount(productId),0);
        Integer saleCount=Objects.requireNonNullElse(saleRepo.ProductSaleCount(productId),0);
        model.addAttribute("currentProduct",productRepo.findById(productId).get());
        model.addAttribute("productCount",incomeCount-saleCount);
        model.addAttribute("basketProducts", update);
        model.addAttribute("count",0);
        return "/sale";
    }


    @PostMapping("count")
    public String  action(
            @RequestParam String action,
            @RequestParam UUID productId,
            @RequestParam UUID categoryId,
            @RequestParam Integer count,
            HttpSession session,
            Model model
            ){
        Basket basket= Objects.requireNonNullElse((Basket) session.getAttribute("basket"),new Basket());
        List<BasketProduct> basketProducts = basket.getBasketProducts();
        if (action.equals("plus")){
            count++;
        }else {
            count--;
        }
        model.addAttribute("productId",productId);
        model.addAttribute("categoryId",categoryId);
        model.addAttribute("products",productRepo.findAllByCategoryId(categoryId));
        model.addAttribute("categories",categoryRepo.findAll());
        double sum = basketProducts.stream().mapToDouble(item -> item.getProduct().getPrice() * item.getAmount()).sum();
        model.addAttribute("sum",sum);
        model.addAttribute("count",count);
        Integer incomeCount=Objects.requireNonNullElse(incomeRepo.ProductIncomeCount(productId),0);
        Integer saleCount=Objects.requireNonNullElse(saleRepo.ProductSaleCount(productId),0);
        model.addAttribute("currentProduct",productRepo.findById(productId).get());
        model.addAttribute("productCount",incomeCount-saleCount);
        model.addAttribute("basketProducts", basketProducts);
        return "/sale";    }

    @PostMapping("add")
    public String addToBasket(@RequestParam UUID productId,
                              @RequestParam UUID categoryId,
                              @RequestParam Integer count,
                              HttpSession session,
                              Model model
                              ){
        Product product = productRepo.findById(productId).get();
        Object object = session.getAttribute("basket");
        Basket basket=null;
        if (object==null){
            basket=new Basket();
            List<BasketProduct> basketProducts=new ArrayList<>();
            basketProducts.add(new BasketProduct(product,count));
            basket.setBasketProducts(basketProducts);
        }else {
            basket=(Basket) object;
            List<BasketProduct> basketProducts = basket.getBasketProducts();
            Optional<BasketProduct> opt = basketProducts.stream()
                    .filter(item -> item.getProduct().getId().equals(productId)).findFirst();
            if (opt.isPresent()){
                BasketProduct basketProduct = opt.get();
                basketProduct.setAmount(count);

            }else {
                model.addAttribute("count",0);
                basket.getBasketProducts().add(new BasketProduct(product,count));
            }
        }
        model.addAttribute("count",count);
        model.addAttribute("productId", productId);
        model.addAttribute("categoryId", categoryId);
        model.addAttribute("products",productRepo.findAllByCategoryId(categoryId));
        model.addAttribute("categories",categoryRepo.findAll());
        session.setAttribute("basket",basket);
        List<BasketProduct> basketProducts = basket.getBasketProducts();
        double sum = basketProducts.stream().mapToDouble(item -> item.getProduct().getPrice() * item.getAmount()).sum();
        model.addAttribute("sum",sum);
        Integer incomeCount=Objects.requireNonNullElse(incomeRepo.ProductIncomeCount(productId),0);
        Integer saleCount=Objects.requireNonNullElse(saleRepo.ProductSaleCount(productId),0);
        model.addAttribute("productCount",incomeCount-saleCount);
        model.addAttribute("currentProduct",productRepo.findById(productId).get());
        model.addAttribute("basketProducts", basketProducts);
        return "/sale";
    }

    @PostMapping("makeOrder")
    public String makeOrder(HttpSession session){
        Basket basket = (Basket)session.getAttribute("basket");
        List<BasketProduct> basketProducts = basket.getBasketProducts();
        for (BasketProduct basketProduct : basketProducts) {
            saleRepo.save(new Sale(basketProduct.getAmount(),basketProduct.getProduct() ));
        }
        session.removeAttribute("basket");
        return "/sale";
    }
}
