package com.example.demo.controller;

import com.example.demo.projection.BalanceRepoProjection;
import com.example.demo.projection.ProfitRepoProjection;
import com.example.demo.projection.SalesRepoProjection;
import com.example.demo.repo.IncomeRepo;
import com.example.demo.repo.ProductRepo;
import com.example.demo.repo.SaleRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("report")
@RequiredArgsConstructor
public class ReportController {

    private final SaleRepo saleRepo;
    private final IncomeRepo incomeRepo;
    private final ProductRepo productRepo;

    @GetMapping
    public String reportPage(){
        return "/report/home";
    }

    @GetMapping("sales")
    public String salesRepo(Model model){
        List<SalesRepoProjection> salesRepoProjections = saleRepo.saleRepo();
        model.addAttribute("saleRepo",salesRepoProjections);
        return "/report/salesReport";
    }

    @GetMapping("profit")
    public String profitRepo(Model model){
        List<ProfitRepoProjection> profitRepoProjections = saleRepo.profitRepo();
        model.addAttribute("profit",profitRepoProjections);
        return "/report/profitReport";
    }

    @GetMapping("balance")
    public String balanceRepo(Model model){
        List<BalanceRepoProjection> balanceRepoProjections = productRepo.balanceRepo();
        model.addAttribute("balance",balanceRepoProjections);
        return "/report/balanceReport";
    }
}
