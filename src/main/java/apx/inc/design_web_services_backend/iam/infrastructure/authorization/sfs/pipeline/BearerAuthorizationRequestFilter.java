package apx.inc.design_web_services_backend.iam.infrastructure.authorization.sfs.pipeline;

import apx.inc.design_web_services_backend.iam.infrastructure.authorization.sfs.model.UsernamePasswordAuthenticationTokenBuilder;
import apx.inc.design_web_services_backend.iam.infrastructure.tokens.jwt.BearerTokenService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.lang.NonNull;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * Bearer Authorization Request Filter.
 * <p>
 * This class is responsible for filtering requests and setting the user authentication.
 * It extends the OncePerRequestFilter class.
 * </p>
 * @see OncePerRequestFilter
 */
public class BearerAuthorizationRequestFilter extends OncePerRequestFilter {

    private static final Logger LOGGER = LoggerFactory.getLogger(BearerAuthorizationRequestFilter.class);
    private final BearerTokenService tokenService;


    @Qualifier("defaultUserDetailsService")
    private final UserDetailsService userDetailsService;

    public BearerAuthorizationRequestFilter(BearerTokenService tokenService, UserDetailsService userDetailsService) {
        this.tokenService = tokenService;
        this.userDetailsService = userDetailsService;
    }

    /**
     * This method is responsible for filtering requests and setting the user authentication.
     * @param request The request object.
     * @param response The response object.
     * @param filterChain The filter chain object.
     */
    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws ServletException, IOException {

        String path = request.getServletPath();
        String method = request.getMethod();

        // ✅ VERIFICACIÓN EXPLÍCITA - Saltar endpoints públicos
        if (path.startsWith("/authentication") ||
                path.startsWith("/ws-notifications") ||
                path.startsWith("/v3/api-docs") ||
                path.startsWith("/swagger-ui")) {

            LOGGER.info("🔍 DEBUG - SKIPPING FILTER for: {} {}", method, path);
            filterChain.doFilter(request, response);
            return; // ✅ IMPORTANTE: Salir del método
        }

        // ✅ Solo ejecutar lógica de autenticación para endpoints protegidos
        try {
            String token = tokenService.getBearerTokenFrom(request);
            LOGGER.info("🔐 Token for protected endpoint: {}", token);
            if (token != null && tokenService.validateToken(token)) {
                String username = tokenService.getUserNameFromToken(token);
                var userDetails = userDetailsService.loadUserByUsername(username);
                SecurityContextHolder.getContext().setAuthentication(UsernamePasswordAuthenticationTokenBuilder.build(userDetails, request));
            } else {
                LOGGER.info("❌ Token is not valid for protected endpoint");
            }
        } catch (Exception e) {
            LOGGER.error("Cannot set user authentication: {}", e.getMessage());
        }

        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getServletPath();
        boolean shouldSkip = path.startsWith("/authentication") ||
                path.startsWith("/ws-notifications") ||
                path.startsWith("/v3/api-docs") ||
                path.startsWith("/swagger-ui");

        LOGGER.info("🔍 shouldNotFilter - Path: {}, Skip: {}", path, shouldSkip);
        return shouldSkip;
    }
}
