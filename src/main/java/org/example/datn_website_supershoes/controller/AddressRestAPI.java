package org.example.datn_website_supershoes.controller;

import jakarta.validation.constraints.NotNull;
import org.example.datn_website_supershoes.dto.request.AddressRequest;
import org.example.datn_website_supershoes.dto.response.Response;
import org.example.datn_website_supershoes.service.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/address")
public class AddressRestAPI {

    @Autowired
    AddressService addressService;

    @PostMapping("/add/{accountId}")
    public ResponseEntity<?> addAddForAccount(@PathVariable("accountId") Long accountId,
                                              @RequestBody @NotNull AddressRequest addressRequest){
        try {
            return ResponseEntity.ok().body(addressService.createAddress(accountId,addressRequest));
        }catch (RuntimeException e){
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(Response.builder()
                            .status(HttpStatus.NOT_FOUND.toString())
                            .mess(e.getMessage())
                            .build()
                    );
        }
    }
    @GetMapping("/get-addresses")
    public ResponseEntity<?> getAddresses(){
        return ResponseEntity.ok().body(addressService.getAllAddressActive());
    }

    @PutMapping("/update/{addressId}")
    public ResponseEntity<?> updateAddress(@PathVariable("addressId") Long addressId,
                                              @RequestBody @NotNull AddressRequest addressRequest){
        try {
            return ResponseEntity.ok().body(addressService.updateAddress(addressId,addressRequest));
        }catch (RuntimeException e){
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(Response.builder()
                            .status(HttpStatus.NOT_FOUND.toString())
                            .mess(e.getMessage())
                            .build()
                    );
        }
    }
    @DeleteMapping("/delete/{addressId}")
    public ResponseEntity<?> deleteAddress(@PathVariable("addressId") Long addressId){
        try {
            addressService.deleteAddress(addressId);
            return ResponseEntity.ok()
                    .body(
                            Response.builder()
                                    .status(HttpStatus.OK.toString())
                                    .mess("Delete success")
                                    .build()

                    );
        }catch (RuntimeException e){
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(Response.builder()
                            .status(HttpStatus.NOT_FOUND.toString())
                            .mess(e.getMessage())
                            .build()
                    );
        }
    }

}
