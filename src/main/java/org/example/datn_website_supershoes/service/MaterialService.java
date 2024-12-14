package org.example.datn_website_supershoes.service;

import org.example.datn_website_supershoes.Enum.Status;
import org.example.datn_website_supershoes.dto.request.MaterialRequest;
import org.example.datn_website_supershoes.dto.response.MaterialResponse;
import org.example.datn_website_supershoes.model.Material;
import org.example.datn_website_supershoes.repository.MaterialRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MaterialService {
    @Autowired
    MaterialRepository materialRepository;

    public List<MaterialResponse> findAllMaterial() {
        return materialRepository.findAllMaterial();
    }

    public List<MaterialResponse> findByStatus() {
        return materialRepository.findByStatus(Status.ACTIVE.toString());
    }

    public Material createMaterial(MaterialRequest materialRequest) {
        Optional<Material> material = materialRepository.findByName(materialRequest.getName());
        if (material.isPresent()) {
            throw new RuntimeException("Chất liệu " + materialRequest.getName() + " đã tồn tại");
        }
        return materialRepository.save(convertMaterialRequestDTO(materialRequest));
    }

    public Material updateStatus(Long id, boolean aBoolean) {
        Optional<Material> material = materialRepository.findById(id);
        if (!material.isPresent()) {
            throw new RuntimeException("Tài nguyên chất liệu không tồn tại trong hệ thống");
        }
        String newStatus = aBoolean ? Status.ACTIVE.toString() : Status.INACTIVE.toString();
        material.get().setStatus(newStatus);
        return materialRepository.save(material.get());
    }

    public Material convertMaterialRequestDTO(MaterialRequest MaterialRequest) {
        Material material = Material.builder()
                .name(MaterialRequest.getName())
                .build();
        material.setStatus(Status.ACTIVE.toString());
        return material;
    }

}
