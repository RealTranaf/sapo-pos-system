package com.sapo.mockprojectpossystem.controller;

import com.sapo.mockprojectpossystem.dto.request.ProductCreateRequest;
import com.sapo.mockprojectpossystem.dto.request.ProductSearchRequest;
import com.sapo.mockprojectpossystem.dto.request.ProductUpdateRequest;
import com.sapo.mockprojectpossystem.dto.request.ProductVariantRequest;
import com.sapo.mockprojectpossystem.dto.response.PaginatedResponse;
import com.sapo.mockprojectpossystem.dto.response.ProductImageResponse;
import com.sapo.mockprojectpossystem.dto.response.ProductResponse;
import com.sapo.mockprojectpossystem.dto.response.ProductVariantResponse;
import com.sapo.mockprojectpossystem.service.interfaces.IProductImageService;
import com.sapo.mockprojectpossystem.service.interfaces.IProductService;
import com.sapo.mockprojectpossystem.service.interfaces.IProductVariantService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("/products")
public class ProductController {
    private final IProductService productService;
    private final IProductImageService productImageService;
    private final IProductVariantService productVariantService;

    @GetMapping
    public ResponseEntity<PaginatedResponse<ProductResponse>> getOrSearchProducts(
        @Valid ProductSearchRequest productSearchRequest
    ) {
        PaginatedResponse<ProductResponse> response = productService.getOrSearchProducts(productSearchRequest);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{productId}")
    public ResponseEntity<ProductResponse> getProductById(@PathVariable Long productId) {
        ProductResponse response = productService.getProductById(productId);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('OWNER', 'SALES')")
    public ResponseEntity<ProductResponse> createProduct(
        @Valid @RequestPart("product") ProductCreateRequest request,
        @RequestPart(value = "images", required = false) List<MultipartFile> images
    ) throws IOException {
        ProductResponse response = productService.createProduct(request, images);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{productId}")
    @PreAuthorize("hasAnyRole('OWNER', 'SALES')")
    public ResponseEntity<ProductResponse> updateProduct(
        @PathVariable Long productId,
        @Valid @RequestBody ProductUpdateRequest request
    ) {
        ProductResponse response = productService.updateProduct(productId, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{productId}")
    @PreAuthorize("hasAnyRole('OWNER', 'SALES')")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long productId) {
        productService.deleteProduct(productId);
        return ResponseEntity.noContent().build();
    }
    // product images

    @PostMapping("/{productId}/images")
    @PreAuthorize("hasAnyRole('OWNER', 'SALES')")
    public ResponseEntity<ProductImageResponse> createProductImage(@PathVariable Long productId, @RequestPart("image") MultipartFile image) throws IOException {
        return ResponseEntity.status(HttpStatus.CREATED).body(productImageService.createProductImage(productId, image));
    }

    @GetMapping("/{productId}/images/{imageId}")
    @PreAuthorize("hasAnyRole('OWNER', 'SALES')")
    public ResponseEntity<ProductImageResponse> getProductImageById(@PathVariable Long productId, @PathVariable Long imageId) {
        return ResponseEntity.status(HttpStatus.OK).body(productImageService.getProductImageById(imageId, productId));
    }

    @GetMapping("/{productId}/images")
    @PreAuthorize("hasAnyRole('OWNER', 'SALES')")
    public ResponseEntity<List<ProductImageResponse>> getAllProductImageWithProductId(@PathVariable Long productId) {
        return ResponseEntity.status(HttpStatus.OK).body(productImageService.getAllImagesWithProductId(productId));
    }

    @DeleteMapping("/{productId}/images/{imageId}")
    @PreAuthorize("hasAnyRole('OWNER', 'SALES')")
    public ResponseEntity<?> deleteProductImageById(@PathVariable Long productId, @PathVariable Long imageId) throws Exception {
        productImageService.deleteProductImageById(imageId, productId);
        return ResponseEntity.noContent().build();
    }

    // product variants

    @GetMapping("/{productId}/variants/{variantId}")
    @PreAuthorize("hasAnyRole('OWNER', 'SALES')")
    public ResponseEntity<ProductVariantResponse> getProductVariantById(@PathVariable Long productId, @PathVariable Long variantId) {
        return ResponseEntity.status(HttpStatus.OK).body(productVariantService.getProductVariantById(variantId, productId));
    }

    @GetMapping("/{productId}/variants")
    public ResponseEntity<List<ProductVariantResponse>> getAllProductVariantsByProductId(@PathVariable Long productId) {
        return ResponseEntity.ok(productVariantService.getAllProductVariantsByProductId(productId));
    }

    @PutMapping("/{productId}/variants/{variantId}")
    @PreAuthorize("hasAnyRole('OWNER', 'SALES')")
    public ResponseEntity<ProductVariantResponse> updateProductVariantWithProductId(
        @PathVariable Long productId,
        @PathVariable Long variantId,
        @RequestBody ProductVariantRequest request) {
        return ResponseEntity.status(HttpStatus.OK).body(productVariantService.updateProductVariant(variantId, productId, request));
    }

    @DeleteMapping("/{productId}/variants/{variantId}")
    @PreAuthorize("hasAnyRole('OWNER', 'SALES')")
    public ResponseEntity<?> deleteProductVariantById(@PathVariable Long productId, @PathVariable Long variantId) {
        productVariantService.deleteProductVariantById(variantId, productId);
        return ResponseEntity.noContent().build();
    }
}
