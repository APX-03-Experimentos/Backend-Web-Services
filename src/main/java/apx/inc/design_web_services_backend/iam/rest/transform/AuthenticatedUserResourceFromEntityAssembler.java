package apx.inc.design_web_services_backend.iam.rest.transform;

import apx.inc.design_web_services_backend.iam.domain.model.aggregates.User;
import apx.inc.design_web_services_backend.iam.rest.resources.AuthenticatedUserResource;

public class AuthenticatedUserResourceFromEntityAssembler {
    public static AuthenticatedUserResource toResourceFromEntity(User user, String token) {
        return new AuthenticatedUserResource(
                user.getId(),
                user.getUserName(),
                token,
                user.getUserRoles().stream().map(
                        role -> role.getName().name()
                ).toList()
        );
    }
}
