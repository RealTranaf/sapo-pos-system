package com.sapo.mockprojectpossystem.product.application.implement;

import com.sapo.mockprojectpossystem.product.domain.model.Brand;
import com.sapo.mockprojectpossystem.product.domain.repository.BrandRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BrandService {

    private final BrandRepository brandRepository;

    // Lấy page brand
    public Page<Brand> getAllBrand(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());
        return brandRepository.findAll(pageable);
    }

    // Lấy brand theo id
    public Brand getBrandById(Integer id) {
        Optional<Brand> optional = brandRepository.findById(id);
        if (optional.isPresent()) {
            return optional.get();
        } else {
            throw new RuntimeException("Brand doesn't exist");
        }
    }

    // Tạo brand từ name
    public void createBrand(String name) {
        Brand brand = new Brand(name);
        brandRepository.save(brand);
    }

    // Cập nhật brand
    public void updateBrand(Integer id, String name) {
        Optional<Brand> optional = brandRepository.findById(id);
        if (optional.isPresent()) {
            Brand brand = optional.get();
            brand.setName(name);
            brandRepository.save(brand);
        } else {
            throw new RuntimeException("Brand doesn't exist");
        }
    }
}
