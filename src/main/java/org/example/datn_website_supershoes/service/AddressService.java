package org.example.datn_website_supershoes.service;

import org.example.datn_website_supershoes.Enum.Status;
import org.example.datn_website_supershoes.dto.request.AddressRequest;
import org.example.datn_website_supershoes.model.Account;
import org.example.datn_website_supershoes.model.Address;
import org.example.datn_website_supershoes.repository.AccountRepository;
import org.example.datn_website_supershoes.repository.AddressRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AddressService {

    @Autowired
    AddressRepository addressRepository;

    @Autowired
    AccountRepository accountRepository;

    public Address createAddress(Long accountId,AddressRequest addressRequest){
        Account account = accountRepository.findById(accountId).orElseGet( () -> {
            throw new RuntimeException("Account not exits");
        });
        Address address = convertAddressRequesDTO(addressRequest);
        address.setAccount(account);
        address.setStatus(Status.ACTIVE.toString());
        return addressRepository.save(address);
    }
    public Address updateAddress(Long id, AddressRequest addressRequest){
        Address address = addressRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Address not found!"));
        address.setAddress(addressRequest.getAddress());
        address.setName(addressRequest.getName());
        address.setPhoneNumber(addressRequest.getPhoneNumber());
        return addressRepository.save(address);
    }

    public List<Address> getAllAddressActive(){
        return addressRepository.findAllByStatus(Status.ACTIVE.toString());
    }

    public void deleteAddress(Long id){
        Address address = addressRepository.findById(id)
                .orElseThrow(() -> {
                    throw new RuntimeException("Address not found!");
                });
        address.setStatus(Status.INACTIVE.toString());
        addressRepository.save(address);
    }

    public Address convertAddressRequesDTO(AddressRequest addressRequest){
        return Address.builder()
                .name(addressRequest.getName())
                .address(addressRequest.getAddress())
                .phoneNumber(addressRequest.getPhoneNumber())
                .build();
    }


}
