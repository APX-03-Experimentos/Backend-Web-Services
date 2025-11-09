package apx.inc.design_web_services_backend.notifications.interfaces.rest.transform;

import apx.inc.design_web_services_backend.notifications.domain.model.commands.CreateNotificationCommand;
import apx.inc.design_web_services_backend.notifications.interfaces.rest.resources.CreateNotificationResource;

public class CreateNotificationCommandFromResourceAssembler {
    public static CreateNotificationCommand toCommandFromResource (CreateNotificationResource resource){
        return new CreateNotificationCommand(
                resource.userId(),
                resource.title(),
                resource.message(),
                resource.type(),
                resource.sourceCourseId(),
                resource.sourceAssignmentId()
        );
    }
}
