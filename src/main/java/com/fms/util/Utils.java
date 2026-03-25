package com.fms.util;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;

public class Utils {
	   public static  String getAuthUserName() {
		   Authentication authentication =SecurityContextHolder.getContext().getAuthentication();
		   User authUser = (User) authentication.getPrincipal();
		   String userName = authUser.getUsername();
		   return userName;
	   }
}
