package apx.inc.design_web_services_backend.iam.infrastructure.authorization.sfs.pipeline;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class UnauthorizedRequestHandlerEntryPoint implements AuthenticationEntryPoint {

    private static final Logger LOGGER = LoggerFactory.getLogger(UnauthorizedRequestHandlerEntryPoint.class);

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authenticationException) throws IOException, ServletException {

        String path = request.getServletPath();
        String method = request.getMethod();

        // ✅ AGREGAR ESTA VERIFICACIÓN - NO bloquear endpoints públicos
        if (path.startsWith("/authentication") ||
                path.startsWith("/ws-notifications") ||
                path.startsWith("/v3/api-docs") ||
                path.startsWith("/swagger-ui")) {

            LOGGER.info("🔍 SKIPPING UnauthorizedHandler for public endpoint: {} {}", method, path);
            // ✅ Dejar que el request continúe hacia el controller
            return;
        }

        // ✅ Solo ejecutar para endpoints protegidos
        LOGGER.error("Unauthorized request for protected endpoint: {} {}", method, path);
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Full authentication is required to access this resource");
    }
}