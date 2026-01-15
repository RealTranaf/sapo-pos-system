package com.sapo.mockprojectpossystem.purchase.domain.model;

import com.sapo.mockprojectpossystem.product.domain.model.ProductVariant;
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


    public static PurchaseItem create(Purchase purchase, ProductVariant variant, int quantity) {
        return new PurchaseItem(purchase, variant, quantity);
    }

    private PurchaseItem(Purchase purchase, ProductVariant variant, int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be > 0");
        }
        this.purchase = purchase;
        this.productVariant = variant;
        this.quantity = quantity;
        this.totalPrice = variant.getPrice() * quantity;
    }
}
