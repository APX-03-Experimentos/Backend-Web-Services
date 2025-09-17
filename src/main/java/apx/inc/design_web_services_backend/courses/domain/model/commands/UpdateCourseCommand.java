package apx.inc.design_web_services_backend.courses.domain.model.commands;

public record UpdateCourseCommand(
        Long groupId,
        String title,
        String imageUrl
) {
}
