package apx.inc.design_web_services_backend.assigments.interfaces.rest.transform;


import apx.inc.design_web_services_backend.assigments.domain.model.commands.CreateAssignmentCommand;
import apx.inc.design_web_services_backend.assigments.interfaces.rest.resource.CreateAssignmentResource;

public class CreateAssignmentCommandFromResourceAssembler {
    public static CreateAssignmentCommand toCommandFromResource(CreateAssignmentResource resource){
        return new CreateAssignmentCommand(
                resource.title(),
                resource.description(),
                resource.courseId(),
                resource.deadline(),
                resource.imageUrl()
        );
    }
}
