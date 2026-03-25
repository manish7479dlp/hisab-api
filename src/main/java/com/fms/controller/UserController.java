package com.fms.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fms.dto.UserResponseDTO;
import com.fms.service.UserService;
import com.fms.util.ApiResponseUtil;
import com.fms.util.SuccessResponse;

@RestController
@RequestMapping("/users")
public class UserController {
	private final UserService userService;
	
	public UserController(UserService userService) {
		this.userService = userService;
	}
	
	
	@GetMapping
	public ResponseEntity<SuccessResponse<List<UserResponseDTO>>> getAllUsers() {
		List<UserResponseDTO> userResponseDTOs = userService.getUsers();
		
		return ApiResponseUtil.success("Users fetch successfully", userResponseDTOs);
	}
	
}
