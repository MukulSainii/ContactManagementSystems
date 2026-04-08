package com.smart.helper;

import com.smart.config.CustomUserDetails;
import com.smart.entities.User;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;


public class SecurityUtils {

        // Get current logged-in user
        public static User getCurrentUser() {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth == null || !auth.isAuthenticated()) {
                return null;
            }
            Object principal = auth.getPrincipal();
            if (principal instanceof CustomUserDetails) {
                return ((CustomUserDetails) principal).getUser();
            }
            return null;
        }

        // Update logged-in user in SecurityContext
        public static void updateCurrentUser(User updatedUser) {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            CustomUserDetails newUserDetails = new CustomUserDetails(updatedUser);
            Authentication newAuth = new UsernamePasswordAuthenticationToken(
                    newUserDetails,
                    auth.getCredentials(),
                    newUserDetails.getAuthorities()
            );
            SecurityContextHolder.getContext().setAuthentication(newAuth);
        }

}
