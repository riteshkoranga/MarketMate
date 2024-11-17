package com.ecommerce.MarketMate.repository.cartRepository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ecommerce.MarketMate.model.cart;

public interface cartRepository extends JpaRepository<cart,Integer>{
    public cart findByProductIdAndUserId(Integer productId,Integer userId);

    public Integer countByUserId(Integer userId);

    public List<cart> findByUserId(Integer userId);

}
