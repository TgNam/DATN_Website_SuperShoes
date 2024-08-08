package org.example.datn_website_supershoes.repository;

import org.example.datn_website_supershoes.dto.response.PaymentMethodResponse;
import org.example.datn_website_supershoes.model.PaymentMethod;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaymentMethodRepository extends JpaRepository<PaymentMethod, Long> {

    @Query(value = """
                SELECT pm.id,pm.method_name,pm.note,pm.status
                FROM 
                PaymentMethod pm WHERE pm.status = :status

            """)
    List<PaymentMethodResponse> listPaymentMethodResponseByStatus(@Param("status") String status);
}
