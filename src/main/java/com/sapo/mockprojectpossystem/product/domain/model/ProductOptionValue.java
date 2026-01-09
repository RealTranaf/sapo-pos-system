package com.sapo.mockprojectpossystem.product.domain.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "product_option_values")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductOptionValue {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "option_id", nullable = false)
    private ProductOption option;

    @Column(nullable = false)
    private String value;
}