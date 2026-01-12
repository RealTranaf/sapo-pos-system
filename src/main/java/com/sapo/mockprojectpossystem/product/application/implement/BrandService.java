package com.sapo.mockprojectpossystem.product.application.implement;

import com.sapo.mockprojectpossystem.common.response.PageResponse;
import com.sapo.mockprojectpossystem.product.domain.model.Brand;
import com.sapo.mockprojectpossystem.product.domain.repository.BrandRepository;
import com.sapo.mockprojectpossystem.product.interfaces.request.BrandQueryParams;
import com.sapo.mockprojectpossystem.product.interfaces.response.BrandResponse;
import com.sapo.mockprojectpossystem.purchase.domain.model.Purchase;
import com.sapo.mockprojectpossystem.purchase.interfaces.response.PurchaseResponse;
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
        Brand brand = new Brand(name);
        return new BrandResponse(brandRepository.save(brand));
    }

    // Cập nhật brand
    public BrandResponse updateBrand(Integer id, String name) {
        Brand brand = brandRepository.findById(id).orElseThrow(() -> new RuntimeException("Brand does not exist"));
        brand.setName(name);
        return new BrandResponse(brandRepository.save(brand));
    }
}
