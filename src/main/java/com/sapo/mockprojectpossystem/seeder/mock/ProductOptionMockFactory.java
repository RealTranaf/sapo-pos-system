package com.sapo.mockprojectpossystem.seeder.mock;

import com.sapo.mockprojectpossystem.model.Product;
import com.sapo.mockprojectpossystem.model.ProductOption;
import com.sapo.mockprojectpossystem.model.ProductOptionValue;

import java.util.List;

public class ProductOptionMockFactory {

    public static List<ProductOption> forProduct(Product product) {

        ProductOption color = ProductOption.builder()
                .product(product)
                .name("Color")
                .position(1)
                .build();

        ProductOption size = ProductOption.builder()
                .product(product)
                .name("Size")
                .position(2)
                .build();

        color.setValues(List.of(
                value(color, "Red"),
                value(color, "Blue"),
                value(color, "Black")
        ));

        size.setValues(List.of(
                value(size, "S"),
                value(size, "M"),
                value(size, "L")
        ));

        return List.of(color, size);
    }

    private static ProductOptionValue value(ProductOption option, String val) {
        return ProductOptionValue.builder()
                .option(option)
                .value(val)
                .build();
    }
}
