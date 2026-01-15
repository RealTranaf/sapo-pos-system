package com.sapo.mockprojectpossystem.seeder.mock;

import com.sapo.mockprojectpossystem.auth.domain.model.User;
import com.sapo.mockprojectpossystem.auth.domain.repository.UserRepository;
import com.sapo.mockprojectpossystem.product.domain.model.ProductStatus;
import com.sapo.mockprojectpossystem.product.domain.model.*;
import com.sapo.mockprojectpossystem.product.domain.repository.BrandRepository;
import com.sapo.mockprojectpossystem.product.domain.repository.ProductRepository;
import com.sapo.mockprojectpossystem.product.domain.repository.TypeRepository;

import java.util.*;

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
        User user = userRepository.findAll().get(0);

        // 2. Tạo PRODUCTS
        List<Product> products = createProducts(brands, types, user);

        // 3. Save products trước (BẮT BUỘC)
        productRepository.saveAll(products);

        // 4. OPTIONS
        for (Product product : products) {
            product.replaceOptions(
                    ProductOptionMockFactory.forProduct()
            );
            product.replaceVariants(
                    ProductVariantMockFactory.forProduct(product)
            );

            ProductImageMockFactory.forProduct(product);
        }

        // 7. Save lại để cascade ALL
        productRepository.saveAll(products);
    }

    // ================= ORIGINAL METHOD =================

    private static List<Product> createProducts(List<Brand> brands, List<Type> types, User user) {
        List<Product> products = new ArrayList<>();
        java.util.Random random = new Random();

        for (int i = 1; i <= 5000; i++) {

            Product product = Product.create(
                    "Product " + i,
                    "Summary of product " + i,
                    "Content of product " + i,
                    random.nextBoolean()
                            ? ProductStatus.ACTIVE
                            : ProductStatus.INACTIVE,
                    user
            );

            // brand
            product.updateBrand(
                    brands.get(random.nextInt(brands.size()))
            );

            // types
            Set<Type> productTypes = new HashSet<>();
            for (int j = 0; j < 1 + random.nextInt(3); j++) {
                productTypes.add(types.get(random.nextInt(types.size())));
            }
            product.syncTypes(productTypes);

            products.add(product);
        }

        return products;
    }
}
