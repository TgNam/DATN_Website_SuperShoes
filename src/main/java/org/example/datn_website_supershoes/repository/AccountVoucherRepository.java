package org.example.datn_website_supershoes.repository;

import org.example.datn_website_supershoes.model.AccountVoucher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountVoucherRepository extends JpaRepository<AccountVoucher, Long> {
}
