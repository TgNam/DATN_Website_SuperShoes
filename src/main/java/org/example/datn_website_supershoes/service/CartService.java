package org.example.datn_website_supershoes.service;

import org.example.datn_website_supershoes.Enum.Status;
import org.example.datn_website_supershoes.dto.request.CartRequest;
import org.example.datn_website_supershoes.dto.response.CartResponse;
import org.example.datn_website_supershoes.model.Account;
import org.example.datn_website_supershoes.model.Cart;
import org.example.datn_website_supershoes.repository.AccountRepository;
import org.example.datn_website_supershoes.repository.CartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CartService {

    @Autowired
    private CartRepository cartRepository;
    @Autowired
    AccountRepository accountRepository;

    public CartResponse getCartResponseByAccountId(Long accountId) {
        Optional<CartResponse> cartResponse = cartRepository.CartResponse(accountId);
        if (!cartResponse.isPresent()){
            CartRequest cartRequest = new CartRequest();
            cartRequest.setIdAccount(accountId);
            Cart cart = cartRepository.save(convertCartRequestDTO(cartRequest));
            CartResponse newCartResponse = new CartResponse();
            newCartResponse.setId(cart.getId());
            newCartResponse.setIdAccount(cart.getAccount().getId());
            return newCartResponse;
        }else{
            return cartResponse.get();
        }
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

