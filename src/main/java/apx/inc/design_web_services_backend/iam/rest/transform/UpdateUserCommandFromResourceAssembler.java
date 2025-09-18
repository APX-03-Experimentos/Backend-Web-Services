package apx.inc.design_web_services_backend.iam.rest.transform;

import apx.inc.design_web_services_backend.iam.domain.model.commands.UpdateUserCommand;
import apx.inc.design_web_services_backend.iam.rest.resources.UpdateUserResource;

public class UpdateUserCommandFromResourceAssembler {
    public static UpdateUserCommand toCommandFromResource(UpdateUserResource updateUserResource){
        return new UpdateUserCommand(
                updateUserResource.userName(),
                updateUserResource.password());
    }
}
