package com.sapo.mockprojectpossystem.customer.interfaces.response;

import com.fasterxml.jackson.annotation.JsonRootName;
import com.sapo.mockprojectpossystem.customer.domain.model.Customer;
import com.sapo.mockprojectpossystem.customer.domain.enums.Gender;
import com.sapo.mockprojectpossystem.purchase.interfaces.response.PurchaseResponse;
import lombok.*;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
@JsonRootName("customer")
public class CustomerResponse {
    private Integer id;
    private String name;
    private String phoneNum;
    private Gender gender;
    private String note;
    private Instant createdOn;
    private Instant lastPurchaseDate;
    private double totalPurchaseAmount;
    private List<PurchaseResponse> purchases;

    public CustomerResponse(Customer customer) {
        this.id = customer.getId();
        this.name = customer.getName();
        this.phoneNum = customer.getPhoneNum();
        this.gender = customer.getGender();
        this.note = customer.getNote();
        this.createdOn = customer.getCreatedOn();
        this.lastPurchaseDate = customer.getLastPurchaseDate();
        this.totalPurchaseAmount = customer.getTotalPurchaseAmount();
        this.purchases = customer.getPurchases().stream().map(PurchaseResponse::new).collect(Collectors.toList());
    }
}