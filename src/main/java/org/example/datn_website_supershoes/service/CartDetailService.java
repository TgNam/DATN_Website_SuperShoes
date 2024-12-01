package org.example.datn_website_supershoes.service;

import org.example.datn_website_supershoes.Enum.Status;
import org.example.datn_website_supershoes.dto.request.CartDetailRequest;
import org.example.datn_website_supershoes.dto.response.CartDetailProductDetailResponse;
import org.example.datn_website_supershoes.dto.response.CartDetailResponse;
import org.example.datn_website_supershoes.model.Account;
import org.example.datn_website_supershoes.model.Cart;
import org.example.datn_website_supershoes.model.CartDetail;
import org.example.datn_website_supershoes.model.ProductDetail;
import org.example.datn_website_supershoes.repository.AccountRepository;
import org.example.datn_website_supershoes.repository.CartDetailRepository;
import org.example.datn_website_supershoes.repository.CartRepository;
import org.example.datn_website_supershoes.repository.ProductDetailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class CartDetailService {

    @Autowired
    private CartDetailRepository cartDetailRepository;
    @Autowired
    private CartRepository cartRepository;

    @Autowired
    CartService cartService;

    @Autowired
    ProductDetailService productDetailService;

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    private ProductDetailRepository productDetailRepository;

    public List<String> listCodeCartByIdCart(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("ID không được để trống.");
        }
        return cartDetailRepository.listCodeCartByIdCart(id);
    }

    public List<CartDetailResponse> listCartDetailResponseById(Long id, String codeCart) {
        if (id == null) {
            throw new IllegalArgumentException("ID không được để trống.");
        } else if (codeCart == null) {
            throw new IllegalArgumentException("codeCart không được để trống.");
        }
        return cartDetailRepository.listCartDetailResponseById(id, codeCart);
    }

    public void deleteByIdCartDetail(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("ID không được để trống.");
        } else {
            cartDetailRepository.deleteById(id);
        }
    }

    public Cart addToCart(CartDetailRequest cartDetailRequest, long accountId) {
        Cart cart = cartService.getCartByAccountId(accountId);
        ProductDetail productDetail = productDetailService.getById(cartDetailRequest.getIdProductDetail());
        CartDetail existingCartDetail = cart.getCartDetails().stream()
                .filter(cd -> Objects.equals(cd.getProductDetail().getId(), productDetail.getId()))
                .findFirst()
                .orElse(null);
        if (existingCartDetail != null) {
            if (existingCartDetail.getQuantity() + cartDetailRequest.getQuantity() > productDetail.getQuantity()) {
                throw new RuntimeException("Đã có " + existingCartDetail.getQuantity() + " sản phẩm trong giỏ hàng!");
            }
            existingCartDetail.setQuantity(existingCartDetail.getQuantity() + cartDetailRequest.getQuantity());
            cartDetailRepository.save(existingCartDetail);
        } else {
            if (cartDetailRequest.getQuantity() > productDetail.getQuantity()) {
                throw new RuntimeException("Số lượng mua vượt quá số lượng sản phẩm trong kho!");
            }
            CartDetail newCartDetail = new CartDetail();
            newCartDetail.setCart(cart);
            newCartDetail.setProductDetail(productDetail);
            newCartDetail.setQuantity(cartDetailRequest.getQuantity());
            cart.getCartDetails().add(newCartDetail);
            cartDetailRepository.save(newCartDetail);
        }
        return cartRepository.save(cart);
    }

    public CartDetail convertCartDetailRequestDTO(CartDetailRequest cartDetailRequest) {
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

    public List<CartDetailProductDetailResponse> getCartDetailByAccountId(long accountId) {
        Optional<Cart> cartOptional = cartRepository.findByAccount_Id(accountId);
        if (cartOptional.isPresent()) {
            List<CartDetailProductDetailResponse> cartDetailProductDetailResponse = cartDetailRepository.findCartDetailByIdAccount(accountId);
            return cartDetailProductDetailResponse;
        }
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new RuntimeException("Account not found!"));
        cartRepository.save(Cart.builder()
                .account(account)
                .build());
        List<CartDetailProductDetailResponse> cartDetailProductDetailResponse = cartDetailRepository.findCartDetailByIdAccount(accountId);
        return cartDetailProductDetailResponse;
    }

    public List<CartDetailProductDetailResponse> getCartDetailByAccountIdAndIdCartDetail(long accountId, List<Long> idCartDetail) {
        Optional<Cart> cartOptional = cartRepository.findByAccount_Id(accountId);
        if (cartOptional.isPresent()) {
            List<CartDetailProductDetailResponse> cartDetailProductDetailResponse = cartDetailRepository.findCartDetailByIdAccountAndIdCartDetail(accountId, idCartDetail);
            return cartDetailProductDetailResponse;
        }
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new RuntimeException("Account not found!"));
        cartRepository.save(Cart.builder()
                .account(account)
                .build());
        List<CartDetailProductDetailResponse> cartDetailProductDetailResponse = cartDetailRepository.findCartDetailByIdAccountAndIdCartDetail(accountId, idCartDetail);
        ;
        return cartDetailProductDetailResponse;
    }
}
