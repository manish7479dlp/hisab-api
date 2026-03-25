package com.fms.service;


import java.util.List;

import org.apache.coyote.BadRequestException;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import com.fms.dto.GroupResponseDTO;
import com.fms.dto.ItemRequestDTO;
import com.fms.dto.ItemResponseDTO;
import com.fms.entity.GroupEntity;
import com.fms.entity.ItemEntity;
import com.fms.entity.UserEntity;
import com.fms.repository.GroupRepository;
import com.fms.repository.ItemRepository;
import com.fms.repository.UserRepository;
import com.fms.util.Utils;


@Service
public class ItemService {
	
   private static final Logger logger = LoggerFactory.getLogger(ItemService.class);
   private final ItemRepository itemRepository;
   private final ModelMapper modelMapper;
   private final UserRepository userRepository;
   private final GroupRepository groupRepository;
   private final GroupService groupService;
   
   public ItemService(ItemRepository itemRepository,
					   ModelMapper modelMapper,
					   UserRepository userRepository,
					   GroupRepository groupRepository,
					   GroupService groupService) {
	   this.itemRepository = itemRepository;
	   this.modelMapper = modelMapper;
	   this.userRepository = userRepository;
	   this.groupRepository = groupRepository;
	   this.groupService = groupService;
   }
   
   public Page<ItemResponseDTO> getPersonal(String userName, Pageable pageable) {
	    // 1. Get the single Page object from the repository
	    Page<ItemEntity> itemPage = itemRepository.findByUserNameAndGroupIdIsNull(userName, pageable); 
	    
	    // 2. Use the Page's built-in map method
	    return itemPage.map(item -> {
	        ItemResponseDTO dto = modelMapper.map(item, ItemResponseDTO.class);
	        
	        // Ensure the username is set (if your DTO needs it)
	        if (item.getCreatedBy() != null) {
	            dto.setCreatedBy(item.getCreatedBy().getUserName());
	        }
	        
	        return dto;
	    });
	}
   
   public Page<ItemResponseDTO> getByUserName(String userName, Pageable pageable) {
	    // 1. Get the single Page object from the repository
	    Page<ItemEntity> itemPage = itemRepository.findByUserName(userName, pageable); 
	    
	    // 2. Use the Page's built-in map method
	    return itemPage.map(item -> {
	        ItemResponseDTO dto = modelMapper.map(item, ItemResponseDTO.class);
	        
	        // Ensure the username is set (if your DTO needs it)
	        if (item.getCreatedBy() != null) {
	            dto.setCreatedBy(item.getCreatedBy().getUserName());
	        }
	        
	        return dto;
	    });
	}
   
   public Page<ItemResponseDTO> getByGroupId(Long groupId, Pageable pageable) throws BadRequestException {
	   String userName = Utils.getAuthUserName();
	   
	    if(!groupRepository.existsById(groupId)) {
	    	throw new BadRequestException("Invalid groupId");
	    }
	    
	   List<GroupResponseDTO> groupResponseDTOs = groupService.getMyAndSharedGroups(userName);
	   
	   boolean isMatch = groupResponseDTOs.stream().anyMatch(p -> p.getId().equals(groupId));
	   
	   if(!isMatch) {
		   throw new BadRequestException("You dont have permission to access this group");
	   }
	    // 1. Get the single Page object from the repository
	    Page<ItemEntity> itemPage = itemRepository.findByGroupId(groupId, pageable); 
	    
	    // 2. Use the Page's built-in map method
	    return itemPage.map(item -> {
	        ItemResponseDTO dto = modelMapper.map(item, ItemResponseDTO.class);
	        
	        // Ensure the username is set (if your DTO needs it)
	        if (item.getCreatedBy() != null) {
	            dto.setCreatedBy(item.getCreatedBy().getUserName());
	        }
	        
	        return dto;
	    });
	}
   
   public ItemResponseDTO add(String userName, Long groupId, ItemRequestDTO itemRequestDTO) throws BadRequestException {
	   
	   ItemEntity itemToInsert = modelMapper.map(itemRequestDTO, ItemEntity.class);
	   UserEntity dbUser = userRepository.findById(userName).get();
	   
	   if(groupId != null) {
		   if(!groupRepository.existsById(groupId)) {
			   throw new BadRequestException(groupId + " doesn't exist.");
		   }
		   GroupEntity group = groupRepository.findById(groupId).get();
		   itemToInsert.setGroupId(group);
	   }
	   
	   itemToInsert.setCreatedBy(dbUser);
	   
	   ItemEntity insertedItem = itemRepository.save(itemToInsert);
	   ItemResponseDTO itemResponseDTO =  modelMapper.map(insertedItem, ItemResponseDTO.class);
	   itemResponseDTO.setCreatedBy(userName);
	   
	   return itemResponseDTO;
   }
   
   public boolean deleteByID(Long id, String userName) throws BadRequestException {
	    
	    // 1. Use orElseThrow to handle "Not Found" cases gracefully
	    ItemEntity existingEntity = itemRepository.findById(id)
	        .orElseThrow(() -> new BadRequestException("Item with id " + id + " not found"));
	    
	    UserEntity owner = existingEntity.getCreatedBy();
	    
	    // 2. Check if the logged-in user is the owner
	    // Note: Use .equals() for string comparison and handle potential nulls
	    if (owner != null && !owner.getUserName().equals(userName)) {
	        throw new BadRequestException("User " + userName + " does not have permission to delete this item."); 
	    }
	    
	    itemRepository.deleteById(id);
	    return true;
	}
   
   public ItemResponseDTO updateByID(Long id, String authUserName, ItemRequestDTO itemRequestDTO) throws BadRequestException {
	    // 1. Fetch the existing entity safely
	    ItemEntity existingItem = itemRepository.findById(id)
	        .orElseThrow(() -> new BadRequestException("Item with id " + id + " not found"));

	    // 2. Security Check: Only the owner can update
	    // We assume ItemEntity has a UserEntity relationship named 'user'
	    if (existingItem.getCreatedBy() == null || !existingItem.getCreatedBy().getUserName().equals(authUserName)) {
	        throw new BadRequestException("User " + authUserName + " is not authorized to update this item");
	    }

	    existingItem.setId(id); 

	    ItemEntity updatedItem = itemRepository.save(existingItem);
	    
	    ItemResponseDTO itemResponseDTO = modelMapper.map(updatedItem, ItemResponseDTO.class);
	    itemResponseDTO.setCreatedBy(authUserName);
	    return itemResponseDTO;
	}
   
   
 
}
