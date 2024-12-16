package org.example.datn_website_supershoes.repository;

import org.example.datn_website_supershoes.dto.response.AddressResponse;
import org.example.datn_website_supershoes.model.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface AddressRepository extends JpaRepository<Address,Long> {
    Optional<Address> findById(Long id);
    @Query(value = """
    SELECT new org.example.datn_website_supershoes.dto.response.AddressResponse(
    ad.id, ad.codeCity, ad.codeDistrict,ad.codeWard, ad.address, ac.id,ac.name,ac.phoneNumber,ac.gender,ac.birthday, ad.type,ad.status) 
    FROM Address ad
    JOIN ad.account ac
    WHERE ac.id = :idAccount
""")
    List<AddressResponse> listAddressResponseByidAccount(@Param("idAccount") Long idAccount);
    @Query(value = """
    SELECT new org.example.datn_website_supershoes.dto.response.AddressResponse(
    ad.id, ad.codeCity, ad.codeDistrict,ad.codeWard, ad.address, ac.id,ac.name,ac.phoneNumber,ac.gender,ac.birthday, ad.type,ad.status) 
    FROM Address ad
    JOIN ad.account ac
    WHERE ac.id = :idAccount and ad.type = 1
""")
    List<AddressResponse> listAddressResponseByidAccountAndType(@Param("idAccount") Long idAccount);

    @Query(value = """
    SELECT new org.example.datn_website_supershoes.dto.response.AddressResponse(
    ad.id, ad.codeCity, ad.codeDistrict,ad.codeWard, ad.address, ac.id,ac.name,ac.phoneNumber,ac.gender,ac.birthday, ad.type,ad.status) 
    FROM Address ad
    RIGHT JOIN ad.account ac ON ad.type = 1
    WHERE ac.status=:status
""")
    List<AddressResponse> listAccountAddressResponseByType(@Param("status") String status);
    @Query(value = """
    SELECT new org.example.datn_website_supershoes.dto.response.AddressResponse(
    ad.id, ad.codeCity, ad.codeDistrict,ad.codeWard, ad.address, ac.id,ac.name,ac.phoneNumber,ac.gender,ac.birthday, ad.type,ad.status) 
    FROM Address ad
    RIGHT JOIN ad.account ac ON ad.type = 1
    WHERE ac.status=:status AND ac.id=:idAccount
""")
    Optional<AddressResponse> findAccountAddressResponseByTypeAndIdAccount(@Param("status") String status,@Param("idAccount") Long idAccount);
    @Query(value = """
    SELECT new org.example.datn_website_supershoes.dto.response.AddressResponse(
    ad.id, ad.codeCity, ad.codeDistrict,ad.codeWard, ad.address, ac.id,ac.name,ac.phoneNumber,ac.gender,ac.birthday, ad.type,ad.status) 
    FROM Address ad
    RIGHT JOIN ad.account ac ON ad.type = 1
    WHERE ac.id=:idAccount
""")
    Optional<AddressResponse> findAccountAddressResponseByIdAccount(@Param("idAccount") Long idAccount);
}
