package com.sapo.mockprojectpossystem.seeder.mock;

import com.sapo.mockprojectpossystem.model.Customer;
import com.sapo.mockprojectpossystem.model.Gender;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CustomerMockFactory {

    private static final Random random = new Random();

    public static List<Customer> all() {
        List<Customer> customers = new ArrayList<>();

        for (int i = 1; i <= 1000; i++) {
            Customer c = new Customer();
            c.setName("Customer " + i);
            c.setPhoneNum(generatePhoneNum(i));
            c.setGender(randomGender());
            c.setNote("Mock customer #" + i);
            c.setCreatedAt(LocalDateTime.now().minusDays(random.nextInt(365)));

            customers.add(c);
        }

        return customers;
    }

    private static String generatePhoneNum(int index) {
        return String.format("09%07d", index);
    }

    private static Gender randomGender() {
        int r = random.nextInt(3);
        switch (r) {
            case 0: return Gender.MALE;
            case 1: return Gender.FEMALE;
            default: return Gender.NaN;
        }
    }
}
