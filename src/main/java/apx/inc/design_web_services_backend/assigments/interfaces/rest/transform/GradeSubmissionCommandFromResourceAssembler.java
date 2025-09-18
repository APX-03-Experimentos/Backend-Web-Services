package apx.inc.design_web_services_backend.assigments.interfaces.rest.transform;


import apx.inc.design_web_services_backend.assigments.domain.model.commands.GradeSubmissionCommand;
import apx.inc.design_web_services_backend.assigments.interfaces.rest.resource.GradeSubmissionResource;

public class GradeSubmissionCommandFromResourceAssembler {
    public static GradeSubmissionCommand toCommandFromResource(Long submissionId, GradeSubmissionResource resource) {
        return new GradeSubmissionCommand(submissionId, resource.score());
    }
}
