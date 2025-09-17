package apx.inc.design_web_services_backend.iam.rest.resources;

public record CourseResource(
        Long courseId,
        String title,
        String imageUrl,
        CourseJoinCodeResource joinCode  // ← Join code como resource
) {
}
