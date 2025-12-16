package com.sapo.mockprojectpossystem.service.interfaces;

import com.sapo.mockprojectpossystem.dto.response.ProductImageResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface IProductImageService {
    ProductImageResponse createProductImage(Long productId, MultipartFile image) throws IOException;

    ProductImageResponse getProductImageById(Long id, Long productId);

    List<ProductImageResponse> getAllImagesWithProductId(Long productId);

    void deleteProductImageById(Long id, Long productId) throws Exception;
}
