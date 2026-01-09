package com.sapo.mockprojectpossystem.seeder.mock;

import com.sapo.mockprojectpossystem.product.domain.model.Product;
import com.sapo.mockprojectpossystem.product.domain.model.ProductImage;
import java.util.UUID;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ProductImageMockFactory {
    private static final Random random = new Random();

    public static List<ProductImage> all(List<Product> products) {
        List<ProductImage> images = new ArrayList<>();
        for (Product p : products) {
            int count = 1 + random.nextInt(3); // 1-3 images per product
            for (int j = 1; j <= count; j++) {
                ProductImage img = new ProductImage();
                img.setProduct(p);
                img.setPosition(j);
                img.setSrc("https://picsum.photos/seed/" + p.getName().replaceAll(" ", "") + j + "/400/400");
                img.setFilename(p.getName().replaceAll(" ", "") + "_" + j + ".jpg");
                img.setAssetId(UUID.randomUUID().toString());
                img.setSize(1024);
                img.setAlt(p.getName() + " image " + j);
                images.add(img);
            }
        }
        return images;
    }
}
