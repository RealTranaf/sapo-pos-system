package com.sapo.mockprojectpossystem.seeder.mock;

import com.sapo.mockprojectpossystem.product.domain.model.Product;
import com.sapo.mockprojectpossystem.product.domain.model.ProductImage;
import java.util.UUID;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ProductImageMockFactory {
    private static final Random random = new Random();

    public static void forProduct(Product product) {
        int count = 1 + random.nextInt(3);

        for (int i = 1; i <= count; i++) {
            product.addImage(
                    i,
                    "https://picsum.photos/seed/" +
                            product.getName().replaceAll(" ", "") + i + "/400/400",
                    product.getName().replaceAll(" ", "") + "_" + i + ".jpg",
                    UUID.randomUUID().toString(),
                    1024
            );
        }
    }
}
