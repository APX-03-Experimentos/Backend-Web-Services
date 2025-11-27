package apx.inc.design_web_services_backend.iam.domain.model.queries;

public record GetUserByUsernameQuery(String username){
    public GetUserByUsernameQuery {
        if (username == null || username.isBlank()){
            throw new IllegalArgumentException("User name must be a positive number");
        }
    }
}
