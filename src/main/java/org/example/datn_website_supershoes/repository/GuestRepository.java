package org.example.datn_website_supershoes.repository;

import org.example.datn_website_supershoes.model.Account;
import org.example.datn_website_supershoes.model.Guest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface GuestRepository extends JpaRepository<Guest,Long> {

    List<Guest> findAllByStatus(String status);
}
