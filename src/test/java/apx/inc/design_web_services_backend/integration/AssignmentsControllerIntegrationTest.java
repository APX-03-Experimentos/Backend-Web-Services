package apx.inc.design_web_services_backend.integration;

import apx.inc.design_web_services_backend.assigments.interfaces.rest.resource.CreateAssignmentResource;
import apx.inc.design_web_services_backend.assigments.interfaces.rest.resource.UpdateAssignmentResource;
import apx.inc.design_web_services_backend.courses.interfaces.rest.resources.CreateCourseResource;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Calendar;
import java.util.Date;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class AssignmentsControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private String teacherToken;
    private Long courseId;

    @BeforeEach
    void setUp() throws Exception {
        // Iniciar sesión
        var signIn = new apx.inc.design_web_services_backend.iam.rest.resources.SignInResource("teacher1", "password1");
        var result = mockMvc.perform(post("/api/v1/authentication/sign-in")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(signIn)))
                .andExpect(status().isOk())
                .andReturn();

        String response = result.getResponse().getContentAsString();
        teacherToken = objectMapper.readTree(response).get("token").asText();

        // Crear un curso
        CreateCourseResource resource = new CreateCourseResource("Curso para Assignments");
        var courseResult = mockMvc.perform(post("/api/v1/courses")
                        .header("Authorization", "Bearer " + teacherToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(resource)))
                .andExpect(status().isOk())
                .andReturn();

        courseId = objectMapper.readTree(courseResult.getResponse().getContentAsString()).get("courseId").asLong();
    }

    @Test
    void createAssignment_CreatesAssignment_WhenValidRequest() throws Exception {
        CreateAssignmentResource resource = new CreateAssignmentResource(
                "Tarea 1",
                "Descripción de la tarea",
                courseId,
                getFutureDate(),
                "https://picsum.photos/400/300?random=1"
        );

        mockMvc.perform(post("/api/v1/assignments")
                        .header("Authorization", "Bearer " + teacherToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(resource)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("Tarea 1"))
                .andExpect(jsonPath("$.courseId").value(courseId));
    }

    @Test
    void getAllAssignments_ReturnsList() throws Exception {
        mockMvc.perform(get("/api/v1/assignments")
                        .header("Authorization", "Bearer " + teacherToken))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void getAssignmentById_ReturnsAssignment_WhenExists() throws Exception {
        // Crear assignment
        CreateAssignmentResource resource = new CreateAssignmentResource(
                "Tarea 2",
                "Otra descripción",
                courseId,
                getFutureDate(),
                "https://picsum.photos/400/300?random=2"
        );
        String response = mockMvc.perform(post("/api/v1/assignments")
                        .header("Authorization", "Bearer " + teacherToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(resource)))
                .andReturn().getResponse().getContentAsString();

        Long assignmentId = objectMapper.readTree(response).get("id").asLong();

        mockMvc.perform(get("/api/v1/assignments/" + assignmentId)
                        .header("Authorization", "Bearer " + teacherToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(assignmentId));
    }

    @Test
    void updateAssignment_UpdatesAssignment_WhenValidRequest() throws Exception {
        // Crear assignment
        CreateAssignmentResource resource = new CreateAssignmentResource(
                "Tarea 3",
                "Descripción original",
                courseId,
                getFutureDate(),
                "https://picsum.photos/400/300?random=3"
        );
        String response = mockMvc.perform(post("/api/v1/assignments")
                        .header("Authorization", "Bearer " + teacherToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(resource)))
                .andReturn().getResponse().getContentAsString();

        Long assignmentId = objectMapper.readTree(response).get("id").asLong();

        UpdateAssignmentResource updateResource = new UpdateAssignmentResource(
                "Tarea 3 Actualizada",
                "Descripción actualizada",
                courseId,
                getFutureDate(),
                "https://picsum.photos/400/300?random=4"
        );

        mockMvc.perform(put("/api/v1/assignments/" + assignmentId)
                        .header("Authorization", "Bearer " + teacherToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateResource)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Tarea 3 Actualizada"));
    }

    @Test
    void deleteAssignment_DeletesAssignment_WhenExists() throws Exception {
        // Crear assignment
        CreateAssignmentResource resource = new CreateAssignmentResource(
                "Tarea 4",
                "Descripción para eliminar",
                courseId,
                getFutureDate(),
                "https://picsum.photos/400/300?random=5"
        );
        String response = mockMvc.perform(post("/api/v1/assignments")
                        .header("Authorization", "Bearer " + teacherToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(resource)))
                .andReturn().getResponse().getContentAsString();

        Long assignmentId = objectMapper.readTree(response).get("id").asLong();

        mockMvc.perform(delete("/api/v1/assignments/" + assignmentId)
                        .header("Authorization", "Bearer " + teacherToken))
                .andExpect(status().isNoContent());
    }

    @Test
    void getAssignmentsByCourseId_ReturnsAssignments() throws Exception {
        // Crear un assignment para el curso
        CreateAssignmentResource resource = new CreateAssignmentResource(
                "Tarea para el curso",
                "Descripción",
                courseId,
                getFutureDate(),
                "https://picsum.photos/400/300?random=6"
        );
        mockMvc.perform(post("/api/v1/assignments")
                        .header("Authorization", "Bearer " + teacherToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(resource)))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/api/v1/assignments/course/" + courseId)
                        .header("Authorization", "Bearer " + teacherToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    private Date getFutureDate() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, 2);
        return cal.getTime();
    }
}