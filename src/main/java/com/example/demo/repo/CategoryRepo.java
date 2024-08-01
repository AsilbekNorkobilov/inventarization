package com.example.demo.repo;

import com.example.demo.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface CategoryRepo extends JpaRepository<Category, UUID> {

    List<Category> findAllByNameContainingIgnoreCase(String name);
}
