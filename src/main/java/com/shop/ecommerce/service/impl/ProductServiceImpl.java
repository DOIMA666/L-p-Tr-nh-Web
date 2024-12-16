package com.shop.ecommerce.service.impl;

import com.shop.ecommerce.entity.CategoryEntity;
import com.shop.ecommerce.entity.ImageEntity;
import com.shop.ecommerce.entity.ProductEntity;
import com.shop.ecommerce.entity.ProductImageEntity;
import com.shop.ecommerce.payload.dto.ProductDto;
import com.shop.ecommerce.payload.dto.ProductImageDto;
import com.shop.ecommerce.payload.request.ProductFilterRequest;
import com.shop.ecommerce.payload.response.BaseResponse;
import com.shop.ecommerce.repository.CategoryRepository;
import com.shop.ecommerce.repository.ImageRepository;
import com.shop.ecommerce.repository.ProductImageRepository;
import com.shop.ecommerce.repository.ProductRepository;
import com.shop.ecommerce.service.ProductService;
import com.shop.ecommerce.utils.ImageUtils;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final ImageRepository imageRepository;
    private final ProductImageRepository productImageRepository;
    private final CategoryRepository categoryRepository;
    private final StorageService storageService;
    private final ModelMapper modelMapper;

    public ProductServiceImpl(ProductRepository productRepository, ImageRepository imageRepository, ProductImageRepository productImageRepository, CategoryRepository categoryRepository, StorageService storageService, ModelMapper modelMapper) {
        this.productRepository = productRepository;
        this.imageRepository = imageRepository;
        this.productImageRepository = productImageRepository;
        this.categoryRepository = categoryRepository;
        this.storageService = storageService;
        this.modelMapper = modelMapper;
    }

    @Override
    public BaseResponse<Page<ProductDto>> getAllByFilter(ProductFilterRequest filterRequest, int page, int size) {
        Pageable pageable = PageRequest.of(page,size);
        Page<ProductEntity> productEntities = productRepository.findAllByFilter(filterRequest,pageable);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        List<ProductDto> productDTOS = productEntities.getContent().stream().map(productEntity -> {
            ProductDto productDTO = modelMapper.map(productEntity,ProductDto.class);
            productDTO.setCategoryId(productEntity.getCategoryEntity().getId());
            productDTO.setCategoryName(productEntity.getCategoryEntity().getCategoryName());
            String formattedDate = productEntity.getCreatedAt().format(formatter);
            productDTO.setCreatedDate(formattedDate);
            productDTO.setSalePrice(productEntity.getSalePrice());
            productDTO.setPrice(productEntity.getPrice());
            if(Objects.equals(productDTO.getThumbnail(), "")) {
                productDTO.setThumbnail(ImageUtils.IMAGE_LINK);
            }
            return productDTO;
        }).collect(Collectors.toList());

        Page<ProductDto> pageData = new PageImpl(productDTOS,pageable,productEntities.getTotalElements());
        BaseResponse<Page<ProductDto>> response = new BaseResponse<>();
        response.setCode(200);
        response.setMessage("success");
        response.setData(pageData);
        return response;
    }

    @Override
    public BaseResponse<Page<ProductDto>> getAllInClientByFilter(ProductFilterRequest filterRequest, int page, int size) {
        Pageable pageable = PageRequest.of(page,size);
        Page<ProductEntity> productEntities = productRepository.findAllInClientByFilter(filterRequest,pageable);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        List<ProductDto> productDTOS = productEntities.getContent().stream().map(productEntity -> {
            ProductDto productDTO = modelMapper.map(productEntity,ProductDto.class);
            productDTO.setCategoryId(productEntity.getCategoryEntity().getId());
            productDTO.setCategoryName(productEntity.getCategoryEntity().getCategoryName());
            String formattedDate = productEntity.getCreatedAt().format(formatter);
            productDTO.setCreatedDate(formattedDate);
            productDTO.setSalePrice(productEntity.getSalePrice());
            productDTO.setPrice(productEntity.getPrice());
            if(Objects.equals(productDTO.getThumbnail(), "")) {
                productDTO.setThumbnail(ImageUtils.IMAGE_LINK);
            }
            return productDTO;
        }).collect(Collectors.toList());

        Page<ProductDto> pageData = new PageImpl(productDTOS,pageable,productEntities.getTotalElements());
        BaseResponse<Page<ProductDto>> response = new BaseResponse<>();
        response.setCode(200);
        response.setMessage("success");
        response.setData(pageData);
        return response;
    }

    @Override
    public ProductEntity save(ProductEntity productEntity) {
        return productRepository.save(productEntity);
    }

    @Override
    public List<ProductDto> getAllIfActive() {
        List<ProductEntity> productEntities = productRepository.findAllByStatus(1);

        List<ProductDto> productDtos = productEntities.stream().map(productEntity -> {
            ProductDto productDto = modelMapper.map(productEntity, ProductDto.class);
            productDto.setCategoryId(productEntity.getCategoryEntity().getId());
            productDto.setCategoryName(productEntity.getCategoryEntity().getCategoryName());
            productDto.setStatus(productEntity.getStatus());
            productDto.setCreatedDate(productEntity.getCreatedAt().toString());
            if(Objects.equals(productDto.getThumbnail(), "")) {
                productDto.setThumbnail(ImageUtils.IMAGE_LINK);
            }
            return productDto;
        }).collect(Collectors.toList());
        return productDtos;
    }

    @Override
    public List<ProductDto> getAll() {
        List<ProductEntity> productEntities = productRepository.findAll();

        List<ProductDto> productDtos = productEntities.stream().map(productEntity -> {
            ProductDto productDto = modelMapper.map(productEntity, ProductDto.class);
            productDto.setCategoryId(productEntity.getCategoryEntity().getId());
            productDto.setCategoryName(productEntity.getCategoryEntity().getCategoryName());
            productDto.setStatus(productEntity.getStatus());
            productDto.setCreatedDate(productEntity.getCreatedAt().toString());
            if(Objects.equals(productDto.getThumbnail(), "")) {
                productDto.setThumbnail(ImageUtils.IMAGE_LINK);
            }
            return productDto;
        }).collect(Collectors.toList());
        return productDtos;
    }

    @Override
    public List<ProductEntity> getTop4() {
        return productRepository.getTop4();
    }

    @Override
    public Long countProductActive() {
        return productRepository.countByStatus(1);
    }

    @Override
    public void save(ProductDto productDto, String username, MultipartFile file) {
        ProductEntity productEntity = new ProductEntity();
        productEntity.setCreatedAt(LocalDateTime.now());
        productEntity.setUpdatedAt(LocalDateTime.now());
        productEntity.setCreatedBy(username);
        productEntity.setUpdatedBy(username);

        productEntity.setProductName(productDto.getProductName());
        productEntity.setAmount(productDto.getAmount());
        productEntity.setDescription(productDto.getDescription());
        productEntity.setThumbnail(productDto.getThumbnail());
        productEntity.setProductName(productDto.getProductName());
        productEntity.setPrice(productDto.getPrice());
        productEntity.setSalePrice(productDto.getSalePrice());
        productEntity.setStatus(productDto.getStatus());

        CategoryEntity categoryEntity = categoryRepository.findById(productDto.getCategoryId()).get();
        productEntity.setCategoryEntity(categoryEntity);

        LocalDateTime ldt = LocalDateTime.now();
        if(file != null) {
            if (!file.isEmpty() && ImageUtils.isImageFile(file)) {
                ImageEntity imageEntity = new ImageEntity();
                String fileName = "product_" + productEntity.getId() + "_" + ldt;
                String saveLink = "";
                saveLink = storageService.uploadFile(file, fileName);
                imageEntity.setCreatedAt(LocalDateTime.now());
                imageEntity.setUpdatedAt(LocalDateTime.now());
                imageEntity.setCreatedBy(username);
                imageEntity.setUpdatedBy(username);
                imageEntity.setImageLink(saveLink);
                imageEntity.setStatus(1);
                imageEntity = imageRepository.save(imageEntity);
                productEntity.setThumbnail(imageEntity.getImageLink());
            }
        }
        productRepository.save(productEntity);
    }

    @Override
    public void update(ProductDto productDto, String username, MultipartFile file) {
        ProductEntity productEntity = productRepository.findById(productDto.getId()).get();
        ProductEntity saveProduct = new ProductEntity();
        saveProduct.setId(productDto.getId());
        saveProduct.setCreatedAt(LocalDateTime.now());
        saveProduct.setUpdatedAt(LocalDateTime.now());
        saveProduct.setCreatedBy(username);
        saveProduct.setUpdatedBy(username);

        saveProduct.setProductName(productDto.getProductName());
        saveProduct.setAmount(productDto.getAmount());
        saveProduct.setDescription(productDto.getDescription());
        saveProduct.setThumbnail(productEntity.getThumbnail());
        saveProduct.setProductName(productDto.getProductName());
        saveProduct.setPrice(productDto.getPrice());
        saveProduct.setSalePrice(productDto.getSalePrice());
        saveProduct.setStatus(productDto.getStatus());

        CategoryEntity categoryEntity = categoryRepository.findById(productDto.getCategoryId()).get();
        saveProduct.setCategoryEntity(categoryEntity);
        if(file != null) {
            if (!file.isEmpty() && ImageUtils.isImageFile(file)) {
                LocalDateTime ldt = LocalDateTime.now();
                ImageEntity imageEntity = new ImageEntity();
                String fileName = "product_" + productEntity.getId() + "_" + ldt;
                String saveLink = "";
                saveLink = storageService.uploadFile(file, fileName);
                imageEntity.setCreatedAt(LocalDateTime.now());
                imageEntity.setUpdatedAt(LocalDateTime.now());
                imageEntity.setCreatedBy(username);
                imageEntity.setUpdatedBy(username);
                imageEntity.setImageLink(saveLink);
                imageEntity.setStatus(1);
                imageEntity = imageRepository.save(imageEntity);
                saveProduct.setThumbnail(imageEntity.getImageLink());
            }
        }
        productRepository.save(saveProduct);
    }

    @Override
    public ProductDto findById(Long id) {
        ProductEntity productEntity = productRepository.findById(id).get();

        ProductDto productDto = modelMapper.map(productEntity, ProductDto.class);
        productDto.setCategoryId(productEntity.getCategoryEntity().getId());
        productDto.setCategoryName(productEntity.getCategoryEntity().getCategoryName());
        productDto.setCreatedDate(productEntity.getCreatedAt().toString());

        List<ProductImageEntity> productImageEntities = productImageRepository.findAllByProductEntity_Id(id);

        List<ProductImageDto> productImageDtos = productImageEntities.stream().map(productImageEntity -> {
            ProductImageDto productImageDto = modelMapper.map(productImageEntity, ProductImageDto.class);
            productImageDto.setImageLink(productImageEntity.getImageEntity().getImageLink());
            productImageDto.setDescription(productImageEntity.getDescription());
            return productImageDto;
        }).toList();
        if(Objects.equals(productDto.getThumbnail(), "")) {
            productDto.setThumbnail(ImageUtils.IMAGE_LINK);
        }

        System.out.println(productImageDtos.size());
        productDto.setImgs(productImageDtos);

        return productDto;
    }

    @Override
    public ProductEntity findEntityById(Long id) {
        return productRepository.findById(id).get();
    }

    @Override
    public void deleteById(Long id, String username) {
        ProductEntity productEntity = productRepository.findById(id).get();
        productEntity.setStatus(0);
        productEntity.setUpdatedBy(username);
        productEntity.setUpdatedAt(LocalDateTime.now());
        productRepository.save(productEntity);
    }

    @Override
    public void deleteProductImage(Long imageId) {
        productImageRepository.deleteByImageEntity_Id(imageId);
        imageRepository.deleteById(imageId);
    }
}
