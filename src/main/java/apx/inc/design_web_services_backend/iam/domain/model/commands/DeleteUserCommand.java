package apx.inc.design_web_services_backend.iam.domain.model.commands;

public record DeleteUserCommand(Long userId) {
    public DeleteUserCommand {
        if (userId==null||userId<=0) {
            throw new IllegalArgumentException("UserId cannot be empty or negative");
        }
    }
}
