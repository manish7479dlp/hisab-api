package com.fms.controller;

import java.util.List;

import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fms.dto.GroupRequestDTO;
import com.fms.dto.GroupResponseDTO;
import com.fms.dto.ItemRequestDTO;
import com.fms.dto.ItemResponseDTO;
import com.fms.service.GroupService;
import com.fms.service.ItemService;
import com.fms.util.ApiResponseUtil;
import com.fms.util.SuccessResponse;
import com.fms.util.Utils;

@RestController
@RequestMapping("/groups")
public class GroupController {
	
	@Autowired
	GroupService groupService;
	
	@Autowired
	ItemService itemService;
	
	@GetMapping
	public ResponseEntity<SuccessResponse<List<GroupResponseDTO>>> getMyAndSharedGroups() throws BadRequestException {
		String userName = Utils.getAuthUserName();
		List<GroupResponseDTO> groups = groupService.getMyAndSharedGroups(userName);
		return ApiResponseUtil.success("Group data fetch successfully.", groups); 
	}
	
	@GetMapping("/{id}/items")
	public ResponseEntity<SuccessResponse<Page<ItemResponseDTO>>> getAllItemsByGroupId(@PathVariable(name = "id") Long groupId, Pageable pageable) throws BadRequestException {
		Page<ItemResponseDTO> items = itemService.getByGroupId(groupId, pageable);
		return ApiResponseUtil.success("Group data fetch successfully.", items); 
	}

	@GetMapping("/all")
	public ResponseEntity<SuccessResponse<Page<GroupResponseDTO>>> getAll(Pageable pageable) {
		Page<GroupResponseDTO> groups = groupService.getAll(pageable);
		return ApiResponseUtil.success("Group data fetch successfully", groups); 
	}
	
	@PostMapping
	public ResponseEntity<SuccessResponse<GroupResponseDTO>> add(@RequestBody GroupRequestDTO groupRequestDTO) {
		GroupResponseDTO groupResponseDTO = groupService.create(groupRequestDTO);
		return ApiResponseUtil.created("Group created successfully", groupResponseDTO);
	}
	
	@PostMapping("{id}/item")
	public ResponseEntity<SuccessResponse<ItemResponseDTO>> add(@PathVariable(name = "id") Long groupId,
								@RequestBody ItemRequestDTO itemRequestDTO) throws BadRequestException {
		String userName = Utils.getAuthUserName();
		ItemResponseDTO itemResponseDTO = itemService.add(userName,groupId, itemRequestDTO);
		return ApiResponseUtil.created("Item created and added to group sucessfully", itemResponseDTO); 
	}
	
	@PutMapping("{id}")
	public ResponseEntity<SuccessResponse<GroupResponseDTO>> update(@PathVariable(name = "id") Long groupId,
								@RequestBody GroupRequestDTO groupRequestDTO) throws BadRequestException {
		GroupResponseDTO groupResponseDTO = groupService.updateById(groupId, groupRequestDTO);
		return ApiResponseUtil.success("Group updated successfully", groupResponseDTO); 
	}
	
	@DeleteMapping("{id}")
	public ResponseEntity<SuccessResponse<Boolean>> delete(@PathVariable(name = "id") Long groupId) throws BadRequestException {
		Boolean response = groupService.deleteById(groupId);
		return ApiResponseUtil.success("Group deleted successfully", true); 
	}
}
