package apx.inc.design_web_services_backend.assigments.domain.model.commands;

public record UpdateSubmissionCommand(
        Long submissionId,
        Long assignmentId,
        Long studentId,
        String content,
        int score,
        String imageUrl
) {

    public UpdateSubmissionCommand {
        if (submissionId == null || submissionId <= 0) {
            throw new IllegalArgumentException("SubmissionId must be greater than 0");
        }
        if (assignmentId == null || assignmentId <= 0) {
            throw new IllegalArgumentException("Challenge ID must be greater than 0");
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
