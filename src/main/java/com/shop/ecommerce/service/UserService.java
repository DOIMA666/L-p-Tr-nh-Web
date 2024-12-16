package com.shop.ecommerce.service;

import com.shop.ecommerce.entity.UserEntity;
import com.shop.ecommerce.payload.dto.UserDto;
import com.shop.ecommerce.payload.request.UserFilterRequest;
import com.shop.ecommerce.payload.response.BaseResponse;
import org.springframework.data.domain.Page;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;


public interface UserService extends UserDetailsService {

    Boolean processOAuthPostLogin(String email, String fullName);

    UserEntity saveUser(UserEntity user);

    Optional<UserEntity> findByEmail(String email);

    int recoverPassword(String password, String email);
    UserDto getProfile(String email);
    void updateProfile(UserDto userDto, MultipartFile file, String email);
    boolean validateCredentials(String username, String password);
    List<UserDto> getAll();

    void updateUserStatus(Long id, Integer status);
    BaseResponse<Page<UserDto>> getAllByFilter(UserFilterRequest filterRequest, int page, int size);
    Long findIdByEmail(String email);

    List<UserDto> getAllAdmins();
    UserEntity findById(Long id);
    UserDto findUserDtoById(Long id);

}

