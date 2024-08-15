package org.example.datn_website_supershoes.service;

import org.example.datn_website_supershoes.model.Color;
import org.example.datn_website_supershoes.repository.ColorRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ColorService {

    @Autowired
    private ColorRepository colorRepository;

    public Color createColor(Color color) {
        return colorRepository.save(color);
    }

    public List<Color> getAllColor() {
        return colorRepository.findAll();
    }

    public Optional<Color> getColorById(Long id) {
        return colorRepository.findById(id);
    }

    public Color updateColor(Long id, Color brand) {
        Color existingbrand = colorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("BillDetail not found"));

        String[] ignoredProperties = {"id", "createdAt", "createdBy"};
        BeanUtils.copyProperties(brand, existingbrand, ignoredProperties);

        // Update associations if needed
        if (brand.getProductDetail() != null) {
            existingbrand.setProductDetail(brand.getProductDetail());
        }

        return colorRepository.save(existingbrand);
    }

    public void deleteColor(Long id) {
        colorRepository.deleteById(id);
    }
}
