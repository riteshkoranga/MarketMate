package com.ecommerce.MarketMate.WebController;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.ecommerce.MarketMate.model.Product;
import com.ecommerce.MarketMate.model.category;
import com.ecommerce.MarketMate.repository.productRepo.productRepo;
import com.ecommerce.MarketMate.service.CategoryService;
import com.ecommerce.MarketMate.service.product.productService;

@Controller
public class HomeController {
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private productService productService;

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/register")
    public String register() {
        return "register";
    }

    @GetMapping("/products")
    public String product(Model m) {
        List<category> categories = categoryService.getAllActiveCategory();
        List<Product> products = productService.getAllActiveProducts();
        m.addAttribute("categories", categories);
        m.addAttribute("products", products);

        return "products";
    }

    @GetMapping("/buy")
    public String buy() {
        return "buy";
    }

}
