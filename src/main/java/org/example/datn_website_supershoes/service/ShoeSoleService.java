package org.example.datn_website_supershoes.service;

import org.example.datn_website_supershoes.model.Category;
import org.example.datn_website_supershoes.model.ShoeSole;
import org.example.datn_website_supershoes.repository.CategoryRepository;
import org.example.datn_website_supershoes.repository.ShoeSoleRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
@Service
public class ShoeSoleService {

    @Autowired
    private ShoeSoleRepository shoeSoleRepository;

    public ShoeSole createShoeSole(ShoeSole shoeSole) {
        return shoeSoleRepository.save(shoeSole);
    }

    public List<ShoeSole> getAllShoeSole() {
        return shoeSoleRepository.findAll();
    }

    public Optional<ShoeSole> getShoeSoleById(Long id) {
        return shoeSoleRepository.findById(id);
    }

    public ShoeSole updateShoeSole(Long id, ShoeSole shoeSole) {
        ShoeSole existingShoeSole = shoeSoleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("BillDetail not found"));

        String[] ignoredProperties = {"id", "createdAt", "createdBy"};
        BeanUtils.copyProperties(shoeSole, existingShoeSole, ignoredProperties);

        // Update associations if needed
        if (shoeSole.getProducts() != null) {
            existingShoeSole.setProducts(shoeSole.getProducts());
        }

        return shoeSoleRepository.save(existingShoeSole);
    }

    public void deleteShoeSole(Long id) {
        shoeSoleRepository.deleteById(id);
    }

}
