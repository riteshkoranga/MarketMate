package com.ecommerce.MarketMate.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ecommerce.MarketMate.model.category;

public interface CategoryRepo extends JpaRepository<category, Integer> {
    public Boolean existsByName(String categoryName);
}
