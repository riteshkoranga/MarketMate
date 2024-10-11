package com.ecommerce.MarketMate.service.product;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

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

    @Override
    public Product getProductById(int id) {
        Product product = prepo.findById(id).orElse(null);

        return product;
    }

    @Override
    public Product updateProduct(Product product, MultipartFile image) {
        Product oldProduct = prepo.findById(product.getId()).orElse(null);
        String imageName = image.isEmpty() ? oldProduct.getImage() : image.getOriginalFilename();
        if (oldProduct != null) {
            oldProduct.setName(product.getName());
            oldProduct.setDescription(product.getDescription());
            oldProduct.setPrice(product.getPrice());
            oldProduct.setStock(product.getStock());
            oldProduct.setCategory(product.getCategory());
            oldProduct.setImage(imageName);

        }
        Product updateProduct = prepo.save(oldProduct);
        if (!ObjectUtils.isEmpty(updateProduct)) {
            if (!image.isEmpty()) {
                try {
                    File saveFile = new ClassPathResource("static/images").getFile();
                    Path path = Paths.get(saveFile.getAbsolutePath() + File.separator + "product" + File.separator
                            + image.getOriginalFilename());

                    System.out.println(path);
                    Files.copy(image.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return product;
        }

        return null;

    }

}
