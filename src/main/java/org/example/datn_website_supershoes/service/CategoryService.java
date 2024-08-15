package org.example.datn_website_supershoes.service;

import org.example.datn_website_supershoes.model.Category;
import org.example.datn_website_supershoes.repository.CategoryRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;

    public Category createCategory(Category category) {
        return categoryRepository.save(category);
    }

    public List<Category> getAllCategory() {
        return categoryRepository.findAll();
    }

    public Optional<Category> getCategoryById(Long id) {
        return categoryRepository.findById(id);
    }

    public Category updateCategory(Long id, Category category) {
        Category existingCategory = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("BillDetail not found"));

        String[] ignoredProperties = {"id", "createdAt", "createdBy"};
        BeanUtils.copyProperties(category, existingCategory, ignoredProperties);

        // Update associations if needed
        if (category.getProducts() != null) {
            existingCategory.setProducts(category.getProducts());
        }

        return categoryRepository.save(existingCategory);
    }

    public void deleteCategory(Long id) {
        categoryRepository.deleteById(id);
    }

}
