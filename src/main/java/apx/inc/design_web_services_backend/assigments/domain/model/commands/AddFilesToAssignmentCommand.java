package apx.inc.design_web_services_backend.assigments.domain.model.commands;


import java.util.List;

public record AddFilesToAssignmentCommand(
        Long assignmentId,
        List<String> fileUrls
) {
    public AddFilesToAssignmentCommand {
        if (assignmentId == null || assignmentId <= 0) {
            throw new IllegalArgumentException("Assignment ID must be greater than 0");
        }
        if (fileUrls == null) {
            throw new IllegalArgumentException("File URLs cannot be null");
        }
    }
}
