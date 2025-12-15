package com.sapo.mockprojectpossystem.response;

import com.sapo.mockprojectpossystem.model.Customer;
import com.sapo.mockprojectpossystem.model.Gender;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class CustomerResponse {
    private Integer id;
    private String name;
    private String phoneNum;
    private String note;
    private Gender gender;
    private LocalDateTime createdAt;
    private LocalDateTime lastPurchaseDate;
    private double totalPurchaseAmount;
    private List<PurchaseResponse> purchases;

    public CustomerResponse(Customer customer) {
        this.id = customer.getId();
        this.name = customer.getName();
        this.phoneNum = customer.getPhoneNum();
        this.note = customer.getNote();
        this.gender = customer.getGender();
        this.createdAt = customer.getCreatedAt();
        this.lastPurchaseDate = customer.getLastPurchaseDate();
        this.totalPurchaseAmount = customer.getTotalPurchaseAmount();
        this.purchases = customer.getPurchases().stream().map(PurchaseResponse::new).collect(Collectors.toList());
    }
}
