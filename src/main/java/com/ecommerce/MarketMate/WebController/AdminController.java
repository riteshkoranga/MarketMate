package com.ecommerce.MarketMate.WebController;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/admin")

public class AdminController {
    @GetMapping("/")
    public String index() {
        return "admin/index";
    }

    @GetMapping("/addProduct")
    public String addProduct() {
        return "admin/addProduct";
    }

    @GetMapping("/addCategory")
    public String addCategory() {
        return "admin/addCategory";
    }

}
