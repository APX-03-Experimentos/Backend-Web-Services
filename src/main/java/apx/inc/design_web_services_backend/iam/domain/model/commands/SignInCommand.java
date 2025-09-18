package apx.inc.design_web_services_backend.iam.domain.model.commands;

public record SignInCommand(String userName, String password) {
    public SignInCommand {
        if (userName==null || userName.isBlank()){
            throw new IllegalArgumentException("Username cannot be empty");
        }
        if (password==null || password.isBlank()){
            throw new IllegalArgumentException("Password cannot be empty");
        }
    }
}
