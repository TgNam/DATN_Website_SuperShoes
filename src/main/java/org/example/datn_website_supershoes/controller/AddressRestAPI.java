package org.example.datn_website_supershoes.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.example.datn_website_supershoes.dto.request.AddressRequest;
import org.example.datn_website_supershoes.dto.response.AddressResponse;
import org.example.datn_website_supershoes.dto.response.Response;
import org.example.datn_website_supershoes.model.Address;
import org.example.datn_website_supershoes.service.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<?> addAddForAccount(@RequestBody @Valid AddressRequest addressRequest, BindingResult result) {
        try {
            if (result.hasErrors()) {
                List<String> errors = result.getAllErrors().stream()
                        .map(error -> error.getDefaultMessage())
                        .collect(Collectors.toList());
                return ResponseEntity.badRequest().body(errors);
            }
            return ResponseEntity.ok().body(addressService.createAddress(addressRequest));
        } catch (RuntimeException e) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(Response.builder()
                            .status(HttpStatus.NOT_FOUND.toString())
                            .mess(e.getMessage())
                            .build()
                    );
        }
    }

    @GetMapping("/getAddressByidAccount")
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
        }catch (RuntimeException e) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(
                            Response.builder()
                                    .status(HttpStatus.NOT_FOUND.toString())
                                    .mess(e.getMessage())
                                    .build()
                    );
        }
    }
    @GetMapping("/findAddress")
    public ResponseEntity<?> findAddress(@RequestParam(value ="idAccount", required = false) Long idAccount) {
        try {
            if (idAccount == null) {
                return ResponseEntity.badRequest().body(
                        Response.builder()
                                .status(HttpStatus.BAD_REQUEST.toString())
                                .mess("Lỗi: ID địa chỉ không được để trống!")
                                .build()
                );
            }
            return ResponseEntity.ok().body(addressService.findAddressById(idAccount));
        }catch (RuntimeException e) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(
                            Response.builder()
                                    .status(HttpStatus.NOT_FOUND.toString())
                                    .mess(e.getMessage())
                                    .build()
                    );
        }
    }
    @PutMapping("/updateAddress")
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
        } catch (RuntimeException e) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(Response.builder()
                            .status(HttpStatus.NOT_FOUND.toString())
                            .mess(e.getMessage())
                            .build()
                    );
        }
    }
    @PutMapping("/updateAddressType")
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
        } catch (RuntimeException e) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(
                            Response.builder()
                                    .status(HttpStatus.NOT_FOUND.toString())
                                    .mess(e.getMessage())
                                    .build()
                    );
        }

    }
    @DeleteMapping("/delete")
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
        } catch (RuntimeException e) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(
                            Response.builder()
                                    .status(HttpStatus.NOT_FOUND.toString())
                                    .mess(e.getMessage())
                                    .build()
                    );
        }
    }
}
