package com.shop.ecommerce.service;

import com.shop.ecommerce.entity.ProductEntity;
import com.shop.ecommerce.payload.dto.ProductDto;
import com.shop.ecommerce.payload.request.ProductFilterRequest;
import com.shop.ecommerce.payload.response.BaseResponse;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ProductService {
    ProductEntity save(ProductEntity productEntity);
    List<ProductDto> getAllIfActive();

    List<ProductDto> getAll();
    List<ProductEntity> getTop4();

    Long countProductActive();
    void save(ProductDto productDto, String username, MultipartFile file);
    void update(ProductDto productDto, String username, MultipartFile file);
    ProductDto findById(Long id);
    ProductEntity findEntityById(Long id);
    void deleteById(Long id, String username);
    void deleteProductImage(Long imageId);
    BaseResponse<Page<ProductDto>> getAllByFilter(ProductFilterRequest filterRequest, int page, int size);
    BaseResponse<Page<ProductDto>> getAllInClientByFilter(ProductFilterRequest filterRequest, int page, int size);

}
