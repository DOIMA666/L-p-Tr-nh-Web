package com.shop.ecommerce.service.impl;

import com.shop.ecommerce.config.SecurityUser;
import com.shop.ecommerce.entity.*;
import com.shop.ecommerce.enums.GenderEnum;
import com.shop.ecommerce.enums.ProviderEnum;
import com.shop.ecommerce.payload.dto.UserDto;
import com.shop.ecommerce.payload.request.UserFilterRequest;
import com.shop.ecommerce.payload.response.BaseResponse;
import com.shop.ecommerce.repository.*;
import com.shop.ecommerce.service.UserService;
import com.shop.ecommerce.utils.ImageUtils;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;


@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final CustomerRepository customerRepository;
    private final CartRepository cartRepository;
    private final ModelMapper modelMapper;
    private final StorageService storageService;
    private final ImageRepository imageRepository;

    @Autowired
    @Lazy
    private PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository, CustomerRepository customerRepository, CartRepository cartRepository, ModelMapper modelMapper, StorageService storageService, ImageRepository imageRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.customerRepository = customerRepository;
        this.cartRepository = cartRepository;
        this.modelMapper = modelMapper;
        this.storageService = storageService;
        this.imageRepository = imageRepository;
    }

    @Override
    @Transactional
    public Boolean processOAuthPostLogin(String email, String fullName) {
        Optional<UserEntity> optionalUserEntity = userRepository.findByEmail(email);
        if(optionalUserEntity.isPresent()) {
            UserEntity user = optionalUserEntity.get();
            if(user.getStatus() == 0) return false;
        }
        else {
            RoleEntity client = roleRepository.findById(3L).get();
            Set<RoleEntity> roleEntities = new HashSet<>();
            roleEntities.add(client);

            UserEntity userEntity = new UserEntity();
            userEntity.setEmail(email);
            userEntity.setProvider(ProviderEnum.GOOGLE);

            userEntity.setRoleEntities(roleEntities);
            userEntity.setCreatedBy("SUPER_ADMIN");
            userEntity.setUpdatedBy("SUPER_ADMIN");
            userEntity.setStatus(1);

            UserEntity savedUser = userRepository.save(userEntity);

            CustomerEntity customerEntity = new CustomerEntity();
            customerEntity.setFullName(fullName);
            customerEntity.setStatus(1);
            customerEntity.setCreatedBy("SUPER_ADMIN");
            customerEntity.setUpdatedBy("SUPER_ADMIN");
            customerEntity.setAddress("");
            customerEntity.setDob(LocalDate.now());
            customerEntity.setPhone(LocalDateTime.now().toString());
            customerEntity.setGender(GenderEnum.MALE);
            customerEntity.setUserEntity(savedUser);
            customerEntity.setStatus(1);

            customerEntity = customerRepository.save(customerEntity);


            CartEntity cartEntity = new CartEntity();
            cartEntity.setTotalCost(BigDecimal.valueOf(0));
            cartEntity.setCreatedBy("SUPER_ADMIN");
            cartEntity.setUpdatedBy("SUPER_ADMIN");
            cartEntity.setCreatedAt(LocalDateTime.now());
            cartEntity.setUpdatedAt(LocalDateTime.now());
            cartEntity.setStatus(1);
            Set<CartDetailEntity> cartDetailEntities = new HashSet<>();
            cartEntity.setCartDetailEntities(cartDetailEntities);



            cartEntity.setCustomerEntity(customerEntity);
            cartEntity = cartRepository.save(cartEntity);

            customerEntity.setCartEntity(cartEntity);
            customerEntity = customerRepository.save(customerEntity);
            savedUser.setCustomerEntity(customerEntity);
            userRepository.save(savedUser);
            System.out.println("Created new savedUser: " + email + "has full name: " + fullName);
        }
        return true;
    }

    @Override
    public BaseResponse<Page<UserDto>> getAllByFilter(UserFilterRequest filterRequest, int page, int size) {
        Pageable pageable = PageRequest.of(page,size);
        Page<UserEntity> userEntities = userRepository.findAllByFilter(filterRequest,pageable);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        List<UserDto> userDtos = userEntities.getContent().stream().map(userEntity -> {
            UserDto userDto = modelMapper.map(userEntity,UserDto.class);
            ImageEntity imageEntity = userEntity.getAvatar();
            if (imageEntity != null) {
                userDto.setAvatar(imageEntity.getImageLink());
            } else {
                userDto.setAvatar("");
            }
            userDto.setStatus(userEntity.getStatus());
            String formattedDate = userEntity.getCreatedAt().format(formatter);
            userDto.setCreatedDate(formattedDate);
            return userDto;
        }).collect(Collectors.toList());

        Page<UserDto> pageData = new PageImpl(userDtos,pageable,userEntities.getTotalElements());
        BaseResponse<Page<UserDto>> response = new BaseResponse<>();
        response.setCode(200);
        response.setMessage("success");
        response.setData(pageData);
        return response;
    }

    @Override
    public Long findIdByEmail(String email) {
        return userRepository.findIdByEmail(email);
    }

    @Override
    public List<UserDto> getAllAdmins() {
        List<UserEntity> userEntities = userRepository.getAllAdmins();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        List<UserDto> userDtos = userEntities.stream().map(userEntity -> {
            UserDto userDto = modelMapper.map(userEntity,UserDto.class);
            ImageEntity imageEntity = userEntity.getAvatar();
            if (imageEntity != null) {
                userDto.setAvatar(imageEntity.getImageLink());
            } else {
                userDto.setAvatar("");
            }
            String formattedDate = userEntity.getCreatedAt().format(formatter);
            userDto.setCreatedDate(formattedDate);
            return userDto;
        }).collect(Collectors.toList());
        return userDtos;
    }

    @Override
    public UserEntity findById(Long id) {
        return userRepository.findById(id).get();
    }

    @Override
    public UserDto findUserDtoById(Long id) {
        UserEntity userEntity = userRepository.findById(id).get();
        UserDto userDto = modelMapper.map(userEntity,UserDto.class);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        ImageEntity imageEntity = userEntity.getAvatar();
        if (imageEntity != null) {
            userDto.setAvatar(imageEntity.getImageLink());
        } else {
            userDto.setAvatar("");
        }
        String formattedDate = userEntity.getCreatedAt().format(formatter);
        userDto.setCreatedDate(formattedDate);
        return userDto;
    }

    @Override
    public int recoverPassword(String password, String email) {
        return userRepository.updatePassword(password, email);
    }

    @Override
    public UserDto getProfile(String email) {
        UserEntity user = userRepository.findByEmailAndStatus(email, 1).get();
        UserDto userDto = modelMapper.map(user, UserDto.class);
        ImageEntity imageEntity = user.getAvatar();
        if (imageEntity != null) {
            userDto.setAvatar(imageEntity.getImageLink());
        } else {
            userDto.setAvatar("");
        }
        userDto.setNewPassword("");
        userDto.setPassword("");
        return userDto;
    }

    @Override
    public void updateProfile(UserDto userDto, MultipartFile file, String email) {
        try{
            UserEntity userEntity = userRepository.findByEmailAndStatus(email, 1).orElseThrow();

            if (!file.isEmpty() && ImageUtils.isImageFile(file)) {
                if (userEntity.getAvatar() == null) {
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
                } else {
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

            userEntity.setUpdatedBy(email);
            userEntity.setUpdatedAt(LocalDateTime.now());

            userEntity.setEmail(userDto.getEmail());
            if (!userDto.getNewPassword().isEmpty()) {
                userEntity.setPassword(passwordEncoder.encode(userDto.getNewPassword()));
            } else {
                userEntity.setPassword(userEntity.getPassword());
            }
            userRepository.save(userEntity);
        }catch(Exception e) {
            System.out.println(e.getMessage());

        }
    }



    @Override
    public boolean validateCredentials(String username, String password) {
        return userRepository.existsByEmailAndPassword(username, passwordEncoder.encode(password));
    }

    @Override
    public List<UserDto> getAll() {
        return userRepository.getAll();
    }

    @Override
    public void updateUserStatus(Long id, Integer status) {
        UserEntity user = userRepository.findById(id).get();
        user.setStatus(status);
        userRepository.save(user);
    }

    @Override
    public UserEntity saveUser(UserEntity user) {
        return userRepository.save(user);
    }

    @Override
    public Optional<UserEntity> findByEmail(String email) {
        return userRepository.findByEmailAndStatus(email, 1);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<UserEntity> userByUsername = userRepository.findByEmailAndStatus(username, 1);
        if (userByUsername.isEmpty()) {
            System.out.println("Could not find user with that email: {}");
            throw new UsernameNotFoundException("Invalid credentials!");
        }
        UserEntity user = userByUsername.get();
        System.out.println(user);
        if (!user.getEmail().equals(username)) {
            System.out.println("Could not find user with that username: {}");
            throw new UsernameNotFoundException("Invalid credentials!");
        }
        Set<GrantedAuthority> grantedAuthorities = new HashSet<>();
        for (RoleEntity role : user.getRoleEntities()) {
            grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_" + role.getName().name()));
        }
        System.out.println(grantedAuthorities);
        return new SecurityUser(user.getEmail(), user.getPassword(), true, true, true, true, grantedAuthorities,
                user.getEmail());
    }
}
