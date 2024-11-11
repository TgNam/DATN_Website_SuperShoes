package org.example.datn_website_supershoes.repository;

import org.example.datn_website_supershoes.model.BillHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BillHistoryRepository extends JpaRepository<BillHistory, Long> {


    @Query("SELECT bh FROM BillHistory bh WHERE bh.bill.id = :billId")
    List<BillHistory> findBillHistoryByBillId(@Param("billId") Long billId);


    @Query("SELECT bh FROM BillHistory bh WHERE bh.account.id = :accountId")
    List<BillHistory> findBillHistoryByAccountId(@Param("accountId") Long accountId);


    @Query("SELECT bh FROM BillHistory bh WHERE bh.bill.id = :billId AND bh.bill.status = :status")
    List<BillHistory> findBillHistoryByBillIdAndStatus(@Param("billId") Long billId, @Param("status") String status);

    @Query("SELECT bh FROM BillHistory bh JOIN bh.bill b WHERE b.codeBill = :codeBill ORDER BY bh.createdAt")
    List<BillHistory> findBillHistoryByBillCode(@Param("codeBill") String codeBill);

}
