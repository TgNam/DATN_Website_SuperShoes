package org.example.datn_website_supershoes.service;

import org.example.datn_website_supershoes.Enum.Status;
import org.example.datn_website_supershoes.dto.request.BrandRequest;
import org.example.datn_website_supershoes.dto.response.BrandResponse;
import org.example.datn_website_supershoes.model.Brand;
import org.example.datn_website_supershoes.repository.BrandRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BrandService {

    @Autowired
    BrandRepository brandRepository;

    public List<BrandResponse> findAllBrand(){
        return brandRepository.findAllBrand();
    }
    public List<BrandResponse> findByStatus(){
        return brandRepository.findByStatus(Status.ACTIVE.toString());
    }
    public Brand createBrand(BrandRequest brandRequest){
        Optional<Brand> brand = brandRepository.findByName(brandRequest.getName());
        if(brand.isPresent()){
            throw new RuntimeException("Hãng "+ brandRequest.getName() +" đã tồn tại");
        }
        return brandRepository.save(convertBrandRequestDTO(brandRequest));
    }
    public Brand updateStatus(Long id, boolean aBoolean){
            Optional<Brand> brand = brandRepository.findById(id);
            if(!brand.isPresent()){
                throw new RuntimeException("Tài nguyên hãng không tồn tại trong hệ thống");
            }
        String newStatus = aBoolean ? Status.ACTIVE.toString() : Status.INACTIVE.toString();
            brand.get().setStatus(newStatus);
            return  brandRepository.save(brand.get());
    }
    public Brand convertBrandRequestDTO(BrandRequest brandRequest){
        Brand brand = Brand.builder()
                .name(brandRequest.getName())
                .build();
        brand.setStatus(Status.ACTIVE.toString());
        return brand;
    }
}
