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
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;

import com.ecommerce.MarketMate.model.Product;
import com.ecommerce.MarketMate.model.category;
import com.ecommerce.MarketMate.model.productOrder;
import com.ecommerce.MarketMate.model.userDetails;
import com.ecommerce.MarketMate.service.CategoryService;
import com.ecommerce.MarketMate.service.commonService;
import com.ecommerce.MarketMate.service.Cart.cartService;
import com.ecommerce.MarketMate.service.User.userService;
import com.ecommerce.MarketMate.service.orderService.orderService;
import com.ecommerce.MarketMate.service.product.productService;
import com.ecommerce.MarketMate.util.CommonUtil;
import com.ecommerce.MarketMate.util.orderStatus;

import jakarta.servlet.http.HttpSession;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
@RequestMapping("/admin")

public class AdminController {
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private commonService commonService;
    @Autowired
    private productService productService;
    @Autowired
    private userService userService;
    @Autowired
    private cartService cartService;
    @Autowired
    private orderService orderService;
@Autowired
    private CommonUtil mail;

     @ModelAttribute
    public void getUserDetails(Principal p,Model m){
            if(p!=null){
                String email=p.getName();
                userDetails user=userService.getUserByEmail(email);
                m.addAttribute("user", user);
                Integer countCart=cartService.getCountCart(user.getId());
                m.addAttribute("countCart", countCart);
            }

            List<category> allActiveCategories=categoryService.getAllActiveCategory();
            m.addAttribute("category", allActiveCategories);

    }

    @GetMapping("/")
    public String index() {
        return "admin/index";
    }

    @GetMapping("/addProduct")
    public String addProduct(Model m) {
        List<category> categories = categoryService.getAllCategory();
        m.addAttribute("categories", categories);
        return "admin/addProduct";
    }

    // category mappings start here

    @GetMapping("/addCategory")
    public String addCategory(Model m) {
        m.addAttribute("categories", categoryService.getAllCategory());
        return "admin/addCategory";
    }

    @PostMapping("/saveCategory")
    public String savecategory(@ModelAttribute category category, @RequestParam("file") MultipartFile file,
            HttpSession session) {

        // commonService.removeSessionMessage(session);

        String imageName = file != null ? file.getOriginalFilename() : "default.jpg";
        category.setCategoryImage(imageName);

        if (categoryService.existsCategory(category.getName())) {
            session.setAttribute("errorMsg", "Category Already exists.");
            return "redirect:/admin/addCategory";

        } else {
            category c = categoryService.saveCategory(category);
            if (ObjectUtils.isEmpty(c)) {
                session.setAttribute("errorMsg", "Internal server error");

            } else {
                try {
                    File saveFile = new ClassPathResource("static/images").getFile();
                    Path path = Paths.get(saveFile.getAbsolutePath() + File.separator + "category" + File.separator
                            + file.getOriginalFilename());

                    System.out.println(path);
                    Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
                } catch (IOException e) {
                    e.printStackTrace(); // Handle the exception appropriately
                    // Optionally, you can log the error or rethrow it as a runtime exception
                }
                session.setAttribute("successMsg", "Category Saved Successfully");

            }

        }

        return "redirect:/admin/addCategory";
    }

    @GetMapping("/deleteCategory/{id}")
    public String deleteCategory(@PathVariable int id, HttpSession session) {

        Boolean value = categoryService.deleteCategory(id);
        if (value) {
            session.setAttribute("sucMsg", "Category Deleted!");

        } else {
            session.setAttribute("errorMsg", "Error something wrong!");
        }
        return "redirect:/admin/addCategory";

    }

    @GetMapping("/editCategory/{id}")
    public String editCategory(@PathVariable int id, Model m) {
        m.addAttribute("category", categoryService.getCategoryById(id));
        return "admin/editCategory";

    }

    @PostMapping("/editCategory")
    public String updateCategory(@ModelAttribute category category, @RequestParam("file") MultipartFile file,
            HttpSession session) {
        category oldcat = categoryService.getCategoryById(category.getId());
        String imageName = file.isEmpty() ? oldcat.getCategoryImage() : file.getOriginalFilename();
        if (oldcat != null) {
            oldcat.setName(category.getName());
            oldcat.setIsActive(category.getIsActive());
            oldcat.setCategoryImage(imageName);

        }
        category c = categoryService.saveCategory(oldcat);
        if (!ObjectUtils.isEmpty(c)) {

            if (!file.isEmpty()) {
                try {
                    File saveFile = new ClassPathResource("static/images").getFile();
                    Path path = Paths.get(saveFile.getAbsolutePath() + File.separator + "category" + File.separator
                            + file.getOriginalFilename());

                    System.out.println(path);
                    Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
                } catch (IOException e) {
                    e.printStackTrace(); // Handle the exception appropriately
                    // Optionally, you can log the error or rethrow it as a runtime exception
                }
            }
            session.setAttribute("sucMsg", "Category Updated successfully");

        } else {
            session.setAttribute("errorMsg", "Error Something wrong!");
        }

        return "redirect:/admin/editCategory/" + category.getId();
    }

