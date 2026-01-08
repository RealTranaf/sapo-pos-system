package com.sapo.mockprojectpossystem.customer.domain.model;

import com.sapo.mockprojectpossystem.model.Purchase;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "customers")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

    private String phoneNum;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    private Instant createdOn;

    private Instant modifiedOn;

    @Lob
    private String note;

    private Instant lastPurchaseDate;

    private double totalPurchaseAmount;

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Purchase> purchases = new ArrayList<>();

    public static Customer create(String name, PhoneNumber phoneNumber, Gender gender, String note) {
        return new Customer(name, phoneNumber, gender, note);
    }

    private Customer(String name, PhoneNumber phoneNumber, Gender gender, String note) {
        validateName(name);
        this.name = name.trim();
        this.phoneNum = phoneNumber.getValue();
        this.gender = gender == null ? Gender.NaN : gender;
        this.note = note;
        this.createdOn = Instant.now();
    }

    public void update(String name, PhoneNumber phoneNumber, Gender gender, String note) {
        validateName(name);
        this.name = name.trim();
        this.phoneNum = phoneNumber.getValue();
        this.gender = gender;
        this.note = note;
        this.modifiedOn = Instant.now();
    }

    public void addPurchase(double amount, Instant purchasedAt) {
        if (amount <= 0) {
            throw new RuntimeException("Purchase amount must be positive");
        }
        this.totalPurchaseAmount += amount;
        this.lastPurchaseDate = purchasedAt;
    }

    private void validateName(String name) {
        if (name == null || name.isBlank()) {
            throw new RuntimeException("Name is required");
        }
        if (name.length() < 2 || name.length() > 40) {
            throw new RuntimeException("Name must be 2–40 characters");
        }
    }
}
