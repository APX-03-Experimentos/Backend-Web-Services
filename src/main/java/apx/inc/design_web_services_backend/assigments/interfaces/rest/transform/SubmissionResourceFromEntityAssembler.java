package apx.inc.design_web_services_backend.assigments.interfaces.rest.transform;


import apx.inc.design_web_services_backend.assigments.domain.model.aggregates.Submission;
import apx.inc.design_web_services_backend.assigments.interfaces.rest.resource.SubmissionResource;

public class SubmissionResourceFromEntityAssembler {
    public static SubmissionResource toResourceFromEntity(Submission submissionEntity){
        return new SubmissionResource(
                submissionEntity.getId(),
                submissionEntity.getAssignmentId(),
                submissionEntity.getStudentId(),
                submissionEntity.getContent(),
                submissionEntity.getScore(),
                submissionEntity.getImageUrl(),
                submissionEntity.getState().name()
        );
    }
}
