package com.example.demo.controller;

import com.example.demo.entity.Product;
import com.example.demo.repo.ProductRepo;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
@RequestMapping("file")
public class FileController {
    private final ProductRepo productRepo;

    @GetMapping("photo")
    public synchronized void getPhoto(HttpServletResponse response, @RequestParam UUID productId) throws IOException {
        Product product = productRepo.findById(productId).get();
        response.getOutputStream().write(product.getPhoto());
    }
}
