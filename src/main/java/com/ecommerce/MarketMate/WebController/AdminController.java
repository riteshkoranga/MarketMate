package com.ecommerce.MarketMate.WebController;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

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

import com.ecommerce.MarketMate.model.category;
import com.ecommerce.MarketMate.service.CategoryService;
import com.ecommerce.MarketMate.service.commonService;

import jakarta.servlet.http.HttpSession;

import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequestMapping("/admin")

public class AdminController {
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private commonService commonService;

    @GetMapping("/")
    public String index() {
        return "admin/index";
    }

    @GetMapping("/addProduct")
    public String addProduct() {
        return "admin/addProduct";
    }

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

}
