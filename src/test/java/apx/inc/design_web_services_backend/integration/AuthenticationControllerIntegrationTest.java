package apx.inc.design_web_services_backend.integration;

import apx.inc.design_web_services_backend.iam.rest.resources.SignInResource;
import apx.inc.design_web_services_backend.iam.rest.resources.SignUpResource;
import apx.inc.design_web_services_backend.iam.domain.model.valueobjects.Roles;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class AuthenticationControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void signUp_CreatesUser_WhenValidRequest() throws Exception {
        SignUpResource signUpResource = new SignUpResource(
                "testuser4",
                "testpassword",
                List.of(Roles.ROLE_STUDENT)
        );

        mockMvc.perform(post("/api/v1/authentication/sign-up")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(signUpResource)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.userName").value("testuser4"));
    }

    @Test
    void signIn_ReturnsAuthenticatedUser_WhenCredentialsAreValid() throws Exception {
        // Registrar el usuario
        SignUpResource signUpResource = new SignUpResource(
                "loginuser5",
                "loginpassword",
                List.of(Roles.ROLE_STUDENT)
        );
        mockMvc.perform(post("/api/v1/authentication/sign-up")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(signUpResource)))
                .andExpect(status().isCreated());

        // Iniciar sesión
        SignInResource signInResource = new SignInResource(
                "loginuser5",
                "loginpassword"
        );
        mockMvc.perform(post("/api/v1/authentication/sign-in")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(signInResource)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("loginuser5"))
                .andExpect(jsonPath("$.token").isNotEmpty());
    }
}