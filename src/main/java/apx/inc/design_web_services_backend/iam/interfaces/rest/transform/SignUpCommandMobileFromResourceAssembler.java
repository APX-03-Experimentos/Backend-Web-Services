package apx.inc.design_web_services_backend.iam.interfaces.rest.transform;

import apx.inc.design_web_services_backend.iam.domain.model.commands.SignUpCommand;
import apx.inc.design_web_services_backend.iam.domain.model.commands.SignUpCommandMobile;
import apx.inc.design_web_services_backend.iam.interfaces.rest.resources.SignUpMobileResource;
import apx.inc.design_web_services_backend.iam.interfaces.rest.resources.SignUpResource;

public class SignUpCommandMobileFromResourceAssembler {
    public static SignUpCommandMobile toCommandFromResource(SignUpMobileResource signUpMobileResource) {
        return new SignUpCommandMobile(
                signUpMobileResource.username(),
                signUpMobileResource.password(),
                signUpMobileResource.roles()
        );
    }
}
