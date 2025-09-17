package apx.inc.design_web_services_backend.iam.infrastructure.tokens.jwt;

import apx.inc.design_web_services_backend.iam.application.internal.outboundservices.tokens.TokenService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;

public interface BearerTokenService extends TokenService {
    String getBearerTokenFrom(HttpServletRequest request);

    String generateToken(Authentication authentication);
}
