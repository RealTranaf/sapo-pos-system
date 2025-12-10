package com.sapo.mockprojectpossystem.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, unique = true)
    @Size(min = 3, max = 40)
    private String name;

    @Column(unique = true)
    private String sku;

    @Column(unique = true)
    private String barcode;

    @Enumerated(EnumType.STRING)
    private ProductStatus status;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String description;

    private double basePrice;

    private double sellPrice;

    private int quantity;

    @ManyToOne
    @JoinColumn(name = "brand_id")
    private Brand brand;

    @ManyToMany
    @JoinTable(
            name = "product_types",
            joinColumns = @JoinColumn(name = "product_id"),
            inverseJoinColumns = @JoinColumn(name = "type_id")
    )
    private List<Type> types;

    @OneToMany(mappedBy = "product")
    private List<PurchaseItem> purchaseItems;

    @Column(name = "created_at", updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    public Product(String name, String sku, String barcode,
                   ProductStatus status, String description,
                   double basePrice, double sellPrice,
                   int quantity, Brand brand, List<Type> types) {
        this.name = name;
        this.sku = sku;
        this.barcode = barcode;
        this.status = status;
        this.description = description;
        this.basePrice = basePrice;
        this.sellPrice = sellPrice;
        this.quantity = quantity;
        this.brand = brand;
        this.types = types;
    }
}
