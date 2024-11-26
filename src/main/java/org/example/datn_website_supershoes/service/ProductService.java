package org.example.datn_website_supershoes.service;

import jakarta.transaction.Transactional;
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
    private ProductRepository productRepository;

    @Autowired
    private ProductDetailRepository productDetailRepository;
    @Autowired
    private ProductDetailService productDetailService;

    @Autowired
    private BrandRepository brandRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private MaterialRepository materialRepository;

    @Autowired
    private ShoeSoleRepository shoeSoleRepository;
    @Autowired
    private RandomPasswordGeneratorService randomCodePromotion;


    private String generatePromotionCode() {
        return "SS" + randomCodePromotion.getCodePromotion();
    }

    @Transactional
    public void addProduct(ProductRequest productRequest) {
        try {
            Optional<Brand> optionalBrand = brandRepository.findByIdAndStatus(productRequest.getIdBrand(), Status.ACTIVE.toString());
            Optional<Category> optionalCategory = categoryRepository.findByIdAndStatus(productRequest.getIdCategory(), Status.ACTIVE.toString());
            Optional<Material> optionalMaterial = materialRepository.findByIdAndStatus(productRequest.getIdMaterial(), Status.ACTIVE.toString());
            Optional<ShoeSole> optionalShoeSole = shoeSoleRepository.findByIdAndStatus(productRequest.getIdShoeSole(), Status.ACTIVE.toString());
            if (optionalBrand.isEmpty()) {
                throw new RuntimeException("IdBrand: " + productRequest.getIdBrand() + " không tồn tại trong hệ thống");
            }
            if (optionalCategory.isEmpty()) {
                throw new RuntimeException("IdCategory: " + productRequest.getIdCategory() + " không tồn tại trong hệ thống");
            }
            if (optionalMaterial.isEmpty()) {
                throw new RuntimeException("IdMaterial: " + productRequest.getIdMaterial() + " không tồn tại trong hệ thống");
            }
            if (optionalShoeSole.isEmpty()) {
                throw new RuntimeException("IdShoeSole: " + productRequest.getIdShoeSole() + " không tồn tại trong hệ thống");
            }
            Product product = Product.builder()
                    .productCode(generatePromotionCode())
                    .name(productRequest.getName())
                    .brand(optionalBrand.get())
                    .category(optionalCategory.get())
                    .material(optionalMaterial.get())
                    .shoeSole(optionalShoeSole.get())
                    .gender(productRequest.isGender())
                    .imageByte(productRequest.getImage())
                    .build();
            product.setStatus(Status.ACTIVE.toString());
            Product saveProduct = productRepository.save(product);
            boolean checkProductDetail = productDetailService.createProductDetail(saveProduct, productRequest.getProductDetailRequest());
            if (!checkProductDetail) {
                throw new RuntimeException("Xảy ra lỗi khi thêm sản phẩm chi tiết");
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
    @Transactional
    public void updateProduct(UpdateProductRequest updateProductRequest){
        Optional<Product> optionalProduct = productRepository.findById(updateProductRequest.getId());
        Optional<Brand> optionalBrand = brandRepository.findByIdAndStatus(updateProductRequest.getIdBrand(), Status.ACTIVE.toString());
        Optional<Category> optionalCategory = categoryRepository.findByIdAndStatus(updateProductRequest.getIdCategory(), Status.ACTIVE.toString());
        Optional<Material> optionalMaterial = materialRepository.findByIdAndStatus(updateProductRequest.getIdMaterial(), Status.ACTIVE.toString());
        Optional<ShoeSole> optionalShoeSole = shoeSoleRepository.findByIdAndStatus(updateProductRequest.getIdShoeSole(), Status.ACTIVE.toString());
        if (optionalProduct.isEmpty()) {
            throw new RuntimeException("IdProduct: " + updateProductRequest.getId() + " không tồn tại trong hệ thống");
        }
        if (optionalBrand.isEmpty()) {
            throw new RuntimeException("IdBrand: " + updateProductRequest.getIdBrand() + " không tồn tại trong hệ thống");
        }
        if (optionalCategory.isEmpty()) {
            throw new RuntimeException("IdCategory: " + updateProductRequest.getIdCategory() + " không tồn tại trong hệ thống");
        }
        if (optionalMaterial.isEmpty()) {
            throw new RuntimeException("IdMaterial: " + updateProductRequest.getIdMaterial() + " không tồn tại trong hệ thống");
        }
        if (optionalShoeSole.isEmpty()) {
            throw new RuntimeException("IdShoeSole: " + updateProductRequest.getIdShoeSole() + " không tồn tại trong hệ thống");
        }
        Product product = optionalProduct.get();
        product.setStatus(Status.ACTIVE.toString());
        product.setBrand(optionalBrand.get());
        product.setCategory(optionalCategory.get());
        product.setMaterial(optionalMaterial.get());
        product.setShoeSole(optionalShoeSole.get());
        product.setName(updateProductRequest.getName());
        product.setGender(updateProductRequest.isGender());
        if(updateProductRequest.getImage()!=null){
            product.setImageByte(updateProductRequest.getImage());
        }
        Product updateProduct = productRepository.save(product);
        productDetailService.updateProduct(updateProductRequest.getProductDetailRequest());
    }

    public ProductResponse findProductById(Long id){
        Optional<ProductResponse> optional = productRepository.findProductRequestsById(id);
        if(optional.isEmpty()){
            throw new RuntimeException("Sản phẩm với Id: "+ id +" không tồn tại");
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
                                (response.getStatus() != null && response.getStatus().toLowerCase().contains(status.trim().toLowerCase()))
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
            throw new RuntimeException("Id: " + id + " của sản phẩm không tồn tại");
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
            throw new RuntimeException("Sản phẩm có Id là: " + idProduct + " không tồn tại");
        }
        return productViewCustomerReponse.get();
    }
}
