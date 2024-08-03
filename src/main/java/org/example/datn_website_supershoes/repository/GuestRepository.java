package org.example.datn_website_supershoes.repository;

import org.example.datn_website_supershoes.model.Guest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GuestRepository extends JpaRepository<Guest, Long> {

    List<Guest> findAllByStatus(String status);
}
