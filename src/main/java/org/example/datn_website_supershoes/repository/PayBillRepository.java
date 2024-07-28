package org.example.datn_website_supershoes.repository;

import org.example.datn_website_supershoes.model.PayBill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PayBillRepository extends JpaRepository<PayBill,Long> {
}
