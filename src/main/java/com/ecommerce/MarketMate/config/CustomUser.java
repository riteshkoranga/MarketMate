package com.ecommerce.MarketMate.config;

import java.util.Arrays;
import java.util.Collection;

import org.hibernate.annotations.DialectOverride.OverridesAnnotation;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.ecommerce.MarketMate.model.userDetails;

public class CustomUser implements UserDetails{

    private userDetails user;

    public CustomUser(userDetails user) {
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        SimpleGrantedAuthority authority=new SimpleGrantedAuthority(user.getRole());

        return Arrays.asList(authority);
    }

    @Override
    public String getPassword() {
        // TODO Auto-generated method stub
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        // TODO Auto-generated method stub
        return user.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        // TODO Auto-generated method stub
        return user.getAccountNonLocked();
    }

    @Override
    public boolean isAccountNonLocked() {
        // TODO Auto-generated method stub
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        // TODO Auto-generated method stub
        return true;
    }

    @Override
    public boolean isEnabled(){
        return user.getIsEnabled();
    }
    

    

   


}
