package com.ecommerce.MarketMate.service.User;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.ecommerce.MarketMate.model.userDetails;
import com.ecommerce.MarketMate.repository.userDetailsrepo.userRepository;
@Service
public class userServiceIMPL implements userService{
@Autowired
    userRepository userRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;


    @Override
    public userDetails saveUser(userDetails user) {
        user.setRole("ROLE_USER");
        String encodePassword=passwordEncoder.encode(user.getPassword());
        user.setPassword(encodePassword);
       userDetails saveuser=userRepo.save(user);
       return saveuser;
       
    }


    @Override
    public userDetails getUserByEmail(String email) {
       return userRepo.findByEmail(email);
    }
}
