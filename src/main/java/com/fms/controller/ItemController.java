package com.fms.controller;

import java.util.List;

import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fms.dto.ItemRequestDTO;
import com.fms.dto.ItemResponseDTO;
import com.fms.service.ItemService;
import com.fms.util.ApiResponseUtil;
import com.fms.util.SuccessResponse;
import com.fms.util.Utils;

@RestController
@RequestMapping("/items")
public class ItemController {
	@Autowired
	ItemService itemService;
	
	
	@GetMapping("/personal")
	public ResponseEntity<SuccessResponse<Page<ItemResponseDTO>>> getPersonalItemes(Pageable pageable) {
		String userName = Utils.getAuthUserName();
		Page<ItemResponseDTO> items = itemService.getPersonal(userName, pageable);
		return ApiResponseUtil.success("All items fetched successfully", items);
				
	}
	
	@GetMapping
	public ResponseEntity<SuccessResponse<Page<ItemResponseDTO>>> getItemes(Pageable pageable) {
		String userName = Utils.getAuthUserName();
		Page<ItemResponseDTO> items = itemService.getByUserName(userName, pageable);
		return ApiResponseUtil.success("All items fetched successfully", items);
				
	}
	
	@PostMapping("/personal")
	public ResponseEntity<SuccessResponse<ItemResponseDTO>> add(@RequestBody ItemRequestDTO itemRequestDTO) throws BadRequestException {
		String userName = Utils.getAuthUserName();
		ItemResponseDTO itemResponseDTO = itemService.add(userName,null, itemRequestDTO);
		return ApiResponseUtil.created("Item created sucessfully", itemResponseDTO); 
	}
	
	@DeleteMapping("{id}")
	public ResponseEntity<SuccessResponse<Boolean>> deleteById(@PathVariable Long id) throws BadRequestException {
		String userName = Utils.getAuthUserName();
		itemService.deleteByID(id, userName);
		return ApiResponseUtil.success("Item deleted successfully", true);
	}
	
	@PutMapping("{id}")
	public ResponseEntity<SuccessResponse<ItemResponseDTO>> updateById(@PathVariable Long id,  @RequestBody ItemRequestDTO itemRequestDTO) throws BadRequestException {
		String userName = Utils.getAuthUserName();
		ItemResponseDTO itemResponseDTO =  itemService.updateByID(id, userName, itemRequestDTO);
		return ApiResponseUtil.success("Item updated successfully", itemResponseDTO);
	}
	
	

	
}
