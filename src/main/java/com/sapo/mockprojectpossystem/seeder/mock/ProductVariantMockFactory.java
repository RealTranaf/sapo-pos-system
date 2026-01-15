package com.sapo.mockprojectpossystem.seeder.mock;

import com.sapo.mockprojectpossystem.product.domain.model.Product;
import com.sapo.mockprojectpossystem.product.domain.model.ProductVariant;
import com.sapo.mockprojectpossystem.product.application.request.ProductVariantRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ProductVariantMockFactory {

    private static final Random random = new Random();
    private static int barcodeCounter = 10000000;

    public static List<ProductVariant> forProduct(Product product) {
        List<ProductVariant> variants = new ArrayList<>();

        int count = 1 + random.nextInt(5); // 1–5 variants per product

        for (int j = 1; j <= count; j++) {

            double price = 100 + random.nextInt(900);
            int inventory = 10 + random.nextInt(90);

            // ✅ create via domain factory
            ProductVariant variant = ProductVariant.create(
                    product.getName() + " Variant " + j,
                    j,
                    price,
                    inventory
            );

            // ✅ maintain aggregate consistency
            variant.assignTo(product);

            // DTO used only for bulk mock convenience
            ProductVariantRequest request = ProductVariantRequest.builder()
                    .sku("SKU" + product.getName().replaceAll("\\s", "") + j)
                    .barcode(String.valueOf(barcodeCounter++))
                    .price(price)
                    .basePrice(price)
                    .compareAtPrice(price + random.nextInt(200))
                    .title(variant.getTitle())
                    .option1("Size " + (j * 5))
                    .option2("Color " + j)
                    .option3(null)
                    .taxable(true)
                    .inventoryQuantity(inventory)
                    .unit("pcs")
                    .position(j)
                    .build();

            variant.updateFrom(request);

            variants.add(variant);
        }

        return variants;
    }
}
