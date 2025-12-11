package com.sapo.mockprojectpossystem.controller;

import com.sapo.mockprojectpossystem.model.Product;
import com.sapo.mockprojectpossystem.model.ProductStatus;
import com.sapo.mockprojectpossystem.response.MessageResponse;
import com.sapo.mockprojectpossystem.response.ProductResponse;
import com.sapo.mockprojectpossystem.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/products")
public class ProductController {
    private final ProductService productService;

    // Lấy danh sách product, có sorting và tìm kiếm
    @GetMapping
    public ResponseEntity<?> getAllProducts(@RequestParam(required = false) String keyword,
                                            @RequestParam(required = false) Integer brandId,
                                            @RequestParam(required = false) List<Integer> typeIds,
                                            @RequestParam(required = false) ProductStatus status,
                                            @RequestParam(required = false) Double minBasePrice,
                                            @RequestParam(required = false) Double maxBasePrice,
                                            @RequestParam(required = false) Double minSellPrice,
                                            @RequestParam(required = false) Double maxSellPrice,
                                            @RequestParam(defaultValue = "0") int page,
                                            @RequestParam(defaultValue = "10") int size,
                                            @RequestParam(defaultValue = "createdAt") String sortBy,
                                            @RequestParam(defaultValue = "desc") String sortDir
    ) {
        try {
            Page<Product> productPage = productService.getAllProduct(keyword, brandId, typeIds, status,
                    minBasePrice, maxBasePrice, minSellPrice, maxSellPrice,
                    page, size, sortBy, sortDir
            );

            List<ProductResponse> productResponses =
                    productPage.stream().map(ProductResponse::new).toList();

            Map<String, Object> response = new HashMap<>();
            response.put("products", productResponses);
            response.put("currentPage", productPage.getNumber());
            response.put("totalItems", productPage.getTotalElements());
            response.put("totalPages", productPage.getTotalPages());

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }

    // Lấy product theo ID
    @GetMapping("/{id}")
    public ResponseEntity<?> getProductById(@PathVariable Integer id) {
        try {
            Product product = productService.getProductById(id);
            return ResponseEntity.ok(new ProductResponse(product));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }

    // Thêm product mới
    @PostMapping
    public ResponseEntity<?> addProduct(@RequestParam String name,
                                        @RequestParam String sku,
                                        @RequestParam(required = false) String barcode,
                                        @RequestParam ProductStatus status,
                                        @RequestParam(required = false) String description,
                                        @RequestParam double basePrice,
                                        @RequestParam double sellPrice,
                                        @RequestParam int quantity,
                                        @RequestParam(required = false) Integer brandId,
                                        @RequestParam(required = false) List<Integer> typeIds) {
        try {
            productService.createProduct(name, sku, barcode,
                    status, description, basePrice,
                    sellPrice, quantity, brandId, typeIds);
            return ResponseEntity.ok("Product added successfully!");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }

    // Cập nhật product
    @PutMapping("/{id}")
    public ResponseEntity<?> updateProduct(@PathVariable Integer id,
                                            @RequestParam String name,
                                            @RequestParam String sku,
                                            @RequestParam(required = false) String barcode,
                                            @RequestParam ProductStatus status,
                                            @RequestParam(required = false) String description,
                                            @RequestParam double basePrice,
                                            @RequestParam double sellPrice,
                                            @RequestParam int quantity,
                                            @RequestParam(required = false) Integer brandId,
                                            @RequestParam(required = false) List<Integer> typeIds) {
        try {
            productService.updateProduct(id, name, sku, barcode,
                    status, description, basePrice,
                    sellPrice, quantity, brandId, typeIds);
            return ResponseEntity.ok("Product updated successfully!");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }
}
