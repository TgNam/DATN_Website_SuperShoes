package org.example.datn_website_supershoes.service;

import org.example.datn_website_supershoes.dto.request.ProductImageRequest;
import org.example.datn_website_supershoes.dto.response.ProductImageResponse;
import org.example.datn_website_supershoes.model.ProductImage;
import org.example.datn_website_supershoes.repository.ProductImageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductImageService {
    @Autowired
    private  ProductImageRepository productImageRepository;
    public List<ProductImageResponse> findAll() {
        return productImageRepository.listProductImageResponse();
    }
    public ProductImage createProductImage(ProductImageRequest productImageRequest){
        byte[] imageBytes = productImageRequest.getImageByte();
        ProductImage productImage = new ProductImage();
        System.out.println(imageBytes);
        if(imageBytes !=null){
            productImage.setImageByte(imageBytes);
        }else {
            throw new RuntimeException("Lỗi không thêm được.");
        }

        return productImageRepository.save(productImage);
    }
}
