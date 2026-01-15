package com.sapo.mockprojectpossystem.product.application.implement;

import com.sapo.mockprojectpossystem.auth.domain.model.User;
import com.sapo.mockprojectpossystem.auth.domain.repository.UserRepository;
import com.sapo.mockprojectpossystem.common.response.FileUploadResponse;
import com.sapo.mockprojectpossystem.common.response.PaginatedResponse;
import com.sapo.mockprojectpossystem.product.application.interfaces.IProductMapper;
import com.sapo.mockprojectpossystem.product.application.request.ProductCreateRequest;
import com.sapo.mockprojectpossystem.product.application.request.ProductSearchRequest;
import com.sapo.mockprojectpossystem.product.application.request.ProductUpdateRequest;
import com.sapo.mockprojectpossystem.product.domain.model.ProductStatus;
import com.sapo.mockprojectpossystem.product.domain.model.*;
import com.sapo.mockprojectpossystem.product.domain.repository.BrandRepository;
import com.sapo.mockprojectpossystem.product.domain.repository.ProductRepository;
import com.sapo.mockprojectpossystem.product.domain.repository.TypeRepository;
import com.sapo.mockprojectpossystem.product.application.response.ProductResponse;
import com.sapo.mockprojectpossystem.common.exception.NotFoundException;
import com.sapo.mockprojectpossystem.product.interfaces.request.*;
import com.sapo.mockprojectpossystem.common.service.IFileUploadService;
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
public class ProductService {
    ProductRepository productRepository;
    IProductMapper productMapper;
    IFileUploadService fileUploadService;
    BrandRepository brandRepository;
    TypeRepository typeRepository;
    UserRepository userRepository;

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

    @Transactional(readOnly = true)
    public ProductResponse getProductById(Integer productId) {
        Product product = productRepository.findById(productId)
            .orElseThrow(() -> new NotFoundException("Product doesn't exist."));

        if (!product.getStatus().getValue().equalsIgnoreCase("ACTIVE") ) {
            throw new NotFoundException("Product doesn't exist.");
        }

        return productMapper.toResponse(product);
    }

    @Transactional
    public ProductResponse createProduct(ProductCreateRequest request, List<MultipartFile> images) throws IOException {

        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new NotFoundException("User not found"));

        Product product = Product.create(
                request.getName(),
                request.getSummary(),
                request.getContent(),
                request.getStatus(),
                user
        );

        applyBrand(product, request.getBrandId());
        applyTypes(product, request.getTypeIds());

        if (request.getOptions() != null) {
            product.replaceOptions(
                    request.getOptions().stream()
                            .map(productMapper::optionRequestToEntity)
                            .toList()
            );
        }

        if (request.getVariants() != null) {
            product.replaceVariants(
                    request.getVariants().stream()
                            .map(productMapper::variantRequestToEntity)
                            .toList()
            );
        }

        applyImages(product, images);

        return productMapper.toResponse(productRepository.save(product));
    }

    @Transactional
    public ProductResponse updateProduct(Integer productId, ProductUpdateRequest request) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new NotFoundException("Product doesn't exist."));

        if (product.getStatus() != ProductStatus.ACTIVE) {
            throw new NotFoundException("Product doesn't exist.");
        }

        product.updateBasicInfo(
                request.getName(),
                request.getSummary(),
                request.getContent()
        );

        product.updateStatus(request.getStatus());

        applyBrand(product, request.getBrandId());
        applyTypes(product, request.getTypeIds());

        if (request.getOptions() != null) {
            product.replaceOptions(
                    request.getOptions().stream()
                            .map(productMapper::optionRequestToEntity)
                            .toList()
            );
        }

        if (request.getVariants() != null) {
            product.replaceVariants(
                    request.getVariants().stream()
                            .map(productMapper::variantRequestToEntity)
                            .toList()
            );
        }

        return productMapper.toResponse(productRepository.save(product));
    }

    @Transactional
    public void deleteProduct(Integer productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new NotFoundException("Product doesn't exist."));

        product.updateStatus(ProductStatus.INACTIVE);
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

    // No longer used
//    private void updateProductRequestToEntity(Product product, ProductUpdateRequest request) {
//        if (request.getName() != null) product.setName(request.getName());
//        if (request.getSummary() != null) product.setSummary(request.getSummary());
//        if (request.getContent() != null) product.setContent(request.getContent());
//        if (request.getStatus() != null) product.setStatus(request.getStatus());
//        applyBrand(product, request.getBrandId());
//        applyTypes(product, request.getTypeIds());
//        applyVariants(product, request.getVariants());
//        applyOptions(product, request.getOptions());
//    }

    // only for create product api.
//    private void applyImages(Product product, List<MultipartFile> images) throws IOException {
//        if (images == null || images.isEmpty()) {
//            return;
//        }
//
//        List<ProductImage> imagesList = new ArrayList<>();
//
//        for (int i = 0; i < images.size(); i++) {
//            MultipartFile image = images.get(i);
//            FileUploadResponse fileUploadResponse = fileUploadService.uploadImageFile(image);
//
//            ProductImage productImage = ProductImage.builder()
//                .product(product)
//                .assetId(fileUploadResponse.getAssetId())
//                .filename(fileUploadResponse.getOriginalName())
//                .size(fileUploadResponse.getSize())
//                .src(fileUploadResponse.getUrl())
//                .position(i + 1)
//                .build();
//            imagesList.add(productImage);
//        }
//
//        product.setImages(imagesList);
//    }

    private void applyImages(Product product, List<MultipartFile> images) throws IOException {
        if (images == null || images.isEmpty()) return;

        int position = product.getImages().size() + 1;

        for (MultipartFile image : images) {
            FileUploadResponse upload = fileUploadService.uploadImageFile(image);

            product.addImage(
                    position++,
                    upload.getUrl(),
                    upload.getOriginalName(),
                    upload.getAssetId(),
                    upload.getSize()
            );
        }
    }


    // create option (No longer used)
