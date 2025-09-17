package apx.inc.design_web_services_backend.iam.application.internal.outboundservices.tokens;

public interface TokenService {
    String generateToken(String userName);

    String getUserNameFromToken(String token);

    boolean validateToken(String token);
}
