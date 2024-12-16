package com.shop.ecommerce.utils;

import org.springframework.web.multipart.MultipartFile;

public class ImageUtils {
    public static String IMAGE_LINK = "https://img.freepik.com/premium-vector/default-image-icon-vector-missing-picture-page-website-design-mobile-app-no-photo-available_87543-11093.jpg?w=1060";
    public static boolean isImageFile(MultipartFile file) {
        String contentType = file.getContentType();
        if (contentType != null) {
            return contentType.startsWith("image/");
        }
        return false;
    }
}
