package com.ecommerce.MarketMate.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ecommerce.MarketMate.model.Category;

public interface CategoryRepo extends JpaRepository<Category, Integer> {
    public Boolean existsByName(String categoryName);

    public List<Category> findByIsActiveTrue();

    
}
