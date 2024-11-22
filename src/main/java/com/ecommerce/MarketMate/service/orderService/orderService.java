package com.ecommerce.MarketMate.service.orderService;



import java.io.UnsupportedEncodingException;
import java.util.List;

import com.ecommerce.MarketMate.model.orderRequest;
import com.ecommerce.MarketMate.model.productOrder;

import jakarta.mail.MessagingException;


public interface orderService {
    public void saveOrder(Integer userId,orderRequest orderRequest) throws UnsupportedEncodingException, MessagingException;
    public List<productOrder> getOrderByUser(Integer userId);
    public List<productOrder> getAllOrders();
    public productOrder updateOrderStatus(Integer id,String status);
}
