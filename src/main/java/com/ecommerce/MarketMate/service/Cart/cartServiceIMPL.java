package com.ecommerce.MarketMate.service.Cart;

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
    public List<cart> getCartByUser(Integer userId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getCartByUser'");
    }

}
