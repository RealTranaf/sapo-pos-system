package com.sapo.mockprojectpossystem.product.application.interfaces;

import com.sapo.mockprojectpossystem.product.application.request.ProductOptionRequest;
import com.sapo.mockprojectpossystem.product.application.request.ProductOptionValueRequest;
import com.sapo.mockprojectpossystem.product.application.request.ProductUpdateRequest;
import com.sapo.mockprojectpossystem.product.application.request.ProductVariantRequest;
import com.sapo.mockprojectpossystem.product.application.response.*;
import com.sapo.mockprojectpossystem.product.domain.model.*;
import com.sapo.mockprojectpossystem.product.interfaces.request.*;
import com.sapo.mockprojectpossystem.product.interfaces.response.*;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(
        componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface IProductMapper {

    /* ===================== PRODUCT ===================== */

    ProductResponse toResponse(Product product);

    void updateProductFromRequest(
            @MappingTarget Product product,
            ProductUpdateRequest request
    );

    /* ===================== IMAGE ===================== */

    ProductImageResponse imageToResponse(ProductImage image);

    /* ===================== VARIANT ===================== */

    default ProductVariant variantRequestToEntity(ProductVariantRequest request) {
        ProductVariant variant = ProductVariant.create(
                request.getTitle(),
                request.getPosition(),
                request.getPrice(),
                request.getInventoryQuantity()
        );

        variant.defineOptions(
                request.getOption1(),
                request.getOption2(),
                request.getOption3()
        );

        variant.updateFrom(request);

        return variant;
    }

    ProductVariantResponse variantToResponse(ProductVariant variant);

    void updateVariantFromRequest(
            @MappingTarget ProductVariant variant,
            ProductVariantRequest request
    );

    /* ===================== OPTION ===================== */

    default ProductOption optionRequestToEntity(ProductOptionRequest request) {
        ProductOption option = ProductOption.create(
                request.getName(),
                0 // position set by Product.replaceOptions()
        );

        if (request.getValues() != null) {
            for (ProductOptionValueRequest valueReq : request.getValues()) {
                option.addValue(
                        ProductOptionValue.create(valueReq.getValue())
                );
            }
        }

        return option;
    }

    ProductOptionResponse optionToResponse(ProductOption option);

    /* ===================== OPTION VALUE ===================== */

    default ProductOptionValue valueToEntity(ProductOptionValueRequest request) {
        return ProductOptionValue.create(request.getValue());
    }

    ProductOptionValueResponse valueToResponse(ProductOptionValue value);
}

