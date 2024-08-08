package org.example.datn_website_supershoes.service;

import org.example.datn_website_supershoes.Enum.Status;
import org.example.datn_website_supershoes.dto.response.CartResponse;
import org.example.datn_website_supershoes.model.Cart;
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

    public Cart createCart(Cart cart) {
        return cartRepository.save(cart);
    }

    public List<CartResponse> getAllCarts() {
        return cartRepository.listCartResponseByStatus(Status.ACTIVE.toString());
    }

    public Optional<Cart> getCartById(Long id) {
        return cartRepository.findById(id);
    }

    public Cart updateCart(Long id, Cart cart) {
        Cart existingCart = cartRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cart not found"));

        String[] ignoredProperties = {"id", "createdAt", "createdBy"};
        BeanUtils.copyProperties(cart, existingCart, ignoredProperties);

        if (cart.getAccount() != null) {
            existingCart.setAccount(cart.getAccount());
        }
        if (cart.getCartDetails() != null) {
            existingCart.setCartDetails(cart.getCartDetails());
        }

        return cartRepository.save(cart);
    }

    public void deleteCart(Long id) {
        cartRepository.deleteById(id);
    }
}

