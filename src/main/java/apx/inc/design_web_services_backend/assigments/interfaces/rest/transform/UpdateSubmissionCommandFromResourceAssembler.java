package apx.inc.design_web_services_backend.assigments.interfaces.rest.transform;


import apx.inc.design_web_services_backend.assigments.domain.model.commands.UpdateSubmissionCommand;
import apx.inc.design_web_services_backend.assigments.interfaces.rest.resource.UpdateSubmissionResource;

public class UpdateSubmissionCommandFromResourceAssembler {
    public static UpdateSubmissionCommand toCommandFromResource(Long submissionId, UpdateSubmissionResource updateSubmissionResource){
        return new UpdateSubmissionCommand(
                submissionId,
                updateSubmissionResource.assignmentId(),
                updateSubmissionResource.studentId(),
                updateSubmissionResource.content(),
                updateSubmissionResource.score(),
                updateSubmissionResource.imageUrl()
        );
    }
}
