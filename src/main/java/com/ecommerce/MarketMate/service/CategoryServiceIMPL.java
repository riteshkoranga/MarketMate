package com.ecommerce.MarketMate.service;

import java.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ecommerce.MarketMate.model.category;
import com.ecommerce.MarketMate.repository.CategoryRepo;

@Service
public class CategoryServiceIMPL implements CategoryService {
    @Autowired
    private CategoryRepo repo;

    @Override
    public category saveCategory(category category1) {

        return repo.save(category1);
    }

    @Override
    public List<category> getAllCategory() {
        return repo.findAll();
    }

    @Override
    public Boolean existsCategory(String name) {
        if (repo.existsByName(name)) {
            return true;
        }
        return false;
    }

    @Override
    public Boolean deleteCategory(int id) {
        category c = repo.findById(id).orElse(null);

        if (c != null) {
            repo.delete(c);
            return true;
        }

        return false;
    }

}
