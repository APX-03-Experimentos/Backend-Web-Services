package apx.inc.design_web_services_backend.iam.interfaces.rest.resources;

import apx.inc.design_web_services_backend.iam.domain.model.entities.Role;
import apx.inc.design_web_services_backend.iam.domain.model.valueobjects.Roles;

import java.util.List;

public record UpdateUserResource(
        String username,
        String password,
        List<Roles> roles
) {
    public UpdateUserResource {
        if (username == null || username.isBlank()) {
            throw new IllegalArgumentException("Email cannot be null or blank");
        }
        if (password == null || password.isBlank()) {
            throw new IllegalArgumentException("Password cannot be null or blank");
        }
    }
}
