package org.example.datn_website_supershoes.service;

import org.springframework.transaction.annotation.Transactional;
import org.example.datn_website_supershoes.Enum.Status;
import org.example.datn_website_supershoes.dto.request.ProductRequest;
import org.example.datn_website_supershoes.dto.request.updateProduct.UpdateProductRequest;
import org.example.datn_website_supershoes.dto.response.*;
import org.example.datn_website_supershoes.model.Brand;
import org.example.datn_website_supershoes.model.Category;
import org.example.datn_website_supershoes.model.Material;
import org.example.datn_website_supershoes.model.Product;
import org.example.datn_website_supershoes.model.ProductDetail;
import org.example.datn_website_supershoes.model.ProductImage;
import org.example.datn_website_supershoes.model.ShoeSole;
import org.example.datn_website_supershoes.model.Voucher;
import org.example.datn_website_supershoes.repository.*;
import org.example.datn_website_supershoes.webconfig.NotificationController;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;


import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductService {

    @Autowired
    ProductRepository productRepository;

    @Autowired
    ProductDetailRepository productDetailRepository;
    @Autowired
    ProductDetailService productDetailService;

    @Autowired
    BrandRepository brandRepository;

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    MaterialRepository materialRepository;

    @Autowired
    ShoeSoleRepository shoeSoleRepository;
    @Autowired
    RandomPasswordGeneratorService randomCodePromotion;
    @Autowired
    NotificationController notificationController;

    private String generatePromotionCode() {
        return "SS" + randomCodePromotion.getCodePromotion();
    }

    @Transactional
    public void addProduct(ProductRequest productRequest) {
        Brand brand = brandRepository.findById(productRequest.getIdBrand())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy tài nguyên hãng trong hệ thống!"));
        if (brand.getStatus().equals(Status.INACTIVE.toString())) {
            throw new RuntimeException("Thương hiệu " + brand.getName() + " không còn hoạt động");
        }
        Category category = categoryRepository.findById(productRequest.getIdCategory())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy tài nguyên danh mục trong hệ thống!"));
        if (category.getStatus().equals(Status.INACTIVE.toString())) {
            throw new RuntimeException("Danh mục " + category.getName() + " không còn hoạt động");
        }
        Material material = materialRepository.findById(productRequest.getIdMaterial())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy tài nguyên chất liệu trong hệ thống!"));
        if (material.getStatus().equals(Status.INACTIVE.toString())) {
            throw new RuntimeException("Chất liệu " + material.getName() + " không còn hoạt động");
        }
        ShoeSole shoeSole = shoeSoleRepository.findById(productRequest.getIdShoeSole())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy tài nguyên đế giày trong hệ thống!"));
        if (shoeSole.getStatus().equals(Status.INACTIVE.toString())) {
            throw new RuntimeException("Đế giày " + shoeSole.getName() + " không còn hoạt động");
        }
        Product product = Product.builder()
                .productCode(generatePromotionCode())
                .name(productRequest.getName())
                .brand(brand)
                .category(category)
                .material(material)
                .shoeSole(shoeSole)
                .gender(productRequest.isGender())
                .imageByte(productRequest.getImage())
                .build();
        product.setStatus(Status.ACTIVE.toString());
        Product saveProduct = productRepository.save(product);
        boolean checkProductDetail = productDetailService.createProductDetail(saveProduct, productRequest.getProductDetailRequest());
        if (!checkProductDetail) {
            throw new RuntimeException("Xảy ra lỗi khi thêm sản phẩm chi tiết");
        }
    }

    @Transactional
    public void updateProduct(UpdateProductRequest updateProductRequest) {
        Product product = productRepository.findById(updateProductRequest.getId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy tài nguyên sản phẩm trong hệ thống!"));
        Brand brand = brandRepository.findById(updateProductRequest.getIdBrand())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy tài nguyên hãng trong hệ thống!"));
        Category category = categoryRepository.findById(updateProductRequest.getIdCategory())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy tài nguyên danh mục trong hệ thống!"));
        Material material = materialRepository.findById(updateProductRequest.getIdMaterial())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy tài nguyên chất liệu trong hệ thống!"));
        ShoeSole shoeSole = shoeSoleRepository.findById(updateProductRequest.getIdShoeSole())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy tài nguyên đế giày trong hệ thống!"));
        product.setStatus(Status.ACTIVE.toString());
        if (!product.getBrand().getId().equals(brand.getId())) {
            if (brand.getStatus().equals(Status.INACTIVE.toString())) {
                throw new RuntimeException("Thương hiệu " + brand.getName() + " không còn hoạt động");
            }
            product.setBrand(brand);
        }
        if (!product.getCategory().getId().equals(category.getId())) {
            if (category.getStatus().equals(Status.INACTIVE.toString())) {
                throw new RuntimeException("Danh mục " + category.getName() + " không còn hoạt động");
            }
            product.setCategory(category);
        }
        if (!product.getMaterial().getId().equals(material.getId())) {
            if (material.getStatus().equals(Status.INACTIVE.toString())) {
                throw new RuntimeException("Chất liệu " + material.getName() + " không còn hoạt động");
            }
            product.setMaterial(material);
        }
        if (!product.getShoeSole().getId().equals(shoeSole.getId())) {
            if (shoeSole.getStatus().equals(Status.INACTIVE.toString())) {
                throw new RuntimeException("Đế giày " + shoeSole.getName() + " không còn hoạt động");
            }
            product.setShoeSole(shoeSole);
        }
        product.setName(updateProductRequest.getName());
        product.setGender(updateProductRequest.isGender());
        if (updateProductRequest.getImage() != null) {
            product.setImageByte(updateProductRequest.getImage());
        }
        productRepository.save(product);
        if (!updateProductRequest.getProductDetailRequest().isEmpty()) {
            productDetailService.updateProduct(updateProductRequest.getProductDetailRequest());
        }
        notificationController.sendNotification();
    }

    public ProductResponse findProductById(Long id) {
        Optional<ProductResponse> optional = productRepository.findProductRequestsById(id);
        if (optional.isEmpty()) {
            throw new RuntimeException("Không tìm thấy tài nguyên sản phẩm trong hệ thống!");
        }
        return optional.get();
    }

    public List<ProductProductDetailResponse> findProductProductDetailResponse() {
        return productRepository.findProductProductDetailResponse();
    }

    public List<ProductProductDetailResponse> filterProductProductDetailResponse(
            String search, String idCategory, String idBrand, String status) {
        return productRepository.findProductProductDetailResponse().stream()
                .filter(response ->
                        search == null ||
                                search.isEmpty() ||
                                (response.getName() != null && response.getName().toLowerCase().contains(search.trim().toLowerCase()))
                )
                .filter(response ->
                        idCategory == null ||
                                idCategory.isEmpty() ||
                                (response.getIdCategory() != null && response.getIdCategory().toString().contains(idCategory.trim()))
                )
                .filter(response ->
                        idBrand == null ||
                                idBrand.isEmpty() ||
                                (response.getIdBrand() != null && response.getIdBrand().toString().contains(idBrand.trim()))
                )
                .filter(response ->
                        status == null ||
                                status.isEmpty() ||
                                (response.getStatus() != null && response.getStatus().equalsIgnoreCase(status.trim().toLowerCase()))
                )
                .collect(Collectors.toList());
    }


    public ProductImageResponse findImageByIdProduct(Long id) {
        return productRepository.findImageByIdProduct(id);
    }

    @Transactional
    public Product updateStatus(Long id, boolean aBoolean) {
        Optional<Product> optionalProduct = productRepository.findById(id);
        if (!optionalProduct.isPresent()) {
            throw new RuntimeException("Không tìm thấy tài nguyên sản phẩm trong hệ thống!");
        }
        String newStatus = aBoolean ? Status.ACTIVE.toString() : Status.INACTIVE.toString();
        optionalProduct.get().setStatus(newStatus);
        Product product = productRepository.save(optionalProduct.get());
        List<ProductDetail> productDetails = productDetailRepository.findByProductId(id);
        for (ProductDetail detail : productDetails) {
            if (detail.getQuantity() > 0) {
                detail.setStatus(newStatus);
                productDetailRepository.save(detail);
            }
        }
        notificationController.sendNotification();
        return product;
    }

    public Map<Long, String> getProductNameById(List<Long> listId) {
        Map<Long, String> mapName = new HashMap<>();
        for (Long id : listId) {
            ProductDetail pd = productDetailRepository.findById(id).get();
            mapName.put(id, pd.getProduct().getName());
        }
        return mapName;
    }

    public List<ProductResponse> findProductRequests() {
        return productRepository.findProductRequests();
    }

    public ProductViewCustomerReponse getFindProductPriceRangeWithPromotionByIdProduct(Long idProduct) {
        Optional<ProductViewCustomerReponse> productViewCustomerReponse = productDetailRepository.findProductPriceRangeWithPromotionByIdProduct(idProduct);
        if (productViewCustomerReponse.isEmpty()) {
            throw new RuntimeException("Không tìm thấy tài nguyên sản phẩm trong hệ thống!");
        }
        return productViewCustomerReponse.get();
    }
}
