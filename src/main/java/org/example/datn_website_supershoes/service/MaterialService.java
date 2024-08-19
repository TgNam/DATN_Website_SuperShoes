package org.example.datn_website_supershoes.service;

import org.example.datn_website_supershoes.Enum.Status;
import org.example.datn_website_supershoes.dto.request.MaterialRequest;
import org.example.datn_website_supershoes.dto.response.MaterialResponse;
import org.example.datn_website_supershoes.model.Material;
import org.example.datn_website_supershoes.model.Material;
import org.example.datn_website_supershoes.repository.MaterialRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MaterialService {
    @Autowired
    private MaterialRepository materialRepository;

    public List<MaterialResponse> findByStatus(){
        return materialRepository.findByStatus();
    }
    public Material createMaterial(MaterialRequest materialRequest){
        Optional<Material> material = materialRepository.findByName(materialRequest.getName());
        if(material.isPresent()){
            throw new RuntimeException("Material "+ materialRequest.getName() +" đã tồn tại");
        }
        return materialRepository.save(convertMaterialRequestDTO(materialRequest));
    }
    public boolean updateStatus(Long id){
        try{
            Optional<Material> material = materialRepository.findById(id);
            if(!material.isPresent()){
                throw new RuntimeException("Material null");
            }
            String newStatus = material.get().getStatus().equals(Status.ACTIVE.toString())  ? "INACTIVE" : "ACTIVE";
            material.get().setStatus(newStatus);
            materialRepository.save(material.get());
            return true;
        }catch (Exception e){
            e.getMessage();
            System.out.println(e.getMessage());
            return false;
        }

    }
    public Material convertMaterialRequestDTO(MaterialRequest MaterialRequest){
        Material material = Material.builder()
                .name(MaterialRequest.getName())
                .build();
        material.setStatus(Status.ACTIVE.toString());
        return material;
    }

}
