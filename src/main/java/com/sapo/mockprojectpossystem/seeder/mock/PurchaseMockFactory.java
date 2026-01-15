package com.sapo.mockprojectpossystem.seeder.mock;

import com.sapo.mockprojectpossystem.auth.domain.model.User;
import com.sapo.mockprojectpossystem.customer.domain.model.Customer;
import com.sapo.mockprojectpossystem.product.domain.model.ProductVariant;
import com.sapo.mockprojectpossystem.purchase.domain.model.Purchase;
import com.sapo.mockprojectpossystem.purchase.domain.model.PurchaseItem;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class PurchaseMockFactory {

    private static final Random random = new Random();

    public static List<Purchase> all(
            List<Customer> customers,
            List<User> users,
            List<ProductVariant> variants
    ) {
        List<Purchase> purchases = new ArrayList<>();

        for (int i = 0; i < 1000; i++) {
            Customer customer = customers.get(random.nextInt(customers.size()));
            User user = users.get(random.nextInt(users.size()));

            Purchase purchase = Purchase.create(customer, user, "Mock purchase #" + (i + 1));

            // Items
            int itemCount = random.nextInt(3) + 1; // 1–3 items
            for (int j = 0; j < itemCount; j++) {
                ProductVariant variant = randomFrom(variants);
                int quantity = random.nextInt(3) + 1;

                if (variant.getInventoryQuantity() < quantity) {
                    continue;
                }

                purchase.addItem(variant, quantity);
            }

            // Calculate total
            if (random.nextBoolean()) {
                double discount = purchase.getTotalAmount() * 0.05;
                purchase.applyDiscount(discount);
            }

            purchases.add(purchase);
        }

        return purchases;
    }

    private static <T> T randomFrom(List<T> list) {
        return list.get(random.nextInt(list.size()));
    }
}
