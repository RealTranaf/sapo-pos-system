package com.sapo.mockprojectpossystem.seeder;

import com.sapo.mockprojectpossystem.customer.domain.model.Customer;
import com.sapo.mockprojectpossystem.customer.domain.repository.CustomerRepository;
import com.sapo.mockprojectpossystem.repository.*;
import com.sapo.mockprojectpossystem.seeder.mock.*;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Profile("dev")
@RequiredArgsConstructor
public class DevDatabaseSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final BrandRepository brandRepository;
    private final TypeRepository typeRepository;
    private final CustomerRepository customerRepository;
    private final ProductRepository productRepository;

    // ✅ THÊM
    private final PurchaseRepository purchaseRepository;
    private final ProductVariantRepository productVariantRepository;

    @Override
    public void run(String... args) {
        System.out.println("🔥 DevDatabaseSeeder RUNNING");

        if (userRepository.count() == 0) {
            userRepository.saveAll(UserMockFactory.all());
            System.out.println("✅ Seeded USERS");
        }

        if (brandRepository.count() == 0) {
            brandRepository.saveAll(BrandMockFactory.all());
            System.out.println("✅ Seeded BRANDS");
        }

        if (typeRepository.count() == 0) {
            typeRepository.saveAll(TypeMockFactory.all());
            System.out.println("✅ Seeded TYPES");
        }

        if (customerRepository.count() == 0) {
            int count = 20;
            List<Customer> customers = CustomerMockFactory.generate(count);
            for (int i = 0; i < count; i++) {
                customerRepository.save(customers.get(i));
            }
            System.out.println("✅ Seeded CUSTOMERS");
        }

        if (productRepository.count() == 0) {
            ProductMockFactory.seedProducts(
                    productRepository,
                    brandRepository,
                    typeRepository,
                    userRepository
            );
            System.out.println("✅ Seeded PRODUCTS + VARIANTS + OPTIONS + IMAGES");
        }

        // ✅ THÊM Ở ĐÂY
        if (purchaseRepository.count() == 0) {
            purchaseRepository.saveAll(
                    PurchaseMockFactory.all(
                            customerRepository.findAll(),
                            userRepository.findAll(),
                            productVariantRepository.findAll()
                    )
            );
            System.out.println("✅ Seeded PURCHASES + PURCHASE ITEMS");
        }

        System.out.println("🔥 DevDatabaseSeeder FINISHED");
    }
}
