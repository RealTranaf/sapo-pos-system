package com.sapo.mockprojectpossystem.product.domain.model;

import com.sapo.mockprojectpossystem.auth.domain.model.User;
import com.sapo.mockprojectpossystem.common.exception.NotFoundException;
import com.sapo.mockprojectpossystem.product.application.request.ProductVariantRequest;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.util.*;

import static com.sapo.mockprojectpossystem.common.validation.CommonValidation.validateName;

@Entity
@Table(name = "products")
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String summary;

    @Column(columnDefinition = "TEXT")
    private String content;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ProductStatus status;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(updatable = false)
    @CreationTimestamp
    private Instant createdOn;

    @UpdateTimestamp
    private Instant modifiedOn;

    @ManyToMany
    @JoinTable(
        name = "product_types",
        joinColumns = @JoinColumn(name = "product_id"),
        inverseJoinColumns = @JoinColumn(name = "type_id")
    )
    private Set<Type> types = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "brand_id")
    private Brand brand;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<ProductImage> images = new ArrayList<>();

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<ProductVariant> variants = new ArrayList<>();

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<ProductOption> options = new ArrayList<>();

    private Product(String name, String summary, String content, ProductStatus status, User user) {
        validateName(name);
        this.name = name;
        this.summary = summary;
        this.content = content;
        this.status = status;
        this.user = user;
    }

    public static Product create(String name, String summary, String content, ProductStatus status, User user) {
        return new Product(name, summary, content, status, user);
    }

    public void updateBasicInfo(String name, String summary, String content) {
        validateName(name);
        this.name = name;
        if (summary != null) this.summary = summary;
        if (content != null) this.content = content;
    }

    public void updateStatus(ProductStatus status) {
        if (status != null) this.status = status;
    }

    public void updateBrand(Brand newBrand) {
        this.brand = newBrand;
    }

    public void removeBrand() {
        this.brand = null;
    }

    public void clearTypes() {
        types.clear();
    }

    public void syncTypes(Set<Type> newTypes) {
        types.clear();
        if (newTypes != null) {
            types.addAll(newTypes);
        }
    }

    // temporary

    public ProductImage addImage(Integer position, String src, String filename, String assetId, Integer size) {
        ProductImage image = ProductImage.create(this, position, src, filename, assetId, size);
        image.assignTo(this);
        images.add(image);
        return image;
    }

    public ProductImage getImage(Integer imageId) {
        return images.stream()
                .filter(i -> i.getId().equals(imageId))
                .findFirst()
                .orElseThrow(() -> new NotFoundException("Image not found"));
    }


    public void removeImage(Integer imageId) {
        images.removeIf(img -> img.getId().equals(imageId));
    }

    public ProductVariant getVariant(Integer variantId) {
        return variants.stream()
                .filter(v -> v.getId().equals(variantId))
                .findFirst()
                .orElseThrow(() -> new NotFoundException("Variant not found"));
    }

    public void updateVariant(Integer variantId, ProductVariantRequest request) {
        ProductVariant variant = getVariant(variantId);

        if (request.getImageId() != null) {
            ProductImage image = getImage(request.getImageId());
            variant.attachImage(image);
        }

        variant.updateFrom(request);
    }

    public void removeVariant(Integer variantId) {
        variants.removeIf(v -> v.getId().equals(variantId));
    }

    public void replaceVariants(List<ProductVariant> newVariants) {
        variants.clear();

        for (ProductVariant variant : newVariants) {
            variant.assignTo(this);
            variants.add(variant);
        }
    }

    public void replaceOptions(List<ProductOption> newOptions) {
        options.clear();

        for (ProductOption option : newOptions) {
            option.assignTo(this);
            options.add(option);
        }
    }

}