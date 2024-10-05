package org.example.datn_website_supershoes.repository;

import org.example.datn_website_supershoes.dto.response.AccountResponse;
import org.example.datn_website_supershoes.dto.response.AddressResponse;
import org.example.datn_website_supershoes.model.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface AddressRepository extends JpaRepository<Address,Long> {
    Optional<Address> findById(Long id);
    @Query(value = """
    SELECT new org.example.datn_website_supershoes.dto.response.AddressResponse(ad.id, ad.name, ad.phoneNumber, ad.address, ac.id, ad.type,ad.status) 
    FROM Address ad
    JOIN ad.account ac
    WHERE ac.id = :idAccount
""")
    List<AddressResponse> listAddressResponseByidAccount(@Param("idAccount") Long idAccount);
    @Query(value = """
    SELECT new org.example.datn_website_supershoes.dto.response.AddressResponse(ad.id, ad.name, ad.phoneNumber, ad.address, ac.id, ad.type,ad.status) 
    FROM Address ad
    JOIN ad.account ac
    WHERE ac.id = :idAccount and ad.type = 1
""")
    List<AddressResponse> listAddressResponseByidAccountAndType(@Param("idAccount") Long idAccount);
}
