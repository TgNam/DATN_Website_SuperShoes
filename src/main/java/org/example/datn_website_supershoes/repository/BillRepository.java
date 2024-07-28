package org.example.datn_website_supershoes.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.example.datn_website_supershoes.model.Bill;
import org.springframework.stereotype.Repository;

@Repository
public interface BillRepository extends JpaRepository<Bill,Long> {
}
