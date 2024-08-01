package com.example.demo.repo;


import com.example.demo.entity.Income;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface IncomeRepo extends JpaRepository<Income, UUID> {

    List<Income> findAllByProductId(UUID productId);

    @Query(nativeQuery = true, value = "select sum(amount) from income where product_id=:productId")
    Integer ProductIncomeCount(UUID productId);
}
