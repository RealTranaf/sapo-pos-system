package com.sapo.mockprojectpossystem.service.interfaces;

import com.sapo.mockprojectpossystem.dto.request.ProductCreateRequest;
import com.sapo.mockprojectpossystem.dto.request.ProductSearchRequest;
import com.sapo.mockprojectpossystem.dto.request.ProductUpdateRequest;
import com.sapo.mockprojectpossystem.dto.response.PaginatedResponse;
import com.sapo.mockprojectpossystem.dto.response.ProductResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface IProductService {
    PaginatedResponse<ProductResponse> getOrSearchProducts(ProductSearchRequest request);

    ProductResponse getProductById(Integer productId);

    ProductResponse createProduct(ProductCreateRequest request, List<MultipartFile> images) throws IOException;

    ProductResponse updateProduct(Integer productId, ProductUpdateRequest request);

    void deleteProduct(Integer productId);
}