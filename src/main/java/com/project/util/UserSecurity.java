package com.project.util;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import com.project.security.services.UserDetailsImpl;

@Component("userSecurity") 
public class UserSecurity {
    public boolean hasUserId(Authentication authentication, Long userId) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return false;
        }
        Object principal = authentication.getPrincipal();
        if (principal instanceof UserDetailsImpl) {
            UserDetailsImpl userDetails = (UserDetailsImpl) principal;
            return userDetails.getId().equals(userId);
        }
        return false;
    }
}
