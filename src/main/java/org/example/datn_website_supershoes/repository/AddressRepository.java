package org.example.datn_website_supershoes.repository;

import org.example.datn_website_supershoes.model.Address;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AddressRepository extends JpaRepository<Address,Long> {
    List<Address> findAllByStatus(String status);
    Optional<Address> findById(Long id);
}
