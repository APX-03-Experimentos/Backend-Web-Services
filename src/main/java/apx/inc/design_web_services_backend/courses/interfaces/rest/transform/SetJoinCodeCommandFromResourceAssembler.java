package apx.inc.design_web_services_backend.courses.interfaces.rest.transform;

import apx.inc.design_web_services_backend.courses.domain.model.commands.SetJoinCodeCommand;
import apx.inc.design_web_services_backend.courses.interfaces.rest.resources.SetJoinCodeResource;

public class SetJoinCodeCommandFromResourceAssembler {
    public static SetJoinCodeCommand toCommandFromResource(Long courseId,SetJoinCodeResource resource) {
        return  new SetJoinCodeCommand(
                courseId,
                resource.keycode(),
                resource.expiration()
        );
    }
}
