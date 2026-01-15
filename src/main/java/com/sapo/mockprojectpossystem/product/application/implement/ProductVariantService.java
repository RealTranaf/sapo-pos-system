package com.sapo.mockprojectpossystem.product.application.implement;

import com.sapo.mockprojectpossystem.product.application.interfaces.IProductMapper;
import com.sapo.mockprojectpossystem.product.application.request.ProductVariantRequest;
import com.sapo.mockprojectpossystem.product.application.response.ProductVariantResponse;
import com.sapo.mockprojectpossystem.product.domain.model.Product;
import com.sapo.mockprojectpossystem.common.exception.NotFoundException;
import com.sapo.mockprojectpossystem.product.domain.repository.ProductRepository;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ProductVariantService{
    ProductRepository productRepository;
    IProductMapper productMapper;

    @Transactional
    public List<ProductVariantResponse> getAllProductVariantsByProductId(Integer productId) {
        Product product = loadProduct(productId);
        return product.getVariants().stream()
                .map(productMapper::variantToResponse)
                .toList();
    }

    @Transactional
    public ProductVariantResponse getProductVariantById(Integer id, Integer productId) {
        Product product = loadProduct(productId);
        return productMapper.variantToResponse(product.getVariant(id));
    }

    public void deleteProductVariantById(Integer id, Integer productId) {
        Product product = loadProduct(productId);
        product.removeVariant(id);
        productRepository.save(product);
    }

    public ProductVariantResponse updateProductVariant(
            Integer id,
            Integer productId,
            ProductVariantRequest request
    ) {
        Product product = loadProduct(productId);

        product.updateVariant(id, request);

        productRepository.save(product);

        return productMapper.variantToResponse(product.getVariant(id));
    }

    private Product loadProduct(Integer productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new NotFoundException("Product not found"));
    }
}
