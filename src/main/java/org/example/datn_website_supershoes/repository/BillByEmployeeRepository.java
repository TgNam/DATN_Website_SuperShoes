package org.example.datn_website_supershoes.repository;

import org.example.datn_website_supershoes.model.Bill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BillByEmployeeRepository extends JpaRepository<Bill, Long> {

    @Query("""
        SELECT b.codeBill
        FROM Bill b JOIN b.employees e
        WHERE e.id = :idEmployees AND b.status = :status ORDER BY b.createdAt asc 
        """)
    List<String> findCodeBillWaitingForPayment(@Param("idEmployees") Long idEmployees, @Param("status") String status);

    @Query(value = """
       SELECT * 
       FROM bill 
       WHERE status IN (:status) 
         AND created_at < DATE_SUB(NOW(), INTERVAL :day DAY)
       """, nativeQuery = true)
    List<Bill> findBillsOlder(@Param("day") int day, @Param("status") List<String> status);



}
