package com.sapo.mockprojectpossystem.product.application.implement;

import com.sapo.mockprojectpossystem.product.domain.model.Type;
import com.sapo.mockprojectpossystem.product.domain.repository.TypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TypeService {
    private final TypeRepository typeRepository;

    public Page<Type> getAllType(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());
        return typeRepository.findAll(pageable);
    }

    public Type getTypeById(Integer id) {
        Optional<Type> optional = typeRepository.findById(id);
        if (optional.isPresent()) {
            return optional.get();
        } else {
            throw new RuntimeException("Brand doesn't exist");
        }
    }

    public void createType(String name) {
        Type type = new Type(name);
        typeRepository.save(type);
    }

    public void updateType(Integer id, String name) {
        Optional<Type> optional = typeRepository.findById(id);
        if (optional.isPresent()) {
            Type type = optional.get();
            type.setName(name);
            typeRepository.save(type);
        } else {
            throw new RuntimeException("Type doesn't exist");
        }
    }
}
