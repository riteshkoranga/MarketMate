package com.ecommerce.MarketMate.service.Cart;

import java.util.*; 

import com.ecommerce.MarketMate.model.cart;

public interface cartService {
    public cart saveCart(Integer productId,Integer userId);
    public List<cart> getCartByUser(Integer userId);

}
