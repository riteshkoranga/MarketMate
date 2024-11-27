package com.ecommerce.MarketMate.service;

import com.ecommerce.MarketMate.model.Category;
import java.util.*;

import org.springframework.data.domain.Page;


public interface CategoryService {

    public Category saveCategory(Category cat);

    public List<Category> getAllCategory();

    public Boolean existsCategory(String name);

    public Boolean deleteCategory(int id);

    public Category getCategoryById(int id);

    public List<Category> getAllActiveCategory();

    public Page<Category> getAllCategoryPagination(Integer pageNo,Integer pageSize);

}
