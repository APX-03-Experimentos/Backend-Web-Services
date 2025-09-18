package apx.inc.design_web_services_backend.assigments.domain.model.queries;

public record GetSubmissionsByStudentIdAndAssignmentIdQuery(Long studentId, Long assignmentId) {

    public GetSubmissionsByStudentIdAndAssignmentIdQuery {
        if (studentId == null || studentId <= 0) {
            throw new IllegalArgumentException("Student ID must be greater than 0");
        }
        if (assignmentId == null || assignmentId <= 0) {
            throw new IllegalArgumentException("Challenge ID must be greater than 0");
        }
    }
}
