package com.ecommerce.MarketMate.service.orderService;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ecommerce.MarketMate.model.cart;
import com.ecommerce.MarketMate.model.orderAddress;
import com.ecommerce.MarketMate.model.orderRequest;
import com.ecommerce.MarketMate.model.productOrder;
import com.ecommerce.MarketMate.repository.productOrderRepository;
import com.ecommerce.MarketMate.repository.cartRepository.cartRepository;
import com.ecommerce.MarketMate.util.orderStatus;
@Service
public class orderServiceIMPL implements orderService{
    @Autowired
    private productOrderRepository orderRepo;

    @Autowired
    private cartRepository cartRepo;



    @Override
    public void saveOrder(Integer userId, orderRequest orderRequest) {
        List<cart> carts=cartRepo.findByUserId(userId);
        for(cart cart:carts){
            productOrder order=new productOrder();
            order.setOrderId(UUID.randomUUID().toString());
            order.setOrderDate(new Date());
            order.setProduct(cart.getProduct());
            order.setQuantity(cart.getQuantity());
            order.setUser(cart.getUser());
            order.setStatus(orderStatus.IN_PROGRESS.getName());
            order.setPaymentType(orderRequest.getPaymentType());

            orderAddress address=new orderAddress();
            address.setFirstName(orderRequest.getFirstName());
            address.setLastName(orderRequest.getLastName());
            address.setEmail(orderRequest.getEmail());
            address.setMobileNo(orderRequest.getMobileNo());
            address.setAddress(orderRequest.getAddress());
            address.setCity(orderRequest.getAddress());
            address.setState(orderRequest.getState());
            address.setPincode(orderRequest.getPincode());

            order.setOrderAddress(address);

            orderRepo.save(order);

            
        }
       
    }

}
