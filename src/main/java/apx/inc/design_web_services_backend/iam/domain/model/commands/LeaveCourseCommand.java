package apx.inc.design_web_services_backend.iam.domain.model.commands;

public record LeaveCourseCommand(Long userId, Long courseId) {

    public LeaveCourseCommand {
        if (userId == null || userId <= 0) {
            throw new IllegalArgumentException("User ID must be a positive number");
        }
        if (courseId == null || courseId <= 0) {
            throw new IllegalArgumentException("Group ID must be a positive number");
        }
    }
}
