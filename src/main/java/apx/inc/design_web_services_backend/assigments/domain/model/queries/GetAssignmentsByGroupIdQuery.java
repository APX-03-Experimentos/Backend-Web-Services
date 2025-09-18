package apx.inc.design_web_services_backend.assigments.domain.model.queries;

public record GetAssignmentsByGroupIdQuery(Long courseId) {

    public GetAssignmentsByGroupIdQuery {
        if (courseId == null || courseId <= 0) {
            throw new IllegalArgumentException("Assignment ID must be a positive number.");
        }
    }
}
