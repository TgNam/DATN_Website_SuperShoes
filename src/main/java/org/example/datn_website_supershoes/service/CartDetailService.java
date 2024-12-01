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

    public  CartDetail plusCartDetail (Long idCartDetail, Long idProductDetail){
        Optional<CartDetail> optionalCartDetail = cartDetailRepository.findById(idCartDetail);
        if(optionalCartDetail.isEmpty()){
            throw new RuntimeException("Giỏ hàng không tồn tại!");
        }
        Optional<ProductDetail> optionalProductDetail = productDetailRepository.findByIdAndAndStatus(idProductDetail,Status.ACTIVE.toString());
        if(optionalProductDetail.isEmpty()){
            throw new RuntimeException("Sản phẩm không tồn tại!");
        }
        if(optionalProductDetail.get().getQuantity() <= 0){
            throw new RuntimeException("Sản phẩm đã hết hàng!");
        }

        int newQuantity = optionalCartDetail.get().getQuantity()+1;

        if(newQuantity > optionalProductDetail.get().getQuantity()){
            throw new RuntimeException("Đã vượt quá số lượng hàng trong kho!");
        }
        optionalCartDetail.get().setQuantity(newQuantity);
        return cartDetailRepository.save(optionalCartDetail.get());
    }
    public  CartDetail subtractCartDetail (Long idCartDetail){
        Optional<CartDetail> optionalCartDetail = cartDetailRepository.findById(idCartDetail);
        if(optionalCartDetail.isEmpty()){
            throw new RuntimeException("Giỏ hàng không tồn tại!");
        }
        int newQuantity = optionalCartDetail.get().getQuantity() - 1;
        if(newQuantity<=0){
            throw new RuntimeException("Cần có tổi thiểu một sản phẩm!");
        }
        optionalCartDetail.get().setQuantity(newQuantity);
        return cartDetailRepository.save(optionalCartDetail.get());
    }
    public void deleteCartDetail(Long idCartDetail){
        Optional<CartDetail> optionalCartDetail = cartDetailRepository.findById(idCartDetail);
        if(optionalCartDetail.isEmpty()){
            throw new RuntimeException("Giỏ hàng không tồn tại!");
        }
        cartDetailRepository.delete(optionalCartDetail.get());
    }
}
