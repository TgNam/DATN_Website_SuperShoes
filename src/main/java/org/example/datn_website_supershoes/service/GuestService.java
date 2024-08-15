package org.example.datn_website_supershoes.service;

import org.example.datn_website_supershoes.Enum.Status;
import org.example.datn_website_supershoes.dto.request.GuestRequest;
import org.example.datn_website_supershoes.model.Guest;
import org.example.datn_website_supershoes.repository.GuestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GuestService {

    @Autowired
    GuestRepository guestRepository;

    public Guest addGuest(GuestRequest guestRequest) {
        Guest guest = convertGuestRequestDTO(guestRequest);
        guest.setStatus(Status.ACTIVE.toString());
        return guestRepository.save(guest);
    }

    public Guest updateGuest(Long id, GuestRequest guestRequest) {
        Guest guest = guestRepository.findById(id).orElseThrow(() -> {
            throw new RuntimeException("Guest not found");
        });
        guest.setName(guestRequest.getName());
        guest.setAddress(guestRequest.getAddress());
        guest.setPhoneNumber(guestRequest.getPhoneNumber());
        return guestRepository.save(guest);
    }

    public void deleteGuest(Long id) {
        Guest guest = guestRepository.findById(id).orElseThrow(() -> {
            throw new RuntimeException("Guest not found");
        });
        guest.setStatus(Status.INACTIVE.toString());
        guestRepository.save(guest);
    }

    public List<Guest> getGuestActive() {
        return guestRepository.findAllByStatus(Status.ACTIVE.toString());
    }

    public Guest convertGuestRequestDTO(GuestRequest guestRequest) {
        return Guest.builder()
                .name(guestRequest.getName())
                .address(guestRequest.getAddress())
                .phoneNumber(guestRequest.getPhoneNumber())
                .build();
    }
}
