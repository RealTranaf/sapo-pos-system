package com.sapo.mockprojectpossystem.service;

import com.sapo.mockprojectpossystem.model.*;
import com.sapo.mockprojectpossystem.repository.BrandRepository;
import com.sapo.mockprojectpossystem.repository.ProductRepository;
import com.sapo.mockprojectpossystem.repository.ProductSpecification;
import com.sapo.mockprojectpossystem.repository.TypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final BrandRepository brandRepository;
    private final TypeRepository typeRepository;
    private final ProductRepository productRepository;

    // Lấy danh sách product, có tìm kiếm và sorting
    // Keyword: tìm kiếm product có name hoặc sku hoặc barcode giống với keyword
    // brandId, typeIds, status: tìm kiếm product có brand, type hoặc giống với loại cần tìm
    // minBasePrice, maxBasePrice: tìm kiếm product có basePrice trong khoảng giá cần tìm
    // minSellPrice, maxSellPrice: tìm kiếm product có sellPrice trong khoảng giá cần tìm
    // minQty, maxQty: tìm kiếm product có quantity trong khoảng cần tìm
    // inStock: tìm kiếm product đã hoặc chưa hết hàng
    // sortBy, sortDir: sorting theo các thuộc tính của product (kiểm tra class Product để lấy các thuộc tính)
    public Page<Product> getAllProduct(String keyword, Integer brandId, List<Integer> typeIds,
                                       ProductStatus status, Double minBasePrice, Double maxBasePrice,
                                       Double minSellPrice, Double maxSellPrice,
                                       int minQty, int maxQty,
                                       Boolean inStock,
                                       int page, int size, String sortBy, String sortDir
    ) {
        if (sortBy == null || sortBy.isBlank()) {
            sortBy = "createdAt";
        }
        Sort.Direction direction = sortDir.equalsIgnoreCase("asc")
                ? Sort.Direction.ASC : Sort.Direction.DESC;

        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));

        Specification<Product> spec = Specification
                .where(ProductSpecification.containKeyword(keyword))
                .and(ProductSpecification.brandEquals(brandId))
                .and(ProductSpecification.hasTypes(typeIds))
                .and(ProductSpecification.statusEquals(status))
                .and(ProductSpecification.basePriceBetween(minBasePrice, maxBasePrice))
                .and(ProductSpecification.sellPriceBetween(minSellPrice, maxSellPrice))
                .and(ProductSpecification.quantityBetween(minQty, maxQty))
                .and(ProductSpecification.inStock(inStock));

        return productRepository.findAll(spec, pageable);
    }

    // Tạo product mới
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

    // Lấy product theo id
    public Product getProductById(Integer id) {
        Optional<Product> optional = productRepository.findById(id);
        if (optional.isPresent()) {
            return optional.get();
        } else {
            throw new RuntimeException("Product doesn't exist");
        }
    }

    // Cập nhật product
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
