package com.ecommerce.MarketMate.service.orderService;



import java.util.List;

import com.ecommerce.MarketMate.model.orderRequest;
import com.ecommerce.MarketMate.model.productOrder;


public interface orderService {
    public void saveOrder(Integer userId,orderRequest orderRequest);
    public List<productOrder> getOrderByUser(Integer userId);
    public Boolean updateOrderStatus(Integer id,String status);
}
