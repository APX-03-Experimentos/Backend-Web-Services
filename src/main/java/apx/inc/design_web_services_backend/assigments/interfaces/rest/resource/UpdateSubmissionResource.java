package apx.inc.design_web_services_backend.assigments.interfaces.rest.resource;

public record UpdateSubmissionResource(
        Long assignmentId,
        Long studentId,
        String content,
        int score,
        String imageUrl
) {

    public UpdateSubmissionResource {

        if (assignmentId == null || assignmentId <= 0) {
            throw new IllegalArgumentException("Assignment ID must be greater than 0");
        }
        if (studentId == null || studentId <= 0) {
            throw new IllegalArgumentException("Student ID must be greater than 0");
        }
        if (content == null || content.isBlank()) {
            throw new IllegalArgumentException("Content cannot be null or blank");
        }
        if (score < 0 || score > 20) {
            throw new IllegalArgumentException("Score must be between 0 and 20");
        }
        if (imageUrl == null || imageUrl.isBlank()) {
            throw new IllegalArgumentException("Image URL cannot be null or blank");
        }
    }
}
