package com.sapo.mockprojectpossystem.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.util.List;

@Entity
@Table(name = "product_images")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductImage {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @OneToMany(mappedBy = "image", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<ProductVariant> productVariants;

    @Column(nullable = false)
    private Integer position;

    @Column(columnDefinition = "TEXT")
    private String src;

    @Column(nullable = false)
    private String alt;

    @Column(nullable = false)
    private String filename;

    @Column(nullable = false)
    private String assetId;

    @Column(nullable = false)
    private Integer size;

    @Column(nullable = false)
    private Integer width;

    @Column(nullable = false)
    private Integer height;

    @CreationTimestamp
    @Column(name = "created_on", nullable = false, updatable = false)
    private Instant createdOn;

    @UpdateTimestamp
    @Column(name = "modified_on")
    private Instant modifiedOn;
}