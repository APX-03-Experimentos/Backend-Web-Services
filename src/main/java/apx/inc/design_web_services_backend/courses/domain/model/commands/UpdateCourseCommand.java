package apx.inc.design_web_services_backend.courses.domain.model.commands;

public record UpdateCourseCommand(
        Long courseId,
        String title,
        String imageUrl
) {
}
