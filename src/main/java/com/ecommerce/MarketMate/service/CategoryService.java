package com.ecommerce.MarketMate.service;

import com.ecommerce.MarketMate.model.category;
import java.util.*;

public interface CategoryService {

    public category saveCategory(category category1);

    public List<category> getAllCategory();

    public Boolean existsCategory(String name);

}