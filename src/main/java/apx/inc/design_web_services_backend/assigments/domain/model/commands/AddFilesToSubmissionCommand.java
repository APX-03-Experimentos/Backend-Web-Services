package apx.inc.design_web_services_backend.assigments.domain.model.commands;

import java.util.List;

public record AddFilesToSubmissionCommand(
        Long submissionId,
        List<String> fileUrls
) {
    public AddFilesToSubmissionCommand {
        if (submissionId == null || submissionId <= 0) {
            throw new IllegalArgumentException("SubmissionId ID must be greater than 0");
        }
        if (fileUrls == null) {
            throw new IllegalArgumentException("File URLs cannot be null");
        }
    }
}