package apx.inc.design_web_services_backend.iam.domain.model.commands;

import apx.inc.design_web_services_backend.iam.domain.model.entities.Role;
import apx.inc.design_web_services_backend.iam.domain.model.valueobjects.Roles;

import java.util.List;

public record UpdateUserCommand(
        String username,
        String password,
        List<Roles> roles) {
    public UpdateUserCommand{
        if (username==null || username.isBlank() ) {
            throw new IllegalArgumentException("Username cannot be empty");
        }
        if (password==null || password.isBlank() ) {
            throw new IllegalArgumentException("Password cannot be empty");
        }
        if (roles==null || roles.isEmpty() ) {
            throw new IllegalArgumentException("Roles cannot be empty");
        }
    }
}
