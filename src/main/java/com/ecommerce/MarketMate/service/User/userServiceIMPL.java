package com.ecommerce.MarketMate.service.User;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import com.ecommerce.MarketMate.model.userDetails;
import com.ecommerce.MarketMate.repository.userDetailsrepo.userRepository;
import com.ecommerce.MarketMate.util.AppConstant;
@Service
public class userServiceIMPL implements userService{
@Autowired
    userRepository userRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;


    @Override
    public userDetails saveUser(userDetails user) {
        user.setRole("ROLE_USER");
        user.setIsEnabled(true);
        user.setAccountNonLocked(true);
        user.setFailedAttempt(0);
      
        String encodePassword=passwordEncoder.encode(user.getPassword());
        user.setPassword(encodePassword);
       userDetails saveuser=userRepo.save(user);
       return saveuser;
       
    }

    


    @Override
   public userDetails saveAdmin(userDetails user) {
      user.setRole("ROLE_ADMIN");
        user.setIsEnabled(true);
        user.setAccountNonLocked(true);
        user.setFailedAttempt(0);
      
        String encodePassword=passwordEncoder.encode(user.getPassword());
        user.setPassword(encodePassword);
       userDetails saveuser=userRepo.save(user);
       return saveuser;
   }




   @Override
    public userDetails getUserByEmail(String email) {
       return userRepo.findByEmail(email);
    }


    @Override
    public List<userDetails> getAllUsers(String role) {
       return userRepo.findByRole(role);
    }


    @Override
    public Boolean updateAccountStatus(int id, Boolean status) {
       Optional<userDetails> findByUser=userRepo.findById(id);
       if(findByUser.isPresent()){
        userDetails user=findByUser.get();
        user.setIsEnabled(status);
        userRepo.save(user);

        return true;

       }
       return false;

    }


   @Override
   public void increaseFailedAttempt(userDetails user) {
      int attempt=user.getFailedAttempt()+1;
      user.setFailedAttempt(attempt);
      userRepo.save(user);
   }


   @Override
   public void userAccountLock(userDetails user) {
      user.setAccountNonLocked(false);
      user.setLockTime(new Date());
      userRepo.save(user);
   }


   @Override
   public Boolean unlockAccountTimeExpired(userDetails user) {
     long lockTime=user.getLockTime().getTime();
     long unlockTime=lockTime+AppConstant.UNLOCK_DURATION_TIME;
   long currentTime=System.currentTimeMillis();
   if(unlockTime<currentTime){
      user.setAccountNonLocked(true);
      user.setFailedAttempt(0);
      user.setLockTime(null);
      userRepo.save(user);
      return true;
   }
   return false;

   }


   @Override
   public void resetAttempt(int userId) {
     
   }


   @Override
   public void updateUserResetToken(String email, String resetToken) {
    userDetails findByEmail=userRepo.findByEmail(email);
    findByEmail.setResetToken(resetToken);
    userRepo.save(findByEmail);
   }


   @Override
   public userDetails getUserByToken(String token) {
      return userRepo.findByResetToken(token);
     
   }


   @Override
   public userDetails updateUser(userDetails user) {
   return userRepo.save(user);
   }


   @Override
   public userDetails updateUserProfile(userDetails user) {
     userDetails dbuser=userRepo.findById(user.getId()).get();


     if(!ObjectUtils.isEmpty(dbuser)){
      dbuser.setName(user.getName());
      dbuser.setMobileNumber(user.getMobileNumber());
      dbuser.setAddress(user.getAddress());
      dbuser.setCity(user.getCity());
      dbuser.setState(user.getState());
      dbuser.setPincode(user.getPincode());
      userRepo.save(dbuser);
     }
      return dbuser;
   }




   @Override
   public Boolean existsUser(String email) {
      return userRepo.existsByEmail(email);
   }


   
}
