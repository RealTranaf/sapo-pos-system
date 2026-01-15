package com.sapo.mockprojectpossystem.product.domain.model;

import com.sapo.mockprojectpossystem.common.exception.InvalidException;
import com.sapo.mockprojectpossystem.product.application.request.ProductVariantRequest;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;

@Entity
@Table(name = "product_variants")
@Getter
@NoArgsConstructor
public class ProductVariant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "product_id")
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "image_id")
    private ProductImage image;

    private String barcode;

    private String sku;

    private Double price;

    @Column(name = "base_price")
    private Double basePrice;

    @Column(name = "compare_at_price")
    private Double compareAtPrice;

    @Column(name = "option1")
    private String option1;

    @Column(name = "option2")
    private String option2;

    @Column(name = "option3")
    private String option3;

    private Boolean taxable;

    @Column(name = "inventory_quantity")
    private Integer inventoryQuantity;

    private String unit;

    @Column(nullable = false)
    private Integer position;

    private String title;

    @Column(updatable = false)
    @CreationTimestamp
    private Instant createdOn;

    @UpdateTimestamp
    private Instant modifiedOn;

    private ProductVariant(String title, Integer position, Double price, Integer inventoryQuantity) {
        this.title = title;
        this.position = position;
        this.price = price;
        this.inventoryQuantity = inventoryQuantity;
        this.taxable = true;
    }

    public static ProductVariant create(String title, Integer position, Double price, Integer inventoryQuantity) {
        return new ProductVariant(title, position, price, inventoryQuantity);
    }

    public void assignTo(Product product) {
        this.product = product;
    }

    void attachImage(ProductImage image) {
        if (image == null) {
            this.image = null;
            return;
        }

        if (this.product == null || image.getProduct() == null) {
            throw new IllegalStateException("Variant must be attached to product first");
        }

        if (!image.getProduct().getId().equals(this.product.getId())) {
            throw new InvalidException("Image does not belong to this product");
        }

        this.image = image;
    }

    public void defineOptions(String option1, String option2, String option3) {
        this.option1 = option1;
        this.option2 = option2;
        this.option3 = option3;
    }

    public void decreaseInventory(int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than 0");
        }

        if (inventoryQuantity < quantity) {
            throw new IllegalStateException("Not enough inventory");
        }

        this.inventoryQuantity -= quantity;
    }

    public void updateFrom(ProductVariantRequest request) {
        if (request.getPrice() != null) {
            this.price = request.getPrice();
        }
        if (request.getBasePrice() != null) {
            this.basePrice = request.getBasePrice();
        }
        if (request.getCompareAtPrice() != null) {
            this.compareAtPrice = request.getCompareAtPrice();
        }
        if (request.getInventoryQuantity() != null) {
            this.inventoryQuantity = request.getInventoryQuantity();
        }
        if (request.getTitle() != null) {
            this.title = request.getTitle();
        }
        if (request.getSku() != null) {
            this.sku = request.getSku();
        }
        if (request.getBarcode() != null) {
            this.barcode = request.getBarcode();
        }
        if (request.getTaxable() != null) {
            this.taxable = request.getTaxable();
        }
    }
}