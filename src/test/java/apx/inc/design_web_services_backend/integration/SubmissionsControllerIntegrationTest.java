package apx.inc.design_web_services_backend.integration;

import apx.inc.design_web_services_backend.assigments.interfaces.rest.resource.CreateAssignmentResource;
import apx.inc.design_web_services_backend.assigments.interfaces.rest.resource.CreateSubmissionResource;
import apx.inc.design_web_services_backend.assigments.interfaces.rest.resource.GradeSubmissionResource;
import apx.inc.design_web_services_backend.assigments.interfaces.rest.resource.UpdateSubmissionResource;
import apx.inc.design_web_services_backend.courses.interfaces.rest.resources.CreateCourseResource;
import apx.inc.design_web_services_backend.iam.rest.resources.SignInResource;
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
public class SubmissionsControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private String teacherToken;
    private String studentToken;
    private Long courseId;
    private Long assignmentId;
    private Long studentId;

    @BeforeEach
    void setUp() throws Exception {
        // Iniciar sesión como teacher
        var signInTeacher = new SignInResource("teacher1", "password1");
        var teacherResult = mockMvc.perform(post("/api/v1/authentication/sign-in")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(signInTeacher)))
                .andExpect(status().isOk())
                .andReturn();
        String teacherResponse = teacherResult.getResponse().getContentAsString();
        teacherToken = objectMapper.readTree(teacherResponse).get("token").asText();

        // Iniciar sesión como student
        var signInStudent = new SignInResource("student1", "password1");
        var studentResult = mockMvc.perform(post("/api/v1/authentication/sign-in")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(signInStudent)))
                .andExpect(status().isOk())
                .andReturn();
        String studentResponse = studentResult.getResponse().getContentAsString();
        studentToken = objectMapper.readTree(studentResponse).get("token").asText();
        studentId = objectMapper.readTree(studentResponse).get("id").asLong();

        // Crear curso
        CreateCourseResource courseResource = new CreateCourseResource("Curso para Submissions");
        var courseResult = mockMvc.perform(post("/api/v1/courses")
                        .header("Authorization", "Bearer " + teacherToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(courseResource)))
                .andExpect(status().isOk())
                .andReturn();
        courseId = objectMapper.readTree(courseResult.getResponse().getContentAsString()).get("courseId").asLong();

        // Obtener el key
        String joinKey = objectMapper.readTree(courseResult.getResponse().getContentAsString()).get("key").asText();

        // El estudiante se une al curso usando el key
        mockMvc.perform(get("/api/v1/courses/join/" + joinKey)
                        .header("Authorization", "Bearer " + studentToken))
                .andExpect(status().isOk());

        // Crear assignment
        CreateAssignmentResource assignmentResource = new CreateAssignmentResource(
                "Assignment para Submissions",
                "Descripción",
                courseId,
                getFutureDate(),
                "https://picsum.photos/400/300?random=100"
        );
        var assignmentResult = mockMvc.perform(post("/api/v1/assignments")
                        .header("Authorization", "Bearer " + teacherToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(assignmentResource)))
                .andExpect(status().isCreated())
                .andReturn();
        assignmentId = objectMapper.readTree(assignmentResult.getResponse().getContentAsString()).get("id").asLong();
    }

    @Test
    void createSubmission_CreatesSubmission_WhenValidRequest() throws Exception {
        CreateSubmissionResource resource = new CreateSubmissionResource(
                assignmentId,
                "Mi entrega de prueba",
                "https://picsum.photos/400/300?random=200"
        );
        mockMvc.perform(post("/api/v1/submissions")
                        .header("Authorization", "Bearer " + studentToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(resource)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.assignmentId").value(assignmentId))
                .andExpect(jsonPath("$.content").value("Mi entrega de prueba"));
    }

    @Test
    void getSubmissionById_ReturnsSubmission_WhenExists() throws Exception {
        // Crear submission
        CreateSubmissionResource resource = new CreateSubmissionResource(
                assignmentId,
                "Entrega para getById",
                "https://picsum.photos/400/300?random=201"
        );
        String response = mockMvc.perform(post("/api/v1/submissions")
                        .header("Authorization", "Bearer " + studentToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(resource)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();
        Long submissionId = objectMapper.readTree(response).get("id").asLong();

        mockMvc.perform(get("/api/v1/submissions/" + submissionId)
                        .header("Authorization", "Bearer " + studentToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(submissionId));
    }

    @Test
    void updateSubmission_UpdatesSubmission_WhenValidRequest() throws Exception {
        // Crear submission
        CreateSubmissionResource resource = new CreateSubmissionResource(
                assignmentId,
                "Entrega original",
                "https://picsum.photos/400/300?random=202"
        );
        String response = mockMvc.perform(post("/api/v1/submissions")
                        .header("Authorization", "Bearer " + studentToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(resource)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();
        Long submissionId = objectMapper.readTree(response).get("id").asLong();

        UpdateSubmissionResource updateResource = new UpdateSubmissionResource(
                assignmentId,
                studentId,
                "Entrega actualizada",
                15,
                "https://picsum.photos/400/300?random=203"
        );
        mockMvc.perform(put("/api/v1/submissions/" + submissionId)
                        .header("Authorization", "Bearer " + teacherToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateResource)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").value("Entrega actualizada"))
                .andExpect(jsonPath("$.score").value(15));
    }

    @Test
    void deleteSubmission_DeletesSubmission_WhenExists() throws Exception {
        // Crear submission
        CreateSubmissionResource resource = new CreateSubmissionResource(
                assignmentId,
                "Entrega para eliminar",
                "https://picsum.photos/400/300?random=204"
        );
        String response = mockMvc.perform(post("/api/v1/submissions")
                        .header("Authorization", "Bearer " + studentToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(resource)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();
        Long submissionId = objectMapper.readTree(response).get("id").asLong();

        mockMvc.perform(delete("/api/v1/submissions/" + submissionId)
                        .header("Authorization", "Bearer " + studentToken))
                .andExpect(status().isNoContent());
    }

    @Test
    void gradeSubmission_GradesSubmission_WhenValidRequest() throws Exception {
        // Crear submission
        CreateSubmissionResource resource = new CreateSubmissionResource(
                assignmentId,
                "Entrega para calificar",
                "https://picsum.photos/400/300?random=205"
        );
        String response = mockMvc.perform(post("/api/v1/submissions")
                        .header("Authorization", "Bearer " + studentToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(resource)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();
        Long submissionId = objectMapper.readTree(response).get("id").asLong();

        GradeSubmissionResource gradeResource = new GradeSubmissionResource(18);
        mockMvc.perform(put("/api/v1/submissions/" + submissionId + "/grade")
                        .header("Authorization", "Bearer " + teacherToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(gradeResource)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.score").value(18));
    }

    @Test
    void getAllSubmissions_ReturnsList() throws Exception {
        mockMvc.perform(get("/api/v1/submissions")
                        .header("Authorization", "Bearer " + teacherToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    void getSubmissionsByAssignmentId_ReturnsList() throws Exception {
        // Crear submission
        CreateSubmissionResource resource = new CreateSubmissionResource(
                assignmentId,
                "Entrega para assignmentId",
                "https://picsum.photos/400/300?random=206"
        );
        mockMvc.perform(post("/api/v1/submissions")
                        .header("Authorization", "Bearer " + studentToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(resource)))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/api/v1/submissions/assignment/" + assignmentId)
                        .header("Authorization", "Bearer " + teacherToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    void getSubmissionsByStudentId_ReturnsList() throws Exception {
        // Crear submission
        CreateSubmissionResource resource = new CreateSubmissionResource(
                assignmentId,
                "Entrega para studentId",
                "https://picsum.photos/400/300?random=207"
        );
        mockMvc.perform(post("/api/v1/submissions")
                        .header("Authorization", "Bearer " + studentToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(resource)))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/api/v1/submissions/student/" + studentId)
                        .header("Authorization", "Bearer " + studentToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    void getSubmissionsByStudentIdAndAssignmentId_ReturnsList() throws Exception {
        // Crear submission
        CreateSubmissionResource resource = new CreateSubmissionResource(
                assignmentId,
                "Entrega para studentId y assignmentId",
                "https://picsum.photos/400/300?random=208"
        );
        mockMvc.perform(post("/api/v1/submissions")
                        .header("Authorization", "Bearer " + studentToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(resource)))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/api/v1/submissions/students/" + studentId + "/assignments/" + assignmentId)
                        .header("Authorization", "Bearer " + studentToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    void getSubmissionsByStudentIdAndCourseId_ReturnsList() throws Exception {
        // Crear submission
        CreateSubmissionResource resource = new CreateSubmissionResource(
                assignmentId,
                "Entrega para studentId y courseId",
                "https://picsum.photos/400/300?random=209"
        );
        mockMvc.perform(post("/api/v1/submissions")
                        .header("Authorization", "Bearer " + studentToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(resource)))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/api/v1/submissions/student/" + studentId + "/group/" + courseId)
                        .header("Authorization", "Bearer " + studentToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    void getSubmissionsByCourseId_ReturnsList() throws Exception {
        // Crear submission
        CreateSubmissionResource resource = new CreateSubmissionResource(
                assignmentId,
                "Entrega para courseId",
                "https://picsum.photos/400/300?random=210"
        );
        mockMvc.perform(post("/api/v1/submissions")
                        .header("Authorization", "Bearer " + studentToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(resource)))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/api/v1/submissions/course/" + courseId)
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