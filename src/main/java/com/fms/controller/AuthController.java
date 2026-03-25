package com.fms.controller;

import org.antlr.v4.runtime.misc.NotNull;
import org.apache.coyote.BadRequestException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fms.dto.LoginRequestDTO;
import com.fms.dto.LoginResponseDTO;
import com.fms.dto.UserRequestDTO;
import com.fms.dto.UserResponseDTO;
import com.fms.service.AuthService;
import com.fms.service.UserService;
import com.fms.util.ApiResponseUtil;
import com.fms.util.SuccessResponse;

@RestController
@RequestMapping("/auth")
public class AuthController {
	Logger logger = LoggerFactory.getLogger(this.getClass());
	private final UserService userService;
	private final AuthService authService;
	
	public AuthController(UserService userService,
					      AuthService authService) {
		this.userService = userService;
		this.authService = authService;
	}
	
	@GetMapping("test")
	public ResponseEntity<SuccessResponse<String>> test() {
		String message = "Auth working fine";
		return ApiResponseUtil.success(message, message);
	}
	
	@PostMapping("/register")
	public ResponseEntity<SuccessResponse<UserResponseDTO>> register(@RequestBody UserRequestDTO userRequestDTO) {
		UserResponseDTO userResponseDTO = userService.register(userRequestDTO);
		return ApiResponseUtil.created("User created sucessfully", userResponseDTO);
	}
	
	@PostMapping("login")
	public ResponseEntity<SuccessResponse<LoginResponseDTO>> login(@RequestBody LoginRequestDTO loginRequestDTO) {
		LoginResponseDTO loginResponseDTO = authService.login(loginRequestDTO);
		return ApiResponseUtil.success("User login successfully", loginResponseDTO);
	}
	
	@PatchMapping("/{userName}/reset-password")
	public ResponseEntity<SuccessResponse<Boolean>> resetPassword(@PathVariable String userName, @RequestParam String newPassword) throws BadRequestException {
		authService.resetPassword(userName, newPassword);
		return ApiResponseUtil.success("Password upadated Successfully", true);
	}
	

}
