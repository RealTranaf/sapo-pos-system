package com.sapo.mockprojectpossystem.service.implementations;

import com.sapo.mockprojectpossystem.dto.request.*;
import com.sapo.mockprojectpossystem.dto.response.FileUploadResponse;
import com.sapo.mockprojectpossystem.dto.response.PaginatedResponse;
import com.sapo.mockprojectpossystem.dto.response.ProductResponse;
import com.sapo.mockprojectpossystem.enums.ProductStatus;
import com.sapo.mockprojectpossystem.model.*;
import com.sapo.mockprojectpossystem.exception.DeleteCloudinaryFileException;
import com.sapo.mockprojectpossystem.exception.InvalidException;
import com.sapo.mockprojectpossystem.exception.NotFoundException;
import com.sapo.mockprojectpossystem.mapper.IProductMapper;
import com.sapo.mockprojectpossystem.repository.*;
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
        Integer brandId = request.getBrandId();
        List<Integer> typeIds = request.getTypeIds();
        Page<Product> productPage = productRepository.searchProducts(search, brandId, typeIds, pageable);
        return buildPaginatedResponse(productPage, page, limit);
    }

    @Override
    @Transactional(readOnly = true)
    public ProductResponse getProductById(Integer productId) {
        Product product = productRepository.findById(productId)
            .orElseThrow(() -> new NotFoundException("Product doesn't exist."));

        if (!product.getStatus().getValue().equalsIgnoreCase("ACTIVE") ) {
            throw new NotFoundException("Product doesn't exist.");
        }

        return productMapper.toResponse(product);
    }

    @Override
    @Transactional
    public ProductResponse createProduct(ProductCreateRequest request, List<MultipartFile> images) throws IOException {
        Product product = productMapper.toEntity(request);

        applyBrand(product, request.getBrandId());
        applyTypes(product, request.getTypeIds());
        createOptions(product, request.getOptions());
        if (request.getOptions() == null || request.getOptions().isEmpty()
            || request.getVariants() == null || request.getVariants().isEmpty()) {
            setDefaultValueForVariant(product);
        } else
            createVariants(product, request.getVariants());
        applyImages(product, images);

        Product savedProduct = productRepository.save(product);
        return productMapper.toResponse(savedProduct);
    }

    @Override
    @Transactional
    public ProductResponse updateProduct(Integer productId, ProductUpdateRequest request) {
        Product product = productRepository.findById(productId)
            .orElseThrow(() -> new NotFoundException("Product doesn't exist."));

        if (!product.getStatus().getValue().equalsIgnoreCase("ACTIVE") ) {
            throw new NotFoundException("Product doesn't exist.");
        }

        applyBrand(product, request.getBrandId());
        applyTypes(product, request.getTypeIds());

        updateProductRequestToEntity(product, request);
        Product updatedProduct = productRepository.save(product);
        return productMapper.toResponse(updatedProduct);
    }

    @Override
    @Transactional
    public void deleteProduct(Integer productId) {
        Product product = productRepository.findById(productId)
            .orElseThrow(() -> new NotFoundException("Product doesn't exist."));

        productRepository.save(product);
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
        applyBrand(product, request.getBrandId());
        applyTypes(product, request.getTypeIds());
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

    // create option
    private void createOptions(Product product, List<ProductOptionRequest> requests) {
        if (requests == null || requests.isEmpty()) {
            // set default value for options and optionValues;
            setDefaultValueForOptionAndOptionValue(product);
            return;
        }

        product.setOptions(new ArrayList<>());
        bindingOptionAndValueRequest(product, requests);
    }

    // binding Option And Value Request
    private void bindingOptionAndValueRequest(Product product, List<ProductOptionRequest> requests) {
        List<ProductOption> productOptions = requests.stream().map(optionRequest -> {
            ProductOption productOption = productMapper.optionRequestToEntity(optionRequest);
            productOption.getValues().forEach(optionValue -> optionValue.setOption(productOption));
            productOption.setProduct(product);
            return productOption;
        }).toList();

        product.getOptions().addAll(productOptions);
    }

    // create variant
    private void createVariants(Product product, List<ProductVariantRequest> requests) {
        product.setVariants(new ArrayList<>());

        List<ProductVariant> productVariants = requests.stream().map(request -> {
            ProductVariant variant = productMapper.variantRequestToEntity(request);
            variant.setProduct(product);
            return variant;
        }).toList();

        product.getVariants().addAll(productVariants);
    }

    // set default value for option and optionValue
    private void setDefaultValueForOptionAndOptionValue(Product product) {
        product.setOptions(new ArrayList<>());

        ProductOption productOption = ProductOption.builder()
            .name("Title")
            .position(1)
            .values(new ArrayList<>())
            .product(product)
            .build();

        ProductOptionValue productOptionValue = ProductOptionValue.builder()
            .value("Default Title")
            .option(productOption)
            .build();

        productOption.getValues().add(productOptionValue);
        product.getOptions().add(productOption);
    }

    // set default value for variant
    private void setDefaultValueForVariant(Product product) {
        product.setVariants(new ArrayList<>());

        ProductVariant productVariant = ProductVariant.builder()
            .title("Default Title")
            .option1("Default Title")
            .position(1)
            .product(product)
            .build();

        product.getVariants().add(productVariant);
    }

    // update options, valueOptions
    private void applyOptions(Product product, List<ProductOptionRequest> optionRequests) {
        // no request do nothing
        if (optionRequests == null) {
            return;
        }

        // empty list means delete
        if (optionRequests.isEmpty()) {
            product.getOptions().clear();
            setDefaultValueForOptionAndOptionValue(product);
            return;
        }

        product.getOptions().clear();
        bindingOptionAndValueRequest(product, optionRequests);
    }

    // update variant
    private void applyVariants(Product product, List<ProductVariantRequest> variantRequests) {
        // have no variant request -> do nothing
        if (variantRequests == null) {
            return;
        }

        if (variantRequests.isEmpty()) {
            product.getVariants().clear();
            setDefaultValueForVariant(product);
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

        if (brandId < 0) {
            Brand oldBrand = product.getBrand();
            if (oldBrand == null) {
                return;
            }
            oldBrand.removeProduct(product);
            return;
        }

        Brand newBrand = brandRepository.findById(brandId)
            .orElseThrow(() -> new NotFoundException("Not found brand with id: " + brandId));

        Brand oldBrand = product.getBrand();

        if (oldBrand != null && oldBrand.getId().equals(newBrand.getId())) {
            return;
        }

        if (oldBrand == null) {
            newBrand.addProduct(product);
            return;
        }

        oldBrand.removeProduct(product);
        newBrand.addProduct(product);
    }

    void applyTypes(Product product, Set<Integer> typeIds) {
        if (product.getTypes() == null) {
            product.setTypes(new HashSet<>());
        }

        if (typeIds == null) {
            return;
        }

        if (typeIds.isEmpty()) {
            for (Type type : new HashSet<>(product.getTypes())) {
                product.removeType(type);
            }
            return;
        }

        // Remove if old type are not in new typeIds
        if (!product.getTypes().isEmpty()) {
            Set<Type> existingTypes = new HashSet<>(product.getTypes());
            for (Type oldType : existingTypes) {
                if (!typeIds.contains(oldType.getId())) {
                    product.removeType(oldType);
                }
            }
        }

        // Add new types
        for (Integer typeId : typeIds) {
            Type type = typeRepository.findById(typeId)
                .orElseThrow(() -> new NotFoundException("Not found type with id: " + typeId));

            if (!product.getTypes().contains(type)) {
                product.addType(type);
            }
        }
    }
}