    // category mappings ends here

    // product mappings start here
    @PostMapping("/saveProduct")
    public String saveProduct(@ModelAttribute Product product, @RequestParam("file") MultipartFile image,
            HttpSession session) {

        String imageName = image.isEmpty() ? "default.jpg" : image.getOriginalFilename();

        product.setImage(imageName);
        product.setDiscount(0);
        product.setDiscountPrice(product.getPrice());

        Product saveProduct = productService.saveProduct(product);
        if (!ObjectUtils.isEmpty(saveProduct)) {

            try {
                File saveFile = new ClassPathResource("static/images").getFile();
                Path path = Paths.get(saveFile.getAbsolutePath() + File.separator + "product" + File.separator
                        + image.getOriginalFilename());

                System.out.println(path);
                Files.copy(image.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException e) {
                e.printStackTrace();
            }

            session.setAttribute("successMsg", "Product saved successfully");
        } else {
            session.setAttribute("errorMsg", "Something went wrong");

        }

        return "redirect:/admin/addProduct";
    }

    @GetMapping("/viewProducts")
    public String viewProducts(Model m) {
        m.addAttribute("products", productService.getAllProducts());
        return "admin/viewProducts";
    }

    @GetMapping("/deleteProduct/{id}")
    public String deleteProduct(@PathVariable int id, HttpSession session) {
        Boolean deleteProduct = productService.deleteProduct(id);
        if (deleteProduct) {
            session.setAttribute("successMsg", "Product Deleted successfully");
        }

        else {
            session.setAttribute("successMsg", "Something went wrong!");
        }
        return "redirect:/admin/viewProducts";
    }

    @GetMapping("/editProduct/{id}")
    public String editProduct(@PathVariable int id, Model m) {
        m.addAttribute("product", productService.getProductById(id));
        m.addAttribute("categories", categoryService.getAllCategory());

        return "admin/editProduct";
    }

    @PostMapping("/updateProduct")
    public String updateProduct(@ModelAttribute Product product, @RequestParam("file") MultipartFile file,
            HttpSession session) {

        if (product.getDiscount() < 0 || product.getDiscount() > 100) {
            session.setAttribute("errorMsg", "Discount must be between 0 to 100 %!");
        }

        else {
            Product updateProduct = productService.updateProduct(product, file);
            if (!ObjectUtils.isEmpty(updateProduct)) {
                session.setAttribute("successMsg", "Product Updated successfully");

            } else {
                session.setAttribute("errorMsg", "Something went wrong!");
            }
        }

        return "redirect:/admin/editProduct/" + product.getId();
    }
    @GetMapping("/users")
    public String getAllUsers(Model m){
        List<userDetails> users=userService.getAllUsers("ROLE_USER");
        m.addAttribute("users", users);

        return "/admin/users";

    }

    @GetMapping("/updateStatus")
    public String updateUserAccountStatus(@RequestParam Boolean status,@RequestParam int id,HttpSession session){
        Boolean update=userService.updateAccountStatus(id,status);

        if(update){
            session.setAttribute("successMsg","Account Status Updated");

        }else{
            session.setAttribute("errorMsg","Something wrong on server.");
        }
        return "redirect:/admin/users";
    }

    @GetMapping("/orders")
    public String getAllOrders(Model m){
        List<productOrder>orders=orderService.getAllOrders();
        m.addAttribute("orders", orders);
        return "/admin/orders";

    }
    

    @PostMapping("/updateOrderStatus")
    public String updateStatus(@RequestParam Integer id,@RequestParam Integer st,HttpSession session){
        orderStatus[] values=orderStatus.values();
        String status=null;
        for(orderStatus orderst:values){
            if(orderst.getId().equals(st)){
                status=orderst.getName();
            }

        }
        productOrder updateOrder=orderService.updateOrderStatus(id, status);
        try{
            mail.SendMailForProductOrder(updateOrder, status);
        }catch(Exception e){
            e.printStackTrace();
        }
        if(!ObjectUtils.isEmpty(updateOrder)){
            session.setAttribute("successMsg", "Order Status Updated");
        }
        else{
            session.setAttribute("errorMsg", "Order Status updation failure");
        }
        return "redirect:/admin/orders";
    }


}
