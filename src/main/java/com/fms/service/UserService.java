package com.fms.service;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.fms.dto.UserRequestDTO;
import com.fms.dto.UserResponseDTO;
import com.fms.entity.UserEntity;
import com.fms.globalException.UserAlreadyExistsException;
import com.fms.repository.UserRepository;


@Service
public class UserService {
	Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	 ModelMapper modelMapper;
	@Autowired
	 UserRepository userRepository;
	@Autowired
	 PasswordEncoder passwordEncoder;
//	public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, ModelMapper modelMapper) {
//		this.userRepository = userRepository;
//		this.passwordEncoder = passwordEncoder;
//		this.modelMapper = modelMapper;
//	}
	
	 public UserResponseDTO register(UserRequestDTO userRequestDTO) {

	        // Check if username exists
	        if (userRepository.existsById(userRequestDTO.getUserName())) {
	            throw new UserAlreadyExistsException("User name already present with: " + userRequestDTO.getUserName());
	        }
	        
	        //decode the password
	        userRequestDTO.setPassword(passwordEncoder.encode(userRequestDTO.getPassword()));

	        // DTO → Entity
	        UserEntity user = modelMapper.map(userRequestDTO, UserEntity.class);
	        user.setRoles(List.of("USER"));
	        user.setProvider("LOCAL");
	       

	        UserEntity savedUser = userRepository.save(user);

	        // Entity → Response DTO
	        return modelMapper.map(savedUser, UserResponseDTO.class);
	  }
	 
	 public List<UserResponseDTO> getUsers() {
		 List<UserEntity> users = userRepository.findAll();
		 
		 List<UserResponseDTO> userResponseDTOs = users.stream().map(user -> modelMapper.map(user, UserResponseDTO.class)).toList();
				 
		 return userResponseDTOs;
		 
	 }
	 
	 
}
