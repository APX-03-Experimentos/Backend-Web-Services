package apx.inc.design_web_services_backend.integration;

import apx.inc.design_web_services_backend.courses.interfaces.rest.resources.CreateCourseResource;
import apx.inc.design_web_services_backend.courses.interfaces.rest.resources.UpdateCourseResource;
import apx.inc.design_web_services_backend.iam.domain.model.valueobjects.Roles;
import apx.inc.design_web_services_backend.iam.rest.resources.SignUpResource;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class CoursesControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private String teacherToken;

    @BeforeEach
    void setUp() throws Exception {
        // Registrar un usuario teacher y obtener el token
//        SignUpResource signUpResource = new SignUpResource(
//                "teacher1",
//                "password1",
//                List.of(Roles.ROLE_TEACHER)
//        );
//        mockMvc.perform(post("/api/v1/authentication/sign-up")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(signUpResource)))
//                .andExpect(status().isCreated());

        // Iniciar sesión para obtener el token
        var signIn = new apx.inc.design_web_services_backend.iam.rest.resources.SignInResource("teacher1", "password1");
        var result = mockMvc.perform(post("/api/v1/authentication/sign-in")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(signIn)))
                .andExpect(status().isOk())
                .andReturn();

        String response = result.getResponse().getContentAsString();
        teacherToken = objectMapper.readTree(response).get("token").asText();
    }

    @Test
    void createCourse_ReturnsCreatedCourse() throws Exception {
        CreateCourseResource resource = new CreateCourseResource("Curso de Prueba");

        mockMvc.perform(post("/api/v1/courses")
                        .header("Authorization", "Bearer " + teacherToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(resource)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Curso de Prueba"));
    }

    @Test
    void getAllCourses_ReturnsList() throws Exception {
        mockMvc.perform(get("/api/v1/courses")
                        .header("Authorization", "Bearer " + teacherToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    void updateCourse_UpdatesTitle() throws Exception {
        // Crear curso
        CreateCourseResource resource = new CreateCourseResource("Curso Original");
        var result = mockMvc.perform(post("/api/v1/courses")
                        .header("Authorization", "Bearer " + teacherToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(resource)))
                .andExpect(status().isOk())
                .andReturn();

        Long courseId = objectMapper.readTree(result.getResponse().getContentAsString()).get("courseId").asLong();

        // Actualizar curso
        UpdateCourseResource updateResource = new UpdateCourseResource("Curso Actualizado", "http://image.com/img.png");
        mockMvc.perform(put("/api/v1/courses/" + courseId)
                        .header("Authorization", "Bearer " + teacherToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateResource)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Curso Actualizado"));
    }

    @Test
    void deleteCourse_RemovesCourse() throws Exception {
        // Crear curso
        CreateCourseResource resource = new CreateCourseResource("Curso a Eliminar");
        var result = mockMvc.perform(post("/api/v1/courses")
                        .header("Authorization", "Bearer " + teacherToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(resource)))
                .andExpect(status().isOk())
                .andReturn();

        Long courseId = objectMapper.readTree(result.getResponse().getContentAsString()).get("courseId").asLong();

        // Eliminar curso
        mockMvc.perform(delete("/api/v1/courses/" + courseId)
                        .header("Authorization", "Bearer " + teacherToken))
                .andExpect(status().isNoContent());
    }
}