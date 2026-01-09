package com.sapo.mockprojectpossystem.seeder.mock;

import com.sapo.mockprojectpossystem.product.domain.model.Brand;
import java.util.ArrayList;
import java.util.List;

public class BrandMockFactory {

    public static List<Brand> all() {
        List<Brand> brands = new ArrayList<>();
        String[] names = {
                "Uniqlo", "Zara", "H&M", "Levi’s", "Nike", "Adidas", "Puma", "Giordano", "Gap", "FILA",
                "Converse", "New Balance", "Reebok", "Under Armour", "Columbia", "Patagonia", "The North Face",
                "Tommy Hilfiger", "Calvin Klein", "Diesel", "Guess", "Mango", "Bershka", "Pull&Bear",
                "Superdry", "Oakley", "Vans", "Asics", "Skechers", "Champion"
        };

        for (String name : names) {
            Brand b = new Brand();
            b.setName(name);
            brands.add(b);
        }

        return brands;
    }
}
