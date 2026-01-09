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

            Purchase purchase = new Purchase();
            purchase.setCustomer(customer);
            purchase.setUser(user);
            purchase.setNote("Mock purchase #" + (i + 1));

            // Items
            List<PurchaseItem> items =
                    PurchaseItemMockFactory.forPurchase(purchase, variants);

            purchase.setPurchaseItems(items);

            // Calculate total
            double total = items.stream()
                    .mapToDouble(PurchaseItem::getTotalPrice)
                    .sum();

            double discount = random.nextBoolean()
                    ? total * 0.05
                    : 0.0;

            purchase.setTotalAmount(total - discount);
            purchase.setDiscountAmount(discount);

            purchases.add(purchase);
        }

        return purchases;
    }
}
