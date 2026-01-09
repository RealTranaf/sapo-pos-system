package com.sapo.mockprojectpossystem.product.application.interfaces;

import com.sapo.mockprojectpossystem.product.domain.model.*;
import com.sapo.mockprojectpossystem.product.interfaces.request.*;
import com.sapo.mockprojectpossystem.product.interfaces.response.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring",
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface IProductMapper {
    Product toEntity(ProductCreateRequest request);

    ProductResponse toResponse(Product product);

    void updateProductFromRequest(@MappingTarget Product product, ProductUpdateRequest request);

    ProductImage imageRequestToEntity(ProductImageRequest request);

    ProductImageResponse imageToResponse(ProductImage image);

    @Mapping(target = "id", ignore = true)
    void updateProductImageFromRequest(@MappingTarget ProductImage image, ProductImageRequest request);

    ProductVariant variantRequestToEntity(ProductVariantRequest request);

    ProductVariantResponse variantToResponse(ProductVariant variant);

    @Mapping(target = "id", ignore = true)
    void updateVariantFromRequest(@MappingTarget ProductVariant variant, ProductVariantRequest request);

    ProductOption optionRequestToEntity(ProductOptionRequest request);

    ProductOptionResponse optionToResponse(ProductOption option);

    @Mapping(target = "id", ignore = true)
    void updateProductOptionFromRequest(@MappingTarget ProductOption option, ProductOptionRequest request);

    ProductOptionValue valueToEntity(ProductOptionValueRequest request);

    ProductOptionValueResponse valueToResponse(ProductOptionValue value);

    @Mapping(target = "id", ignore = true)
    void updateProductOptionValueFromRequest(@MappingTarget ProductOptionValue optionValue, ProductOptionValueRequest request);
}
