package com.sapo.mockprojectpossystem.service.implementations;

import com.sapo.mockprojectpossystem.dto.response.FileUploadResponse;
import com.sapo.mockprojectpossystem.dto.response.ProductImageResponse;
import com.sapo.mockprojectpossystem.model.Product;
import com.sapo.mockprojectpossystem.model.ProductImage;
import com.sapo.mockprojectpossystem.exception.NotFoundException;
import com.sapo.mockprojectpossystem.mapper.IProductMapper;
import com.sapo.mockprojectpossystem.repository.ProductImageRepository;
import com.sapo.mockprojectpossystem.repository.ProductRepository;
import com.sapo.mockprojectpossystem.service.interfaces.IFileUploadService;
import com.sapo.mockprojectpossystem.service.interfaces.IProductImageService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Transactional
public class ProductImageService implements IProductImageService {
    ProductImageRepository productImageRepository;
    ProductRepository productRepository;
    IProductMapper productMapper;
    IFileUploadService fileUploadService;

    @Override
    @Transactional
    public ProductImageResponse createProductImage(Integer productId, MultipartFile image) throws IOException {
        Product product = productRepository.findById(productId)
            .orElseThrow(() -> new NotFoundException("Not found product with product id: " + productId));

        FileUploadResponse fileUploadResponse = fileUploadService.uploadImageFile(image);
        int position = 1;
        if (!product.getImages().isEmpty()) {
            position = product.getImages().size() + 1;
        }

        ProductImage imageEntity = ProductImage.builder()
            .product(product)
            .src(fileUploadResponse.getUrl())
            .filename(fileUploadResponse.getOriginalName())
            .size(fileUploadResponse.getSize())
            .assetId(fileUploadResponse.getAssetId())
            .position(position)
            .build();

        product.getImages().add(imageEntity);
        return productMapper.imageToResponse(productImageRepository.save(imageEntity));
    }

    @Override
    public ProductImageResponse getProductImageById(Integer id, Integer productId) {
        ProductImage image = productImageRepository.findByIdAndProduct_Id(id, productId)
            .orElseThrow(() -> new NotFoundException("Not found image with id: " + id + " and product id: " + productId));
        return productMapper.imageToResponse(image);
    }

    @Override
    public List<ProductImageResponse> getAllImagesWithProductId(Integer productId) {
        return productImageRepository.
            findAllByProductId(productId).stream().map(
                productMapper::imageToResponse).toList();
    }

    @Override
    public void deleteProductImageById(Integer id, Integer productId) throws Exception {
        ProductImage productImage = productImageRepository.findById(id)
            .orElseThrow(() -> new NotFoundException("Not found product image with id: " + id));

        fileUploadService.deleteFile(productImage.getAssetId());

        productImageRepository.deleteProductImageByIdAndProduct_id(id, productId);
    }
}
