package org.example.datn_website_supershoes.service;

import org.example.datn_website_supershoes.dto.request.ProductImageRequest;
import org.example.datn_website_supershoes.dto.response.ProductImageResponse;
import org.example.datn_website_supershoes.model.ProductDetail;
import org.example.datn_website_supershoes.model.ProductImage;
import org.example.datn_website_supershoes.repository.ProductDetailRepository;
import org.example.datn_website_supershoes.repository.ProductImageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

import java.util.stream.Collectors;

@Service
public class ProductImageService {
    @Autowired
    private  ProductImageRepository productImageRepository;

    @Autowired
    private ProductDetailRepository productDetailRepository;

    public List<ProductImageResponse> findAll() {
        return productImageRepository.listProductImageResponse();
    }
    public List<ProductImage> createProductImage(ProductImageRequest productImageRequest) {
        Long idProductDetail = productImageRequest.getIdProductDetail();
        List<byte[]> imageBytesList = productImageRequest.getImageBytes();

        if (imageBytesList == null || imageBytesList.isEmpty() || idProductDetail == null) {
            throw new RuntimeException("Image bytes list and ProductDetail ID cannot be null or empty.");
        }

        // Retrieve the associated ProductDetail
        ProductDetail productDetail = productDetailRepository.findById(idProductDetail)
                .orElseThrow(() -> new RuntimeException("ProductDetail not found with ID: " + idProductDetail));

        // Create a list to store each saved ProductImage
        List<ProductImage> savedImages = new ArrayList<>();

        // Iterate over each byte array in the list and save as a ProductImage
        for (byte[] imageBytes : imageBytesList) {
            ProductImage productImage = new ProductImage();
            productImage.setImageByte(imageBytes);
            productImage.setProductDetail(productDetail);

            // Save the ProductImage to the repository
            savedImages.add(productImageRepository.save(productImage));
        }

        return savedImages; // Return the list of saved images
    }




    public List<ProductImage> findAllByIds(Collection<Long> imageIds) {
        // Gọi phương thức findAllByIdIn với một collection các ID
        return productImageRepository.findAllByIdIn(imageIds);
    }


    public List<ProductImage> updateProductImageByProductDetailId(Long idProductDetail, ProductImageRequest productImageRequest) {
        ProductDetail productDetail = productDetailRepository.findById(idProductDetail)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy ProductDetail với ID: " + idProductDetail));

        List<byte[]> imageBytesList = productImageRequest.getImageBytes();

        if (imageBytesList == null || imageBytesList.isEmpty()) {
            throw new RuntimeException("No image bytes provided for updating.");
        }

        List<ProductImage> updatedImages = new ArrayList<>();

        // Delete existing images if required (or you can choose to update existing images)
        productImageRepository.deleteByProductDetail(productDetail);

        // Save each image in the imageBytes list
        for (byte[] imageBytes : imageBytesList) {
            ProductImage productImage = new ProductImage();
            productImage.setImageByte(imageBytes);
            productImage.setProductDetail(productDetail);

            // Save each new ProductImage to the repository
            updatedImages.add(productImageRepository.save(productImage));
        }

        return updatedImages; // Return the list of saved/updated images
    }




}
