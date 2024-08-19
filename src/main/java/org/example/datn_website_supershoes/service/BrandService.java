package org.example.datn_website_supershoes.service;

import org.example.datn_website_supershoes.Enum.Status;
import org.example.datn_website_supershoes.dto.request.BrandRequest;
import org.example.datn_website_supershoes.dto.response.BrandResponse;
import org.example.datn_website_supershoes.model.Brand;
import org.example.datn_website_supershoes.model.Size;
import org.example.datn_website_supershoes.repository.BrandRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BrandService {

    @Autowired
    private BrandRepository brandRepository;

    public List<BrandResponse> findByStatus(){
        return brandRepository.findByStatus();
    }
    public Brand createBrand(BrandRequest brandRequest){
        Optional<Brand> brand = brandRepository.findByName(brandRequest.getName());
        if(brand.isPresent()){
            throw new RuntimeException("Hãng "+ brandRequest.getName() +" đã tồn tại");
        }
        return brandRepository.save(convertBrandRequestDTO(brandRequest));
    }
    public boolean updateStatus(Long id){
        try{
            Optional<Brand> brand = brandRepository.findById(id);
            if(!brand.isPresent()){
                throw new RuntimeException("Brand null");
            }
            String newStatus = brand.get().getStatus().equals(Status.ACTIVE.toString())  ? "INACTIVE" : "ACTIVE";
            brand.get().setStatus(newStatus);
            brandRepository.save(brand.get());
            return true;
        }catch (Exception e){
            e.getMessage();
            System.out.println(e.getMessage());
            return false;
        }

    }
    public Brand convertBrandRequestDTO(BrandRequest brandRequest){
        Brand brand = Brand.builder()
                .name(brandRequest.getName())
                .build();
        brand.setStatus(Status.ACTIVE.toString());
        return brand;
    }
}
