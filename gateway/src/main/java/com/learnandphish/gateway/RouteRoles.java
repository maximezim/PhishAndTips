package com.learnandphish.gateway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;
import java.util.Map;

public class RouteRoles {

    private static final Logger logger = LoggerFactory.getLogger(RouteRoles.class);

    private static final Map<String, List<String>> routeRoleMap = Map.ofEntries(
        Map.entry("/admin/**", List.of("ADMIN")),
        Map.entry("/user/**", List.of("USER", "ADMIN")),
        Map.entry("/import-users", List.of("ADMIN")),
        Map.entry("/export-users", List.of("ADMIN")),
        Map.entry("/change-password", List.of("USER", "ADMIN")),
        Map.entry("/need-change-password", List.of("USER", "ADMIN")),
        Map.entry("/reset-password", List.of("USER","ADMIN")),
        Map.entry("/update-user", List.of("ADMIN")),
        Map.entry("/delete-user", List.of("ADMIN")),
        Map.entry("/get-user", List.of("USER", "ADMIN")),
        Map.entry("/register", List.of("ADMIN")),
        Map.entry("/test-admin", List.of("ADMIN")),
        Map.entry("/test-user", List.of("USER", "ADMIN")),
        Map.entry("/test-both", List.of("USER", "ADMIN")),
        Map.entry("/api/**", List.of("ADMIN")),
        Map.entry("/get-all-gophish-users", List.of("ADMIN")),
        Map.entry("/get-all-users", List.of("ADMIN")),
        Map.entry("/get-roles", List.of("ADMIN")),
        Map.entry("/formation/**", List.of("USER", "ADMIN")),
        Map.entry("/scoring/admin/**", List.of("ADMIN")),
        Map.entry("/scoring/**", List.of("USER", "ADMIN")),
        Map.entry("/monitor/**", List.of("ADMIN")),
        Map.entry("/my-scan/**", List.of("USER", "ADMIN")),
        Map.entry("/assets/**", List.of("USER","ADMIN"))
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
