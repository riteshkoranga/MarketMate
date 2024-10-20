package com.ecommerce.MarketMate.config;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;

import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import com.ecommerce.MarketMate.model.userDetails;
import com.ecommerce.MarketMate.repository.userDetailsrepo.userRepository;
import com.ecommerce.MarketMate.util.AppConstant;
import com.ecommerce.MarketMate.service.User.userService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class AuthFailurehandler extends SimpleUrlAuthenticationFailureHandler{

    @Autowired
    private userRepository userRepo;

    @Autowired
    private userService userService;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
            AuthenticationException exception) throws IOException, ServletException {
        String email=request.getParameter("username");
        userDetails user=userRepo.findByEmail(email);

        if(user.getIsEnabled()){
            if(user.getAccountNonLocked()){
                if(user.getFailedAttempt()<AppConstant.ATTEMPTS){
userService.increaseFailedAttempt(user);
                }
                else{
                    userService.userAccountLock(user);
                    exception=new LockedException("Your login attempts are exceeded, try again after some time");
                }

            }else{
                if(userService.unlockAccountTimeExpired(user)){
                     exception=new LockedException("Your account unlocked,login limit exceeded");
                }
                exception =new LockedException("Your Acoount is unlocked, please log in");
            }

        }else{
            exception =new LockedException("Your Acoount is Inactive");
        }
        super.setDefaultFailureUrl("/signin?error");
        super.onAuthenticationFailure(request, response, exception);
    }


}
