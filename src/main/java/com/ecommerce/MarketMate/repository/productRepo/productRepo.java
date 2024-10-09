package com.ecommerce.MarketMate.repository.productRepo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ecommerce.MarketMate.model.Product;

public interface productRepo extends JpaRepository<Product, Integer> {

}
