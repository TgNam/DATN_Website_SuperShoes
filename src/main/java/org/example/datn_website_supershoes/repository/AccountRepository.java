package org.example.datn_website_supershoes.repository;

import org.example.datn_website_supershoes.dto.request.AccountRequest;
import org.example.datn_website_supershoes.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {

    Optional<Account> findByEmail(String email);
    List<Account> findAllByStatus(String status);




}
