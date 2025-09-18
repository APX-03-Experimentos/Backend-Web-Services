package apx.inc.design_web_services_backend.iam.rest.transform;

import apx.inc.design_web_services_backend.iam.domain.model.commands.SignUpCommand;
import apx.inc.design_web_services_backend.iam.rest.resources.SignUpResource;

public class SignUpCommandFromResourceAssembler {
    public static SignUpCommand toCommandFromResource(SignUpResource signUpResource) {
        return new SignUpCommand(
                signUpResource.userName(),
                signUpResource.password(),
                signUpResource.roles()
        );
    }
}
