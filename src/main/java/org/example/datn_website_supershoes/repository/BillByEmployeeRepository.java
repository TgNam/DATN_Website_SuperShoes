package org.example.datn_website_supershoes.repository;

import org.example.datn_website_supershoes.dto.response.BillResponse;
import org.example.datn_website_supershoes.dto.response.CartResponse;
import org.example.datn_website_supershoes.model.Bill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BillByEmployeeRepository extends JpaRepository<Bill, Long> {

    @Query("""
        SELECT b.codeBill
        FROM Bill b JOIN b.employees e
        WHERE e.id = :idEmployees AND b.status = :status
        """)
    List<String> findCodeBillWaitingForPayment(@Param("idEmployees") Long idEmployees, @Param("status") String status);


}
