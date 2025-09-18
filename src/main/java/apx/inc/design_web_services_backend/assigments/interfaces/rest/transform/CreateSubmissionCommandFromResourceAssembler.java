package apx.inc.design_web_services_backend.assigments.interfaces.rest.transform;


import apx.inc.design_web_services_backend.assigments.domain.model.commands.CreateSubmissionCommand;
import apx.inc.design_web_services_backend.assigments.interfaces.rest.resource.CreateSubmissionResource;

public class CreateSubmissionCommandFromResourceAssembler {
    public static CreateSubmissionCommand toCommandFromResource(CreateSubmissionResource createSubmissionResource, Long studentId) {
        return new CreateSubmissionCommand(
                createSubmissionResource.assignmentId(),
                studentId,
                createSubmissionResource.content(),
                createSubmissionResource.imageUrl()
        );
    }
}