package com.ecommerce.MarketMate.service.User;

import com.ecommerce.MarketMate.model.userDetails;
import java.util.*;


public interface userService {
 public userDetails saveUser(userDetails user);
 public userDetails getUserByEmail(String email);
 public List<userDetails> getAllUsers(String role);
public Boolean updateAccountStatus(int id, Boolean status);
public void increaseFailedAttempt(userDetails user);
public void userAccountLock(userDetails user);
public Boolean unlockAccountTimeExpired(userDetails user);
public void resetAttempt(int userId);
public void updateUserResetToken(String email, String resetToken);
public userDetails getUserByToken(String token);
public userDetails updateUser(userDetails user);

public userDetails updateUserProfile(userDetails user);

public userDetails saveAdmin(userDetails user);


}
