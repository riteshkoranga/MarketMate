package com.ecommerce.MarketMate.service;

import java.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.ecommerce.MarketMate.model.Category;
import com.ecommerce.MarketMate.repository.CategoryRepo;

@Service
public class CategoryServiceIMPL implements CategoryService {
    @Autowired
    private CategoryRepo repo;

    @Override
    public Category saveCategory(Category cat) {

        return repo.save(cat);
    }

    @Override
    public List<Category> getAllCategory() {
        return repo.findAll();
    }

    @Override
    public Boolean existsCategory(String name) {
        return repo.existsByName(name);
    }

    @Override
    public Boolean deleteCategory(int id) {
        Category c = repo.findById(id).orElse(null);

        if (c != null) {
            repo.delete(c);
            return true;
        }

        return false;
    }

    @Override
    public Category getCategoryById(int id) {
        Category c = repo.findById(id).orElse(null);
        return c;
    }

    @Override
    public List<Category> getAllActiveCategory() {
        List<Category> categories = repo.findByIsActiveTrue();
        return categories;
    }

    @Override
    public Page<Category> getAllCategoryPagination(Integer pageNo,Integer pageSize) {
       Pageable pageable=PageRequest.of(pageNo, pageSize);
       return repo.findAll(pageable);
        
    }

}
