package com.sapo.mockprojectpossystem.seeder.mock;

import com.sapo.mockprojectpossystem.product.domain.model.Product;
import com.sapo.mockprojectpossystem.product.domain.model.ProductOption;
import com.sapo.mockprojectpossystem.product.domain.model.ProductOptionValue;

import java.util.List;

public class ProductOptionMockFactory {

    public static List<ProductOption> forProduct() {

        ProductOption color = ProductOption.create("Color", 1);
        color.addValue(ProductOptionValue.create("Red"));
        color.addValue(ProductOptionValue.create("Blue"));
        color.addValue(ProductOptionValue.create("Black"));

        ProductOption size = ProductOption.create("Size", 2);
        size.addValue(ProductOptionValue.create("S"));
        size.addValue(ProductOptionValue.create("M"));
        size.addValue(ProductOptionValue.create("L"));

        return List.of(color, size);
    }
}
