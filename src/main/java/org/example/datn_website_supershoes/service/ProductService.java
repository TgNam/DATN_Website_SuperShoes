package org.example.datn_website_supershoes.service;

import jakarta.transaction.Transactional;
import org.example.datn_website_supershoes.Enum.Status;
import org.example.datn_website_supershoes.dto.request.ProductRequest;
import org.example.datn_website_supershoes.dto.response.ProductResponse;
import org.example.datn_website_supershoes.dto.response.VoucherResponse;
import org.example.datn_website_supershoes.model.Brand;
import org.example.datn_website_supershoes.model.Category;
import org.example.datn_website_supershoes.model.Material;
import org.example.datn_website_supershoes.model.Product;
import org.example.datn_website_supershoes.model.ProductDetail;
import org.example.datn_website_supershoes.model.ProductImage;
import org.example.datn_website_supershoes.model.ShoeSole;
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


import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;



    @Autowired
    private BrandRepository brandRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private MaterialRepository materialRepository;

    @Autowired
    private ShoeSoleRepository shoeSoleRepository;

    public Product createProduct1(Product product) {
        // Lưu đối tượng Product vào cơ sở dữ liệu
        Product savedProduct = productRepository.save(product);
        return  productRepository.save(product);
        // Chuyển đổi đối tượng đã lưu thành ProductResponse để trả về

    }


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
        if (product.getProductDetails() != null) {
            List<Long> productDetailIds = product.getProductDetails().stream()
                    .map(ProductDetail::getId)
                    .collect(Collectors.toList());
            response.setIdProductDetail(productDetailIds);

        }
        if (product.getProductDetails() != null && !product.getProductDetails().isEmpty()) {
            ProductDetail firstProductDetail = product.getProductDetails().get(0); // Lấy ProductDetail đầu tiên

            // Thiết lập quantity, price, description từ ProductDetail đầu tiên
            response.setQuantity(firstProductDetail.getQuantity() != null ? firstProductDetail.getQuantity() : 0);
            response.setPrice(firstProductDetail.getPrice() != null ? firstProductDetail.getPrice() : BigDecimal.ZERO);
            response.setDescription(firstProductDetail.getDescription());

            // Chuyển đổi danh sách ProductImage sang imageBytes nếu có
            if (firstProductDetail.getProductImage() != null) {
                response.setImageBytes(firstProductDetail.getProductImage().stream()
                        .map(ProductImage::getImageByte)
                        .collect(Collectors.toList()));
            }

            // Chuyển đổi thông tin Color nếu có
            if (firstProductDetail.getColor() != null) {
                response.setIdColor(firstProductDetail.getColor().getId());
                response.setNameColor(firstProductDetail.getColor().getName());
            }

            // Chuyển đổi thông tin Size nếu có
            if (firstProductDetail.getSize() != null) {
                response.setIdSize(firstProductDetail.getSize().getId());
                response.setNameSize(firstProductDetail.getSize().getName());
            }
        }


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


//    public List<ProductResponse> findProductRequests(){
//    return productRepository.findProductRequests();
//    }
}
