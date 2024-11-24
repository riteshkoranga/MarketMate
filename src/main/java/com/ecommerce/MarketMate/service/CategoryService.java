package com.ecommerce.MarketMate.service;

import com.ecommerce.MarketMate.model.category;
import java.util.*;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CategoryService {

    public category saveCategory(category category1);

    public List<category> getAllCategory();

    public Boolean existsCategory(String name);

    public Boolean deleteCategory(int id);

    public category getCategoryById(int id);

    public List<category> getAllActiveCategory();

    public Page<category> getAllCategoryPagination(Integer pageNo,Integer pageSize);

}
