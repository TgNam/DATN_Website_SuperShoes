package org.example.datn_website_supershoes.controller;

import jakarta.validation.Valid;
import org.example.datn_website_supershoes.dto.request.AddressRequest;
import org.example.datn_website_supershoes.dto.response.AddressResponse;
import org.example.datn_website_supershoes.dto.response.Response;
import org.example.datn_website_supershoes.service.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/address")
public class AddressRestAPI {

    @Autowired
    AddressService addressService;

    @PostMapping("/createAddress")
    @PreAuthorize("hasAnyRole('ADMIN','EMPLOYEE','CUSTOMER')")
    public ResponseEntity<?> addAddForAccount(@RequestBody @Valid AddressRequest addressRequest, BindingResult result) {
        try {
            if (result.hasErrors()) {
                List<String> errors = result.getAllErrors().stream()
                        .map(error -> error.getDefaultMessage())
                        .collect(Collectors.toList());
                return ResponseEntity.badRequest().body(errors);
            }
            return ResponseEntity.ok().body(addressService.createAddress(addressRequest));
        } catch (RuntimeException e){
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body(Response.builder()
                            .status(HttpStatus.CONFLICT.toString())
                            .mess(e.getMessage())
                            .build()
                    );
        }
    }

    @GetMapping("/getAddressByidAccount")
    @PreAuthorize("hasAnyRole('ADMIN','EMPLOYEE','CUSTOMER')")
    public ResponseEntity<?> getAddresses(@RequestParam(value ="idAccount", required = false) Long idAccount) {
        try {
            if (idAccount == null) {
                return ResponseEntity.badRequest().body(
                        Response.builder()
                                .status(HttpStatus.BAD_REQUEST.toString())
                                .mess("Lỗi: ID tài khoản không được để trống!")
                                .build()
                );
            }
            return ResponseEntity.ok().body(addressService.listAddressResponseByidAccount(idAccount));
        }catch (RuntimeException e){
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body(Response.builder()
                            .status(HttpStatus.CONFLICT.toString())
                            .mess(e.getMessage())
                            .build()
                    );
        }
    }
    @GetMapping("/findAddress")
    @PreAuthorize("hasAnyRole('ADMIN','EMPLOYEE','CUSTOMER')")
    public ResponseEntity<?> findAddress(@RequestParam(value ="idAddress", required = false) Long idAddress) {
        try {
            if (idAddress == null) {
                return ResponseEntity.badRequest().body(
                        Response.builder()
                                .status(HttpStatus.BAD_REQUEST.toString())
                                .mess("Lỗi: ID địa chỉ của tài khoản không được để trống!")
                                .build()
                );
            }
            return ResponseEntity.ok().body(addressService.findAddressById(idAddress));
        }catch (RuntimeException e){
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body(Response.builder()
                            .status(HttpStatus.CONFLICT.toString())
                            .mess(e.getMessage())
                            .build()
                    );
        }
    }
    @GetMapping("/findAccountAddress")
    @PreAuthorize("hasAnyRole('ADMIN','EMPLOYEE','CUSTOMER')")
    public ResponseEntity<?> findAccountAddress(@RequestParam(value ="idAccount", required = false) Long idAccount) {
        try {
            if (idAccount == null) {
                return ResponseEntity.badRequest().body(
                        Response.builder()
                                .status(HttpStatus.BAD_REQUEST.toString())
                                .mess("Lỗi: ID tài khoản không được để trống!")
                                .build()
                );
            }
            return ResponseEntity.ok().body(addressService.findAccountAddressResponseByTypeAndIdAccount(idAccount));
        }catch (RuntimeException e){
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body(Response.builder()
                            .status(HttpStatus.CONFLICT.toString())
                            .mess(e.getMessage())
                            .build()
                    );
        }
    }
    @GetMapping("/getAccountAddress")
    @PreAuthorize("hasAnyRole('ADMIN','EMPLOYEE','CUSTOMER')")
    public ResponseEntity<?> getAccountAddresses() {
        try {
            return ResponseEntity.ok().body(addressService.listAccountAddressResponseByType());
        }catch (RuntimeException e){
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body(Response.builder()
                            .status(HttpStatus.CONFLICT.toString())
                            .mess(e.getMessage())
                            .build()
                    );
        }
    }
    @GetMapping("/getAccountAddressSearch")
    @PreAuthorize("hasAnyRole('ADMIN','EMPLOYEE','CUSTOMER')")
    public List<AddressResponse> getAccountAddressesSearch(
            @RequestParam("search") String search) {

        String searchLower = search.trim().toLowerCase();
        return addressService.listAccountAddressResponseByType().stream()
                .filter(addressRequest -> {
                    String accountName = addressRequest.getNameAccount().toLowerCase();
                    String accountPhone = addressRequest.getPhoneNumber().toLowerCase();
                    return accountName.contains(searchLower) || accountPhone.contains(searchLower);
                })
                .collect(Collectors.toList());
    }
    @PutMapping("/updateAddress")
    @PreAuthorize("hasAnyRole('ADMIN','EMPLOYEE','CUSTOMER')")
    public ResponseEntity<?> updateAddress(
            @RequestParam(value = "addressId", required = false) Long addressId,
            @RequestBody @Valid AddressRequest addressRequest,
            BindingResult result
    ) {
        try {
            // Kiểm tra nếu addressId bị trống (null)
            if (addressId == null) {
                return ResponseEntity.badRequest().body(
                        Response.builder()
                                .status(HttpStatus.BAD_REQUEST.toString())
                                .mess("Lỗi: ID địa chỉ không được để trống!")
                                .build()
                );
            }
            if (result.hasErrors()) {
                List<String> errors = result.getAllErrors().stream()
                        .map(error -> error.getDefaultMessage())
                        .collect(Collectors.toList());
                return ResponseEntity.badRequest().body(errors);
            }

            return ResponseEntity.ok().body(addressService.updateAddress(addressId, addressRequest));
        } catch (RuntimeException e){
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body(Response.builder()
                            .status(HttpStatus.CONFLICT.toString())
                            .mess(e.getMessage())
                            .build()
                    );
        }
    }
    @PutMapping("/updateAddressType")
    @PreAuthorize("hasAnyRole('ADMIN','EMPLOYEE','CUSTOMER')")
    public ResponseEntity<?> updateAddressType(@RequestParam(value = "addressId", required = false) Long addressId){
        try {
            // Kiểm tra nếu addressId bị trống (null)
            if (addressId == null) {
                return ResponseEntity.badRequest().body(
                        Response.builder()
                                .status(HttpStatus.BAD_REQUEST.toString())
                                .mess("Lỗi: ID địa chỉ không được để trống!")
                                .build()
                );
            }
            AddressResponse addressResponse = addressService.updateAddressType(addressId);
            return ResponseEntity.ok()
                    .body(addressResponse);
        } catch (RuntimeException e){
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body(Response.builder()
                            .status(HttpStatus.CONFLICT.toString())
                            .mess(e.getMessage())
                            .build()
                    );
        }

    }
    @DeleteMapping("/delete")
    @PreAuthorize("hasAnyRole('ADMIN','EMPLOYEE','CUSTOMER')")
    public ResponseEntity<?> deleteAddress(@RequestParam(value = "addressId", required = false) Long addressId) {
        try {
            // Kiểm tra nếu addressId bị trống (null)
            if (addressId == null) {
                return ResponseEntity.badRequest().body(
                        Response.builder()
                                .status(HttpStatus.BAD_REQUEST.toString())
                                .mess("Lỗi: ID địa chỉ không được để trống!")
                                .build()
                );
            }

            // Xóa địa chỉ nếu addressId hợp lệ
            return ResponseEntity.ok()
                    .body(addressService.deleteAddress(addressId));
        } catch (RuntimeException e){
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body(Response.builder()
                            .status(HttpStatus.CONFLICT.toString())
                            .mess(e.getMessage())
                            .build()
                    );
        }
    }
}
