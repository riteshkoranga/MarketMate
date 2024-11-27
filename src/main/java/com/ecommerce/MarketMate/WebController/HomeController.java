package com.ecommerce.MarketMate.WebController;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.util.StringUtils;

import com.ecommerce.MarketMate.model.Product;
import com.ecommerce.MarketMate.model.Category;
import com.ecommerce.MarketMate.model.userDetails;

import com.ecommerce.MarketMate.service.CategoryService;
import com.ecommerce.MarketMate.service.Cart.cartService;
import com.ecommerce.MarketMate.service.product.productService;
import com.ecommerce.MarketMate.util.CommonUtil;

import ch.qos.logback.core.util.StringUtil;

import com.ecommerce.MarketMate.service.User.userService;

import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
public class HomeController {
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private productService productService;
    @Autowired
    private userService userService;

    @Autowired
    private CommonUtil CommonUtil;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private cartService cartService;

    @ModelAttribute
    public void getUserDetails(Principal p,Model m){
            if(p!=null){
                String email=p.getName();
                userDetails user=userService.getUserByEmail(email);
                m.addAttribute("user", user);
                Integer countCart=cartService.getCountCart(user.getId());
                m.addAttribute("countCart", countCart);
            }
           

            List<Category> allActiveCategories=categoryService.getAllActiveCategory();
            m.addAttribute("category", allActiveCategories);

    }

    @GetMapping("/")
    public String index(Model m) {
        List<Category> allActiveCategory=categoryService.getAllActiveCategory().stream()
        .sorted((c1, c2) -> Integer.compare(c2.getId(), c1.getId())) 
        .limit(6).toList();
        List<Product> allActiveProducts = productService.getAllActiveProducts("").stream()
    .sorted((p1, p2) -> Integer.compare(p2.getId(), p1.getId())) // Option 1
    .limit(8)
    .toList();
        m.addAttribute("category", allActiveCategory);
        m.addAttribute("products", allActiveProducts);
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
    @RequestParam(name = "pageNo", defaultValue="0") Integer pageNo,@RequestParam(name = "pageSize",defaultValue = "3") Integer pageSize,
            HttpSession session,@RequestParam(defaultValue = "") String ch) {

        // System.out.println(category);
        List<Category> categories = categoryService.getAllActiveCategory();
        //List<Product> products = productService.getAllActiveProducts(category);
        
        m.addAttribute("categories", categories);
        //m.addAttribute("products", products);
        m.addAttribute("paramValue", category);
        Page<Product> page=null;
        if(StringUtils.isEmpty(ch)){
             page=productService.getAllActiveProductsPagination(pageNo, pageSize,category);
        }else{
            page=productService.searchActiveProductPagination( pageNo, pageSize,category,ch);

        }

        
        List<Product> products = page.getContent();
        if (ObjectUtils.isEmpty(products)) {
            session.setAttribute("errorMsg", "No Products available currently :(");
        }
        m.addAttribute("products",products);
        m.addAttribute("productSize",products.size());
        m.addAttribute("pageSize", products.size());
        m.addAttribute("pageNo", page.getNumber());
        m.addAttribute("totalElements", page.getTotalElements());
        m.addAttribute("totalPages", page.getTotalPages());
        m.addAttribute("isFirst", page.isFirst());
        m.addAttribute("isLast", page.isLast());
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

    //forget password logic
    @GetMapping("/forgotPassword")
    public String showForgotPassword(){
        return "forgotPassword.html";

    }

    @PostMapping("/forgotPassword")
    public String processForgotPassword(@RequestParam String email,HttpSession session,HttpServletRequest request) throws UnsupportedEncodingException, MessagingException{
        userDetails user=userService.getUserByEmail(email);

        if(ObjectUtils.isEmpty(user)){
            session.setAttribute("errorMsg", "Invalid Email, Email not in our system");

        }else{
            String resetToken=UUID.randomUUID().toString();
            userService.updateUserResetToken(email,resetToken);
            
            //generate URL http://localhost/resetPass?token=sdfsdfsdfsdfdsf
            String url=CommonUtil.generateUrl(request)+"/resetPassword?token="+resetToken;

            Boolean sendMail=CommonUtil.sendMail(url,email);
            if(sendMail){
                session.setAttribute("successMsg", "Email send, Please check");
            }
            else{
                session.setAttribute("errorMsg", "Something wrong on server! Email not send");
            }
        }
        return "redirect:/forgotPassword";
    }

    @GetMapping("/resetPassword")
    public String resetPassword(@RequestParam String token,HttpSession session,Model m){
        userDetails userByToken=userService.getUserByToken(token);
        if(userByToken==null){
            m.addAttribute("errorMsg", "Your link is invalid or expired, Please try again");
            return "error.html";

        }
        m.addAttribute("token", token);
        return "resetPassword";

    }

    @PostMapping("/resetPassword")
    public String resetPassword(@RequestParam String token,@RequestParam String password,HttpSession session,Model  m){
        userDetails userByToken=userService.getUserByToken(token);
        if(userByToken==null){
            m.addAttribute("errorMsg", "Your link is invalid or expired, Please try again");
            return "error.html";

        }else{
            userByToken.setPassword(passwordEncoder.encode(password));
            userByToken.setResetToken(null);
            userService.updateUser(userByToken);
            return "redirect:/passwordChange";
        }
        
        

    }
    @GetMapping("/passwordChange")
    public String passwordChange(){
        return "passwordChange";
    }

    @GetMapping("/search")
    public String searchProduct(@RequestParam String ch,Model m){

        if(ObjectUtils.isEmpty(ch)){
            return "redirect:/products";
            
        }
        else{
            List<Product> searchProducts=productService.searchProduct(ch);
            List<Category> categories=categoryService.getAllActiveCategory();
            m.addAttribute("categories", categories);
            m.addAttribute("products", searchProducts);
            return "products";
        }
        
        

        
    }

    

}
