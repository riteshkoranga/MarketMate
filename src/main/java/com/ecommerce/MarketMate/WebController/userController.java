package com.ecommerce.MarketMate.WebController;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/user")
public class userController {
    @GetMapping("/")
    public String home(){
        return "user/home";

    }

}
