package com.sapo.mockprojectpossystem.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, unique = true)
    @Size(min = 3, max = 40)
    private String name;

    @Column(nullable = false, unique = true, length = 12)
    private String phoneNum;

    @Enumerated(EnumType.STRING)
    private Gender gender = Gender.NaN;

    @Column(name = "created_at", updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String note;

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Purchase> purchases = new ArrayList<>();

    private LocalDateTime lastPurchaseDate;

    private float totalPurchaseAmount = 0;

    public Customer(String name, String phoneNum, Gender gender, String note) {
        this.name = name;
        this.phoneNum = phoneNum;
        this.gender = gender;
        this.note = note;
    }
}
