package com.ecommerce.MarketMate.repository.userDetailsrepo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ecommerce.MarketMate.model.userDetails;
import java.util.List;

public interface userRepository extends JpaRepository<userDetails,Integer>{

    public userDetails findByEmail(String username);
    public List<userDetails> findByRole(String role);
    public userDetails findByResetToken(String token);
    public Boolean existsByEmail(String email);
}
