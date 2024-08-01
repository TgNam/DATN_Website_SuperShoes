package org.example.datn_website_supershoes.controller;

import jakarta.validation.constraints.NotNull;
import org.example.datn_website_supershoes.dto.request.AccountRequest;
import org.example.datn_website_supershoes.dto.request.GuestRequest;
import org.example.datn_website_supershoes.dto.response.Response;
import org.example.datn_website_supershoes.model.Account;
import org.example.datn_website_supershoes.model.Guest;
import org.example.datn_website_supershoes.repository.GuestRepository;
import org.example.datn_website_supershoes.service.AccountService;
import org.example.datn_website_supershoes.service.GuestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/guest")
public class GuestRestAPI {
    @Autowired
    GuestService guestService;

    @PostMapping("/create")
    public ResponseEntity<?> createAccount(@RequestBody @NotNull GuestRequest guestRequest){
        try {
            return ResponseEntity.ok(guestService.addGuest(guestRequest));
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

    @PutMapping("update/{id}")
    public ResponseEntity<?> updateAccount(@PathVariable("id") long id,
                                           @RequestBody GuestRequest guestRequest){
        try {
            return ResponseEntity.ok(guestService.updateGuest(id,guestRequest));
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

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") Long id){
        try {
            guestService.deleteGuest(id);
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(Response.builder()
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

    @GetMapping("/list-guest")
    public List<Guest> getAllAccount(){
        return guestService.getGuestActive();
    }
}
