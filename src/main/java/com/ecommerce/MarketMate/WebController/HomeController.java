package com.ecommerce.MarketMate.WebController;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import com.ecommerce.MarketMate.model.Product;
import com.ecommerce.MarketMate.model.category;
import com.ecommerce.MarketMate.repository.productRepo.productRepo;
import com.ecommerce.MarketMate.service.CategoryService;
import com.ecommerce.MarketMate.service.product.productService;

import jakarta.servlet.http.HttpSession;

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
    public String product(Model m, @RequestParam(value = "category", defaultValue = "") String category,
            HttpSession session) {

        // System.out.println(category);
        List<category> categories = categoryService.getAllActiveCategory();
        List<Product> products = productService.getAllActiveProducts(category);
        if (ObjectUtils.isEmpty(products)) {
            session.setAttribute("errorMsg", "No Products available currently :(");
        }
        m.addAttribute("categories", categories);
        m.addAttribute("products", products);
        m.addAttribute("paramValue", category);

        return "products";
    }

    @GetMapping("/product/{id}")
    public String buy(@PathVariable int id, Model m) {
        Product product = productService.getProductById(id);
        m.addAttribute("product", product);
        return "buy";
    }

}
