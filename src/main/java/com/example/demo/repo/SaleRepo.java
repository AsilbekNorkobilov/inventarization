package com.example.demo.repo;

import com.example.demo.entity.Sale;
import com.example.demo.projection.ProfitRepoProjection;
import com.example.demo.projection.SalesRepoProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface SaleRepo extends JpaRepository<Sale, UUID> {

    List<Sale> findAllByProductId(UUID productId);

    @Query(nativeQuery = true, value = "select sum(amount) from sale where product_id=:productId")
    Integer ProductSaleCount(UUID productId);
    @Query(nativeQuery = true,value = """
        select p.name as product_name,sum(amount) as total_amount, sum(amount*p.price) total_sum
        from sale s join product p on s.product_id = p.id group by p.name
        """)
    List<SalesRepoProjection> saleRepo();


    @Query(nativeQuery = true, value = """
        SELECT
            p.name AS product_name,
            SUM(s.amount * p.price) - SUM(s.amount * avg_income_price.avg_price) AS profit
        FROM
            product p
                JOIN
            sale s ON p.id = s.product_id
                JOIN
            income i ON p.id = i.product_id
                JOIN
            (SELECT product_id, AVG(price) AS avg_price
             FROM income
             GROUP BY product_id) avg_income_price ON p.id = avg_income_price.product_id
        GROUP BY
            p.id, p.name, avg_income_price.avg_price
        """)
    List<ProfitRepoProjection> profitRepo();
}
