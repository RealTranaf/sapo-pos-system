package com.sapo.mockprojectpossystem.product.domain.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;

@Entity
@Table(name = "product_option_values")
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ProductOptionValue {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "option_id", nullable = false)
    private ProductOption option;

    @Column(nullable = false)
    private String value;

    @Column(updatable = false)
    @CreationTimestamp
    private Instant createdOn;

    @UpdateTimestamp
    private Instant modifiedOn;

    private ProductOptionValue(String value) {
        this.value = value;
    }
    public static ProductOptionValue create(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Option value is required");
        }
        return new ProductOptionValue(value);
    }

    void assignTo(ProductOption option) {
        this.option = option;
    }
}