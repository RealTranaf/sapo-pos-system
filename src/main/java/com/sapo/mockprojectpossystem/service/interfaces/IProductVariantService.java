package com.sapo.mockprojectpossystem.service.interfaces;

import com.sapo.mockprojectpossystem.dto.request.ProductVariantRequest;
import com.sapo.mockprojectpossystem.dto.response.ProductVariantResponse;

import java.util.List;

public interface IProductVariantService {
    List<ProductVariantResponse> getAllProductVariantsByProductId(Integer productId);

    ProductVariantResponse getProductVariantById(Integer id, Integer productId);

    void deleteProductVariantById(Integer id, Integer productId);

    ProductVariantResponse updateProductVariant(Integer id, Integer productId, ProductVariantRequest request);
}
