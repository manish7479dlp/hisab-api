package com.fms.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fms.util.ApiResponseUtil;
import com.fms.util.SuccessResponse;

@RestController
public class Test {
	@GetMapping("")
	public ResponseEntity<SuccessResponse<String>> test() {
		String message = "Api working fine";
		return ApiResponseUtil.success(message, message);
	}
}
