package com.fms.service;

import org.apache.coyote.BadRequestException;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.fms.dto.LoginRequestDTO;
import com.fms.dto.LoginResponseDTO;
import com.fms.dto.UserRequestDTO;
import com.fms.entity.UserEntity;
import com.fms.repository.UserRepository;

@Service
public class AuthService {
	private final UserRepository userRepository;
	private final AuthenticationManager authenticationManager;
	private final JwtService jwtService;
	private final ModelMapper modelMapper;
	private final PasswordEncoder passwordEncoder;
	public AuthService(UserRepository userRepository,
					   AuthenticationManager authenticationManager,
					   JwtService jwtService,
					   ModelMapper modelMapper,
					   PasswordEncoder passwordEncoder
					  ) {
		this.userRepository = userRepository;
		this.authenticationManager = authenticationManager;
		this.jwtService = jwtService;
		this.modelMapper = modelMapper;
		this.passwordEncoder = passwordEncoder;
	
	}
	
	public LoginResponseDTO login(LoginRequestDTO loginRequestDTO) {
		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(
						loginRequestDTO.getUserName(),
						loginRequestDTO.getPassword()
						)
				);
		
		if(authentication.isAuthenticated()) {
			User user =  (User) authentication.getPrincipal();
			String token = jwtService.generateToken(loginRequestDTO.getUserName());
			UserEntity userEntity = userRepository.findById(user.getUsername()).get();
			System.out.println(userEntity.getUserName());
			
			LoginResponseDTO loginResponseDTO = modelMapper.map(userEntity, LoginResponseDTO.class);
			loginResponseDTO.setToken(token);
			
			return loginResponseDTO;
	
		} else {
			return null;
		}
		
	}
	
	public void resetPassword(String userName, String newPassword) throws BadRequestException { 
	    UserEntity user = userRepository.findById(userName)
	        .orElseThrow(() -> new RuntimeException("User not found"));
	    
	    // Check provider
	    if("GOOGLE".equalsIgnoreCase(user.getProvider())) {
	        throw new BadRequestException("You logged in with GOOGLE. Password reset is not available for social accounts.");
	    } 
	    
	    user.setPassword(passwordEncoder.encode(newPassword));
	    userRepository.save(user);
	}
}
