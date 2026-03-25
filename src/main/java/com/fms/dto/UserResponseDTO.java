package com.fms.dto;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


public class UserResponseDTO {
	private String userName;
	private String name;
	private String image;
	private String provider;
	private List<String> roles;
	
	public UserResponseDTO() {}
	
	public UserResponseDTO(String userName, String name, String image, String provider, List<String> roles) {
		super();
		this.userName = userName;
		this.name = name;
		this.image = image;
		this.provider = provider;
		this.roles = roles;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
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
