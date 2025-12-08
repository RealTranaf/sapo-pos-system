package com.sapo.mockprojectpossystem;

import com.sapo.mockprojectpossystem.model.*;
import com.sapo.mockprojectpossystem.repository.CustomerRepository;
import com.sapo.mockprojectpossystem.repository.PurchaseRepository;
import com.sapo.mockprojectpossystem.repository.UserRepository;
import com.sapo.mockprojectpossystem.service.AuthService;
import com.sapo.mockprojectpossystem.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.List;

@SpringBootApplication
@RequiredArgsConstructor
public class MockProjectPosSystemApplication implements CommandLineRunner {
    private final UserRepository userRepository;
    private final CustomerRepository customerRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthService authService;
    private final CustomerService customerService;

    public static void main(String[] args) {
        SpringApplication.run(MockProjectPosSystemApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        if (userRepository.findByUsername("Owner").isEmpty()) {
            User user = new User("Owner", "Trần Linh Duy", "0968682409", passwordEncoder.encode("admin"), true, Role.OWNER);
            userRepository.save(user);
        }
        if (userRepository.findByUsername("TEST_CS").isEmpty()) {
            authService.signup("TEST_CS", "Ngô Văn A", "0987654321", "testcs",true, Role.CS);
            authService.signup("TEST_SALES", "Ngô Văn B", "0987654322", "testsales", true, Role.SALES);
            authService.signup("TEST_WH", "Ngô Văn C", "0987654323", "testwh", true, Role.WAREHOUSE);
        }
        if (customerRepository.count() == 0) {
            customerService.createCustomer("Nguyen Van A", "0901111111", Gender.MALE, "VIP");
            customerService.createCustomer("Tran Thi B", "0902222222", Gender.FEMALE,"Frequent buyer");
            customerService.createCustomer("Le Van C", "0903333333", Gender.MALE,null);
            customerService.createCustomer("Pham Thi D", "0904444444", Gender.FEMALE,"Returning after 6 months");
            customerService.createCustomer("Hoang Van E", "0905555555", Gender.NaN,"New customer");
            customerService.createCustomer("Do Minh F", "0906666666", Gender.FEMALE,"Occasional buyer");
            customerService.createCustomer("Ly Thi G", "0907777777", Gender.FEMALE,null);
            customerService.createCustomer("Vo Van H", "0908888888", Gender.MALE,"VIP - high spender");
            customerService.createCustomer("Bui Thi I", "0909999999", Gender.FEMALE,"Prefers cash payments");
            customerService.createCustomer("Dang Van K", "0910000000", Gender.NaN,null);

            List<Customer> customers = customerRepository.findAll();

            Customer c1 = customers.get(0);
            Customer c2 = customers.get(1);
            Customer c3 = customers.get(2);
            Customer c4 = customers.get(3);
            Customer c5 = customers.get(4);
            Customer c6 = customers.get(5);
            Customer c7 = customers.get(6);
            Customer c8 = customers.get(7);
            Customer c9 = customers.get(8);
            Customer c10 = customers.get(9);

            customerService.addPurchase(c1.getId(), 1200000, LocalDateTime.now().minusDays(1), "Test 1");
            customerService.addPurchase(c1.getId(), 800000, LocalDateTime.now().minusDays(10), "Test 2");
            customerService.addPurchase(c2.getId(), 2300000, LocalDateTime.now().minusDays(2), "Test 3");
            customerService.addPurchase(c2.getId(), 700000, LocalDateTime.now().minusDays(5), "Test 4");
            customerService.addPurchase(c3.getId(), 600000, LocalDateTime.now().minusDays(3), "Test 5");
            customerService.addPurchase(c4.getId(), 3300000, LocalDateTime.now().minusDays(30), "Test 6");
            customerService.addPurchase(c5.getId(), 400000, LocalDateTime.now(), "Test 7");
            customerService.addPurchase(c6.getId(), 950000, LocalDateTime.now().minusDays(4), "Test 8");
            customerService.addPurchase(c7.getId(), 420000, LocalDateTime.now().minusDays(12), "Test 9");
            customerService.addPurchase(c8.getId(), 5100000, LocalDateTime.now().minusDays(7), "Test 10");
            customerService.addPurchase(c9.getId(), 1500000, LocalDateTime.now().minusDays(20), "Test 11");
            customerService.addPurchase(c10.getId(), 300000, LocalDateTime.now().minusHours(5), "Test 12");
        }
    }
}
