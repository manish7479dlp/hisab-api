package com.fms.dto;

public class LoginResponseDTO extends UserResponseDTO {
  private String token;
  
  public LoginResponseDTO() {}

public LoginResponseDTO(String token) {
	super();
	this.token = token;
}

public String getToken() {
	return token;
}

public void setToken(String token) {
	this.token = token;
}
  
}
