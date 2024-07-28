package org.example.datn_website_supershoes.service;

import org.example.datn_website_supershoes.model.CartDetail;
import org.example.datn_website_supershoes.repository.CartDetailRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CartDetailService {

    @Autowired
    private CartDetailRepository cartDetailRepository;

    public CartDetail createCartDetail(CartDetail cartDetail) {
        return cartDetailRepository.save(cartDetail);
    }

    public List<CartDetail> getAllCartDetails() {
        return cartDetailRepository.findAll();
    }

    public Optional<CartDetail> getCartDetailById(Long id) {
        return cartDetailRepository.findById(id);
    }

    public CartDetail updateCartDetail(Long id, CartDetail cartDetail) {
        CartDetail existingCartDetail = cartDetailRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("CartDetail not found"));

        String[] ignoredProperties = {"id", "createdAt", "createdBy"};
        BeanUtils.copyProperties(cartDetail, existingCartDetail, ignoredProperties);

        return cartDetailRepository.save(existingCartDetail);
    }

    public void deleteCartDetail(Long id) {
        cartDetailRepository.deleteById(id);
    }
}
