package apx.inc.design_web_services_backend.iam.rest.resources;

import apx.inc.design_web_services_backend.iam.domain.model.entities.Role;

import java.util.Set;

public record CreateUserResource(
        String userName,
        String password,
        Set<Role> roles
) {
    public CreateUserResource {

        if (userName == null || userName.isBlank()) {
            throw new IllegalArgumentException("First name cannot be null or blank");
        }
        if (password == null || password.isBlank()) {
            throw new IllegalArgumentException("Password cannot be null or blank");
        }
        if (roles == null || roles.isEmpty()) {
            throw new IllegalArgumentException("Roles cannot be null or empty");
        }
    }
}
