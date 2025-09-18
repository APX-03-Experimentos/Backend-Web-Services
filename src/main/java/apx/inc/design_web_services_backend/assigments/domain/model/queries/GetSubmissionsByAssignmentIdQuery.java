package apx.inc.design_web_services_backend.assigments.domain.model.queries;

public record GetSubmissionsByAssignmentIdQuery(Long assignmentId) {

    public GetSubmissionsByAssignmentIdQuery {
        if (assignmentId == null || assignmentId <= 0) {
            throw new IllegalArgumentException("ChallengeId must be greater than 0");
        }
    }
}
