package com.sapo.mockprojectpossystem.service;

import com.sapo.mockprojectpossystem.model.*;
import com.sapo.mockprojectpossystem.repository.BrandRepository;
import com.sapo.mockprojectpossystem.repository.ProductRepository;
import com.sapo.mockprojectpossystem.repository.TypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final BrandRepository brandRepository;
    private final TypeRepository typeRepository;
    private final ProductRepository productRepository;

    public Page<Product> getAllProduct(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());
        return productRepository.findAll(pageable);
    }

    public void createProduct(String name, String sku, String barcode,
                              ProductStatus status, String description,
                              double basePrice, double sellPrice,
                              int quantity, Integer brandId, List<Integer> typeIds) {
        Optional<Brand> optionalBrand = brandRepository.findById(brandId);
        List<Type> types = typeRepository.findAllById(typeIds);
        if (types.size() != typeIds.size() || optionalBrand.isEmpty()) {
            throw new RuntimeException("Some types or brand were not found");
        }

        Brand brand = optionalBrand.get();
        Product product = new Product(name, sku, barcode,
                status, description, basePrice,
                sellPrice, quantity, brand, types);
        productRepository.save(product);
    }

    public Product getProductById(Integer id) {
        Optional<Product> optional = productRepository.findById(id);
        if (optional.isPresent()) {
            return optional.get();
        } else {
            throw new RuntimeException("Product doesn't exist");
        }
    }

    public void updateProduct(Integer id, String name, String sku, String barcode,
                              ProductStatus status, String description,
                              double basePrice, double sellPrice,
                              int quantity, Integer brandId, List<Integer> typeIds) {

        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found: " + id));

        Brand brand = brandRepository.findById(brandId)
                .orElseThrow(() -> new RuntimeException("Brand not found: " + brandId));

        List<Type> types = typeRepository.findAllById(typeIds);
        if (types.size() != typeIds.size()) {
            throw new RuntimeException("Invalid type IDs");
        }

        product.setName(name);
        product.setSku(sku);
        product.setBarcode(barcode);
        product.setStatus(status);
        product.setDescription(description);
        product.setBasePrice(basePrice);
        product.setSellPrice(sellPrice);
        product.setQuantity(quantity);
        product.setBrand(brand);
        product.setTypes(types);

        productRepository.save(product);
    }
}