//    private void createOptions(Product product, List<ProductOptionRequest> requests) {
//        if (requests == null || requests.isEmpty()) {
//            // set default value for options and optionValues;
//            setDefaultValueForOptionAndOptionValue(product);
//            return;
//        }
//
//        product.setOptions(new ArrayList<>());
//        bindingOptionAndValueRequest(product, requests);
//    }

//    // binding Option And Value Request (Not in use)
//    private void bindingOptionAndValueRequest(Product product, List<ProductOptionRequest> requests) {
//        List<ProductOption> productOptions = requests.stream().map(optionRequest -> {
//            ProductOption productOption = productMapper.optionRequestToEntity(optionRequest);
//            productOption.getValues().forEach(optionValue -> optionValue.setOption(productOption));
//            productOption.setProduct(product);
//            return productOption;
//        }).toList();
//
//        product.getOptions().addAll(productOptions);
//    }

//    // create variant (Not in use)
//    private void createVariants(Product product, List<ProductVariantRequest> requests) {
//        product.setVariants(new ArrayList<>());
//
//        List<ProductVariant> productVariants = requests.stream().map(request -> {
//            ProductVariant variant = productMapper.variantRequestToEntity(request);
//            variant.setProduct(product);
//            return variant;
//        }).toList();
//
//        product.getVariants().addAll(productVariants);
//    }

//    // set default value for option and optionValue (Not in use currently)
//    private void setDefaultValueForOptionAndOptionValue(Product product) {
//        product.setOptions(new ArrayList<>());
//
//        ProductOption productOption = ProductOption.builder()
//            .name("Title")
//            .position(1)
//            .values(new ArrayList<>())
//            .product(product)
//            .build();
//
//        ProductOptionValue productOptionValue = ProductOptionValue.builder()
//            .value("Default Title")
//            .option(productOption)
//            .build();
//
//        productOption.getValues().add(productOptionValue);
//        product.getOptions().add(productOption);
//    }

    // set default value for variant (Not in use)
//    private void setDefaultValueForVariant(Product product) {
//        product.setVariants(new ArrayList<>());
//
//        ProductVariant productVariant = ProductVariant.builder()
//            .title("Default Title")
//            .option1("Default Title")
//            .position(1)
//            .product(product)
//            .build();
//
//        product.getVariants().add(productVariant);
//    }

    // update options, valueOptions (Not in use right now)
//    private void applyOptions(Product product, List<ProductOptionRequest> optionRequests) {
//        // no request do nothing
//        if (optionRequests == null) {
//            return;
//        }
//
//        // empty list means delete
//        if (optionRequests.isEmpty()) {
//            product.getOptions().clear();
//            setDefaultValueForOptionAndOptionValue(product);
//            return;
//        }
//
//        product.getOptions().clear();
//        bindingOptionAndValueRequest(product, optionRequests);
//    }

    // update variant (Not in use)
//    private void applyVariants(Product product, List<ProductVariantRequest> variantRequests) {
//        // have no variant request -> do nothing
//        if (variantRequests == null) {
//            return;
//        }
//
//        if (variantRequests.isEmpty()) {
//            product.getVariants().clear();
//            setDefaultValueForVariant(product);
//        }
//
//        product.getVariants().clear();
//        List<ProductVariant> productVariants = variantRequests.stream().map(request -> {
//            ProductVariant variant = productMapper.variantRequestToEntity(request);
//            if (request.getImageId() != null) {
//                ProductImage image = productImageRepository.findById(request.getImageId()).orElseThrow(() -> new NotFoundException("Not found image with id: " + request.getImageId()));
//                if (!Objects.equals(image.getProduct().getId(), product.getId())) {
//                    throw new InvalidException("Invalid image for variant");
//                }
//                variant.setImage(image);
//            }
//            variant.setProduct(product);
//            return variant;
//        }).toList();
//        product.getVariants().addAll(productVariants);
//    }

    void applyBrand(Product product, Integer brandId) {
        if (brandId == null) {
            return;
        }

        // Explicit unbind
        if (brandId == 0) {
            product.removeBrand();
            return;
        }

        Brand brand = brandRepository.findById(brandId)
                .orElseThrow(() -> new NotFoundException("Not found brand with id: " + brandId));

        product.updateBrand(brand);
    }

    void applyTypes(Product product, Set<Integer> typeIds) {
        if (typeIds == null) {
            return;
        }

        // Remove all
        if (typeIds.isEmpty()) {
            product.clearTypes();
            return;
        }

        Set<Type> newTypes = typeIds.stream()
                .map(id -> typeRepository.findById(id)
                        .orElseThrow(() -> new NotFoundException("Not found type with id: " + id)))
                .collect(Collectors.toSet());

        product.syncTypes(newTypes);
    }


}