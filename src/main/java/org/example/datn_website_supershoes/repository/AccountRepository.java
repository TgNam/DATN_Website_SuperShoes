package org.example.datn_website_supershoes.repository;

import org.example.datn_website_supershoes.dto.response.AccountResponse;
import org.example.datn_website_supershoes.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {

    Optional<Account> findByEmail(String email);
    List<Account> findAllByStatus(String status);


    @Query(value = """
        SELECT new org.example.datn_website_supershoes.dto.response.AccountResponse(
            a.id, a.name, a.email, a.password, a.role, 
            ad.address, ad.phoneNumber, a.status) 
        FROM Account a 
        JOIN a.address ad
        WHERE a.status = :status 
          AND a.role = 'EMPLOYEE'
        """)
    List<AccountResponse> listAccountResponseByStatus(@Param("status") String status);


}
