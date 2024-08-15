package org.example.datn_website_supershoes.service;

import org.example.datn_website_supershoes.model.BillDetail;
import org.example.datn_website_supershoes.model.Size;
import org.example.datn_website_supershoes.repository.BillDetailRepository;
import org.example.datn_website_supershoes.repository.SizeReposiotry;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SizeService {

    @Autowired
    private SizeReposiotry sizeReposiotry;

    public Size createSize(Size size) {
        return sizeReposiotry.save(size);
    }

    public List<Size> getAllSize() {
        return sizeReposiotry.findAll();
    }

    public Optional<Size> getSizeById(Long id) {
        return sizeReposiotry.findById(id);
    }

    public Size updateSize(Long id, Size size) {
        Size existingSize = sizeReposiotry.findById(id)
                .orElseThrow(() -> new RuntimeException("BillDetail not found"));

        String[] ignoredProperties = {"id", "createdAt", "createdBy"};
        BeanUtils.copyProperties(size, existingSize, ignoredProperties);

        // Update associations if needed
        if (size.getProductDetail() != null) {
            existingSize.setProductDetail(size.getProductDetail());
        }

        return sizeReposiotry.save(existingSize);
    }

    public void deleteSize(Long id) {
        sizeReposiotry.deleteById(id);
    }

}
