package com.sapo.mockprojectpossystem.product.application.interfaces;

import com.sapo.mockprojectpossystem.product.interfaces.request.ProductCreateRequest;
import com.sapo.mockprojectpossystem.product.interfaces.request.ProductSearchRequest;
import com.sapo.mockprojectpossystem.product.interfaces.request.ProductUpdateRequest;
import com.sapo.mockprojectpossystem.common.response.PaginatedResponse;
import com.sapo.mockprojectpossystem.product.interfaces.response.ProductResponse;
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