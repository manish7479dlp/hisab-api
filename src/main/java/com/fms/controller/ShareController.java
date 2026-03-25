package com.fms.controller;

import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fms.dto.GroupRequestDTO;
import com.fms.dto.GroupResponseDTO;
import com.fms.dto.ShareRequestDTO;
import com.fms.dto.ShareResponseDTO;
import com.fms.service.GroupService;
import com.fms.service.ItemService;
import com.fms.service.ShareService;
import com.fms.util.ApiResponseUtil;
import com.fms.util.SuccessResponse;

@RestController
@RequestMapping("/shares")
public class ShareController {
	@Autowired
	ShareService shareService;
	
//	@GetMapping
//	public ResponseEntity<SuccessResponse<Page<GroupResponseDTO>>> getAll(Pageable pageable) {
//		Page<GroupResponseDTO> groups = groupService.getAll(pageable);
//		return ApiResponseUtil.success("Group data fetch successfully", groups); 
//	}
	
	
	@PostMapping("/group")
	public ResponseEntity<SuccessResponse<ShareResponseDTO>> add(@RequestBody ShareRequestDTO shareRequestDTO) throws BadRequestException {
		ShareResponseDTO response = shareService.add(shareRequestDTO);
		return ApiResponseUtil.created("Group shared successfully", response);
	}
	
	@PatchMapping("/{id}/accept")
	public ResponseEntity<SuccessResponse<Boolean>> accept(@PathVariable(name = "id") Long shareId) throws BadRequestException {
		Boolean  response = shareService.isAccepted(shareId);
		return ApiResponseUtil.created("Request accepted", response);
	}
}
