package com.sapo.mockprojectpossystem.product.application.implement;

import com.sapo.mockprojectpossystem.common.response.PageResponse;
import com.sapo.mockprojectpossystem.product.domain.model.Brand;
import com.sapo.mockprojectpossystem.product.domain.repository.BrandRepository;
import com.sapo.mockprojectpossystem.product.application.request.BrandQueryParams;
import com.sapo.mockprojectpossystem.product.application.response.BrandResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BrandService {

    private final BrandRepository brandRepository;

    // Lấy page brand
    public PageResponse<BrandResponse> getAllBrand(BrandQueryParams query) {
        Integer page = query.getPage();
        Integer size = query.getSize();
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());

        Page<BrandResponse> responsePage = brandRepository.findAll(pageable).map(BrandResponse::new);
        return new PageResponse<BrandResponse>("brands", responsePage);
    }

    // Lấy brand theo id
    public BrandResponse getBrandById(Integer id) {
        Brand brand = brandRepository.findById(id).orElseThrow(() -> new RuntimeException("Brand does not exist"));
        return new BrandResponse(brand);
    }

    // Tạo brand từ name
    public BrandResponse createBrand(String name) {
        brandRepository.findByName(name).ifPresent(b -> {
            throw new RuntimeException("Brand name already exists");
        });
        Brand brand = Brand.create(name);
        return new BrandResponse(brandRepository.save(brand));
    }

    // Cập nhật brand
    public BrandResponse updateBrand(Integer id, String name) {
        Brand brand = brandRepository.findById(id).orElseThrow(() -> new RuntimeException("Brand does not exist"));
        brand.update(name);
        return new BrandResponse(brandRepository.save(brand));
    }
}
