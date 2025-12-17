package com.sapo.mockprojectpossystem;

import com.sapo.mockprojectpossystem.model.*;
import com.sapo.mockprojectpossystem.repository.*;
//import com.sapo.mockprojectpossystem.request.PurchaseItemRequest;
//import com.sapo.mockprojectpossystem.request.PurchaseRequest;
import com.sapo.mockprojectpossystem.service.*;
//import jakarta.persistence.EntityManager;
//import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.List;

@SpringBootApplication
@RequiredArgsConstructor
public class MockProjectPosSystemApplication implements CommandLineRunner {
    private final UserRepository userRepository;
    private final CustomerRepository customerRepository;
    private final BrandRepository brandRepository;
    private final TypeRepository typeRepository;
//    private final ProductRepository productRepository;
//    private final PurchaseRepository purchaseRepository;
//    private final PurchaseItemRepository purchaseItemRepository;
    private final BrandService brandService;
    private final TypeService typeService;
//    private final ProductService productService;
//    private final PurchaseService purchaseService;
    private final AuthService authService;
    private final CustomerService customerService;
//    private final EntityManager entityManager;

    public static void main(String[] args) {
        SpringApplication.run(MockProjectPosSystemApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        // User seeding
        if (userRepository.findByUsername("Owner").isEmpty()) {
            authService.signup("Owner", "Trần Linh Duy", "0968682409", "testowner", true, Role.OWNER);
        }
        if (userRepository.findByUsername("TEST_CS").isEmpty()) {
            authService.signup("TEST_CS", "Ngô Văn A", "0987654321", "testcs",true, Role.CS);
            authService.signup("TEST_SALES", "Ngô Văn B", "0987654322", "testsales", true, Role.SALES);
            authService.signup("TEST_WH", "Ngô Văn C", "0987654323", "testwh", true, Role.WAREHOUSE);
        }

        // Brand seeding
        if (brandRepository.count() == 0) {
            brandService.createBrand("Uniqlo");
            brandService.createBrand("Zara");
            brandService.createBrand("H&M");
            brandService.createBrand("Levi’s");
            brandService.createBrand("Giordano");
            brandService.createBrand("Nike");
            brandService.createBrand("Adidas");
            brandService.createBrand("Puma");
        }
        // Type seeding
        if (typeRepository.count() == 0) {
            typeService.createType("T-Shirt");
            typeService.createType("Polo");
            typeService.createType("Jean");
            typeService.createType("Short");
            typeService.createType("Jacket");
            typeService.createType("Dress");
            typeService.createType("Hoodie");
            typeService.createType("Sweater");
            typeService.createType("Tracksuit");
        }
//
//        // Customer seeding
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
            customerService.createCustomer("Nguyen Van M", "0911111111", Gender.MALE, null);
            customerService.createCustomer("Tran Thi N", "0912222222", Gender.FEMALE, "Prefers Visa payment");
            customerService.createCustomer("Hoang Gia P", "0913333333", Gender.MALE, "New buyer");
            customerService.createCustomer("Luong Thi Q", "0914444444", Gender.FEMALE, "Member card holder");
            customerService.createCustomer("Dang Minh R", "0915555555", Gender.MALE, null);
            customerService.createCustomer("Do Thi S", "0916666666", Gender.FEMALE, "Frequent returns");
            customerService.createCustomer("Vo Thanh T", "0917777777", Gender.MALE, "VIP 2");
            customerService.createCustomer("Ngo Thi U", "0918888888", Gender.FEMALE, null);
            customerService.createCustomer("Bui Minh V", "0919999999", Gender.MALE, "Cash buyer");
            customerService.createCustomer("Le Hoang X", "0920000000", Gender.MALE, null);
        }
//
//        if (purchaseRepository.count() == 0) {
//
//            User staff = userRepository.findByUsername("TEST_SALES").get();
//
//            // Lấy danh sách customer
//            Customer c1 = customerRepository.findByPhoneNum("0901111111").get(); // Nguyen Van A
//            Customer c2 = customerRepository.findByPhoneNum("0902222222").get(); // Tran Thi B
//            Customer c3 = customerRepository.findByPhoneNum("0903333333").get(); // Le Van C
//            Customer c4 = customerRepository.findByPhoneNum("0904444444").get(); // Pham Thi D
//            Customer c5 = customerRepository.findByPhoneNum("0905555555").get(); // Hoang Van E
//
//            // Lấy products cố định
//            Product p1 = productRepository.findByBarcode("10000001").get(); // Áo thun Uniqlo - 199k
//            Product p2 = productRepository.findByBarcode("10000002").get(); // Jean Levi's 501 - 690k
//            Product p3 = productRepository.findByBarcode("10000003").get(); // Polo Zara - 320k
//            Product p4 = productRepository.findByBarcode("10000004").get(); // Jacket H&M - 490k
//            Product p5 = productRepository.findByBarcode("10000005").get(); // Hoodie Uniqlo - 399k
//
//
//            // --------------------- ORDER 1 ---------------------
//            List<PurchaseItemRequest> purchase1Items = List.of(
//                    new PurchaseItemRequest(p1.getId(), 2),  // 199k x 2 = 398k
//                    new PurchaseItemRequest(p3.getId(), 1)   // 320k x 1 = 320k
//            );
//            double purchase1Total = 398000 + 320000;
//            purchaseService.createPurchase(
//                    new PurchaseRequest(c1.getId(), staff.getId(), purchase1Total, 0, "TEST 1", purchase1Items)
//            );
//
//            // --------------------- ORDER 2 ---------------------
//            List<PurchaseItemRequest> purchase2Items = List.of(
//                    new PurchaseItemRequest(p2.getId(), 1)   // 690k
//            );
//            double purchase2Total = 690000;
//            purchaseService.createPurchase(
//                    new PurchaseRequest(c2.getId(), staff.getId(), purchase2Total, 50000, "TEST 1", purchase2Items)
//            );
//
//            // --------------------- ORDER 3 ---------------------
//            List<PurchaseItemRequest> purchase3Items = List.of(
//                    new PurchaseItemRequest(p4.getId(), 1),  // 490k
//                    new PurchaseItemRequest(p1.getId(), 1)   // 199k
//            );
//            double purchase3Total = 490000 + 199000;
//            purchaseService.createPurchase(
//                    new PurchaseRequest(c3.getId(), staff.getId(), purchase3Total, 0, "TEST 1", purchase3Items)
//            );
//
//            // --------------------- ORDER 4 ---------------------
//            List<PurchaseItemRequest> purchase4Items = List.of(
//                    new PurchaseItemRequest(p3.getId(), 2),  // 320k x2 = 640k
//                    new PurchaseItemRequest(p5.getId(), 1)   // 399k
//            );
//            double purchase4Total = 640000 + 399000;
//            purchaseService.createPurchase(
//                    new PurchaseRequest(c4.getId(), staff.getId(), purchase4Total, 0, "TEST 1", purchase4Items)
//            );
//
//            // --------------------- ORDER 5 ---------------------
//            List<PurchaseItemRequest> purchase5Items = List.of(
//                    new PurchaseItemRequest(p5.getId(), 2),  // 399k x 2 = 798k
//                    new PurchaseItemRequest(p1.getId(), 1)   // 199k
//            );
//            double purchase5Total = 798000 + 199000;
//            purchaseService.createPurchase(
//                    new PurchaseRequest(c5.getId(), staff.getId(), purchase5Total, 100000, "TEST 1", purchase5Items)
//            );
//
//            // ORDER 6
//            purchaseService.createPurchase(new PurchaseRequest(
//                    c2.getId(), staff.getId(),
//                    250000 + 550000,
//                    0,
//                    "Test Order 6",
//                    List.of(
//                            new PurchaseItemRequest(p1.getId(), 1),
//                            new PurchaseItemRequest(p4.getId(), 1)
//                    )
//            ));
//
//            // ORDER 7
//            purchaseService.createPurchase(new PurchaseRequest(
//                    c3.getId(), staff.getId(),
//                    250000 + 320000 + 199000,
//                    20000,
//                    "Test Order 7",
//                    List.of(
//                            new PurchaseItemRequest(p1.getId(), 1),
//                            new PurchaseItemRequest(p3.getId(), 1),
//                            new PurchaseItemRequest(p1.getId(), 1)
//                    )
//            ));
//
//            // ORDER 8
//            purchaseService.createPurchase(new PurchaseRequest(
//                    c4.getId(), staff.getId(),
//                    399000 * 2,
//                    0,
//                    "Test Order 8",
//                    List.of(
//                            new PurchaseItemRequest(p5.getId(), 2)
//                    )
//            ));
//
//            // ORDER 9
//            purchaseService.createPurchase(new PurchaseRequest(
//                    c5.getId(), staff.getId(),
//                    550000 + 690000,
//                    50000,
//                    "Test Order 9",
//                    List.of(
//                            new PurchaseItemRequest(p4.getId(), 1),
//                            new PurchaseItemRequest(p2.getId(), 1)
//                    )
//            ));
//
//            // ORDER 10
//            purchaseService.createPurchase(new PurchaseRequest(
//                    c1.getId(), staff.getId(),
//                    250000 + 320000 + 399000,
//                    0,
//                    "Test Order 10",
//                    List.of(
//                            new PurchaseItemRequest(p1.getId(), 1),
//                            new PurchaseItemRequest(p3.getId(), 1),
//                            new PurchaseItemRequest(p5.getId(), 1)
//                    )
//            ));
//        }
    }
}
