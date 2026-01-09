package com.sapo.mockprojectpossystem.product.interfaces.rest;

import com.sapo.mockprojectpossystem.product.interfaces.request.ProductCreateRequest;
import com.sapo.mockprojectpossystem.product.interfaces.request.ProductSearchRequest;
import com.sapo.mockprojectpossystem.product.interfaces.request.ProductUpdateRequest;
import com.sapo.mockprojectpossystem.product.interfaces.request.ProductVariantRequest;
import com.sapo.mockprojectpossystem.common.response.PaginatedResponse;
import com.sapo.mockprojectpossystem.product.interfaces.response.ProductImageResponse;
import com.sapo.mockprojectpossystem.product.interfaces.response.ProductResponse;
import com.sapo.mockprojectpossystem.product.interfaces.response.ProductVariantResponse;
import com.sapo.mockprojectpossystem.product.application.interfaces.IProductImageService;
import com.sapo.mockprojectpossystem.product.application.interfaces.IProductService;
import com.sapo.mockprojectpossystem.product.application.interfaces.IProductVariantService;
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
    public ResponseEntity<ProductResponse> getProductById(@PathVariable Integer productId) {
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
        @PathVariable Integer productId,
        @Valid @RequestBody ProductUpdateRequest request
    ) {
        ProductResponse response = productService.updateProduct(productId, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{productId}")
    @PreAuthorize("hasAnyRole('OWNER', 'SALES')")
    public ResponseEntity<Void> deleteProduct(@PathVariable Integer productId) {
        productService.deleteProduct(productId);
        return ResponseEntity.noContent().build();
    }
    // product images

    @PostMapping("/{productId}/images")
    @PreAuthorize("hasAnyRole('OWNER', 'SALES')")
    public ResponseEntity<ProductImageResponse> createProductImage(@PathVariable Integer productId, @RequestPart("image") MultipartFile image) throws IOException {
        return ResponseEntity.status(HttpStatus.CREATED).body(productImageService.createProductImage(productId, image));
    }

    @GetMapping("/{productId}/images/{imageId}")
    @PreAuthorize("hasAnyRole('OWNER', 'SALES')")
    public ResponseEntity<ProductImageResponse> getProductImageById(@PathVariable Integer productId, @PathVariable Integer imageId) {
        return ResponseEntity.status(HttpStatus.OK).body(productImageService.getProductImageById(imageId, productId));
    }

    @GetMapping("/{productId}/images")
    @PreAuthorize("hasAnyRole('OWNER', 'SALES')")
    public ResponseEntity<List<ProductImageResponse>> getAllProductImageWithProductId(@PathVariable Integer productId) {
        return ResponseEntity.status(HttpStatus.OK).body(productImageService.getAllImagesWithProductId(productId));
    }

    @DeleteMapping("/{productId}/images/{imageId}")
    @PreAuthorize("hasAnyRole('OWNER', 'SALES')")
    public ResponseEntity<?> deleteProductImageById(@PathVariable Integer productId, @PathVariable Integer imageId) throws Exception {
        productImageService.deleteProductImageById(imageId, productId);
        return ResponseEntity.noContent().build();
    }

    // product variants

    @GetMapping("/{productId}/variants/{variantId}")
    @PreAuthorize("hasAnyRole('OWNER', 'SALES')")
    public ResponseEntity<ProductVariantResponse> getProductVariantById(@PathVariable Integer productId, @PathVariable Integer variantId) {
        return ResponseEntity.status(HttpStatus.OK).body(productVariantService.getProductVariantById(variantId, productId));
    }

    @GetMapping("/{productId}/variants")
    public ResponseEntity<List<ProductVariantResponse>> getAllProductVariantsByProductId(@PathVariable Integer productId) {
        return ResponseEntity.ok(productVariantService.getAllProductVariantsByProductId(productId));
    }

    @PutMapping("/{productId}/variants/{variantId}")
    @PreAuthorize("hasAnyRole('OWNER', 'SALES')")
    public ResponseEntity<ProductVariantResponse> updateProductVariantWithProductId(
        @PathVariable Integer productId,
        @PathVariable Integer variantId,
        @RequestBody ProductVariantRequest request) {
        return ResponseEntity.status(HttpStatus.OK).body(productVariantService.updateProductVariant(variantId, productId, request));
    }

    @DeleteMapping("/{productId}/variants/{variantId}")
    @PreAuthorize("hasAnyRole('OWNER', 'SALES')")
    public ResponseEntity<?> deleteProductVariantById(@PathVariable Integer productId, @PathVariable Integer variantId) {
        productVariantService.deleteProductVariantById(variantId, productId);
        return ResponseEntity.noContent().build();
    }
}
