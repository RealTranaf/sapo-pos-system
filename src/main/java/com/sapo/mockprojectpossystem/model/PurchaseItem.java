package com.sapo.mockprojectpossystem.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class PurchaseItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "product_variant_id")
    private ProductVariant productVariant;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Purchase purchase;

    private int quantity;
    private double totalPrice;

    public PurchaseItem(ProductVariant product, Purchase purchase, int quantity) {
        this.productVariant = product;
        this.purchase = purchase;
        this.quantity = quantity;
    }
}
