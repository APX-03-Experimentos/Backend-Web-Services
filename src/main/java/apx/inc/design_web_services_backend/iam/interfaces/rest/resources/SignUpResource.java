package apx.inc.design_web_services_backend.iam.interfaces.rest.resources;

import apx.inc.design_web_services_backend.iam.domain.model.valueobjects.Roles;

import java.util.List;

public record SignUpResource(
        String username,
        String password,
        List<Roles> roles,
        String recaptchaToken
) {
}
