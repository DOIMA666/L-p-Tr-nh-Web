package com.shop.ecommerce.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.shop.ecommerce.enums.ProviderEnum;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.Set;

@Entity
@Table(name = "Users",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "email")
        },
        catalog = "")
@Data
public class UserEntity extends AbstractEntity {
    @NotBlank
    @Size(max = 50)
    private String email;

    @Size(max = 120)
    private String password;

    @Basic
    @Column(name = "phone_number", nullable = true, length = 45)
    private String phoneNumber;

    @OneToOne(mappedBy = "userEntity")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @JsonIgnore
    private CustomerEntity customerEntity;


    @ManyToMany(fetch = FetchType.EAGER)
    @JsonIgnore
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @JoinTable(name = "user_role",
            joinColumns = @JoinColumn(name="user_id"),
            inverseJoinColumns = @JoinColumn(name="role_id"))
    private Set<RoleEntity> roleEntities;


    @OneToOne
    @JoinColumn(name = "avatar")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private ImageEntity avatar;

    @Enumerated(EnumType.STRING)
    private ProviderEnum provider;



    @OneToOne(mappedBy = "userEntity")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @JsonIgnore
    private ReplyEntity replyEntity;



	public String getEmail() {
		return email;
	}



	public void setEmail(String email) {
		this.email = email;
	}



	public String getPassword() {
		return password;
	}



	public void setPassword(String password) {
		this.password = password;
	}



	public String getPhoneNumber() {
		return phoneNumber;
	}



	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}



	public CustomerEntity getCustomerEntity() {
		return customerEntity;
	}



	public void setCustomerEntity(CustomerEntity customerEntity) {
		this.customerEntity = customerEntity;
	}



	public Set<RoleEntity> getRoleEntities() {
		return roleEntities;
	}



	public void setRoleEntities(Set<RoleEntity> roleEntities) {
		this.roleEntities = roleEntities;
	}



	public ImageEntity getAvatar() {
		return avatar;
	}



	public void setAvatar(ImageEntity avatar) {
		this.avatar = avatar;
	}



	public ProviderEnum getProvider() {
		return provider;
	}



	public void setProvider(ProviderEnum provider) {
		this.provider = provider;
	}



	public ReplyEntity getReplyEntity() {
		return replyEntity;
	}



	public void setReplyEntity(ReplyEntity replyEntity) {
		this.replyEntity = replyEntity;
	}
    
    
}
