package com.ecommerce.MarketMate.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.ecommerce.MarketMate.model.userDetails;
import com.ecommerce.MarketMate.repository.userDetailsrepo.userRepository;

@Service
public class UserDetailsServiceIMPL implements UserDetailsService{

    @Autowired
    private userRepository userRepo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
       userDetails user=userRepo.findByEmail(username);
       if(user==null){
        throw new UsernameNotFoundException("User name not found");
       }
       return new CustomUser(user);
        
    }


}
