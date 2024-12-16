package com.shop.ecommerce.entity;

import com.shop.ecommerce.enums.GenderEnum;
import com.shop.ecommerce.utils.DateUtils;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.Set;

@Entity
@Table(name = "Customers",
        catalog = "")
@Data
public class CustomerEntity extends AbstractEntity {
    @Basic
    @Column(name = "full_name", nullable = true, length = 255, columnDefinition = "nvarchar(255)")
    private String fullName;

    @Enumerated(EnumType.STRING)
    @Column(name = "gender", nullable = true, length = 255, columnDefinition = "nvarchar(255)")
    private GenderEnum gender;

    @Basic
    @Column(name = "phone", nullable = true, length = 20)
    private String phone;

    @Basic
    @Column(name = "address", nullable = true, length = 255, columnDefinition = "nvarchar(255)")
    private String address;

    @DateTimeFormat(pattern = DateUtils.DATE_FORMAT)
    @Column(name = "dob", nullable = true)
    private LocalDate dob;

    @OneToOne(cascade = CascadeType.ALL)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private UserEntity userEntity;

    @OneToOne(cascade = CascadeType.ALL)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @JoinColumn(name = "cart_id", referencedColumnName = "id")
    private CartEntity cartEntity;

    @OneToMany(mappedBy = "customerEntity", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Set<FeedbackEntity> feedbackEntities;

    @OneToMany(mappedBy = "customerEntity", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Set<OrderEntity> orderEntities;

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public GenderEnum getGender() {
		return gender;
	}

	public void setGender(GenderEnum gender) {
		this.gender = gender;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public LocalDate getDob() {
		return dob;
	}

	public void setDob(LocalDate dob) {
		this.dob = dob;
	}

	public UserEntity getUserEntity() {
		return userEntity;
	}

	public void setUserEntity(UserEntity userEntity) {
		this.userEntity = userEntity;
	}

	public CartEntity getCartEntity() {
		return cartEntity;
	}

	public void setCartEntity(CartEntity cartEntity) {
		this.cartEntity = cartEntity;
	}

	public Set<FeedbackEntity> getFeedbackEntities() {
		return feedbackEntities;
	}

	public void setFeedbackEntities(Set<FeedbackEntity> feedbackEntities) {
		this.feedbackEntities = feedbackEntities;
	}

	public Set<OrderEntity> getOrderEntities() {
		return orderEntities;
	}

	public void setOrderEntities(Set<OrderEntity> orderEntities) {
		this.orderEntities = orderEntities;
	}
    
    

}
