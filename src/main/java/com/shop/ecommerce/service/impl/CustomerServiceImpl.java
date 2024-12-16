package com.shop.ecommerce.service.impl;

import com.shop.ecommerce.entity.CustomerEntity;
import com.shop.ecommerce.entity.ImageEntity;
import com.shop.ecommerce.entity.UserEntity;
import com.shop.ecommerce.enums.GenderEnum;
import com.shop.ecommerce.payload.dto.CustomerDto;
import com.shop.ecommerce.repository.*;
import com.shop.ecommerce.service.CustomerService;
import com.shop.ecommerce.utils.DateUtils;
import com.shop.ecommerce.utils.ImageUtils;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


@Service
public class CustomerServiceImpl implements CustomerService {
    private final CustomerRepository customerRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final FeedbackRepository feedbackRepository;
    private final ImageRepository imageRepository;
    private final StorageService storageService;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;

    public CustomerServiceImpl(CustomerRepository customerRepository, OrderDetailRepository orderDetailRepository, FeedbackRepository feedbackRepository, ImageRepository imageRepository, StorageService storageService, UserRepository userRepository, ModelMapper modelMapper, PasswordEncoder passwordEncoder) {
        this.customerRepository = customerRepository;
        this.orderDetailRepository = orderDetailRepository;
        this.feedbackRepository = feedbackRepository;
        this.imageRepository = imageRepository;
        this.storageService = storageService;
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
        this.passwordEncoder = passwordEncoder;
    }


    @Override
    public CustomerEntity saveEntity(CustomerEntity customerEntity) {
        return customerRepository.save(customerEntity);
    }

    @Override
    public Long countCustomers() {
        return customerRepository.countByStatus(1);
    }

    @Override
    public List<CustomerDto> getAll() {
        List<CustomerEntity> customerEntities = customerRepository.getAll();
        return customerEntities.stream().map(customerEntity -> {
            CustomerDto customerDto = modelMapper.map(customerEntity, CustomerDto.class);
            if(customerEntity.getUserEntity() == null) {
                customerDto.setEmail("");
            }
            else {
                customerDto.setEmail(customerEntity.getUserEntity().getEmail());
            }
            if(customerEntity.getUserEntity().getAvatar() != null) {
                customerDto.setImageLink(customerEntity.getUserEntity().getAvatar().getImageLink());
            }
            else {
                customerDto.setImageLink("");
            }
            return customerDto;
        }).collect(Collectors.toList());
    }

    @Override
    public CustomerDto getProfile(String email) {
        UserEntity userEntity = userRepository.findByEmailAndStatus(email, 1).get();
        CustomerEntity customerEntity = userEntity.getCustomerEntity();
        CustomerDto customerDto = new CustomerDto();
        customerDto.setId(customerEntity.getId());
        customerDto.setFullName(customerEntity.getFullName());
        customerDto.setAddress(customerEntity.getAddress());
        if(customerEntity.getDob() != null) {
            customerDto.setDob(customerEntity.getDob().toString());
        } else customerDto.setDob("");

        if(customerEntity.getGender() != null) {
            customerDto.setGender(customerEntity.getGender().name());
        } else customerDto.setGender("");

        customerDto.setAddress(customerEntity.getAddress());
        customerDto.setEmail(email);
        customerDto.setPhoneNumber(userEntity.getPhoneNumber());
        ImageEntity imageEntity = userEntity.getAvatar();

        if(imageEntity != null) {
            customerDto.setImageLink(imageEntity.getImageLink());
            customerDto.setImageId(imageEntity.getId());
        }
        else {
            customerDto.setImageLink("");
        }
        customerDto.setPassword("");
        customerDto.setNewPassword("");
        return customerDto;
    }

