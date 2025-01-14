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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import com.ecommerce.MarketMate.model.Product;
import com.ecommerce.MarketMate.repository.productRepo.productRepo;
import com.ecommerce.MarketMate.service.FileService;
import com.ecommerce.MarketMate.util.BucketType;
import com.ecommerce.MarketMate.util.CommonUtil;



@Service
public class productServiceIMPL implements productService {
    @Autowired
    private productRepo prepo;
    @Autowired
    private CommonUtil commonUtil;
    @Autowired
    private FileService fileService;


    @Override
    public Product saveProduct(Product product) {
        return prepo.save(product);

    }

    @Override
    public List<Product> getAllProducts() {
        return prepo.findAll();
    }

    

    @Override
    public Page<Product> getAllProductsPagination(Integer pageNo, Integer pageSize) {
       Pageable pageable=PageRequest.of(pageNo, pageSize);
       return prepo.findAll(pageable);
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
        //String imageName = image.isEmpty() ? oldProduct.getImage() : image.getOriginalFilename();
        String ImageUrl ="";
        if(image.isEmpty()){
            ImageUrl=oldProduct.getImage();
            
        }else{
            ImageUrl =commonUtil.getImageUrl(image, BucketType.PRODUCT.getId());
        }
        
        if (!ObjectUtils.isEmpty(oldProduct)) {
            oldProduct.setName(product.getName());
            oldProduct.setDescription(product.getDescription());
            oldProduct.setPrice(product.getPrice());
            oldProduct.setStock(product.getStock());
            oldProduct.setCategory(product.getCategory());
            oldProduct.setImage(ImageUrl);
            oldProduct.setIsActive(product.getIsActive());

            oldProduct.setDiscount(product.getDiscount());
            // 100*5/100; 100-5=95
            Double discount = product.getPrice() * (product.getDiscount() / 100.0);
            Double newPrice = product.getPrice() - discount;
            oldProduct.setDiscountPrice(newPrice);

        }
        Product updateProduct = prepo.save(oldProduct);
        if (!ObjectUtils.isEmpty(updateProduct)) {
            if (!image.isEmpty()) {
                // try {
                //     File saveFile = new ClassPathResource("static/images").getFile();
                //     Path path = Paths.get(saveFile.getAbsolutePath() + File.separator + "product" + File.separator
                //             + image.getOriginalFilename());

                //     System.out.println(path);
                //     Files.copy(image.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
                // } catch (IOException e) {
                //     e.printStackTrace();
                // }
                fileService.uploadFileS3(image, BucketType.PRODUCT.getId());
            }
            return product;
        }

        return null;

    }

    @Override
    public List<Product> getAllActiveProducts(String category) {
        List<Product> products;
        if (ObjectUtils.isEmpty(category)) {
            products = prepo.findByIsActiveTrue();
        } else {
            products = prepo.findByCategory(category);
            if (ObjectUtils.isEmpty(products)) {

            }
        }

        return products;
    }

    @Override
    public List<Product> searchProduct(String ch) {

        return prepo.findByNameContainingIgnoreCaseOrCategoryContainingIgnoreCase(ch,ch);
        
         
    }

    @Override
    public Page<Product> getAllActiveProductsPagination(Integer pageNo, Integer pageSize,String category) {
        Pageable pageable=PageRequest.of(pageNo, pageSize);
        Page<Product> pageProduct=null;
        if (ObjectUtils.isEmpty(category)) {
            pageProduct = prepo.findByIsActiveTrue(pageable);
        } else {
            pageProduct = prepo.findByCategory(pageable,category);
            
        }
        return pageProduct;
    }

    

    @Override
    public Page<Product> searchActiveProductPagination(Integer pageNo, Integer pageSize,String category,String ch) {
        Pageable pageable=PageRequest.of(pageNo, pageSize);
        Page<Product> pageProduct=null;
        pageProduct=prepo.findByisActiveTrueAndNameContainingIgnoreCaseOrCategoryContainingIgnoreCase(ch,ch,pageable);
        // if (ObjectUtils.isEmpty(category)) {
        //     pageProduct = prepo.findByIsActiveTrue(pageable);
        // } else {
        //     pageProduct = prepo.findByCategory(pageable,category);
            
        // }
        return pageProduct;
       
    }

    @Override
    public Page<Product> searchProductPagination(String ch, Integer pageNo, Integer pageSize) {
        Pageable pageable=PageRequest.of(pageNo, pageSize);
        return prepo.findByNameContainingIgnoreCaseOrCategoryContainingIgnoreCase(ch,ch,pageable);

        
    }

}
