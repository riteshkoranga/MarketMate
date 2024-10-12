package com.ecommerce.MarketMate.service.product;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.ecommerce.MarketMate.model.Product;

public interface productService {

    public Product saveProduct(Product product);

    public List<Product> getAllProducts();

    public Boolean deleteProduct(int id);

    public Product getProductById(int id);

    public Product updateProduct(Product product, MultipartFile file);

    public List<Product> getAllActiveProducts(String category);

}