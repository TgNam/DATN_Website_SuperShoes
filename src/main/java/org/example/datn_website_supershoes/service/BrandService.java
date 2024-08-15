package org.example.datn_website_supershoes.service;

import org.example.datn_website_supershoes.model.Brand;
import org.example.datn_website_supershoes.model.Size;
import org.example.datn_website_supershoes.repository.BrandRepository;
import org.example.datn_website_supershoes.repository.SizeReposiotry;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BrandService {

    @Autowired
    private BrandRepository brandRepository;

    public Brand createBrand(Brand brand) {
        return brandRepository.save(brand);
    }

    public List<Brand> getAllBrand() {
        return brandRepository.findAll();
    }

    public Optional<Brand> getBrandById(Long id) {
        return brandRepository.findById(id);
    }

    public Brand updateBrand(Long id, Brand brand) {
        Brand existingbrand = brandRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("BillDetail not found"));

        String[] ignoredProperties = {"id", "createdAt", "createdBy"};
        BeanUtils.copyProperties(brand, existingbrand, ignoredProperties);

        // Update associations if needed
        if (brand.getProducts() != null) {
            existingbrand.setProducts(brand.getProducts());
        }

        return brandRepository.save(existingbrand);
    }

    public void deletebrand(Long id) {
        brandRepository.deleteById(id);
    }
}
