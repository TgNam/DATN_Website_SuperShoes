package org.example.datn_website_supershoes.service;

import org.example.datn_website_supershoes.Enum.Status;
import org.example.datn_website_supershoes.dto.request.CartRequest;
import org.example.datn_website_supershoes.dto.request.SizeRequest;
import org.example.datn_website_supershoes.dto.response.CartResponse;
import org.example.datn_website_supershoes.model.Account;
import org.example.datn_website_supershoes.model.Cart;
import org.example.datn_website_supershoes.model.Size;
import org.example.datn_website_supershoes.repository.AccountRepository;
import org.example.datn_website_supershoes.repository.CartRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CartService {

    @Autowired
    private CartRepository cartRepository;
    @Autowired
    AccountRepository accountRepository;

    public CartResponse getCartResponseByAccountId(Long accountId) {
        return cartRepository.CartResponse(accountId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy giỏ hàng cho tài khoản này!"));
    }
    public Cart createCart(CartRequest cartRequest){
        return cartRepository.save(convertCartRequestDTO(cartRequest));
    }
    public void deleteCartById(Long id) {
        if (cartRepository.existsById(id)) {
            cartRepository.deleteById(id);
        } else {
            throw new RuntimeException("Cart với id " + id + " không tồn tại.");
        }
    }
    public Cart convertCartRequestDTO(CartRequest cartRequest){
        Account account = accountRepository.findById(cartRequest.getIdAccount())
                .orElseThrow(() -> new RuntimeException("Không tìm tài khoản này!"));
        Cart cart = Cart.builder()
                .account(account)
                .build();
        cart.setCreatedBy(account.getName());
        cart.setUpdatedBy(account.getName());
        cart.setStatus(Status.ACTIVE.toString());
        return cart;
    }
}

