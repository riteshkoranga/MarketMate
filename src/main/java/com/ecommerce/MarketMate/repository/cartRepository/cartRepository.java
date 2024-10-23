package com.ecommerce.MarketMate.repository.cartRepository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ecommerce.MarketMate.model.cart;

public interface cartRepository extends JpaRepository<cart,Integer>{
    public cart findByProductIdAndUserId(Integer productId,Integer userId);

}
