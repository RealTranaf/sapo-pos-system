package com.sapo.mockprojectpossystem.purchase.domain.model;

import com.sapo.mockprojectpossystem.auth.domain.model.User;
import com.sapo.mockprojectpossystem.customer.domain.model.Customer;
import com.sapo.mockprojectpossystem.product.domain.model.ProductVariant;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.util.ArrayList;
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

    @UpdateTimestamp
    private Instant modifiedOn;

    @OneToMany(mappedBy = "purchase", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PurchaseItem> purchaseItems = new ArrayList<>();

    public static Purchase create(Customer customer, User user, String note) {
        return new Purchase(customer, user, note);
    }

    public Purchase(Customer customer, User user, String note) {
        this.customer = customer;
        this.user = user;
        this.note = note;
        this.totalAmount = 0.0;
        this.discountAmount = 0.0;
    }

    public void addItem(ProductVariant variant, int quantity) {
        variant.decreaseInventory(quantity);

        PurchaseItem item = PurchaseItem.create(this, variant, quantity);
        purchaseItems.add(item);

        recalculateTotals();
    }

    private void recalculateTotals() {
        this.totalAmount = purchaseItems.stream()
                .mapToDouble(PurchaseItem::getTotalPrice)
                .sum();
    }

    public void applyDiscount(double discountAmount) {
        if (discountAmount < 0) {
            throw new IllegalArgumentException("Discount must be >= 0");
        }
        this.discountAmount = discountAmount;
    }
}
