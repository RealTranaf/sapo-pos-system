package com.sapo.mockprojectpossystem.customer.domain.model;

import com.sapo.mockprojectpossystem.customer.domain.enums.Gender;
import com.sapo.mockprojectpossystem.purchase.domain.model.Purchase;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import static com.sapo.mockprojectpossystem.customer.validation.CustomerValidation.validateCustomer;

@Entity
@Table(name = "customers")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String phoneNum;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Column(updatable = false, nullable = false)
    @CreationTimestamp
    private Instant createdOn;

    @UpdateTimestamp
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
        validateCustomer(name, phoneNumber.getValue());
        this.name = name.trim();
        this.phoneNum = phoneNumber.getValue();
        this.gender = gender == null ? Gender.NaN : gender;
        this.note = note;
    }

    public void update(String name, PhoneNumber phoneNumber, Gender gender, String note) {
        validateCustomer(name, phoneNumber.getValue());
        this.name = name.trim();
        this.phoneNum = phoneNumber.getValue();
        this.gender = gender;
        this.note = note;
    }

    public void addPurchase(double amount, Instant purchasedAt) {
        if (amount <= 0) {
            throw new RuntimeException("Purchase amount must be positive");
        }
        this.totalPurchaseAmount += amount;
        this.lastPurchaseDate = purchasedAt;
    }
}
