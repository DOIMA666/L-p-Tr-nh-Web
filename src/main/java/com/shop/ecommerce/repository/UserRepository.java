package com.shop.ecommerce.repository;

import com.shop.ecommerce.entity.UserEntity;
import com.shop.ecommerce.payload.dto.UserDto;
import com.shop.ecommerce.payload.request.UserFilterRequest;
import jakarta.transaction.Transactional;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@SpringBootApplication
public interface UserRepository extends JpaRepository<UserEntity, Long> {

    public UserEntity save(UserEntity user);

    Optional<UserEntity> findByEmailAndStatus(String email, Integer status);

    //    public UserDto save(UserDto userDto);
    Optional<UserEntity> findByEmail(String email);


    @Modifying
    @Transactional
    @Query("UPDATE UserEntity as u set u.password =?1 where u.email=?2")
    int updatePassword(String password, String email);

    @Query("select u.password from UserEntity u where u.email=?1")
    String getUserEntitiesByUserId(String email);

    boolean existsByEmailAndPassword(String email, String password);

    @Query("select new com.shop.ecommerce.payload.dto.UserDto(u.id, u.email, count(f), u.status, u.createdAt) " +
            "from UserEntity u " +
            "left join u.customerEntity c " +
            "left join c.feedbackEntities f " +
            "group by u.id, u.email, u.status, u.createdAt")
    List<UserDto> getAll();

    @Query(value = "SELECT u FROM UserEntity u " +
            "LEFT JOIN u.customerEntity c " +
            "WHERE " +
            "(:#{#condition.email} IS NULL OR LOWER(u.email) LIKE '%' || LOWER(:#{#condition.email}) || '%') "
    )
    Page<UserEntity> findAllByFilter(@Param("condition") UserFilterRequest filterRequest, Pageable pageable);

    @Query(value = "SELECT u.id FROM UserEntity u " +
            "WHERE u.email = :email"
    )
    Long findIdByEmail(@Param("email") String email);

    @Query("SELECT DISTINCT u FROM UserEntity u " +
            "LEFT JOIN FETCH u.roleEntities r " +
            "WHERE r.name = 'ADMIN' OR r.name = 'SUPER_ADMIN'")
    List<UserEntity> getAllAdmins();


}
