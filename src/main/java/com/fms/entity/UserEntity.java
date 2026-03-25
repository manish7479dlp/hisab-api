package com.fms.entity;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;



@Entity
@Table(name = "user")
public class UserEntity {
	@Id
	@Column(name = "userName")
	private String userName;
	private String name;
	private String password;
	private String image;
	private String provider;
	private List<String> roles;
	
	public UserEntity() {}
	
	public UserEntity(String userName, String name, String password, String image, String provider, List<String> roles) {
		super();
		this.userName = userName;
		this.name = name;
		this.password = password;
		this.image = image;
		this.provider = provider;
		this.roles = roles;
	}

	public String getUserName() {   // ✔ Correct
	    return userName;
	}

	public void setUserName(String userName) {   // ✔ Already correct
	    this.userName = userName;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getProvider() {
		return provider;
	}

	public void setProvider(String provider) {
		this.provider = provider;
	}

	public List<String> getRoles() {
		return roles;
	}

	public void setRoles(List<String> roles) {
		this.roles = roles;
	}

}
