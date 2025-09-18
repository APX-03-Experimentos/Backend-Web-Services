package apx.inc.design_web_services_backend.assigments.interfaces.rest.transform;

import apx.inc.design_web_services_backend.assigments.domain.model.commands.UpdateAssignmentCommand;
import apx.inc.design_web_services_backend.assigments.interfaces.rest.resource.UpdateAssignmentResource;

public class UpdateAssignmentCommandFromResourceAssembler {
    public static UpdateAssignmentCommand toCommandFromResource(Long challengeId, UpdateAssignmentResource resource){
        return new UpdateAssignmentCommand(
                challengeId,
                resource.title(),
                resource.description(),
                resource.groupId(),
                resource.deadline(),
                resource.imageUrl()
        );
    }
}