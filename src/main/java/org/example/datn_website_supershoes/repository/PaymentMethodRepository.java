package org.example.datn_website_supershoes.repository;

import org.example.datn_website_supershoes.dto.response.PaymentMethodResponse;
import org.example.datn_website_supershoes.model.PaymentMethod;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface PaymentMethodRepository extends JpaRepository<PaymentMethod, Long> {

    @Query(value = """
                SELECT pm.id,pm.methodName,pm.note,pm.status
                FROM 
                PaymentMethod pm WHERE pm.status = :status

            """)

    List<PaymentMethodResponse> listPaymentMethodResponseByStatus(@Param("status") String status);
    Optional<PaymentMethod> findByMethodNameAndType(String methodName,Integer type);
    Optional<PaymentMethod> findByType(Integer type);


}
