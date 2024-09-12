package org.example.datn_website_supershoes.service;

import org.example.datn_website_supershoes.Enum.Status;
import org.example.datn_website_supershoes.dto.request.CategoryRequest;
import org.example.datn_website_supershoes.dto.response.CategoryResponse;
import org.example.datn_website_supershoes.model.Category;
import org.example.datn_website_supershoes.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;

    public List<CategoryResponse> findByStatus() {
        return categoryRepository.findByStatus();
    }

    public Category createCategory(CategoryRequest categoryRequest) {
        Optional<Category> category = categoryRepository.findByName(categoryRequest.getName());
        if (category.isPresent()) {
            throw new RuntimeException("Danh mục " + categoryRequest.getName() + " đã tồn tại");
        }
        return categoryRepository.save(convertCategoryRequestDTO(categoryRequest));
    }

    public boolean updateStatus(Long id) {
        try {
            Optional<Category> category = categoryRepository.findById(id);
            if (!category.isPresent()) {
                throw new RuntimeException("Category null");
            }
            String newStatus = category.get().getStatus().equals(Status.ACTIVE.toString()) ? "INACTIVE" : "ACTIVE";
            category.get().setStatus(newStatus);
            categoryRepository.save(category.get());
            return true;
        } catch (Exception e) {
            e.getMessage();
            System.out.println(e.getMessage());
            return false;
        }

    }

    public Category convertCategoryRequestDTO(CategoryRequest CategoryRequest) {
        Category category = Category.builder()
                .name(CategoryRequest.getName())
                .build();
        category.setStatus(Status.ACTIVE.toString());
        return category;
    }
}
