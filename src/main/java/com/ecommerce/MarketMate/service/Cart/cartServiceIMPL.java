package com.ecommerce.MarketMate.service.Cart;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.parsing.Problem;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import com.ecommerce.MarketMate.model.Product;
import com.ecommerce.MarketMate.model.cart;
import com.ecommerce.MarketMate.model.userDetails;
import com.ecommerce.MarketMate.repository.cartRepository.cartRepository;
import com.ecommerce.MarketMate.repository.productRepo.productRepo;
import com.ecommerce.MarketMate.repository.userDetailsrepo.userRepository;

@Service
public class cartServiceIMPL implements cartService {
    @Autowired
    private cartRepository cartRepo;

    @Autowired
    private userRepository userRepository;

    @Autowired
    private productRepo productRepo;

    @Override
    public cart saveCart(Integer productId, Integer userId) {
        userDetails user=userRepository.findById(userId).get();
        Product product = productRepo.findById(productId).get();

        cart cartStatus=cartRepo.findByProductIdAndUserId(productId, userId);
        cart cart=null;
        if(ObjectUtils.isEmpty(cartStatus)){
            cart=new cart();
            cart.setProduct(product);
            cart.setUser(user);
            cart.setQuantity(1);
            cart.setTotalPrice(1*product.getDiscountPrice());


        }
        else{
            cart=cartStatus;
            cart.setQuantity(cart.getQuantity()+1);
            cart.setTotalPrice(cart.getQuantity()*cart.getProduct().getDiscountPrice());
        }
        cart saveCart=cartRepo.save(cart);

        return saveCart;
       
    }

    @Override
    public List<cart> getCartsByUser(Integer userId) {
       List<cart> carts=cartRepo.findByUserId(userId);
       Double totalOrderPrice=0.0;

        List<cart> updateCarts=new ArrayList<>();
       for(cart c:carts){
       Double totalPrice= (c.getProduct().getDiscountPrice()*c.getQuantity());
        c.setTotalPrice(totalPrice);
        totalOrderPrice+=totalPrice;
        c.setTotalOrderAmount(totalOrderPrice);
        updateCarts.add(c);
       }
       
       return updateCarts;
    }

    @Override
    public Integer getCountCart(Integer userId) {
        Integer countByUserId=cartRepo.countByUserId(userId);

        return countByUserId;
    }

    @Override
    public void updateQuantity(String sy, Integer cid) {
        cart cart=cartRepo.findById(cid).get();
        int updateQuantity;
        if(sy.equalsIgnoreCase("de")){
             updateQuantity=cart.getQuantity()-1;

             if(updateQuantity<=0){
                cartRepo.delete(cart);
                
                
             }else{
                cart.setQuantity(updateQuantity);
                cartRepo.save(cart);
             }
             
        }else{
            updateQuantity=cart.getQuantity()+1;
            cart.setQuantity(updateQuantity);
            cartRepo.save(cart);
        }
        

       
        
    }

}
