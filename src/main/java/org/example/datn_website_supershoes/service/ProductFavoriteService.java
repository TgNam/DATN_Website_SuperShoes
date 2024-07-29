package org.example.datn_website_supershoes.service;

import org.example.datn_website_supershoes.model.Product;
import org.example.datn_website_supershoes.model.ProductFavorite;
import org.example.datn_website_supershoes.repository.ProductFavoriteRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductFavoriteService {
    @Autowired
    private ProductFavoriteRepository productFavoriteRepository;

    public ProductFavorite createProductFavorite(ProductFavorite productFavorite){
        return productFavoriteRepository.save(productFavorite);
    }

    public List<ProductFavorite> getAllProductFavorite(){
        return productFavoriteRepository.findAll();
    }
    public Optional<ProductFavorite> getProductFavoriteById(Long id){
        return productFavoriteRepository.findById(id);
    }
    public ProductFavorite updateProductFavorite(Long id, ProductFavorite productFavorite) {
        ProductFavorite existingProductFavorite = productFavoriteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cart not found"));

        String[] ignoredProperties = {"id", "createdAt", "createdBy"};
        BeanUtils.copyProperties(productFavorite, existingProductFavorite, ignoredProperties);

        if (productFavorite.getProduct() != null) {
            existingProductFavorite.setProduct(productFavorite.getProduct());
        }
        if (productFavorite.getAccount() != null) {
            existingProductFavorite.setAccount(productFavorite.getAccount());
        }

        return productFavoriteRepository.save(productFavorite);
    }
    public void deleteProductFavorite(Long id){
        productFavoriteRepository.deleteById(id);
    }
}
