package com.fms.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.fms.entity.UserEntity;
import com.fms.repository.UserRepository;


@Component
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    public UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        UserEntity user = userRepository.findByUserName(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getUserName())
                .password(user.getPassword()) // already BCrypt encoded
                .authorities(
                        user.getRoles().stream()
                                .map(role -> "ROLE_" + role)  // add prefix yourself
                                .map(org.springframework.security.core.authority.SimpleGrantedAuthority::new)
                                .toList()
                )
                .build();
    }
}
