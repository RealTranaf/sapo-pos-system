package com.sapo.mockprojectpossystem.product.application.interfaces;

import com.sapo.mockprojectpossystem.product.interfaces.response.ProductImageResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface IProductImageService {
    ProductImageResponse createProductImage(Integer productId, MultipartFile image) throws IOException;

    ProductImageResponse getProductImageById(Integer id, Integer productId);

    List<ProductImageResponse> getAllImagesWithProductId(Integer productId);

    void deleteProductImageById(Integer id, Integer productId) throws Exception;
}
