package org.example.datn_website_supershoes.service;

import org.example.datn_website_supershoes.Enum.Status;
import org.example.datn_website_supershoes.dto.response.ProductResponse;
import org.example.datn_website_supershoes.dto.response.VoucherResponse;
import org.example.datn_website_supershoes.model.Product;
import org.example.datn_website_supershoes.model.Voucher;
import org.example.datn_website_supershoes.repository.BrandRepository;
import org.example.datn_website_supershoes.repository.CategoryRepository;
import org.example.datn_website_supershoes.repository.MaterialRepository;
import org.example.datn_website_supershoes.repository.ProductRepository;
import org.example.datn_website_supershoes.repository.ShoeSoleRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;


import java.util.Optional;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

public ProductResponse createProduct(Product product) {
    // Lưu đối tượng Product vào cơ sở dữ liệu
    Product savedProduct = productRepository.save(product);

    // Chuyển đổi đối tượng đã lưu thành ProductResponse để trả về
    return convertToProductResponse(savedProduct);
}


    public Page<ProductResponse> getAllProduct(Specification<Product> spec, Pageable pageable) {
        return productRepository.findAll(spec, pageable).map(this::convertToProductResponse);
    }

private ProductResponse convertToProductResponse(Product product) {
    ProductResponse response = new ProductResponse();
    // Chép các thuộc tính đơn giản
    response.setId(product.getId());
    response.setName(product.getName());
    response.setProductCode(product.getProductCode());
    response.setImageByte(product.getImageByte());
    // Chép các thuộc tính từ các đối tượng liên quan
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
    response.setStatus(product.getStatus());

    return response;

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


    public List<ProductResponse> findProductRequests(){
    return productRepository.findProductRequests();
    }
}
