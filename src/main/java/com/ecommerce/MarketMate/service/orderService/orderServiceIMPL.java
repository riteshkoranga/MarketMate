package com.ecommerce.MarketMate.service.orderService;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ecommerce.MarketMate.model.cart;
import com.ecommerce.MarketMate.model.orderAddress;
import com.ecommerce.MarketMate.model.orderRequest;
import com.ecommerce.MarketMate.model.productOrder;
import com.ecommerce.MarketMate.repository.productOrderRepository;
import com.ecommerce.MarketMate.repository.cartRepository.cartRepository;
import com.ecommerce.MarketMate.util.CommonUtil;
import com.ecommerce.MarketMate.util.orderStatus;

import jakarta.mail.MessagingException;
@Service
public class orderServiceIMPL implements orderService{
    @Autowired
    private productOrderRepository orderRepo;

    @Autowired
    private cartRepository cartRepo;

    @Autowired
    private CommonUtil commonUtil;



    @Override
    public void saveOrder(Integer userId, orderRequest orderRequest) throws UnsupportedEncodingException, MessagingException {
        List<cart> carts=cartRepo.findByUserId(userId);
        for(cart cart:carts){
            productOrder order=new productOrder();
            order.setOrderId(UUID.randomUUID().toString());
            order.setOrderDate(new Date());
            order.setProduct(cart.getProduct());
            order.setQuantity(cart.getQuantity());
            order.setUser(cart.getUser());
            order.setStatus(orderStatus.IN_PROGRESS.getName ());
            order.setPaymentType(orderRequest.getPaymentType());
            order.setPrice(cart.getProduct().getDiscountPrice());

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

            productOrder saveOrder=orderRepo.save(order);
            commonUtil.SendMailForProductOrder(saveOrder, "successful");

            
        }
       
    }



    @Override
    public List<productOrder> getOrderByUser(Integer userId) {
       List<productOrder>orders=orderRepo.findByUserIdOrderByIdDesc(userId);
        return orders;
    }



    @Override
    public productOrder updateOrderStatus(Integer id, String status) {
       Optional<productOrder> findById=orderRepo.findById(id);
       if(findById.isPresent()){
        productOrder order=findById.get();
        order.setStatus(status);
        productOrder updateOrder=orderRepo.save(order);
        return updateOrder;
       }
       return null;
    }



    @Override
    public List<productOrder> getAllOrders() {
        return orderRepo.findAll();
        
    }

}
