package org.example.datn_website_supershoes.dto.response;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.example.datn_website_supershoes.model.Bill;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString

public class BillSummaryResponse {

    @NotNull(message = "Code bill cannot be null")
    @NotBlank(message = "Code bill cannot be blank")
    private String codeBill;

    private String status;

    @NotNull(message = "Name of the customer cannot be null")
    @Size(min = 3, max = 100, message = "Name must be between 3 and 100 characters")
    private String nameCustomer;

    @NotNull(message = "Phone number cannot be null")
    @Size(min = 10, max = 15, message = "Phone number must be between 10 and 15 digits")
    private String phoneNumber;

    @NotNull(message = "Address cannot be null")
    @Size(min = 10, message = "Address must be at least 10 characters long")
    private String address;

    @Size(max = 200, message = "Note cannot exceed 200 characters")
    private String note;


    public BillSummaryResponse(Bill bill) {
        this.codeBill = bill.getCodeBill();
        this.status = bill.getStatus();
        this.nameCustomer = bill.getNameCustomer();
        this.address = bill.getAddress();
        this.phoneNumber = bill.getPhoneNumber();
        this.note = bill.getNote();
    }
}
