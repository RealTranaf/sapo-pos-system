package com.sapo.mockprojectpossystem.seeder.mock;

import com.sapo.mockprojectpossystem.product.domain.model.ProductVariant;
import com.sapo.mockprojectpossystem.purchase.domain.model.Purchase;
import com.sapo.mockprojectpossystem.purchase.domain.model.PurchaseItem;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class PurchaseItemMockFactory {

    private static final Random random = new Random();

    public static List<PurchaseItem> forPurchase(
            Purchase purchase,
            List<ProductVariant> variants
    ) {
        List<PurchaseItem> items = new ArrayList<>();

        int itemCount = 1 + random.nextInt(5); // 1–5 items / purchase

        for (int i = 0; i < itemCount; i++) {
            ProductVariant variant = variants.get(random.nextInt(variants.size()));
            int quantity = 1 + random.nextInt(5);

            PurchaseItem item = new PurchaseItem();
            item.setPurchase(purchase);
            item.setProductVariant(variant);
            item.setQuantity(quantity);
            item.setTotalPrice(quantity * variant.getPrice());

            items.add(item);
        }

        return items;
    }
}
