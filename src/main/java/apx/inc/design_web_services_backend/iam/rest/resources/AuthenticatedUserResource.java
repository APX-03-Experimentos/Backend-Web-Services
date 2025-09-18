package apx.inc.design_web_services_backend.iam.rest.resources;

import java.util.List;

public record AuthenticatedUserResource(
        Long id,
        String username,
        String token,
        List<String> roles
) {
}
