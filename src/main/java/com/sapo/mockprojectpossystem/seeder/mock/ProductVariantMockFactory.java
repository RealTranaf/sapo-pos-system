package com.sapo.mockprojectpossystem.seeder.mock;

import com.sapo.mockprojectpossystem.product.domain.model.Product;
import com.sapo.mockprojectpossystem.product.domain.model.ProductVariant;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ProductVariantMockFactory {

    private static final Random random = new Random();

    public static List<ProductVariant> all(List<Product> products) {
        List<ProductVariant> variants = new ArrayList<>();
        int barcodeCounter = 10000000;

        for (Product p : products) {
            int count = 1 + random.nextInt(5); // 1–5 variants per product

            for (int j = 1; j <= count; j++) {
                ProductVariant v = new ProductVariant();

                double price = 100 + random.nextInt(900);

                v.setProduct(p);
                v.setTitle(p.getName() + " Variant " + j);

                v.setPrice(price);
                v.setBasePrice(price);
                v.setCompareAtPrice(price + random.nextInt(200));

                v.setSku("SKU" + p.getName().replaceAll("\\s", "") + j);
                v.setBarcode(String.valueOf(barcodeCounter++));

                v.setInventoryQuantity(10 + random.nextInt(90));
                v.setTaxable(true);

                v.setOption1("Size " + (j * 5));
                v.setOption2("Color " + j);
                v.setOption3(null);

                v.setUnit("pcs");

                v.setPosition(j);

                variants.add(v);
            }
        }

        return variants;
    }
}
