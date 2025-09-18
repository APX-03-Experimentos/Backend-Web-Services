package apx.inc.design_web_services_backend.assigments.domain.model.queries;

public record GetAssignmentByIdQuery(Long assignmentId) {

    public GetAssignmentByIdQuery {
        if (assignmentId == null || assignmentId <= 0)
            throw new IllegalArgumentException("Assignment ID must be a positive number.");
    }
}
