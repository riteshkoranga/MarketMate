package com.ecommerce.MarketMate.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configurers.userdetails.DaoAuthenticationConfigurer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class securityConfig {
    @Autowired
    private AuthSuccesshandler  authSuccesshandler;
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
    @Bean
    public UserDetailsService userDeatilsService(){
        return new UserDetailsServiceIMPL();
    }
    @Bean
    public DaoAuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider authenticationProvider=new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDeatilsService());
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return authenticationProvider;
    }

    @Bean
   public SecurityFilterChain filterChain(HttpSecurity http)throws Exception{
    http.csrf(csrf->csrf.disable()).cors(cors->cors.disable())
        .authorizeHttpRequests(req->req.requestMatchers("/user/**").hasRole("USER")
        .requestMatchers("/admin/**").hasRole("ADMIN")
        .requestMatchers("/**").permitAll())
        .formLogin(form->form.loginPage("/signin")
            .loginProcessingUrl("/login")
            //.defaultSuccessUrl("/")
            .successHandler(authSuccesshandler)
            )
            
        .logout(logout->logout.permitAll());

    return http.build();
   }



}
