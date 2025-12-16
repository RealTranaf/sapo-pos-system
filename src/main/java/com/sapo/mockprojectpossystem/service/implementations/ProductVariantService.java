package com.sapo.mockprojectpossystem.service.implementations;

import com.sapo.mockprojectpossystem.dto.request.ProductVariantRequest;
import com.sapo.mockprojectpossystem.dto.response.ProductVariantResponse;
import com.sapo.mockprojectpossystem.model.ProductVariant;
import com.sapo.mockprojectpossystem.exception.NotFoundException;
import com.sapo.mockprojectpossystem.mapper.IProductMapper;
import com.sapo.mockprojectpossystem.repository.ProductVariantRepository;
import com.sapo.mockprojectpossystem.service.interfaces.IProductVariantService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ProductVariantService implements IProductVariantService {
    ProductVariantRepository productVariantRepository;
    IProductMapper productMapper;

    @Override
    public List<ProductVariantResponse> getAllProductVariantsByProductId(Long productId) {
        return productVariantRepository.findAllByProduct_Id(
                productId).stream().map(productMapper::variantToResponse)
            .toList();
    }

    @Override
    public ProductVariantResponse getProductVariantById(Long id, Long productId) {
        ProductVariant variant = productVariantRepository.findByIdAndProduct_id(id, productId)
            .orElseThrow(() -> new NotFoundException("Not found variant with id: " + id + "and product id: " + productId));
        return productMapper.variantToResponse(variant);
    }

    @Override
    public void deleteProductVariantById(Long id, Long productId) {
        productVariantRepository.deleteByIdAndProduct_Id(id, productId);
    }

    @Override
    public ProductVariantResponse updateProductVariant(Long id, Long productId, ProductVariantRequest request) {
        ProductVariant variant = productVariantRepository.findByIdAndProduct_id(id, productId)
            .orElseThrow(() -> new NotFoundException("Not found variant with id: " + id + "and product id: " + productId));

        productMapper.updateVariantFromRequest(variant, request);

        return productMapper.variantToResponse(productVariantRepository.save(variant));
    }
}
