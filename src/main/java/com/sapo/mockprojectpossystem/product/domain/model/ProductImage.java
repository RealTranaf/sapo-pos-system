package com.sapo.mockprojectpossystem.product.domain.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.util.List;

@Entity
@Table(name = "product_images")
@Getter
@NoArgsConstructor
public class ProductImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @OneToMany(mappedBy = "image", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<ProductVariant> productVariants;

    @Column(nullable = false)
    private Integer position;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String src;

    private String alt;

    @Column(nullable = false)
    private String filename;

    @Column(nullable = false)
    private String assetId;

    @Column(nullable = false)
    private Integer size;

    private Integer width;

    private Integer height;

    @Column(updatable = false)
    @CreationTimestamp
    private Instant createdOn;

    @UpdateTimestamp
    private Instant modifiedOn;

    public static ProductImage create(Product product, Integer position, String src, String filename, String assetId, Integer size) {
        return new ProductImage(product, position, src, filename, assetId, size);
    }

    private ProductImage(Product product, Integer position, String src, String filename, String assetId, Integer size) {
        this.product = product;
        this.position = position;
        this.src = src;
        this.filename = filename;
        this.assetId = assetId;
        this.size = size;
    }

    public void assignTo(Product product) {
        this.product = product;
    }
}