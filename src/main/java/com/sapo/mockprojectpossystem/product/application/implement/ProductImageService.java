package com.sapo.mockprojectpossystem.product.application.implement;

import com.sapo.mockprojectpossystem.common.response.FileUploadResponse;
import com.sapo.mockprojectpossystem.product.application.interfaces.IProductMapper;
import com.sapo.mockprojectpossystem.product.domain.model.ProductStatus;
import com.sapo.mockprojectpossystem.product.application.response.ProductImageResponse;
import com.sapo.mockprojectpossystem.product.domain.model.Product;
import com.sapo.mockprojectpossystem.product.domain.model.ProductImage;
import com.sapo.mockprojectpossystem.common.exception.NotFoundException;
import com.sapo.mockprojectpossystem.product.domain.repository.ProductRepository;
import com.sapo.mockprojectpossystem.common.service.IFileUploadService;
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
public class ProductImageService {
    ProductRepository productRepository;
    IProductMapper productMapper;
    IFileUploadService fileUploadService;

    @Transactional
    public ProductImageResponse createProductImage(Integer productId, MultipartFile image) throws IOException {
        Product product = loadActiveProduct(productId);

        FileUploadResponse upload = fileUploadService.uploadImageFile(image);

        ProductImage productImage = product.addImage(
                product.getImages().size() + 1,
                upload.getUrl(),
                upload.getOriginalName(),
                upload.getAssetId(),
                upload.getSize()
        );

        productRepository.save(product);

        return productMapper.imageToResponse(productImage);
    }

    @Transactional(readOnly = true)
    public ProductImageResponse getProductImageById(Integer imageId, Integer productId) {
        Product product = loadActiveProduct(productId);
        return productMapper.imageToResponse(product.getImage(imageId));
    }

    @Transactional(readOnly = true)
    public List<ProductImageResponse> getAllImagesWithProductId(Integer productId) {
        Product product = loadActiveProduct(productId);
        return product.getImages().stream()
                .map(productMapper::imageToResponse)
                .toList();
    }

    public void deleteProductImageById(Integer imageId, Integer productId) throws Exception {
        Product product = loadActiveProduct(productId);

        ProductImage image = product.getImage(imageId);

        fileUploadService.deleteFile(image.getAssetId());

        product.removeImage(imageId);

        productRepository.save(product);
    }

    private Product loadActiveProduct(Integer productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new NotFoundException("Product not found"));
        if (product.getStatus() != ProductStatus.ACTIVE) {
            throw new NotFoundException("Product not active");
        }
        return product;
    }
}
