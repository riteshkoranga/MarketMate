package com.ecommerce.MarketMate.WebController;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.ecommerce.MarketMate.model.Product;
import com.ecommerce.MarketMate.model.category;
import com.ecommerce.MarketMate.model.userDetails;

import com.ecommerce.MarketMate.service.CategoryService;
import com.ecommerce.MarketMate.service.product.productService;
import com.ecommerce.MarketMate.service.User.userService;

import jakarta.servlet.http.HttpSession;

@Controller
public class HomeController {
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private productService productService;
    @Autowired
    private userService userService;

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

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/signin")
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

    @PostMapping("/saveUser")
    public String saveUser(@ModelAttribute userDetails user,HttpSession session){
        //String imageName=file.isEmpty()?"default.jpg":file.getOriginalFilename();
        //user.setProfileImage(imageName);
        // @RequestParam("img") MultipartFile file,
        userDetails saveuser=userService.saveUser(user);

        if(!ObjectUtils.isEmpty(saveuser)){
            session.setAttribute("successMsg", "User Registration Successful");
            // if(!file.isEmpty()){
            //     try {
            //         File saveFile = new ClassPathResource("static/images").getFile();
            //         Path path = Paths.get(saveFile.getAbsolutePath() + File.separator + "profile_img" + File.separator
            //                 + file.getOriginalFilename());

            //         System.out.println(path);
            //         Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
            //     } catch (IOException e) {
            //         e.printStackTrace(); // Handle the exception appropriately
            //         // Optionally, you can log the error or rethrow it as a runtime exception
            //     }
            //     session.setAttribute("sucMsg", "Category Updated successfully");
            // }
            
        }
        else {
            session.setAttribute("errorMsg", "Error! Something wrong!");
        }


        return "redirect:/register";
    }

    

}
