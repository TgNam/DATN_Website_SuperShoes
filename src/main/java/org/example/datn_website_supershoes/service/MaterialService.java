package org.example.datn_website_supershoes.service;

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

    public Material createMaterial(Material material) {
        return materialRepository.save(material);
    }

    public List<Material> getAllMaterial() {
        return materialRepository.findAll();
    }

    public Optional<Material> getMaterialById(Long id) {
        return materialRepository.findById(id);
    }

    public Material updateMaterial(Long id, Material material) {
        Material existingMaterial = materialRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("BillDetail not found"));

        String[] ignoredProperties = {"id", "createdAt", "createdBy"};
        BeanUtils.copyProperties(material, existingMaterial, ignoredProperties);

        // Update associations if needed
        if (material.getProducts() != null) {
            existingMaterial.setProducts(material.getProducts());
        }

        return materialRepository.save(existingMaterial);
    }

    public void deleteMaterial(Long id) {
        materialRepository.deleteById(id);
    }

}
