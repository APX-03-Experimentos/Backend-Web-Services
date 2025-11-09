package apx.inc.design_web_services_backend.iam.interfaces.rest.transform;

import apx.inc.design_web_services_backend.iam.domain.model.commands.SignInCommand;
import apx.inc.design_web_services_backend.iam.interfaces.rest.resources.SignInResource;

public class SignInCommandFromResourceAssembler {
    public static SignInCommand toCommandFromResource(SignInResource signInResource){
        return new SignInCommand(
                signInResource.userName(),
                signInResource.password()
        );
    }
}
