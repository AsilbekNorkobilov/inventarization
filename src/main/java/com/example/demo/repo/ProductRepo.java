package com.example.demo.repo;

import com.example.demo.entity.Product;
import com.example.demo.projection.BalanceRepoProjection;
import com.example.demo.projection.ProfitRepoProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface ProductRepo extends JpaRepository<Product, UUID> {
    List<Product> findAllByCategoryId(UUID categoryId);
    List<Product> findAllByCategoryIdAndNameContainingIgnoreCase(UUID categoryId, String name);

    @Query(nativeQuery = true,value = """
            SELECT
              p.name AS product_name,
              COALESCE(i.total_income, 0) - COALESCE(s.total_sales, 0) AS balance
          FROM
              product p
                  LEFT JOIN
              (SELECT product_id, SUM(amount) AS total_income
               FROM income
               GROUP BY product_id) i ON p.id = i.product_id
                  LEFT JOIN
              (SELECT product_id, SUM(amount) AS total_sales
               FROM sale
               GROUP BY product_id) s ON p.id = s.product_id
          GROUP BY
              p.name, i.total_income, s.total_sales
          ORDER BY
              balance;
        """)
    List<BalanceRepoProjection> balanceRepo();



}
