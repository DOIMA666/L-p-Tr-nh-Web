package com.shop.ecommerce.service.impl;

import com.shop.ecommerce.entity.ImageEntity;
import com.shop.ecommerce.entity.ProductEntity;
import com.shop.ecommerce.entity.ProductImageEntity;
import com.shop.ecommerce.repository.ImageRepository;
import com.shop.ecommerce.repository.ProductImageRepository;
import com.shop.ecommerce.repository.ProductRepository;
import com.shop.ecommerce.service.ImageService;
import com.shop.ecommerce.utils.ImageUtils;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

@Service
public class ImageServiceImpl implements ImageService {
    private final ImageRepository imageRepository;
    private final ProductRepository productRepository;
    private final ProductImageRepository productImageRepository;
    private final StorageService storageService;
    private final ModelMapper modelMapper;

    public ImageServiceImpl(ImageRepository imageRepository, ProductRepository productRepository, ProductImageRepository productImageRepository, StorageService storageService, ModelMapper modelMapper) {
        this.imageRepository = imageRepository;
        this.productRepository = productRepository;
        this.productImageRepository = productImageRepository;
        this.storageService = storageService;
        this.modelMapper = modelMapper;
    }

    @Override
    public void uploadUpdate(Long id, MultipartFile file, String description, String username) {
        ProductEntity productEntity = productRepository.findById(id).get();
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

                ProductImageEntity productImageEntity = new ProductImageEntity();
                productImageEntity.setProductEntity(productEntity);
                productImageEntity.setImageEntity(imageEntity);
                productImageEntity.setDescription(description);
                productImageEntity.setStatus(1);
                productImageEntity.setCreatedAt(LocalDateTime.now());
                productImageEntity.setUpdatedAt(LocalDateTime.now());
                productImageEntity.setCreatedBy(username);
                productImageEntity.setUpdatedBy(username);
                productImageRepository.save(productImageEntity);
            }


        }
    }

    @Override
    public void updateDescription(Long id, String content, String email) {
        ProductImageEntity productImageEntity = productImageRepository.findByImageEntity_Id(id);
        productImageEntity.setDescription(content);
        productImageEntity.setUpdatedAt(LocalDateTime.now());
        productImageEntity.setUpdatedBy(email);
        productImageRepository.save(productImageEntity);
    }
}
