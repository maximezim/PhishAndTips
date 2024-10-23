package com.learnandphish.gateway;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class RouteRoles {

    private static final Logger logger = LoggerFactory.getLogger(RouteRoles.class);

    private static final Map<String, List<String>> routeRoleMap = Map.of(
            "/admin/**", List.of("ADMIN"),
            "/user/**", List.of("USER", "ADMIN"),
            "/export-users", List.of("ADMIN"),
            "/change-password", List.of("USER", "ADMIN"),
            "/test-admin", List.of("ADMIN"),
            "/test-user", List.of("USER", "ADMIN"),
            "/test-both", List.of("USER", "ADMIN")
    );

    public static List<String> requiredRolesForPath(String path) {
        for (Map.Entry<String, List<String>> entry : routeRoleMap.entrySet()) {
            String pattern = entry.getKey().replace("/**", "");
            if (path.startsWith(pattern)) {
                logger.info("Request made to path: {}. Required roles: {}", path, entry.getValue());
                return entry.getValue();
            }
        }
        logger.info("Request made to path: {}. No specific role requirements.", path);
        return null; // No specific role requirements
    }

    public static boolean hasRequiredRole(String userRole, List<String> requiredRoles) {
        if (requiredRoles == null || requiredRoles.isEmpty()) {
            logger.info("No specific role requirements for the current request.");
            return true; // No specific role requirements
        }
        boolean hasRole = requiredRoles.contains(userRole.toUpperCase());
        logger.info("User role: {}. Has required role: {}", userRole, hasRole);
        return hasRole;
    }
}
