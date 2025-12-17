package com.sapo.mockprojectpossystem.validator;

import com.sapo.mockprojectpossystem.annotation.ValidSortBy;
import jakarta.persistence.EntityManager;
import jakarta.persistence.metamodel.EntityType;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;

public class SortByValidator implements ConstraintValidator<ValidSortBy, String> {
    @Autowired
    private EntityManager entityManager;

    private Class<?> entityClass;

    @Override
    public void initialize(ValidSortBy constraintAnnotation) {
        this.entityClass = constraintAnnotation.entity();
    }

    @Override
    public boolean isValid(String sortBy, ConstraintValidatorContext constraintValidatorContext) {
        EntityType<?> entityType = entityManager.getMetamodel().entity(entityClass);
        return entityType.getAttributes().stream()
            .anyMatch(attr -> attr.getName().equalsIgnoreCase(sortBy));
    }
}
