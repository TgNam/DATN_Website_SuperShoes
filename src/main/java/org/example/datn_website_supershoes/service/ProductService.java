package org.example.datn_website_supershoes.service;

import org.example.datn_website_supershoes.model.Cart;
import org.example.datn_website_supershoes.model.Product;
import org.example.datn_website_supershoes.repository.ProductRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepository;

    public Product createProduct(Product product){
        return productRepository.save(product);
    }

    public List<Product> getAllProduct(){
        return productRepository.findAll();
    }
    public Optional<Product> getProductById(Long id){
        return productRepository.findById(id);
    }
    public Product updateProduct(Long id, Product product) {
        Product existingProduct = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cart not found"));

        String[] ignoredProperties = {"id", "createdAt", "createdBy"};
        BeanUtils.copyProperties(product, existingProduct, ignoredProperties);

        if (product.getProductFavorites() != null) {
            existingProduct.setProductFavorites(product.getProductFavorites());
        }
        if (product.getProductDetails() != null) {
            existingProduct.setProductDetails(product.getProductDetails());
        }

        return productRepository.save(product);
    }
    public void deleteProduct(Long id){
        productRepository.deleteById(id);
    }
}
