package com.sapo.mockprojectpossystem.product.domain.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Set;

import static com.sapo.mockprojectpossystem.common.validation.CommonValidation.validateName;

@Entity
@Getter
@NoArgsConstructor
public class Type {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, unique = true, length = 40)
    private String name;

    @Column(updatable = false)
    @CreationTimestamp
    private Instant createdOn;

    @UpdateTimestamp
    private Instant modifiedOn;

    @ManyToMany(mappedBy = "types", fetch = FetchType.LAZY)
    private Set<Product> products;

    public static Type create(String name) {
        validateName(name);
        return new Type(name);
    }

    private Type(String name) {
        this.name = name;
    }

    public void update(String newName) {
        validateName(newName);
        this.name = newName;
    }
}
