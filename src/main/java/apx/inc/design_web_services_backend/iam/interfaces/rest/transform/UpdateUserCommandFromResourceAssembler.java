package apx.inc.design_web_services_backend.iam.interfaces.rest.transform;

import apx.inc.design_web_services_backend.iam.domain.model.commands.UpdateUserCommand;
import apx.inc.design_web_services_backend.iam.interfaces.rest.resources.UpdateUserResource;

public class UpdateUserCommandFromResourceAssembler {
    public static UpdateUserCommand toCommandFromResource(UpdateUserResource updateUserResource){
        return new UpdateUserCommand(
                updateUserResource.username(),
                updateUserResource.password(),
                updateUserResource.roles());
    }
}
