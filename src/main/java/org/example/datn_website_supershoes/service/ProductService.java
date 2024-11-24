package org.example.datn_website_supershoes.service;

import jakarta.transaction.Transactional;
import org.example.datn_website_supershoes.Enum.Status;
import org.example.datn_website_supershoes.dto.request.ProductRequest;
import org.example.datn_website_supershoes.dto.response.ProductImageResponse;
import org.example.datn_website_supershoes.dto.response.ProductResponse;
import org.example.datn_website_supershoes.dto.response.ProductViewCustomerReponse;
import org.example.datn_website_supershoes.dto.response.VoucherResponse;
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
    public void addProduct(ProductRequest productRequest){
        try {
            Optional<Brand> optionalBrand = brandRepository.findByIdAndStatus(productRequest.getIdBrand(),Status.ACTIVE.toString());
            Optional<Category> optionalCategory = categoryRepository.findByIdAndStatus(productRequest.getIdCategory(),Status.ACTIVE.toString());
            Optional<Material> optionalMaterial = materialRepository.findByIdAndStatus(productRequest.getIdMaterial(),Status.ACTIVE.toString());
            Optional<ShoeSole> optionalShoeSole = shoeSoleRepository.findByIdAndStatus(productRequest.getIdShoeSole(),Status.ACTIVE.toString());
            if (optionalBrand.isEmpty()){
                throw new RuntimeException("IdBrand: "+productRequest.getIdBrand()+" không tồn tại trong hệ thống");
            }
            if (optionalCategory.isEmpty()){
                throw new RuntimeException("IdCategory: "+productRequest.getIdCategory()+" không tồn tại trong hệ thống");
            }
            if (optionalMaterial.isEmpty()){
                throw new RuntimeException("IdMaterial: "+productRequest.getIdMaterial()+" không tồn tại trong hệ thống");
            }
            if (optionalShoeSole.isEmpty()){
                throw new RuntimeException("IdShoeSole: "+productRequest.getIdShoeSole()+" không tồn tại trong hệ thống");
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
            boolean checkProductDetail = productDetailService.createProductDetail(saveProduct,productRequest.getProductDetailRequest());
            if (!checkProductDetail){
                throw new RuntimeException("Xảy ra lỗi khi thêm sản phẩm chi tiết");
            }

        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    public ProductImageResponse findImageByIdProduct(Long id){
        return productRepository.findImageByIdProduct(id);
    }

    public Map<Long, String> getProductNameById(List<Long> listId) {
        Map<Long, String> mapName = new HashMap<>();
        for (Long id : listId) {
            ProductDetail pd = productDetailRepository.findById(id).get();
            mapName.put(id, pd.getProduct().getName());
        }
        return mapName;
    }


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
        response.setGender(product.isGender()); // Thêm trường giới tính nếu có
        response.setStatus(product.getStatus());

        // Chép các thuộc tính từ Brand nếu có
        if (product.getBrand() != null) {
            response.setIdBrand(product.getBrand().getId());
            response.setNameBrand(product.getBrand().getName());
        }

        // Chép các thuộc tính từ Category nếu có
        if (product.getCategory() != null) {
            response.setIdCategory(product.getCategory().getId());
            response.setNameCategory(product.getCategory().getName());
        }

        // Chép các thuộc tính từ Material nếu có
        if (product.getMaterial() != null) {
            response.setIdMaterial(product.getMaterial().getId());
            response.setNameMaterial(product.getMaterial().getName());
        }

        // Chép các thuộc tính từ ShoeSole nếu có
        if (product.getShoeSole() != null) {
            response.setIdShoeSole(product.getShoeSole().getId());
            response.setNameShoeSole(product.getShoeSole().getName());
        }


        return response;
    }

    public Optional<Product> getProductById(Long id) {
        return productRepository.findById(id);
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
