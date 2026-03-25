package com.fms.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.coyote.BadRequestException;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.fms.dto.GroupRequestDTO;
import com.fms.dto.GroupResponseDTO;
import com.fms.entity.GroupEntity;
import com.fms.entity.ShareEntity;
import com.fms.entity.UserEntity;
import com.fms.repository.GroupRepository;
import com.fms.repository.ShareRepository;
import com.fms.repository.UserRepository;
import com.fms.util.*;

@Service
public class GroupService {
	private static final Logger logger = LoggerFactory.getLogger(GroupService.class);
	
	private final GroupRepository groupRepository;
	private final ModelMapper modelMapper;
	private final UserRepository userRepository;
	private final ShareRepository shareRepository;
	
	public GroupService(GroupRepository groupRepository,
						ModelMapper modelMapper,
						UserRepository userRepository,
						ShareRepository shareRepository) {
		this.groupRepository = groupRepository;
		this.modelMapper = modelMapper;
		this.userRepository = userRepository;
		this.shareRepository = shareRepository;
	}
	
	public Page<GroupResponseDTO> getAll(Pageable pageable) {
		Page<GroupEntity> groups = groupRepository.findAll(pageable);
		
		Page<GroupResponseDTO> groupResponseDTOs = groups
				.map(group -> {
				    GroupResponseDTO groupResponseDTO =	modelMapper.map(group, GroupResponseDTO.class);
				    
				    if(group.getCreatedBy() != null) {
				    	groupResponseDTO.setCreatedBy(group.getCreatedBy().getUserName());
				    }
				    
				    return groupResponseDTO;
					
				});
		
		return groupResponseDTOs;
				
	}
	
	public GroupResponseDTO create(GroupRequestDTO groupRequestDTO) {
		GroupEntity groupToInsert = modelMapper.map(groupRequestDTO, GroupEntity.class);
		
		String userName = Utils.getAuthUserName();
		UserEntity user = userRepository.findById(userName).get();
		
		groupToInsert.setCreatedBy(user);
		
		GroupEntity insertedGroup = groupRepository.save(groupToInsert);
		GroupResponseDTO finalGroup =  modelMapper.map(insertedGroup, GroupResponseDTO.class);
		finalGroup.setCreatedBy(userName);
		return finalGroup;
	}
	
	public boolean deleteById(Long groupId) throws BadRequestException {
		if(!groupRepository.existsById(groupId)) {
			throw new BadRequestException("Group not exist with groupId: " + groupId);
		}
		
		String userName = Utils.getAuthUserName();
		GroupEntity existingGroup = groupRepository.findById(groupId).get();
		
		if(!userName.equals(existingGroup.getCreatedBy().getUserName())) {
			throw new BadRequestException("UserName: " + userName + " have no permission to delete group with groupId: " + groupId); 
		}
		
		groupRepository.deleteById(groupId);
		return true;
	}
	
	public GroupResponseDTO updateById(Long groupId, GroupRequestDTO groupRequestDTO) throws BadRequestException {
		if(!groupRepository.existsById(groupId)) {
			throw new BadRequestException("Group not exist with groupId: " + groupId);
		}
		
		String userName = Utils.getAuthUserName();
		GroupEntity existingGroup = groupRepository.findById(groupId).get();
		
		if(!userName.equals(existingGroup.getCreatedBy().getUserName())) {
			throw new BadRequestException("UserName: " + userName + " have no permission to update group with groupId: " + groupId); 
		}
		
		GroupEntity groupToUpdate = modelMapper.map(groupRequestDTO, GroupEntity.class);
		groupToUpdate.setId(groupId);
		groupToUpdate.setCreatedBy(existingGroup.getCreatedBy());
		groupToUpdate.setCreatedAt(existingGroup.getCreatedAt());
		
		if(groupRequestDTO.getName() == null) {
			groupToUpdate.setName(existingGroup.getName());
		}
		
		if(groupRequestDTO.getIsPinned() == null) {
			groupToUpdate.setIsPinned(existingGroup.getIsPinned());
		}
		
		GroupEntity updatedGroup =  groupRepository.save(groupToUpdate);
		
		GroupResponseDTO finalGroup =  modelMapper.map(updatedGroup, GroupResponseDTO.class);
		finalGroup.setCreatedBy(userName);
		return finalGroup;
	}
	
	public List<GroupResponseDTO> getMyAndSharedGroups(String username) throws BadRequestException {

	    // 🔹 Get UserEntity once
	    UserEntity user = userRepository.findById(username)
	            .orElseThrow(() -> new BadRequestException("User not found"));

	    // 1️⃣ Groups created by the user
	    List<GroupEntity> myGroups = groupRepository.findByCreatedBy(user);

	    // 2️⃣ Shared groups where user is shareTo AND accepted
	    List<ShareEntity> shares = shareRepository.findByShareToAndIsAcceptedTrue(user);

	    List<GroupEntity> sharedGroups = shares.stream()
	            .map(ShareEntity::getGroup)
	            .toList();

	    // 3️⃣ Merge without duplicates
	    Map<Long, GroupEntity> groupMap = new HashMap<>();
	    myGroups.forEach(g -> groupMap.put(g.getId(), g));
	    sharedGroups.forEach(g -> groupMap.put(g.getId(), g));

	    // 4️⃣ Convert to DTO
	    return groupMap.values().stream()
	            .map(group -> modelMapper.map(group, GroupResponseDTO.class))
	            .toList();
	}

	
//	public List<GroupResponseDTO> getMyAndSharedGroups(String username) {
//
//	    List<GroupEntity> myGroups = groupRepository.findByCreatedBy(username);
//	    List<GroupEntity> sharedGroups = shareRepository.findAcceptedSharedGroups(username);
//
//	    Map<Long, GroupEntity> groupMap = new HashMap<>();
//	    myGroups.forEach(g -> groupMap.put(g.getId(), g));
//	    sharedGroups.forEach(g -> groupMap.put(g.getId(), g));
//
//	    return groupMap.values().stream()
//	            .map(group -> modelMapper.map(group, GroupResponseDTO.class))
//	            .toList();
//	}

	//not optimize code 
//	public List<GroupResponseDTO> getMyAndSharedGroupsV0(String username) {
//
//	    // 1️⃣ Groups created by user
//	    List<GroupEntity> myGroups = groupRepository.findByCreatedBy(username);
//
//	    // 2️⃣ Only ACCEPTED shared groups
//	    List<ShareEntity> shares = shareRepository.findByShareToAndIsAcceptedTrue(username);
//
//	    List<Long> sharedGroupIds = shares.stream()
//	            .map(share -> share.getGroup().getId())
//	            .toList();
//
//	    List<GroupEntity> sharedGroups = sharedGroupIds.isEmpty()
//	            ? List.of()
//	            : groupRepository.findByIdIn(sharedGroupIds);
//
//	    // 3️⃣ Merge without duplicates
//	    Map<Long, GroupEntity> groupMap = new HashMap<>();
//
//	    myGroups.forEach(g -> groupMap.put(g.getId(), g));
//	    sharedGroups.forEach(g -> groupMap.put(g.getId(), g));
//
//	    // 4️⃣ Convert to DTO
//	    return groupMap.values().stream()
//	            .map(group -> modelMapper.map(group, GroupResponseDTO.class))
//	            .toList();
//	}

}
