package org.example.datn_website_supershoes.service;

import jakarta.transaction.Transactional;
import org.example.datn_website_supershoes.Enum.Status;
import org.example.datn_website_supershoes.dto.request.ProductRequest;
import org.example.datn_website_supershoes.dto.response.ProductResponse;
import org.example.datn_website_supershoes.dto.response.ProductViewCustomerReponse;
import org.example.datn_website_supershoes.dto.response.VoucherResponse;
import org.example.datn_website_supershoes.model.Brand;
import org.example.datn_website_supershoes.model.Category;
import org.example.datn_website_supershoes.model.Material;
import org.example.datn_website_supershoes.model.Product;
import org.example.datn_website_supershoes.model.ShoeSole;
import org.example.datn_website_supershoes.model.Voucher;
import org.example.datn_website_supershoes.repository.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductDetailRepository productDetailRepository;

    @Autowired
    private BrandRepository brandRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private MaterialRepository materialRepository;

    @Autowired
    private ShoeSoleRepository shoeSoleRepository;




public ProductResponse createProduct(Product product) {
    // Lưu đối tượng Product vào cơ sở dữ liệu
    Product savedProduct = productRepository.save(product);

    // Chuyển đổi đối tượng đã lưu thành ProductResponse để trả về
    return convertToProductResponse(savedProduct);
}

    @Transactional
    public Product updateProduct(Long id, ProductRequest productRequest) {
        // Tìm sản phẩm theo ID
        Product existingProduct = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        // Cập nhật thông tin thương hiệu nếu có trong ProductRequest
        if (productRequest.getIdBrand() != null) {
            Brand brand = brandRepository.findById(productRequest.getIdBrand())
                    .orElseThrow(() -> new RuntimeException("Brand not found"));
            existingProduct.setBrand(brand);
        }

        // Cập nhật thông tin danh mục nếu có trong ProductRequest
        if (productRequest.getIdCategory() != null) {
            Category category = categoryRepository.findById(productRequest.getIdCategory())
                    .orElseThrow(() -> new RuntimeException("Category not found"));
            existingProduct.setCategory(category);
        }

        // Cập nhật thông tin chất liệu nếu có trong ProductRequest
        if (productRequest.getIdMaterial() != null) {
            Material material = materialRepository.findById(productRequest.getIdMaterial())
                    .orElseThrow(() -> new RuntimeException("Material not found"));
            existingProduct.setMaterial(material);
        }

        // Cập nhật thông tin đế giày nếu có trong ProductRequest
        if (productRequest.getIdShoeSole() != null) {
            ShoeSole shoeSole = shoeSoleRepository.findById(productRequest.getIdShoeSole())
                    .orElseThrow(() -> new RuntimeException("ShoeSole not found"));
            existingProduct.setShoeSole(shoeSole);
        }

        // Cập nhật các trường dữ liệu sản phẩm nếu có giá trị mới
        if (productRequest.getName() != null) {
            existingProduct.setName(productRequest.getName());
        }
        if (productRequest.getProductCode() != null) {
            existingProduct.setProductCode(productRequest.getProductCode());
        }
//        if (productRequest.getImageByte() != null) {
//            existingProduct.setImageByte(productRequest.getImageByte());
//        }

        existingProduct.setGender(productRequest.isGender());
        // Cập nhật danh sách chi tiết sản phẩm (ProductDetails) nếu có


        // Lưu sản phẩm đã cập nhật
        return productRepository.save(existingProduct);
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

    public ProductViewCustomerReponse getFindProductPriceRangeWithPromotionByIdProduct(Long idProduct){
        Optional<ProductViewCustomerReponse> productViewCustomerReponse = productDetailRepository.findProductPriceRangeWithPromotionByIdProduct(idProduct);
        if (productViewCustomerReponse.isEmpty()){
            throw new RuntimeException("Sản phẩm có Id là: " +idProduct+" không tồn tại");
        }
        return productViewCustomerReponse.get();
    }
}
