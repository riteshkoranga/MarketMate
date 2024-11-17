package com.ecommerce.MarketMate.service.Cart;

import java.util.*; 

import com.ecommerce.MarketMate.model.cart;

public interface cartService {
    public cart saveCart(Integer productId,Integer userId);
    public List<cart> getCartsByUser(Integer userId);
    public Integer getCountCart(Integer userId);
    public void updateQuantity(String sy, Integer cid);
}
