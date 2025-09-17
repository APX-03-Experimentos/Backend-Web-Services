package apx.inc.design_web_services_backend.courses.interfaces.rest.transform;

import apx.inc.design_web_services_backend.courses.domain.model.commands.JoinByJoinCodeCommand;
import apx.inc.design_web_services_backend.courses.interfaces.rest.resources.JoinByJoinCodeResource;

public class JoinByJoinCodeCommandFromResourceAssembler {
    public static JoinByJoinCodeCommand toCommandFromResource(JoinByJoinCodeResource resource) {
        return new JoinByJoinCodeCommand(
                resource.studentId(),
                resource.joinCode()
        );
    }
}
