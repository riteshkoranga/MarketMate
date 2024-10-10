package com.ecommerce.MarketMate.service.product;

import java.util.List;

import com.ecommerce.MarketMate.model.Product;

public interface productService {

    public Product saveProduct(Product product);

    public List<Product> getAllProducts();

    public Boolean deleteProduct(int id);

}
