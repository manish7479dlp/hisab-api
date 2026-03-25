package com.fms.config;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;

import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import com.fms.entity.UserEntity;
import com.fms.repository.UserRepository;
import com.fms.service.JwtService;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler  {
	private static final Logger logger = LoggerFactory.getLogger(OAuth2SuccessHandler.class);
	
	@Autowired
	JwtService jwtService;
	
	@Autowired
	UserRepository userRepository;
	
	@Value("${frontend_redirect_url}")
    private String FRONT_END_REDIRECT_URL;

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {
//		 	logger.info("Successful authentication");
//	        logger.info(authentication.toString());
	        
		    
	        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
	        
	        
	        String registrationId = "unknown";
	        if (authentication instanceof OAuth2AuthenticationToken token) {
	            registrationId = token.getAuthorizedClientRegistrationId();
	        }
//
//	        logger.info("registrationId:" + registrationId);
//	        logger.info("user:" + oAuth2User.getAttributes().toString());
	        
	        String email = oAuth2User.getAttribute("email");
	        String name = oAuth2User.getAttribute("name");
	        String picture = oAuth2User.getAttribute("picture");
	        String provider = ((OAuth2AuthenticationToken) authentication)
	                .getAuthorizedClientRegistrationId();
	        
	        UserEntity user;
	        
	        if(userRepository.existsById(email)) {
	        	user = userRepository.findById(email).get();
	        	if(user.getImage() == null) {
	        		user.setImage(picture);
	        		userRepository.save(user);
	        	}
	        	
	        } else {
	        	user = new UserEntity();
	        	user.setUserName(email);
	        	user.setName(name);
	        	user.setProvider(provider.toUpperCase());
	        	user.setImage(picture);
	        	user.setRoles(List.of("USER"));
	        	userRepository.save(user);
	        }

	        // 🔥 Generate JWT
	        String token = jwtService.generateToken(email);
	        String roles = user.getRoles().stream().collect(Collectors.joining(","));

	        // 🔥 Redirect to frontend with data
	        String redirectUrl = UriComponentsBuilder
	                .fromHttpUrl(FRONT_END_REDIRECT_URL + "/oauth-success")
	                .queryParam("token", token)
	                .queryParam("userName", email)
	                .queryParam("name", name)
	                .queryParam("picture", picture)
	                .queryParam("roles", roles)
	                .build()
	                .toUriString();

	        response.sendRedirect(redirectUrl);

	        
	     // ✅ Send token in response
//	        response.setContentType("application/json");
//	        response.setCharacterEncoding("UTF-8");
//
//	        String jsonResponse = String.format(
//	                "{\"token\":\"%s\",\"email\":\"%s\",\"name\":\"%s\",\"provider\":\"%s\"}",
//	                token, email, name, provider
//	        );
//
//	        response.getWriter().write(jsonResponse);
//	        response.getWriter().flush();

		
	}
	
	
	

}
