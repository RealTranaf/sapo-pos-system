package com.sapo.mockprojectpossystem.seeder.mock;

import com.sapo.mockprojectpossystem.customer.domain.model.Customer;
import com.sapo.mockprojectpossystem.customer.domain.enums.Gender;
import com.sapo.mockprojectpossystem.customer.domain.model.PhoneNumber;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class CustomerMockFactory {

    private static final Random RANDOM = new Random();
    // Cần refactor

    public static List<Customer> generate(int count) {
        return IntStream.rangeClosed(1, count)
                .mapToObj(CustomerMockFactory::createCustomer)
                .collect(Collectors.toList());
    }

    private static Customer createCustomer(int index) {
        var customer = Customer.create(
                "Customer " + index,
                new PhoneNumber(generatePhoneNum(index)),
                randomGender(),
                "Mock customer #" + index
        );

        return customer;
    }

    private static String generatePhoneNum(int index) {
        return String.format("09%08d", index);
    }

    private static Gender randomGender() {
        int r = RANDOM.nextInt(3);
        switch (r) {
            case 0: return Gender.MALE;
            case 1: return Gender.FEMALE;
            default: return Gender.NaN;
        }
    }
}
