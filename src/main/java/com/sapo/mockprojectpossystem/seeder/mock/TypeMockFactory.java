package com.sapo.mockprojectpossystem.seeder.mock;

import com.sapo.mockprojectpossystem.model.Type;
import java.util.ArrayList;
import java.util.List;

public class TypeMockFactory {

    public static List<Type> all() {
        List<Type> types = new ArrayList<>();
        String[] names = {
                "T-Shirt", "Polo", "Jean", "Short", "Jacket", "Dress", "Hoodie", "Sweater", "Tracksuit",
                "Skirt", "Blouse", "Coat", "Cardigan", "Tank Top", "Leggings", "Shirt", "Pants", "Jeans",
                "Jumpsuit", "Suit", "Scarf", "Gloves", "Hat", "Belt", "Shoes", "Sneakers", "Boots", "Sandals",
                "Flip-flops", "Socks"
        };

        for (String name : names) {
            Type t = new Type();
            t.setName(name);
            types.add(t);
        }

        return types;
    }
}
