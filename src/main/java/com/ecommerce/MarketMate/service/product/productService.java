package com.ecommerce.MarketMate.service.product;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import com.ecommerce.MarketMate.model.Product;

public interface productService {

    public Product saveProduct(Product product);

    public List<Product> getAllProducts();

    public Boolean deleteProduct(int id);

    public Product getProductById(int id);

    public Product updateProduct(Product product, MultipartFile file);

    public List<Product> getAllActiveProducts(String category);

    public List<Product> searchProduct(String ch);

    public Page<Product> getAllActiveProductsPagination(Integer pageNo,Integer pageSize,String category);

    public Page<Product> getAllProductsPagination(Integer pageNo,Integer pageSize);
    public Page<Product> searchProductPagination(String ch,Integer pageNo,Integer pageSize);
}
