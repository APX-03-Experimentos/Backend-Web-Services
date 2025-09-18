package apx.inc.design_web_services_backend.assigments.interfaces.rest.resource;

public record SubmissionResource(
        Long id,
        Long assignmentId,
        Long studentId,
        String content,
        int score,
        String imageUrl,
        String status
) {
}
