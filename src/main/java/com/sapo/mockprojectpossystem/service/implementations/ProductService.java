package com.sapo.mockprojectpossystem.service.implementations;

import com.sapo.mockprojectpossystem.dto.request.*;
import com.sapo.mockprojectpossystem.dto.response.FileUploadResponse;
import com.sapo.mockprojectpossystem.dto.response.PaginatedResponse;
import com.sapo.mockprojectpossystem.dto.response.ProductResponse;
import com.sapo.mockprojectpossystem.model.*;
import com.sapo.mockprojectpossystem.exception.DeleteCloudinaryFileException;
import com.sapo.mockprojectpossystem.exception.InvalidException;
import com.sapo.mockprojectpossystem.exception.NotFoundException;
import com.sapo.mockprojectpossystem.mapper.IProductMapper;
import com.sapo.mockprojectpossystem.repository.BrandRepository;
import com.sapo.mockprojectpossystem.repository.ProductImageRepository;
import com.sapo.mockprojectpossystem.repository.ProductRepository;
import com.sapo.mockprojectpossystem.repository.TypeRepository;
import com.sapo.mockprojectpossystem.service.interfaces.IFileUploadService;
import com.sapo.mockprojectpossystem.service.interfaces.IProductService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ProductService implements IProductService {
    ProductRepository productRepository;
    ProductImageRepository productImageRepository;
    IProductMapper productMapper;
    IFileUploadService fileUploadService;
    BrandRepository brandRepository;
    TypeRepository typeRepository;

    @Override
    public PaginatedResponse<ProductResponse> getOrSearchProducts(ProductSearchRequest request) {
        int page = request.getPage();
        int limit = request.getLimit();
        String sortBy = request.getSortBy();
        String order = request.getOrder();
        String search = request.getSearch();

        Sort.Direction direction = order.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(page, limit, Sort.by(direction, sortBy));

        if (search != null && !search.isBlank()) {
            Page<Product> productPage = productRepository.searchByNameOrSku(search, pageable);

            return buildPaginatedResponse(productPage, page, limit);
        }
        Page<Product> productPage = productRepository.findAll(pageable);
        return buildPaginatedResponse(productPage, page, limit);
    }

    @Override
    @Transactional(readOnly = true)
    public ProductResponse getProductById(Long productId) {
        Product product = productRepository.findById(productId)
            .orElseThrow(() -> new NotFoundException("Product doesn't exist."));

        return productMapper.toResponse(product);
    }

    @Override
    @Transactional
    public ProductResponse createProduct(ProductCreateRequest request, List<MultipartFile> images) throws IOException {
        Product product = productMapper.toEntity(request);

        applyBrand(product, request.getBrandId());
        applyTypes(product, request.getTypeIds());
        applyOptions(product, request.getOptions());
        applyVariants(product, request.getVariants());
        applyImages(product, images);

        Product savedProduct = productRepository.save(product);
        return productMapper.toResponse(savedProduct);
    }

    @Override
    @Transactional
    public ProductResponse updateProduct(Long productId, ProductUpdateRequest request) {
        Product product = productRepository.findById(productId)
            .orElseThrow(() -> new NotFoundException("Product doesn't exist."));

        applyBrand(product, request.getBrandId());
        applyTypes(product, request.getTypeIds());

        updateProductRequestToEntity(product, request);
        Product updatedProduct = productRepository.save(product);
        return productMapper.toResponse(updatedProduct);
    }

    @Override
    @Transactional
    public void deleteProduct(Long productId) {
        Product product = productRepository.findById(productId)
            .orElseThrow(() -> new NotFoundException("Product doesn't exist."));

        product.getImages().forEach((image) -> {
            try {
                fileUploadService.deleteFile(image.getAssetId());
            } catch (Exception e) {
                throw new DeleteCloudinaryFileException(e);
            }
        });

        productRepository.delete(product);
    }

    private PaginatedResponse<ProductResponse> buildPaginatedResponse(Page<Product> productPage, Integer page, Integer size) {
        List<ProductResponse> data = productPage.getContent().stream()
            .map(productMapper::toResponse)
            .collect(Collectors.toList());

        return PaginatedResponse.<ProductResponse>builder()
            .data(data)
            .currentPage(page)
            .pageSize(size)
            .totalPages(productPage.getTotalPages())
            .totalElements(productPage.getTotalElements())
            .hasNext(productPage.hasNext())
            .hasPrevious(productPage.hasPrevious())
            .build();
    }

    private void updateProductRequestToEntity(Product product, ProductUpdateRequest request) {
        if (request.getName() != null) product.setName(request.getName());
        if (request.getSummary() != null) product.setSummary(request.getSummary());
        if (request.getContent() != null) product.setContent(request.getContent());
        if (request.getStatus() != null) product.setStatus(request.getStatus());
        applyVariants(product, request.getVariants());
        applyOptions(product, request.getOptions());
    }

    // only for create product api.
    private void applyImages(Product product, List<MultipartFile> images) throws IOException {
        if (images == null || images.isEmpty()) {
            return;
        }

        List<ProductImage> imagesList = new ArrayList<>();

        for (int i = 0; i < images.size(); i++) {
            MultipartFile image = images.get(i);
            FileUploadResponse fileUploadResponse = fileUploadService.uploadImageFile(image);

            ProductImage productImage = ProductImage.builder()
                .product(product)
                .assetId(fileUploadResponse.getAssetId())
                .filename(fileUploadResponse.getOriginalName())
                .size(fileUploadResponse.getSize())
                .src(fileUploadResponse.getUrl())
                .position(i + 1)
                .build();
            imagesList.add(productImage);
        }

        product.setImages(imagesList);
    }

    private void applyOptions(Product product, List<ProductOptionRequest> optionRequests) {
        if (product.getOptions() == null) {
            product.setOptions(new ArrayList<>());
        }

        if (optionRequests == null || optionRequests.isEmpty()) {
            product.getOptions().clear();
            return;
        }

        product.getOptions().clear();
        List<ProductOption> productOptions = optionRequests.stream().map(optionRequest -> {
            ProductOption productOption = productMapper.optionRequestToEntity(optionRequest);
            productOption.getValues().forEach(optionValue -> optionValue.setOption(productOption));
            productOption.setProduct(product);
            return productOption;
        }).toList();

        product.getOptions().addAll(productOptions);
    }

    private void applyVariants(Product product, List<ProductVariantRequest> variantRequests) {
        if (product.getVariants() == null) {
            product.setVariants(new ArrayList<>());
        }

        if (variantRequests == null || variantRequests.isEmpty()) {
            product.getVariants().clear();
            return;
        }

        product.getVariants().clear();
        List<ProductVariant> productVariants = variantRequests.stream().map(request -> {
            ProductVariant variant = productMapper.variantRequestToEntity(request);
            if (request.getImageId() != null) {
                ProductImage image = productImageRepository.findById(request.getImageId()).orElseThrow(() -> new NotFoundException("Not found image with id: " + request.getImageId()));
                if (!Objects.equals(image.getProduct().getId(), product.getId())) {
                    throw new InvalidException("Invalid image for variant");
                }
                variant.setImage(image);
            }
            variant.setProduct(product);
            return variant;
        }).toList();
        product.getVariants().addAll(productVariants);
    }

    void applyBrand(Product product, Integer brandId) {
        if (brandId == null) {
            return;
        }

        Brand brand = brandRepository.findById(brandId)
            .orElseThrow(() -> new NotFoundException("Not found brand with id: " + brandId));
        if (brand.getProducts().isEmpty()) {
            brand.setProducts(List.of(product));
        } else
            brand.getProducts().add(product);
        product.setBrand(brand);
    }

    void applyTypes(Product product, Set<Integer> typeIds) {
        if (product.getTypes() == null) {
            product.setTypes(new HashSet<>());
        }

        if (typeIds.isEmpty()) {
            product.getTypes().clear();
            return;
        }

        Set<Type> types = new HashSet<>();

        typeIds.forEach(typeId -> {
            Type type = typeRepository.findById(typeId)
                .orElseThrow(() -> new NotFoundException("Not found type with id: " + typeId));
            if (type.getProducts().isEmpty()) {
                type.setProducts(Set.of(product));
            }
            else
                type.getProducts().add(product);
            types.add(type);
        });

        product.setTypes(types);
    }
}