package apx.inc.design_web_services_backend.assigments.domain.model.commands;

public record RemoveFileFromSubmissionCommand(
        Long submissionId,
        String fileUrl
) {
    public RemoveFileFromSubmissionCommand {
        if (submissionId == null || submissionId <= 0) {
            throw new IllegalArgumentException("SubmissionId ID must be greater than 0");
        }
        if (fileUrl == null || fileUrl.isBlank()) {
            throw new IllegalArgumentException("File URL cannot be null or blank");
        }
    }
}