package com.sapo.mockprojectpossystem.product.infrastructure;
import com.sapo.mockprojectpossystem.product.domain.model.Type;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface JpaTypeRepository extends JpaRepository<Type, Integer> {
    Optional<Type> findByName(String name);
}
