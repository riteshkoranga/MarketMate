package com.ecommerce.MarketMate.WebController;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.ecommerce.MarketMate.model.cart;
import com.ecommerce.MarketMate.model.category;
import com.ecommerce.MarketMate.model.orderRequest;
import com.ecommerce.MarketMate.model.productOrder;
import com.ecommerce.MarketMate.model.userDetails;
import com.ecommerce.MarketMate.service.CategoryService;
import com.ecommerce.MarketMate.service.Cart.cartService;
import com.ecommerce.MarketMate.service.User.userService;
import com.ecommerce.MarketMate.service.orderService.orderService;
import com.ecommerce.MarketMate.util.orderStatus;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/user")
public class userController {

    @Autowired
    private userService userService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private cartService cartService;
    @Autowired
    private orderService orderService;
    
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
    
    @GetMapping("/addCart")
    public String addToCart(@RequestParam Integer pid,@RequestParam Integer uid,HttpSession session){
        cart saveCart=cartService.saveCart(pid, uid);
        if(ObjectUtils.isEmpty(saveCart)){
             session.setAttribute("errorMsg", "Product add to cart failed");
         }
         else{
             session.setAttribute("successMsg", "Product added to cart");
         }
        
        return "redirect:/product/"+pid;

    }

    @GetMapping("/cart")
    public String loadCartPage(Principal p,Model m){

       

        userDetails user=getLoggedInUserDetails(p);
        List<cart> carts=cartService.getCartsByUser(user.getId());
        m.addAttribute("carts", carts);
        if(carts.size()>0){
            m.addAttribute("totalOrderPrice", carts.get(carts.size()-1).getTotalOrderAmount());
        }
        
        return "/user/cart";
    }
        
        
    private userDetails getLoggedInUserDetails(Principal p) {
        String email=p.getName();
        userDetails user=userService.getUserByEmail(email);
        return user;
    }
    @GetMapping("/cartQuantityUpdate")
    public String updateCartQuantity(@RequestParam String sy,@RequestParam Integer cid){
        cartService.updateQuantity( sy, cid);
        return "redirect:/user/cart";
    }

    @GetMapping("/order")
    public String order(Principal p,Model m){
        userDetails user=getLoggedInUserDetails(p);
        List<cart> carts=cartService.getCartsByUser(user.getId());
        m.addAttribute("carts", carts);
        if(carts.size()>0){
            Double orderPrice=carts.get(carts.size()-1).getTotalOrderAmount();
            Double totalOrderPrice=carts.get(carts.size()-1).getTotalOrderAmount()+250+100;

            m.addAttribute("orderPrice", orderPrice);
            m.addAttribute("totalOrderPrice", totalOrderPrice);
        }
        return "/user/order";
    }

    @PostMapping("/saveOrder")
    public String saveOrder(@ModelAttribute orderRequest request,Principal p){
        //System.out.println(request);
        userDetails user=getLoggedInUserDetails(p);
        orderService.saveOrder(user.getId(), request);
        return "redirect:/user/success";
    }
    @GetMapping("/success")
    public String loadSuccess(){
        return "/user/success";
    }

    @GetMapping("/userOrders")
    public String userOrders(Model m,Principal p){
        userDetails user=getLoggedInUserDetails(p);

        List<productOrder>orders=orderService.getOrderByUser(user.getId());
        m.addAttribute("orders", orders);
        return "/user/userOrders";
    }
    @GetMapping("/updateStatus")
    public String updateStatus(@RequestParam Integer id,@RequestParam Integer st,HttpSession session){
        orderStatus[] values=orderStatus.values();
        String status=null;
        for(orderStatus orderst:values){
            if(orderst.getId().equals(st)){
                status=orderst.getName();
            }

        }
        Boolean updateOrder=orderService.updateOrderStatus(id, status);
        if(updateOrder){
            session.setAttribute("successMsg", "Order Status Updated");
        }
        else{
            session.setAttribute("errorMsg", "Order Status updation failure");
        }
        return "redirect:/user/userOrders";
    }

    

}
