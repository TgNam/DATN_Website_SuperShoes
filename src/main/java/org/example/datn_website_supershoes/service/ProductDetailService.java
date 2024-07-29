package org.example.datn_website_supershoes.service;

import org.example.datn_website_supershoes.model.ProductDetail;
import org.example.datn_website_supershoes.repository.ProductDetailRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductDetailService {
    @Autowired
    private ProductDetailRepository productDetailRepository;


    public ProductDetail createProductDetail(ProductDetail productDetail){

        return productDetailRepository.save(productDetail);
    }

    public List<ProductDetail> getAllProductDetail(){
        return productDetailRepository.findAll();
    }
    public Optional<ProductDetail> getProductByIdDetail(Long id){
        return productDetailRepository.findById(id);
    }


    public ProductDetail updateProductDetail(Long id, ProductDetail productDetail) {
        ProductDetail existingProductDetail = productDetailRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("ProductDetail not found"));

        String[] ignoredProperties = {"id", "createdAt", "createdBy"};
        BeanUtils.copyProperties(productDetail, existingProductDetail, ignoredProperties);

        if (productDetail.getProduct() != null) {
            existingProductDetail.setProduct(productDetail.getProduct());
        }
        if (productDetail.getSize() != null) {
            existingProductDetail.setSize(productDetail.getSize());
        }
        if (productDetail.getColor() != null) {
            existingProductDetail.setColor(productDetail.getColor());
        }
        if (productDetail.getProductImage() != null) {
            existingProductDetail.setProductImage(productDetail.getProductImage());
        }
        if (productDetail.getPromotionDetail() != null) {
            existingProductDetail.setPromotionDetail(productDetail.getPromotionDetail());
        }
        if (productDetail.getCartDetails() != null) {
            existingProductDetail.setCartDetails(productDetail.getCartDetails());
        }
        if (productDetail.getBillDetails() != null) {
            existingProductDetail.setBillDetails(productDetail.getBillDetails());
        }

        return productDetailRepository.save(productDetail);
    }
    public void deleteProductDetail(Long id){
        productDetailRepository.deleteById(id);
    }
}
