package apx.inc.design_web_services_backend.iam.interfaces.rest.transform;

import apx.inc.design_web_services_backend.iam.domain.model.commands.SignUpCommand;
import apx.inc.design_web_services_backend.iam.interfaces.rest.resources.SignUpResource;

public class SignUpCommandFromResourceAssembler {
    public static SignUpCommand toCommandFromResource(SignUpResource signUpResource) {
        return new SignUpCommand(
                signUpResource.username(),
                signUpResource.password(),
                signUpResource.roles(),
                signUpResource.recaptchaToken()
        );
    }
}
