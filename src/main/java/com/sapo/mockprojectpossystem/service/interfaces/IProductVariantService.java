package com.sapo.mockprojectpossystem.service.interfaces;

import com.sapo.mockprojectpossystem.dto.request.ProductVariantRequest;
import com.sapo.mockprojectpossystem.dto.response.ProductVariantResponse;

import java.util.List;

public interface IProductVariantService {
    List<ProductVariantResponse> getAllProductVariantsByProductId(Long productId);

    ProductVariantResponse getProductVariantById(Long id, Long productId);

    void deleteProductVariantById(Long id, Long productId);

    ProductVariantResponse updateProductVariant(Long id, Long productId, ProductVariantRequest request);
}
