package com.ecommerce.MarketMate.service.User;

import com.ecommerce.MarketMate.model.userDetails;

public interface userService {
 public userDetails saveUser(userDetails user);
 public userDetails getUserByEmail(String email);
}
