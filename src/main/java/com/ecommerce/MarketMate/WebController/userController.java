package com.ecommerce.MarketMate.WebController;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ecommerce.MarketMate.model.category;
import com.ecommerce.MarketMate.model.userDetails;
import com.ecommerce.MarketMate.service.CategoryService;
import com.ecommerce.MarketMate.service.User.userService;

@Controller
@RequestMapping("/user")
public class userController {

    @Autowired
    private userService userService;
    @Autowired
    private CategoryService categoryService;
    @GetMapping("/")
    public String home(){
        return "user/home";

    }

     @ModelAttribute
    public void getUserDetails(Principal p,Model m){
            if(p!=null){
                String email=p.getName();
                userDetails user=userService.getUserByEmail(email);
                m.addAttribute("user", user);
            }

            List<category> allActiveCategories=categoryService.getAllActiveCategory();
            m.addAttribute("category", allActiveCategories);

    }

}
