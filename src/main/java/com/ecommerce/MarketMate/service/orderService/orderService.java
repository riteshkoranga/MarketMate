package com.ecommerce.MarketMate.service.orderService;



import com.ecommerce.MarketMate.model.orderRequest;


public interface orderService {
    public void saveOrder(Integer userId,orderRequest orderRequest);

}
