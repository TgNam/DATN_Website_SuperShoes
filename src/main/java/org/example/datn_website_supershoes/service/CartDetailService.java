package org.example.datn_website_supershoes.service;

import org.example.datn_website_supershoes.Enum.Status;
import org.example.datn_website_supershoes.dto.request.CartDetailRequest;
import org.example.datn_website_supershoes.dto.response.CartDetailResponse;
import org.example.datn_website_supershoes.model.Cart;
import org.example.datn_website_supershoes.model.CartDetail;
import org.example.datn_website_supershoes.model.ProductDetail;
import org.example.datn_website_supershoes.repository.CartDetailRepository;
import org.example.datn_website_supershoes.repository.CartRepository;
import org.example.datn_website_supershoes.repository.ProductDetailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CartDetailService {

    @Autowired
    private CartDetailRepository cartDetailRepository;
    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private ProductDetailRepository productDetailRepository;
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
    public void deleteByIdCartDetail(Long id){
        if (id == null) {
            throw new IllegalArgumentException("ID không được để trống.");
        }else{
            cartDetailRepository.deleteById(id);
        }
    }
    public void createCartDetail(CartDetailRequest cartDetailRequest){
        CartDetail cartDetail = convertCartDetailRequestDTO(cartDetailRequest);
        cartDetailRepository.save(cartDetail);
    }
    public CartDetail convertCartDetailRequestDTO(CartDetailRequest cartDetailRequest){
        Cart cart = cartRepository.findById(cartDetailRequest.getIdCart()).get();
        ProductDetail productDetail = productDetailRepository.findById(cartDetailRequest.getIdCart()).get();
        CartDetail cartDetail = CartDetail.builder()
                .codeCart(cartDetailRequest.getCodeCart())
                .quantity(cartDetailRequest.getQuantity())
                .cart(cart)
                .productDetail(productDetail)
                .build();
        cartDetail.setStatus(Status.ACTIVE.toString());
        return cartDetail;
    }
}
