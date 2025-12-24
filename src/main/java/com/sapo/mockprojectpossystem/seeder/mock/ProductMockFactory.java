package com.sapo.mockprojectpossystem.seeder.mock;

import com.sapo.mockprojectpossystem.model.*;
import com.sapo.mockprojectpossystem.repository.*;

import java.util.List;

public class ProductMockFactory {

    public static void seedProducts(
            ProductRepository productRepository,
            BrandRepository brandRepository,
            TypeRepository typeRepository,
            UserRepository userRepository
    ) {
        // 1. Lấy data cần thiết
        List<Brand> brands = brandRepository.findAll();
        List<Type> types = typeRepository.findAll();
        int createdByUserId = userRepository.findAll().get(0).getId();

        // 2. Tạo PRODUCTS
        List<Product> products = all(brands, types, createdByUserId);

        // 3. Save products trước (BẮT BUỘC)
        productRepository.saveAll(products);

        // 4. OPTIONS
        for (Product p : products) {
            List<ProductOption> options = ProductOptionMockFactory.forProduct(p);
            p.setOptions(options);
        }

        // 5. VARIANTS
        List<ProductVariant> variants = ProductVariantMockFactory.all(products);
        for (Product p : products) {
            p.setVariants(
                    variants.stream()
                            .filter(v -> v.getProduct().equals(p))
                            .toList()
            );
        }

        // 6. IMAGES
        List<ProductImage> images = ProductImageMockFactory.all(products);
        for (Product p : products) {
            p.setImages(
                    images.stream()
                            .filter(i -> i.getProduct().equals(p))
                            .toList()
            );
        }

        // 7. Save lại để cascade ALL
        productRepository.saveAll(products);
    }

    // ================= ORIGINAL METHOD =================

    public static List<Product> all(List<Brand> brands, List<Type> types, int createdByUserId) {
        List<Product> products = new java.util.ArrayList<>();
        java.util.Random random = new java.util.Random();

        for (int i = 1; i <= 5000; i++) {

            // random 1–3 types
            java.util.Set<Type> productTypes = new java.util.HashSet<>();
            for (int j = 0; j < 1 + random.nextInt(3); j++) {
                productTypes.add(types.get(random.nextInt(types.size())));
            }

            Product p = Product.builder()
                    .name("Product " + i)
                    .summary("Summary of product " + i)
                    .content("Content of product " + i)
                    .status(random.nextBoolean()
                            ? com.sapo.mockprojectpossystem.enums.ProductStatus.ACTIVE
                            : com.sapo.mockprojectpossystem.enums.ProductStatus.INACTIVE)
                    .brand(brands.get(random.nextInt(brands.size())))
                    .createdByUserId(createdByUserId)
                    .createdAt(java.time.LocalDateTime.now().minusDays(random.nextInt(365)))
                    .types(productTypes)
                    .build();

            products.add(p);
        }

        return products;
    }
}
