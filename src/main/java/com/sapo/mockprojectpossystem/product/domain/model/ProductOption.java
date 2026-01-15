package com.sapo.mockprojectpossystem.product.domain.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import static com.sapo.mockprojectpossystem.common.validation.CommonValidation.validateName;

@Entity
@Table(name = "product_options")
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ProductOption {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Integer position;

    @OneToMany(mappedBy = "option", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<ProductOptionValue> values = new ArrayList<>();

    @Column(updatable = false)
    @CreationTimestamp
    private Instant createdOn;

    @UpdateTimestamp
    private Instant modifiedOn;

    public static ProductOption create(String name, Integer position) {
        ProductOption option = new ProductOption(null, name, position, new ArrayList<>());
        return option;
    }

    private ProductOption(Product product, String name, Integer position, List<ProductOptionValue> values) {
        validateName(name);
        this.product = product;
        this.name = name;
        this.position = position;
        this.values = values;
    }

    public void assignTo(Product product) {
        this.product = product;
    }

    public void addValue(ProductOptionValue value) {
        value.assignTo(this);
        values.add(value);
    }

    public void clearValues() {
        values.clear();
    }
}
