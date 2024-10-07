package com.ecommerce.MarketMate.WebController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.ecommerce.MarketMate.model.category;
import com.ecommerce.MarketMate.service.CategoryService;

import jakarta.servlet.http.HttpSession;

import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequestMapping("/admin")

public class AdminController {
    @Autowired
    private CategoryService categoryService;

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

    @PostMapping("/saveCategory")
    public String savecategory(@ModelAttribute category category, @RequestParam("file") MultipartFile file,
            HttpSession session) {

        String imageName = file != null ? file.getOriginalFilename() : "default.jpg";
        category.setCategoryImage(imageName);

        if (categoryService.existsCategory(category.getName())) {
            session.setAttribute("errorMsg", "Category Already exists.");
            System.out.println("Category Already exists.");
        } else {
            category c = categoryService.saveCategory(category);
            if (ObjectUtils.isEmpty(c)) {
                session.setAttribute("errorMsg", "Internal server error");
                System.out.println("Internal server error");
            } else {
                session.setAttribute("successMsg", "Category Saved Successfully");
                System.out.println("Category Saved Successfully");
            }

            // }
            // try {
            // category c = categoryService.saveCategory(category);
            // session.setAttribute("successMsg", "Category Saved Successfully");
            // } catch (DataIntegrityViolationException e) {
            // session.setAttribute("errorMsg", "Category Already exists.");
            // } catch (Exception e) {
            // session.setAttribute("errorMsg", "Internal server error");
            // }

        }
        return "redirect:/addCategory";
    }

}
