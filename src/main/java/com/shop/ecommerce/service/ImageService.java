package com.shop.ecommerce.service;

import org.springframework.web.multipart.MultipartFile;

public interface ImageService {
    void uploadUpdate(Long id, MultipartFile file, String description, String username);

    void updateDescription(Long id, String content, String email);

}
