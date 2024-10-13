package com.ecommerce.MarketMate.service.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ecommerce.MarketMate.model.userDetails;
import com.ecommerce.MarketMate.repository.userDetailsrepo.userRepository;
@Service
public class userServiceIMPL implements userService{
@Autowired
    userRepository userRepo;

    @Override
    public userDetails saveUser(userDetails user) {
       userDetails saveuser=userRepo.save(user);
       return saveuser;
       
    }
}
