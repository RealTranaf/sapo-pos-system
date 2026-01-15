package com.sapo.mockprojectpossystem.purchase.application.implement;

import com.sapo.mockprojectpossystem.auth.domain.model.User;
import com.sapo.mockprojectpossystem.auth.domain.repository.UserRepository;
import com.sapo.mockprojectpossystem.common.response.PageResponse;
import com.sapo.mockprojectpossystem.common.util.DateTimeUtils;
import com.sapo.mockprojectpossystem.common.util.StringUtils;
import com.sapo.mockprojectpossystem.customer.domain.model.Customer;
import com.sapo.mockprojectpossystem.customer.domain.repository.CustomerRepository;
import com.sapo.mockprojectpossystem.product.domain.model.ProductVariant;
import com.sapo.mockprojectpossystem.product.domain.repository.ProductVariantRepository;
import com.sapo.mockprojectpossystem.purchase.domain.model.Purchase;
import com.sapo.mockprojectpossystem.purchase.domain.repository.PurchaseRepository;
import com.sapo.mockprojectpossystem.purchase.infrastructure.PurchaseSpecification;
import com.sapo.mockprojectpossystem.purchase.application.request.PurchaseItemRequest;
import com.sapo.mockprojectpossystem.purchase.application.request.PurchaseQueryParams;
import com.sapo.mockprojectpossystem.purchase.application.request.PurchaseRequest;
import com.sapo.mockprojectpossystem.purchase.application.response.PurchaseResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

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
    public PageResponse<PurchaseResponse> getAllPurchase(PurchaseQueryParams query) {

        String keyword = query.getKeyword();
        Integer customerId = query.getCustomerId();
        Integer userId = query.getUserId();
        Double minTotal = query.getMinTotal();
        Double maxTotal = query.getMaxTotal();
        Double minDiscount = query.getMinDiscount();
        Double maxDiscount = query.getMaxDiscount();
        String startDate = query.getStartDate();
        String endDate = query.getEndDate();
        Integer productId = query.getProductId();
        Integer page = query.getPage();
        Integer size = query.getSize();
        String sortBy = query.getSortBy();
        String sortDir = query.getSortDir();

        if (sortBy == null || sortBy.isBlank()) {
            sortBy = "createdOn";
        }
        Sort.Direction direction = sortDir.equalsIgnoreCase("asc")
                ? Sort.Direction.ASC : Sort.Direction.DESC;

        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));

        Instant startInstant = null;
        Instant endInstant = null;

        if (StringUtils.hasText(startDate) && StringUtils.hasText(endDate)) {
            LocalDate start = LocalDate.parse(startDate);
            LocalDate end = LocalDate.parse(endDate);

            startInstant = DateTimeUtils.startOfDayUtc(start);
            endInstant = DateTimeUtils.endOfDayUtc(end);
        }

        Specification<Purchase> spec = Specification
                .where(PurchaseSpecification.containKeyword(keyword))
                .and(PurchaseSpecification.customerEquals(customerId))
                .and(PurchaseSpecification.userEquals(userId))
                .and(PurchaseSpecification.totalAmountBetween(minTotal, maxTotal))
                .and(PurchaseSpecification.discountAmountBetween(minDiscount, maxDiscount))
                .and(PurchaseSpecification.createdDateBetween(startInstant, endInstant))
                .and(PurchaseSpecification.containsProduct(productId));

        Page<PurchaseResponse> responsePage = purchaseRepository.findAll(spec, pageable).map(PurchaseResponse::new);

        return new PageResponse<PurchaseResponse>("purchases", responsePage);
    }

    // Tính toán giá trị đơn hàng ở front-end
    // Tạo đơn hàng, cập nhật luôn giá trị totalPurchaseAmount của customer
    @Transactional
    public void createPurchase(PurchaseRequest request) {

        Integer customerId = request.getCustomerId();
        Integer userId = request.getUserId();
        Double discountAmount = request.getDiscountAmount();
        String note = request.getNote();
        List<PurchaseItemRequest> purchaseItems = request.getPurchaseItems();

        Customer customer = customerRepository.findById(customerId).orElseThrow(() -> new RuntimeException("Customer not found"));
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));

        Purchase purchase = new Purchase(customer, user, note);

        for (PurchaseItemRequest itemReq : purchaseItems) {
            ProductVariant variant = productVariantRepository.findById(Long.parseLong(String.valueOf(itemReq.getProductVariantId())))
                    .orElseThrow(() -> new RuntimeException("Product not found: " + itemReq.getProductVariantId()));

            purchase.addItem(variant, itemReq.getQuantity());
        }
        purchase.applyDiscount(discountAmount);
        purchaseRepository.save(purchase);
        customer.addPurchase(purchase.getTotalAmount(), Instant.now());
        customerRepository.save(customer);
    }

    // Lấy đơn hàng theo ID
    public PurchaseResponse getPurchaseById(Integer id) {
        Purchase purchase = purchaseRepository.findById(id).orElseThrow(() -> new RuntimeException("Purchase does not exist"));
        return new PurchaseResponse(purchase);
    }
}
