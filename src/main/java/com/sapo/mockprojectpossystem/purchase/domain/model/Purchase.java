package com.sapo.mockprojectpossystem.purchase.domain.model;

import com.sapo.mockprojectpossystem.auth.domain.model.User;
import com.sapo.mockprojectpossystem.customer.domain.model.Customer;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class Purchase {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id")
    private User user;

    private Double totalAmount;
    private Double discountAmount;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String note;

    @Column(updatable = false)
    @CreationTimestamp
    private Instant createdOn;

    @OneToMany(mappedBy = "purchase", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PurchaseItem> purchaseItems;

    public static Purchase create(Customer customer, User user, double totalAmount, double discountAmount, String note) {
        return new Purchase(customer, user, totalAmount, discountAmount, note);
    };

    public Purchase(Customer customer, User user, double totalAmount, double discountAmount, String note) {
        this.customer = customer;
        this.user = user;
        this.totalAmount = totalAmount;
        this.discountAmount = discountAmount;
        this.note = note;
    }
}
