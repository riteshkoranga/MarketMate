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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.security.crypto.password.PasswordEncoder;
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
import com.ecommerce.MarketMate.model.Category;
import com.ecommerce.MarketMate.model.productOrder;
import com.ecommerce.MarketMate.model.userDetails;
import com.ecommerce.MarketMate.service.CategoryService;
import com.ecommerce.MarketMate.service.FileService;
import com.ecommerce.MarketMate.service.commonService;
import com.ecommerce.MarketMate.service.Cart.cartService;
import com.ecommerce.MarketMate.service.User.userService;
import com.ecommerce.MarketMate.service.orderService.orderService;
import com.ecommerce.MarketMate.service.product.productService;
import com.ecommerce.MarketMate.util.BucketType;
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
    private CommonUtil commonUtil;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private FileService fileService;

    

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
    public String index() {
        return "admin/index";
    }

    @GetMapping("/addProduct")
    public String addProduct(Model m) {
        List<Category> categories = categoryService.getAllCategory();
        m.addAttribute("categories", categories);
        
        return "admin/addProduct";
    }

    // category mappings start here

    @GetMapping("/addCategory")
    public String addCategory(Model m,@RequestParam(name = "pageNo",defaultValue = "0") Integer pageNo,@RequestParam(name = "pageSize",defaultValue = "10") Integer pageSize) {
        //m.addAttribute("categories", categoryService.getAllCategory());
        Page<Category> page=categoryService.getAllCategoryPagination(pageNo, pageSize);
        
        if (page == null) {
            m.addAttribute("errorMsg", "No categories found.");
            return "admin/addCategory";
        }
        List<Category> categories = page.getContent();
        
        m.addAttribute("categories",categories);
        //m.addAttribute("categorySize",products.size());
        m.addAttribute("pageSize", pageSize);
        m.addAttribute("pageNo", page.getNumber());
        m.addAttribute("totalElements", page.getTotalElements());
        m.addAttribute("totalPages", page.getTotalPages());
        m.addAttribute("isFirst", page.isFirst());
        m.addAttribute("isLast", page.isLast());
        return "admin/addCategory";
    }

    @PostMapping("/saveCategory")
    public String saveCategory(
        @RequestParam("name") String name,
        @RequestParam("isActive") String isActive,
        @RequestParam("file") MultipartFile file,
        HttpSession session) {

   
    Category category = new Category();
    category.setName(name);
    category.setIsActive(Boolean.parseBoolean(isActive));

    //String imageName = file != null ? file.getOriginalFilename() : "default.jpg";
    
    String ImageUrl =commonUtil.getImageUrl(file, BucketType.CATEGORY.getId());
    category.setCategoryImage(ImageUrl);

    // Rest of your logic
    Boolean existCategory = categoryService.existsCategory(category.getName());

    if (existCategory) {
        session.setAttribute("errorMsg", "Category Already exists.");
        return "redirect:/admin/addCategory";
    } else {
        Category c = categoryService.saveCategory(category);
        if (ObjectUtils.isEmpty(c)) {
            session.setAttribute("errorMsg", "Internal server error");
        } else {
            // try {
            //     // File saveFile = new ClassPathResource("static/images").getFile();
            //     // Path path = Paths.get(saveFile.getAbsolutePath() + File.separator + "category" + File.separator
            //     //         + file.getOriginalFilename());

            //     // System.out.println(path);
            //     // Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
                
            // } catch (IOException e) {
            //     e.printStackTrace();
            // }
            fileService.uploadFileS3(file, 1);
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
        System.out.println(id);
        System.out.println(categoryService.getCategoryById(id));
        m.addAttribute("category", categoryService.getCategoryById(id));
        return "/admin/editCategory";

    }

    @PostMapping("/editCategory")
public String updateCategory(
        @RequestParam("id") int id,
        @RequestParam("name") String name,
        @RequestParam("isActive") boolean isActive,
        @RequestParam(value = "file", required = false) MultipartFile file,
        HttpSession session) {
    System.out.println("Editing category with ID: " + id);

    // Fetch the existing category by ID
    Category oldcat = categoryService.getCategoryById(id);
    String ImageUrl =commonUtil.getImageUrl(file, BucketType.CATEGORY.getId());
    
    if (oldcat == null) {
        session.setAttribute("errorMsg", "Category not found!");
        return "redirect:/admin/categories";
    }

    //System.out.println("Old Category Name: " + oldcat.getName());

    // Update category fields
    //String imageName = file != null && !file.isEmpty() ? file.getOriginalFilename() : oldcat.getCategoryImage();
    oldcat.setName(name);
    oldcat.setIsActive(isActive);
    oldcat.setCategoryImage(ImageUrl);

    // Save the updated category
    Category updatedCategory = categoryService.saveCategory(oldcat);
    if (updatedCategory != null) {
        if (file != null && !file.isEmpty()) {
            // try {
            //     // Save the new image file
            //     File saveFile = new ClassPathResource("static/images").getFile();
            //     Path path = Paths.get(saveFile.getAbsolutePath() + File.separator + "category" + File.separator
            //             + file.getOriginalFilename());
            //     Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
            // } catch (IOException e) {
            //     e.printStackTrace();
            // }
            fileService.uploadFileS3(file, 1);
        }
        session.setAttribute("sucMsg", "Category updated successfully!");
    } else {
        session.setAttribute("errorMsg", "Error: Something went wrong!");
    }

    return "redirect:/admin/editCategory/" + id;
}


    // category mappings ends here

    // product mappings start here
    @PostMapping("/saveProduct")
    public String saveProduct(@ModelAttribute Product product, @RequestParam("file") MultipartFile image,
            HttpSession session) {

        //String imageName = image.isEmpty() ? "default.jpg" : image.getOriginalFilename();
        String ImageUrl =commonUtil.getImageUrl(image, BucketType.PRODUCT.getId());

        product.setImage(ImageUrl);
        product.setDiscount(0);
        product.setDiscountPrice(product.getPrice());

        Product saveProduct = productService.saveProduct(product);
        if (!ObjectUtils.isEmpty(saveProduct)) {

            // try {
            //     File saveFile = new ClassPathResource("static/images").getFile();
            //     Path path = Paths.get(saveFile.getAbsolutePath() + File.separator + "product" + File.separator
            //             + image.getOriginalFilename());

            //     System.out.println(path);
            //     Files.copy(image.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
            // } catch (IOException e) {
            //     e.printStackTrace();
            // }
            fileService.uploadFileS3(image, BucketType.PRODUCT.getId());

            session.setAttribute("successMsg", "Product saved successfully");
        } else {
            session.setAttribute("errorMsg", "Something went wrong");

        }

        return "redirect:/admin/addProduct";
    }

    @GetMapping("/viewProducts")
    public String viewProducts(Model m,@RequestParam(defaultValue = "") String ch,@RequestParam(name = "pageNo",defaultValue = "0") Integer pageNo,@RequestParam(name = "pageSize",defaultValue = "10") Integer pageSize) {
        // List<Product> products=null;
        // if(ch!=null && ch.length()>0){
        //     products=productService.searchProduct(ch);
        //     m.addAttribute("search", true);
            
        // }
        // else{
        //     products=productService.getAllProducts();
        // }
        // m.addAttribute("products", products);


        Page<Product> page=null;
        if(ch!=null && ch.length()>0){
            page=productService.searchProductPagination(ch,pageNo,pageSize);
            m.addAttribute("search", true);
            
        }
        else{
            page=productService.getAllProductsPagination(pageNo,pageSize);
        }
        m.addAttribute("products", page.getContent());

        
        
        //m.addAttribute("categories",categories);
        //m.addAttribute("categorySize",products.size());
        m.addAttribute("pageSize", pageSize);
        m.addAttribute("pageNo", page.getNumber());
        m.addAttribute("totalElements", page.getTotalElements());
        m.addAttribute("totalPages", page.getTotalPages());
        m.addAttribute("isFirst", page.isFirst());
        m.addAttribute("isLast", page.isLast());
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
    public String getAllUsers(Model m,@RequestParam Integer type){
        List<userDetails> users=null;
        if(type==1){
            users=userService.getAllUsers("ROLE_USER");
        }else{
           users =userService.getAllUsers("ROLE_ADMIN");
        }
       m.addAttribute("userType", type);
        m.addAttribute("users", users);

        return "/admin/users";

    }
   

    @GetMapping("/updateStatus")
    public String updateUserAccountStatus(@RequestParam Boolean status,@RequestParam int id,@RequestParam Integer type,HttpSession session){
        Boolean update=userService.updateAccountStatus(id,status);

        if(update){
            session.setAttribute("successMsg","Account Status Updated");

        }else{
            session.setAttribute("errorMsg","Something wrong on server.");
        }
        return "redirect:/admin/users?type="+type;
    }

    @GetMapping("/orders")
    public String getAllOrders(Model m,@RequestParam(name = "pageNo",defaultValue = "0") Integer pageNo,@RequestParam(name = "pageSize",defaultValue = "10") Integer pageSize){
        // List<productOrder>orders=orderService.getAllOrders();
        // m.addAttribute("orders", orders);
        // m.addAttribute("search", false);
        Page<productOrder>page=orderService.getAllOrdersPagination(pageNo,pageSize);
        m.addAttribute("orders", page.getContent());
        m.addAttribute("search", false);


        // m.addAttribute("products", page.getContent());
        m.addAttribute("pageSize", pageSize);
        m.addAttribute("pageNo", page.getNumber());
        m.addAttribute("totalElements", page.getTotalElements());
        m.addAttribute("totalPages", page.getTotalPages());
        m.addAttribute("isFirst", page.isFirst());
        m.addAttribute("isLast", page.isLast());
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
            commonUtil.SendMailForProductOrder(updateOrder, status);
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

    @GetMapping("/searchOrder")
    public String searchOrder(@RequestParam String orderId,Model m,HttpSession session){
        
            productOrder order=orderService.getOrdersByOrderId(orderId.trim());

            if(ObjectUtils.isEmpty(order)){
                session.setAttribute("errorMsg", "Order not found");
                m.addAttribute("orderDetails", null);
            }
            else{
                 m.addAttribute("orderDetails", order);
            }
            m.addAttribute("search", true);
        
        
        

        return "/admin/orders";
        
        

        
    }

    @GetMapping("/addAdmin")
    public String adminAdd(){
        return "/admin/addAdmin";
    }

    @PostMapping("/saveAdmin")
    public String saveAdmin(@ModelAttribute userDetails user,HttpSession session){
        //String imageName=file.isEmpty()?"default.jpg":file.getOriginalFilename();
        //user.setProfileImage(imageName);
        // @RequestParam("img") MultipartFile file,
        userDetails saveuser=userService.saveAdmin(user);

        if(!ObjectUtils.isEmpty(saveuser)){
            session.setAttribute("successMsg", "Admin Registration Successful");
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


        return "redirect:/admin/addAdmin";
    }

    @GetMapping("/profile")
    public String profile(){
        return "/admin/profile";
    }

    

    @PostMapping("/updateProfile")
    public String updateProfile(@ModelAttribute userDetails user,HttpSession session){
        userDetails updatedProfile=userService.updateUserProfile(user);

        if(ObjectUtils.isEmpty(updatedProfile)){
session.setAttribute("errorMsg", "Profile Not updated");
        }
        else{
            session.setAttribute("successMsg", "Profile updated");
        }

        
        return "redirect:/admin/profile";
    }

    @PostMapping("/changePassword")
    public String changePassword(@RequestParam String newPassword,@RequestParam String curPassword,Principal p,HttpSession session){
        userDetails loggedInUser=commonUtil.getLoggedInUserDetails(p);
        boolean matches=passwordEncoder.matches(curPassword, loggedInUser.getPassword());

        if(matches){
            String encodedPassword=passwordEncoder.encode(newPassword);
            loggedInUser.setPassword(encodedPassword);
            userDetails updatedUser=userService.updateUser(loggedInUser);
            if(ObjectUtils.isEmpty(updatedUser)){
                session.setAttribute("errorMsg", "Password not Updated, Error in server");

            }else{
                session.setAttribute("successMsg", "Password Updated Successfully");
            }

        }else{
            session.setAttribute("errorMsg", "Current Password Invalid");
        }


        return "redirect:/admin/profile";

    }

   





}
