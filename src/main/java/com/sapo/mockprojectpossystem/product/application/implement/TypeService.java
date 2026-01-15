package com.sapo.mockprojectpossystem.product.application.implement;

import com.sapo.mockprojectpossystem.common.response.PageResponse;
import com.sapo.mockprojectpossystem.product.domain.model.Type;
import com.sapo.mockprojectpossystem.product.domain.repository.TypeRepository;
import com.sapo.mockprojectpossystem.product.application.request.TypeQueryParams;
import com.sapo.mockprojectpossystem.product.application.response.TypeResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TypeService {
    private final TypeRepository typeRepository;

    public PageResponse<TypeResponse> getAllType(TypeQueryParams query) {
        Integer page = query.getPage();
        Integer size = query.getSize();
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());
        Page<TypeResponse> responsePage = typeRepository.findAll(pageable).map(TypeResponse::new);
        return new PageResponse<TypeResponse>("types", responsePage);
    }

    public TypeResponse getTypeById(Integer id) {
        Type type = typeRepository.findById(id).orElseThrow(() -> new RuntimeException("Type does not exist"));
        return new TypeResponse(type);
    }

    public TypeResponse createType(String name) {
        typeRepository.findByName(name).ifPresent(t -> {
            throw new RuntimeException("Type name already exists");
        });
        Type type = Type.create(name);
        return new TypeResponse(typeRepository.save(type));
    }

    public TypeResponse updateType(Integer id, String name) {
        Type type = typeRepository.findById(id).orElseThrow(() -> new RuntimeException("Type does not exist"));
        type.update(name);
        return new TypeResponse(typeRepository.save(type));
    }
}
