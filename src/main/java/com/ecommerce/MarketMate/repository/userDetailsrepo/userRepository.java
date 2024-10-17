package com.ecommerce.MarketMate.repository.userDetailsrepo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ecommerce.MarketMate.model.userDetails;

public interface userRepository extends JpaRepository<userDetails,Integer>{

    public userDetails findByEmail(String username);
}
