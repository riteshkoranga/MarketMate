package com.ecommerce.MarketMate.service.product;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

}