    @Override
    @Transactional
    public void updateProfile(CustomerDto customerDto, MultipartFile file, String email) {
        UserEntity userEntity = userRepository.findByEmailAndStatus(email, 1).get();
        if (!file.isEmpty() && ImageUtils.isImageFile(file)) {
            if(userEntity.getAvatar() == null) {
                ImageEntity imageEntity = new ImageEntity();
                imageEntity.setStatus(1);
                imageEntity.setCreatedAt(LocalDateTime.now());
                imageEntity.setUpdatedAt(LocalDateTime.now());
                imageEntity.setCreatedBy(email);
                imageEntity.setUpdatedBy(email);
                String fileName = "user_" + userEntity.getId();
                String saveLink = "";
                saveLink = storageService.uploadFile(file, fileName);
                imageEntity.setImageLink(saveLink);
                imageEntity = imageRepository.save(imageEntity);
                userEntity.setAvatar(imageEntity);
            }
            else {
                ImageEntity imageEntity = userEntity.getAvatar();
                imageEntity.setUpdatedAt(LocalDateTime.now());
                imageEntity.setUpdatedBy(email);
                String fileName = "user_" + userEntity.getId();
                String saveLink = "";
                saveLink = storageService.uploadFile(file, fileName);
                imageEntity.setImageLink(saveLink);
                imageEntity = imageRepository.save(imageEntity);
                userEntity.setAvatar(imageEntity);
            }
        }
        CustomerEntity customerEntity = userEntity.getCustomerEntity();
        CustomerEntity saveCustomer = new CustomerEntity();
        saveCustomer.setId(customerDto.getId());
        saveCustomer.setCreatedAt(customerEntity.getCreatedAt());
        saveCustomer.setCreatedBy(customerEntity.getCreatedBy());
        saveCustomer.setStatus(1);
        saveCustomer.setCartEntity(customerEntity.getCartEntity());
        saveCustomer.setId(customerDto.getId());
        saveCustomer.setPhone(customerDto.getPhoneNumber());
        saveCustomer.setFullName(customerDto.getFullName());
        saveCustomer.setAddress(customerDto.getAddress());
        String DATE_FORMAT="yyyy-MM-dd";
        DateTimeFormatter formatter=DateTimeFormatter.ofPattern(DATE_FORMAT);
        LocalDate date = LocalDate.parse(customerDto.getDob(), formatter);
        saveCustomer.setDob(date);
        saveCustomer.setGender(GenderEnum.valueOf(customerDto.getGender()));
        saveCustomer.setUpdatedBy(email);
        saveCustomer.setUpdatedAt(LocalDateTime.now());
        saveCustomer.setCreatedAt(userEntity.getCreatedAt());
        saveCustomer.setCreatedBy(userEntity.getCreatedBy());
        saveCustomer.setCartEntity(customerEntity.getCartEntity());
        saveCustomer.setFeedbackEntities(customerEntity.getFeedbackEntities());

        saveCustomer.setFeedbackEntities(new HashSet<>());
        saveCustomer.getFeedbackEntities().addAll(customerEntity.getFeedbackEntities());

        saveCustomer.setOrderEntities(new HashSet<>());
        saveCustomer.getOrderEntities().addAll(customerEntity.getOrderEntities());

        saveCustomer.setDob(DateUtils.toDate(customerDto.getDob()));
        customerRepository.save(saveCustomer);

        userEntity.setEmail(customerDto.getEmail());
        if(Objects.equals(customerDto.getNewPassword(), "")) {
            userEntity.setPassword(userEntity.getPassword());
        }
        else {
            userEntity.setPassword(passwordEncoder.encode(customerDto.getNewPassword()));
        }
        userEntity.setPhoneNumber(customerDto.getPhoneNumber());
        userEntity = userRepository.save(userEntity);
        saveCustomer.setUserEntity(userEntity);
        saveCustomer = customerRepository.save(saveCustomer);
        userEntity.setCustomerEntity(saveCustomer);
        userRepository.save(userEntity);
    }

    @Override
    public Long getIdByEmail(String email) {
        return customerRepository.findByUserEntity_Email(email).getId();
    }

    @Override
    public CustomerEntity findByEmail(String email) {
        return customerRepository.findByUserEntity_Email(email);
    }

    @Override
    public Boolean checkExistOrder(Long userId, Long productId) {
        return orderDetailRepository.existsByOrderEntity_CustomerEntity_UserEntity_IdAndProductEntity_Id(userId, productId)
                && !feedbackRepository.existsByCustomerEntity_UserEntity_IdAndProductEntity_Id(userId, productId);
    }

    @Override
    public CustomerDto findById(Long customerId) {
        CustomerEntity customerEntity = customerRepository.findById(customerId).get();
        CustomerDto customerDto = new CustomerDto();
        customerDto.setId(customerEntity.getId());
        customerDto.setFullName(customerEntity.getFullName());
        customerDto.setAddress(customerEntity.getAddress());
        if(customerEntity.getDob() != null) {
            customerDto.setDob(customerEntity.getDob().toString());
        }
        else {
            customerDto.setDob("");
        }

        if(customerEntity.getGender() == null) {
            customerDto.setGender("");
        }
        else {
            customerDto.setGender(customerEntity.getGender().name());
        }
        customerDto.setAddress(customerEntity.getAddress());
        ImageEntity imageEntity = customerEntity.getUserEntity().getAvatar();

        if(imageEntity != null) {
            customerDto.setImageLink(imageEntity.getImageLink());
            customerDto.setImageId(imageEntity.getId());
        }
        else {
            customerDto.setImageLink("");
        }
        customerDto.setPassword("");
        customerDto.setNewPassword("");
        return customerDto;
    }
}
