package cl.duoc.spa.spa_catalog_service.controller;

import org.springframework.security.core.Authentication;

final class AuthUtil {
    private AuthUtil() {}

    static boolean isAdmin(Authentication auth) {
        return auth != null && auth.getAuthorities().stream()
                .anyMatch(a -> "ROLE_ADMIN".equals(a.getAuthority()));
    }
}
