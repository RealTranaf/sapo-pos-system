package com.sapo.mockprojectpossystem.service;

import com.sapo.mockprojectpossystem.model.*;
import com.sapo.mockprojectpossystem.repository.CustomerRepository;
import com.sapo.mockprojectpossystem.repository.PurchaseRepository;
import com.sapo.mockprojectpossystem.repository.ProductRepository;
import com.sapo.mockprojectpossystem.repository.UserRepository;
import com.sapo.mockprojectpossystem.request.PurchaseItemRequest;
import com.sapo.mockprojectpossystem.request.PurchaseRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

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
    private final ProductRepository productRepository;

    public Page<Purchase> getAllPurchase(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        return purchaseRepository.findAll(pageable);
    }

    // Tính toán giá trị đơn hàng ở front-end
    public void createPurchase(PurchaseRequest purchaseRequest) {
        Customer customer = customerRepository.findById(purchaseRequest.getCustomerId())
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        User user = userRepository.findById(purchaseRequest.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Purchase purchase = new Purchase(customer, user, purchaseRequest.getTotalAmount(), purchaseRequest.getDiscountAmount(), purchaseRequest.getNote(), null);
        List<PurchaseItem> items = new ArrayList<>();

        for (PurchaseItemRequest itemReq : purchaseRequest.getPurchaseItems()) {
            Product product = productRepository.findById(itemReq.getProductId())
                    .orElseThrow(() -> new RuntimeException("Product not found: " + itemReq.getProductId()));
            double itemTotal = product.getSellPrice() * itemReq.getQuantity();
            PurchaseItem item = new PurchaseItem(product, purchase, itemReq.getQuantity());
            item.setTotalPrice(itemTotal);
            items.add(item);
            product.setQuantity(product.getQuantity() - itemReq.getQuantity());
            productRepository.save(product);
        }
        purchase.setPurchaseItems(items);
        purchaseRepository.save(purchase);

        double tempTotal = customer.getTotalPurchaseAmount() + purchase.getTotalAmount();
        customer.setLastPurchaseDate(LocalDateTime.now());
        customer.setTotalPurchaseAmount(tempTotal);
        customerRepository.save(customer);
    }

    public Purchase getPurchaseById(Integer id) {
        Optional<Purchase> optional = purchaseRepository.findById(id);
        if (optional.isPresent()) {
            return optional.get();
        } else {
            throw new RuntimeException("Purchase doesn't exist");
        }
    }
}
