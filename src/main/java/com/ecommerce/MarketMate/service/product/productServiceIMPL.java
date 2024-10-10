package com.ecommerce.MarketMate.service.product;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import com.ecommerce.MarketMate.model.Product;
import com.ecommerce.MarketMate.repository.productRepo.productRepo;

@Service
public class productServiceIMPL implements productService {
    @Autowired
    private productRepo prepo;

    @Override
    public Product saveProduct(Product product) {
        return prepo.save(product);

    }

    @Override
    public List<Product> getAllProducts() {
        return prepo.findAll();
    }

    @Override
    public Boolean deleteProduct(int id) {
        Product product = prepo.findById(id).orElse(null);
        if (!ObjectUtils.isEmpty(product)) {
            prepo.delete(product);
            return true;
        }
        return false;
    }

}
