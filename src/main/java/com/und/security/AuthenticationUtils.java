package com.und.security;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

public class AuthenticationUtils {

  public static boolean isUserLoggedIn() {
    SecurityContext securityContext = SecurityContextHolder.getContext();
    if (securityContext == null) {
      return false;
    }

    Authentication authentication = securityContext.getAuthentication();
    if (authentication == null || !authentication.isAuthenticated() || (authentication instanceof AnonymousAuthenticationToken)) {
      return false;
    }
    return true;
  }

  public static Object getPrincipal() {
    SecurityContext securityContext = SecurityContextHolder.getContext();
    if (securityContext == null) {
      throw new AccessDeniedException("User is not logged in to the system.");
    }

    Authentication authentication = securityContext.getAuthentication();
    if (authentication == null) {
      throw new AccessDeniedException("User is not logged in to the system.");
    }

    Object principal = authentication.getPrincipal();
    return principal;
  }


  public static String getName() {
    Object principal = getPrincipal();
    if (principal instanceof EventUser) {
      return ((EventUser) principal).getUsername();
    }
    return null;
  }

  public static String getId() {
    Object principal = getPrincipal();
    if (principal instanceof EventUser) {
      return "" + ((EventUser) principal).getId();
    }
    return "_";
  }


}