package com.ecommerce.MarketMate.repository.productRepo;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.ecommerce.MarketMate.model.Product;

public interface productRepo extends JpaRepository<Product, Integer> {
    List<Product> findByIsActiveTrue();

    List<Product> findByCategory(String category);
    List<Product> findByNameContainingIgnoreCaseOrCategoryContainingIgnoreCase(String ch,String ch2);

    Page<Product> findByIsActiveTrue(Pageable pageable);

    Page<Product> findByCategory(Pageable pageable, String category);

}
