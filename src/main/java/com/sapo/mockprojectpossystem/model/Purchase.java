package com.sapo.mockprojectpossystem.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Purchase {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    private float amount;

    @Column(nullable = false)
    private LocalDateTime purchaseDate;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String note;

    public Purchase(Customer customer, float amount, LocalDateTime purchaseDate, String note) {
        this.customer = customer;
        this.amount = amount;
        this.purchaseDate = purchaseDate;
        this.note = note;
    }
}
