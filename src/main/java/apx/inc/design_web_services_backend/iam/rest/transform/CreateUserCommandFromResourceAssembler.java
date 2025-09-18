package apx.inc.design_web_services_backend.iam.rest.transform;

import apx.inc.design_web_services_backend.iam.domain.model.commands.CreateUserCommand;
import apx.inc.design_web_services_backend.iam.rest.resources.CreateUserResource;

public class CreateUserCommandFromResourceAssembler {
    public static CreateUserCommand toCommandFromResource(CreateUserResource createUserResource) {
        return new CreateUserCommand(
                createUserResource.userName(),
                createUserResource.password(),
                createUserResource.roles().stream().toList());
    }
}
