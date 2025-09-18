package apx.inc.design_web_services_backend.iam.rest.resources;

import apx.inc.design_web_services_backend.iam.domain.model.valueobjects.Roles;

import java.util.List;

public record SignUpResource(
        String userName,
        String password,
        List<Roles> roles
) {
}
