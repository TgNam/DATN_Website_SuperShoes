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
import org.example.datn_website_supershoes.webconfig.NotificationController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class CartDetailService {

    @Autowired
    CartDetailRepository cartDetailRepository;
    @Autowired
    CartRepository cartRepository;

    @Autowired
    CartService cartService;

    @Autowired
    ProductDetailService productDetailService;

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    ProductDetailRepository productDetailRepository;
    @Autowired
    NotificationController notificationController;

    @Transactional
    public void findCartDetailsOlderThanOneDay() {
        List<CartDetail> cartDetails = cartDetailRepository.findCartDetailOlderThanOneDay();
        if (!cartDetails.isEmpty()) {
            cartDetailRepository.deleteAll(cartDetails);
            notificationController.sendNotification();
        }
    }

    public Cart addToCart(CartDetailRequest cartDetailRequest, long accountId) {
        Cart cart = cartService.getCartByAccountId(accountId);

        ProductDetail productDetail = productDetailRepository.findById(cartDetailRequest.getIdProductDetail())
                .orElseThrow(() -> new RuntimeException("Sản phẩm không còn hàng!"));
        if (productDetail.getStatus().equals(Status.INACTIVE.toString())) {
            throw new RuntimeException("Sản phẩm "+productDetail.getProduct().getName()+" đã ngừng bán!");
        }

        List<CartDetail> cartDetails = cart.getCartDetails();
        if (cartDetails == null) {
            cartDetails = new ArrayList<>();
            cart.setCartDetails(cartDetails);
        }
        CartDetail existingCartDetail = cartDetails.stream()
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
        notificationController.sendNotification();
        return cartRepository.save(cart);
    }
    public Cart addCartLocalToCart(CartDetailRequest cartDetailRequest, long accountId) {
        Cart cart = cartService.getCartByAccountId(accountId);

        ProductDetail productDetail = productDetailRepository.findById(cartDetailRequest.getIdProductDetail())
                .orElseThrow(() -> new RuntimeException("Sản phẩm không còn hàng!"));
        if (productDetail.getStatus().equals(Status.INACTIVE.toString())) {
            throw new RuntimeException("Sản phẩm "+productDetail.getProduct().getName()+" đã ngừng bán!");
        }

        List<CartDetail> cartDetails = cart.getCartDetails();
        if (cartDetails == null) {
            cartDetails = new ArrayList<>();
            cart.setCartDetails(cartDetails);
        }
        CartDetail existingCartDetail = cartDetails.stream()
                .filter(cd -> Objects.equals(cd.getProductDetail().getId(), productDetail.getId()))
                .findFirst()
                .orElse(null);
        if (existingCartDetail != null) {
            if (existingCartDetail.getQuantity() + cartDetailRequest.getQuantity() > productDetail.getQuantity()) {
                existingCartDetail.setQuantity(productDetail.getQuantity());
            }else{
                existingCartDetail.setQuantity(existingCartDetail.getQuantity() + cartDetailRequest.getQuantity());
            }
            cartDetailRepository.save(existingCartDetail);
        } else {
            CartDetail newCartDetail = new CartDetail();
            newCartDetail.setCart(cart);
            newCartDetail.setProductDetail(productDetail);
            if (cartDetailRequest.getQuantity() > productDetail.getQuantity()) {
                newCartDetail.setQuantity(productDetail.getQuantity());
            }else{
                newCartDetail.setQuantity(cartDetailRequest.getQuantity());
            }
            cart.getCartDetails().add(newCartDetail);
            cartDetailRepository.save(newCartDetail);
        }
        notificationController.sendNotification();
        return cartRepository.save(cart);
    }

    @Transactional
    public List<CartDetailProductDetailResponse> getCartDetailByAccountId(long accountId) {
        ensureCartExistsForAccount(accountId);
        List<CartDetailProductDetailResponse> cartDetailResponses = cartDetailRepository.findCartDetailByIdAccount(accountId);
        // Kiểm tra và cập nhật số lượng nếu cần
        boolean isUpdated = updateCartDetails(cartDetailResponses);

        return isUpdated ? cartDetailRepository.findCartDetailByIdAccount(accountId) : cartDetailResponses;
    }

    @Transactional
    public List<CartDetailProductDetailResponse> getCartDetailByAccountIdAndIdCartDetail(long accountId, List<Long> idCartDetail) {
        ensureCartExistsForAccount(accountId);
        List<CartDetailProductDetailResponse> cartDetailResponses = cartDetailRepository.findCartDetailByIdAccountAndIdCartDetail(accountId, idCartDetail);

        // Kiểm tra và cập nhật số lượng nếu cần
        boolean isUpdated = updateCartDetails(cartDetailResponses);

        return isUpdated ? cartDetailRepository.findCartDetailByIdAccountAndIdCartDetail(accountId, idCartDetail) : cartDetailResponses;
    }

    private void ensureCartExistsForAccount(long accountId) {
        cartRepository.findByAccount_Id(accountId).orElseGet(() -> {
            Account account = accountRepository.findById(accountId)
                    .orElseThrow(() -> new RuntimeException("Tài khoản không tồn tại trong hệ thống!"));
            return cartRepository.save(Cart.builder().account(account).build());
        });
    }

    private boolean updateCartDetails(List<CartDetailProductDetailResponse> cartDetailResponses) {
        boolean isUpdated = false;

        for (CartDetailProductDetailResponse response : cartDetailResponses) {
            if (response.getQuantityCartDetail() > response.getQuantityProductDetail()) {

                Optional<CartDetail> optionalCartDetail = cartDetailRepository.findById(response.getIdCartDetail());

                optionalCartDetail.ifPresent(cartDetail -> {
                    if (response.getQuantityProductDetail() > 0) {

                        cartDetail.setQuantity(response.getQuantityProductDetail());
                        cartDetailRepository.save(cartDetail);
                    } else {

                        cartDetailRepository.delete(cartDetail);
                    }
                });

                isUpdated = true;
            }
        }

        return isUpdated;
    }


    @Transactional
    public CartDetail plusCartDetail(Long idCartDetail) {
        Optional<CartDetail> optionalCartDetail = cartDetailRepository.findById(idCartDetail);
        if (optionalCartDetail.isEmpty()) {
            throw new RuntimeException("Giỏ hàng không tồn tại!");
        }
        Optional<ProductDetail> optionalProductDetail = productDetailRepository.findByIdAndAndStatus(optionalCartDetail.get().getProductDetail().getId(), Status.ACTIVE.toString());
        if (optionalProductDetail.isEmpty()) {
            throw new RuntimeException("Sản phẩm không tồn tại!");
        }
        if (optionalProductDetail.get().getQuantity() <= 0) {
            throw new RuntimeException("Sản phẩm đã hết hàng!");
        }

        int newQuantity = optionalCartDetail.get().getQuantity() + 1;

        if (newQuantity > optionalProductDetail.get().getQuantity()) {
            throw new RuntimeException("Đã vượt quá số lượng hàng trong kho!");
        }
        optionalCartDetail.get().setQuantity(newQuantity);
        CartDetail detail = cartDetailRepository.save(optionalCartDetail.get());
        notificationController.sendNotification();
        return detail;
    }
    @Transactional
    public CartDetail subtractCartDetail(Long idCartDetail) {
        Optional<CartDetail> optionalCartDetail = cartDetailRepository.findById(idCartDetail);
        if (optionalCartDetail.isEmpty()) {
            throw new RuntimeException("Giỏ hàng không tồn tại!");
        }
        int newQuantity = optionalCartDetail.get().getQuantity() - 1;
        if (newQuantity <= 0) {
            throw new RuntimeException("Cần có tổi thiểu một sản phẩm!");
        }
        optionalCartDetail.get().setQuantity(newQuantity);
        CartDetail detail = cartDetailRepository.save(optionalCartDetail.get());
        notificationController.sendNotification();
        return detail;
    }
    @Transactional
    public void deleteCartDetail(Long idCartDetail) {
        Optional<CartDetail> optionalCartDetail = cartDetailRepository.findById(idCartDetail);
        if (optionalCartDetail.isEmpty()) {
            throw new RuntimeException("Giỏ hàng không tồn tại!");
        }
        cartDetailRepository.delete(optionalCartDetail.get());
        notificationController.sendNotification();
    }
}
