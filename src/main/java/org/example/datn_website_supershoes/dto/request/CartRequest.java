package org.example.datn_website_supershoes.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartRequest {
    @NotNull
    private Long idAccount;

}
