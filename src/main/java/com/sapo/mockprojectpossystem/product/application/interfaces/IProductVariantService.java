package com.sapo.mockprojectpossystem.product.application.interfaces;

import com.sapo.mockprojectpossystem.product.interfaces.request.ProductVariantRequest;
import com.sapo.mockprojectpossystem.product.interfaces.response.ProductVariantResponse;

import java.util.List;

public interface IProductVariantService {
    List<ProductVariantResponse> getAllProductVariantsByProductId(Integer productId);

    ProductVariantResponse getProductVariantById(Integer id, Integer productId);

    void deleteProductVariantById(Integer id, Integer productId);

    ProductVariantResponse updateProductVariant(Integer id, Integer productId, ProductVariantRequest request);
}
