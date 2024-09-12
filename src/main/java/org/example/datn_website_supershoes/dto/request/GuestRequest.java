package org.example.datn_website_supershoes.dto.request;

import jakarta.persistence.Column;
import lombok.Data;

@Data
public class GuestRequest {
    private String name;
    private String phoneNumber;
    private String address;
}
