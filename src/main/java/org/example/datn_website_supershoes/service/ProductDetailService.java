package org.example.datn_website_supershoes.service;

import org.example.datn_website_supershoes.Enum.Status;
import org.example.datn_website_supershoes.dto.response.ProductDetailResponse;
import org.example.datn_website_supershoes.dto.response.ProductResponse;
import org.example.datn_website_supershoes.model.Product;
import org.example.datn_website_supershoes.model.ProductDetail;
import org.example.datn_website_supershoes.repository.ProductDetailRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class ProductDetailService {
    @Autowired
    private ProductDetailRepository productDetailRepository;


    public ProductDetail createProductDetail(ProductDetail productDetail) {

        return productDetailRepository.save(productDetail);
    }

    public Page<ProductDetailResponse> getAllProductDetail(Specification<ProductDetail> spec, Pageable pageable) {
//
        return productDetailRepository.findAll(spec, pageable).map(this::convertToProductDetailResponse);
    }

    private ProductDetailResponse convertToProductDetailResponse(ProductDetail productDetail) {
        ProductDetailResponse response = new ProductDetailResponse();

        // Chép các thuộc tính từ ProductDetail
        response.setId(productDetail.getId());
        response.setQuantity(productDetail.getQuantity() != null ? productDetail.getQuantity() : 0); // Cung cấp giá trị mặc định nếu null
        response.setPrice(productDetail.getPrice() != null ? productDetail.getPrice() : BigDecimal.ZERO); // Cung cấp giá trị mặc định nếu null


        // Kiểm tra nếu Product không bị null
        if (productDetail.getProduct() != null) {
            Product product = productDetail.getProduct();
            response.setIdProduct(product.getId());
            response.setNameProduct(product.getName());
            response.setImageByte(product.getImageByte());
            response.setGender(product.isGender());

            // Chuyển tiếp các giá trị từ các đối tượng liên quan
            if (product.getBrand() != null) {
                response.setIdBrand(product.getBrand().getId());
                response.setNameBrand(product.getBrand().getName());
            }
            if (product.getCategory() != null) {
                response.setIdCategory(product.getCategory().getId());
                response.setNameCategory(product.getCategory().getName());
            }
            if (product.getMaterial() != null) {
                response.setIdMaterial(product.getMaterial().getId());
                response.setNameMaterial(product.getMaterial().getName());
            }
            if (product.getShoeSole() != null) {
                response.setIdShoeSole(product.getShoeSole().getId());
                response.setNameShoeSole(product.getShoeSole().getName());
            }
        }

        // Gán các giá trị khác nếu có
        if (productDetail.getColor() != null) {
            response.setIdColor(productDetail.getColor().getId());
            response.setNameColor(productDetail.getColor().getName());
        }
        if (productDetail.getSize() != null) {
            response.setIdSize(productDetail.getSize().getId());
            response.setNameSize(productDetail.getSize().getName());
        }

        response.setStatus(productDetail.getStatus());

        return response;
    }


    public Optional<ProductDetail> getProductByIdDetail(Long id) {
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

    public void deleteProductDetail(Long id) {
        productDetailRepository.deleteById(id);
    }
}
