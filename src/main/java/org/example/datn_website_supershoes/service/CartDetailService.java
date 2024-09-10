package org.example.datn_website_supershoes.service;

import org.example.datn_website_supershoes.Enum.Status;
import org.example.datn_website_supershoes.dto.response.CartDetailResponse;
import org.example.datn_website_supershoes.model.CartDetail;
import org.example.datn_website_supershoes.repository.CartDetailRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CartDetailService {

    @Autowired
    private CartDetailRepository cartDetailRepository;

    public List<String> listCodeCartByIdCart(Long id){
        if (id == null) {
            throw new IllegalArgumentException("ID không được để trống.");
        }
        return cartDetailRepository.listCodeCartByIdCart(id);
    }
    public List<CartDetailResponse> listCartDetailResponseById( Long id, String codeCart){
        if (id == null) {
            throw new IllegalArgumentException("ID không được để trống.");
        } else if (codeCart == null) {
            throw new IllegalArgumentException("codeCart không được để trống.");
        }
        return cartDetailRepository.listCartDetailResponseById(id,codeCart);
    }

}
