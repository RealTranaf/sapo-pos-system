package com.sapo.mockprojectpossystem.service;

import com.sapo.mockprojectpossystem.model.*;
import com.sapo.mockprojectpossystem.repository.*;
import com.sapo.mockprojectpossystem.request.PurchaseItemRequest;
import com.sapo.mockprojectpossystem.request.PurchaseRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PurchaseService {
    private final PurchaseRepository purchaseRepository;
    private final CustomerRepository customerRepository;
    private final UserRepository userRepository;
    private final ProductVariantRepository productVariantRepository;

    // Lấy danh sách đơn hàng, có tìm kiếm và sorting
    // Keyword: tìm kiếm đơn hàng có note hoặc tên khách hàng hoặc tên nhân viên giống với keyword
    // minTotal, maxTotal: tìm kiếm đơn hàng có tổng tiền trong khoản cần tìm
    // minDiscount, maxDiscount: tìm kiếm đơn hàng có tiền khuyến mãi trong khoản cần tìm
    // startDate, endDate: tìm kiếm đơn hàng đã mua trong khoản thời gian cần tìm
    // productId: tìm kiếm đơn hàng có chứa product nhất định
    // sortBy, sortDir: sorting theo các thuộc tính của đơn hàng (kiểm tra class Purchase để lấy các thuộc tính)
    public Page<Purchase> getAllPurchase(String keyword, Integer customerId, Integer userId,
                                         Double minTotal, Double maxTotal, Double minDiscount, Double maxDiscount,
                                         String startDate, String endDate,
                                         Integer productId,
                                         int page, int size, String sortBy, String sortDir
    ) {
        if (sortBy == null || sortBy.isBlank()) {
            sortBy = "createdAt";
        }
        Sort.Direction direction = sortDir.equalsIgnoreCase("asc")
                ? Sort.Direction.ASC : Sort.Direction.DESC;

        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));

        LocalDateTime startDateTime = null;
        LocalDateTime endDateTime = null;

        if (startDate != null && !startDate.isBlank()
                && endDate != null && !endDate.isBlank()) {

            LocalDate start = LocalDate.parse(startDate);
            LocalDate end = LocalDate.parse(endDate);

            startDateTime = start.atStartOfDay();
            endDateTime = end.plusDays(1).atStartOfDay().minusSeconds(1);
        }

        Specification<Purchase> spec = Specification
                .where(PurchaseSpecification.containKeyword(keyword))
                .and(PurchaseSpecification.customerEquals(customerId))
                .and(PurchaseSpecification.userEquals(userId))
                .and(PurchaseSpecification.totalAmountBetween(minTotal, maxTotal))
                .and(PurchaseSpecification.discountAmountBetween(minDiscount, maxDiscount))
                .and(PurchaseSpecification.createdDateBetween(startDateTime, endDateTime))
                .and(PurchaseSpecification.containsProduct(productId));

        return purchaseRepository.findAll(spec, pageable);
    }

    // Tính toán giá trị đơn hàng ở front-end
    // Tạo đơn hàng, cập nhật luôn giá trị totalPurchaseAmount của customer
    public void createPurchase(PurchaseRequest purchaseRequest) {
        Customer customer = customerRepository.findById(purchaseRequest.getCustomerId())
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        User user = userRepository.findById(purchaseRequest.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Purchase purchase = new Purchase(customer, user, purchaseRequest.getTotalAmount(), purchaseRequest.getDiscountAmount(), purchaseRequest.getNote(), null);
        List<PurchaseItem> items = new ArrayList<>();

        for (PurchaseItemRequest itemReq : purchaseRequest.getPurchaseItems()) {
            ProductVariant product = productVariantRepository.findById(Long.parseLong(String.valueOf(itemReq.getProductVariantId())))
                    .orElseThrow(() -> new RuntimeException("Product not found: " + itemReq.getProductVariantId()));
            double itemTotal = product.getPrice() * itemReq.getQuantity();
            PurchaseItem item = new PurchaseItem(product, purchase, itemReq.getQuantity());
            item.setTotalPrice(itemTotal);
            items.add(item);
            product.setInventoryQuantity(product.getInventoryQuantity() - itemReq.getQuantity());
            productVariantRepository.save(product);
        }
        purchase.setPurchaseItems(items);
        purchaseRepository.save(purchase);

        double tempTotal = customer.getTotalPurchaseAmount() + purchase.getTotalAmount();
        customer.setLastPurchaseDate(LocalDateTime.now());
        customer.setTotalPurchaseAmount(tempTotal);
        customerRepository.save(customer);
    }

    // Lấy đơn hàng theo ID
    public Purchase getPurchaseById(Integer id) {
        Optional<Purchase> optional = purchaseRepository.findById(id);
        if (optional.isPresent()) {
            return optional.get();
        } else {
            throw new RuntimeException("Purchase doesn't exist");
        }
    }
}
