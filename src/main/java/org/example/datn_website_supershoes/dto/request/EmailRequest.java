package org.example.datn_website_supershoes.dto.request;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class EmailRequest {
    private String to;
    private String subject;
    private String body;

}
