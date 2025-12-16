package com.sapo.mockprojectpossystem.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "product_variants")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductVariant {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "image_id")
    private ProductImage image;

    private String barcode;

    @Column(nullable = false, unique = true)
    private String sku;

    private BigDecimal price;

    @Column(name = "base_price")
    private BigDecimal basePrice;

    @Column(name = "compare_at_price")
    private BigDecimal compareAtPrice;

    @Column(name = "option1")
    private String option1;

    @Column(name = "option2")
    private String option2;

    @Column(name = "option3")
    private String option3;

    @Column(nullable = false)
    private Boolean taxable;

    @Column(name = "inventory_quantity")
    private Integer inventoryQuantity;

    @Column(nullable = false)
    private String unit;

    @Column(nullable = false)
    private Integer position;

    @Column(nullable = false)
    private String title;

    @CreationTimestamp
    @Column(name = "created_on", nullable = false, updatable = false)
    private Instant createdOn;

    @UpdateTimestamp
    @Column(name = "modified_on")
    private Instant modifiedOn;
}