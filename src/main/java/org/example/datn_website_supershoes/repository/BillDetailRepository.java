package org.example.datn_website_supershoes.repository;

import org.example.datn_website_supershoes.model.BillDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BillDetailRepository extends JpaRepository<BillDetail, Long> {

}
