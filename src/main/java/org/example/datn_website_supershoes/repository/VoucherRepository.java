package org.example.datn_website_supershoes.repository;

import org.example.datn_website_supershoes.model.Voucher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VoucherRepository extends JpaRepository<Voucher, Long> {

    List<Voucher> findAllByStatus(String status);

}
