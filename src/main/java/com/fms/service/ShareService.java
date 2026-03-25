package com.fms.service;

import java.util.List;

import org.apache.coyote.BadRequestException;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.fms.dto.ShareRequestDTO;
import com.fms.dto.ShareResponseDTO;
import com.fms.entity.GroupEntity;
import com.fms.entity.ShareEntity;
import com.fms.entity.UserEntity;
import com.fms.repository.GroupRepository;
import com.fms.repository.ShareRepository;
import com.fms.repository.UserRepository;
import com.fms.util.Utils;

@Service
public class ShareService {
	private static final Logger logger = LoggerFactory.getLogger(ShareService.class);
	
	private UserRepository userRepository;
	private GroupRepository groupRepository;
	private ShareRepository shareRepository;
	private ModelMapper modelMapper;
	
	public ShareService(UserRepository userRepository,
						GroupRepository groupRepository,
						ShareRepository shareRepository,
						ModelMapper modelMapper) {
		this.userRepository = userRepository;
		this.groupRepository = groupRepository;
		this.shareRepository = shareRepository;
		this.modelMapper = modelMapper;
	}
	
//	@Transactional
	public ShareResponseDTO add(ShareRequestDTO  shareRequestDTO) throws BadRequestException {

	    String shareByUserName = shareRequestDTO.getShareBy();
	    String shareToUserName = shareRequestDTO.getShareTo();
	    String userName = Utils.getAuthUserName();
	    
	    if(!groupRepository.existsById(shareRequestDTO.getGroupId())) {
	    	throw new BadRequestException("Group doesn't exit with groud_id: " + shareRequestDTO.getGroupId());
	    }

	    if(!userRepository.existsById(shareByUserName)) {
	        throw new BadRequestException("Shareby with userName: " + shareByUserName + " doesn't exist");
	    }
	    
	    if(!userRepository.existsById(shareToUserName)) {
	        throw new BadRequestException("Shareto with userName: " + shareToUserName + " doesn't exist");
	    }
	    
	    if(!shareByUserName.equals(userName)) {
	        throw new BadRequestException("Shareby must be login user");
	    }
	    
	    if(shareByUserName.equals(shareToUserName)) {
	        throw new BadRequestException("Shareby and shareto must be different");
	    }
	    
        if(shareRepository.existsByShareTo_UserName(shareToUserName)) {
        	throw new BadRequestException("Group is already shared with you");
        }

	    // ✅ Fetch managed references
	    GroupEntity group = groupRepository.getReferenceById(shareRequestDTO.getGroupId());
	    
	    if(!group.getCreatedBy().getUserName().equals(shareByUserName)) {
	    	throw new BadRequestException("You don't have permission to share.");
	    }
	    
	    UserEntity shareBy = userRepository.getReferenceById(shareByUserName);
	    UserEntity shareTo = userRepository.getReferenceById(shareToUserName);

	    // ✅ Create entity
	    ShareEntity shareEntity = new ShareEntity();
	    shareEntity.setGroup(group);
	    shareEntity.setShareBy(shareBy);
	    shareEntity.setShareTo(shareTo);

	    ShareEntity temp = shareRepository.save(shareEntity); // INSERT

	    ShareResponseDTO response = modelMapper.map(shareRequestDTO, ShareResponseDTO.class);
	    response.setId(temp.getId());
	    return response;
	}
	
	public boolean isAccepted(Long shareId) throws BadRequestException {
		String userName = Utils.getAuthUserName();
		
		if(!shareRepository.existsById(shareId)) {
			throw new BadRequestException("ShareId: " + shareId + " doesn't exist");
		}
		
		ShareEntity shareDetails = shareRepository.findById(shareId).get();
		
		if(!userName.equals(shareDetails.getShareTo().getUserName())) {
			throw new BadRequestException("ShareId: " + shareId +" is not shared with you");
		}
		
		if(shareDetails.getIsAccepted()) {
			throw new BadRequestException("Request is already accepted");
		}
		
		shareDetails.setIsAccepted(true);
		
		shareRepository.save(shareDetails);
		
		return true;
	}

	
}
