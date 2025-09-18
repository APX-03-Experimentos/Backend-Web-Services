package apx.inc.design_web_services_backend.assigments.interfaces.rest;

import apx.inc.design_web_services_backend.assigments.domain.model.commands.DeleteSubmissionCommand;
import apx.inc.design_web_services_backend.assigments.domain.model.queries.*;
import apx.inc.design_web_services_backend.assigments.domain.services.SubmissionCommandService;
import apx.inc.design_web_services_backend.assigments.domain.services.SubmissionQueryService;
import apx.inc.design_web_services_backend.assigments.interfaces.rest.resource.CreateSubmissionResource;
import apx.inc.design_web_services_backend.assigments.interfaces.rest.resource.GradeSubmissionResource;
import apx.inc.design_web_services_backend.assigments.interfaces.rest.resource.SubmissionResource;
import apx.inc.design_web_services_backend.assigments.interfaces.rest.resource.UpdateSubmissionResource;
import apx.inc.design_web_services_backend.assigments.interfaces.rest.transform.CreateSubmissionCommandFromResourceAssembler;
import apx.inc.design_web_services_backend.assigments.interfaces.rest.transform.GradeSubmissionCommandFromResourceAssembler;
import apx.inc.design_web_services_backend.assigments.interfaces.rest.transform.SubmissionResourceFromEntityAssembler;
import apx.inc.design_web_services_backend.assigments.interfaces.rest.transform.UpdateSubmissionCommandFromResourceAssembler;
import apx.inc.design_web_services_backend.iam.infrastructure.authorization.sfs.model.UserDetailsImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(value = "/api/v1/submissions", produces = APPLICATION_JSON_VALUE)
@Tag(name = "Submissions", description = "Operations related to submissions")
public class SubmissionsController {
    private final SubmissionCommandService submissionCommandService;
    private final SubmissionQueryService submissionQueryService;

    public SubmissionsController(SubmissionCommandService submissionCommandService, SubmissionQueryService submissionQueryService) {
        this.submissionCommandService = submissionCommandService;
        this.submissionQueryService = submissionQueryService;
    }

    private Long getAuthenticatedUserId() {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        var principal = auth.getPrincipal();
        if (principal instanceof UserDetailsImpl userDetails) {
            System.out.println("🪪 Authenticated User ID: " + userDetails.getId());
            return userDetails.getId();
        }
        throw new RuntimeException("Invalid principal type");
    }

    @PreAuthorize("hasRole('ROLE_STUDENT')")
    @PostMapping
    @Operation(summary = "Create a new submission", description = "Creates a new submission for a challenge by a student.")
    @ApiResponses(value = {
            @ApiResponse (responseCode = "201", description = "Submission created successfully"),
            @ApiResponse (responseCode = "400", description = "Invalid input data")
    })
    public ResponseEntity<SubmissionResource> createSubmission(@RequestBody CreateSubmissionResource submissionResource){

        Long authenticatedUserId = getAuthenticatedUserId();

        var createdSubmission= CreateSubmissionCommandFromResourceAssembler.toCommandFromResource(submissionResource, authenticatedUserId);
        var submissionId = submissionCommandService.handle(createdSubmission);
        if(submissionId ==null|| submissionId ==0L) {
            return ResponseEntity.badRequest().build();//da una respuestra 400 y vacia
        }
        var getSubmissionByIdQuery = new GetSubmissionByIdQuery(submissionId);
        var submission= submissionQueryService.handle(getSubmissionByIdQuery,authenticatedUserId);

        if(submission.isEmpty()) {
            return ResponseEntity.notFound().build(); //404 Not Found
        }
        var submissionEntity= submission.get();
        var submissionResponse= SubmissionResourceFromEntityAssembler.toResourceFromEntity(submissionEntity);
        return new ResponseEntity<>(submissionResponse, HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole('ROLE_TEACHER')")
    @PutMapping("/{submissionId}")
    @Operation(summary = "Update a submission", description = "Update a submission's score by its ID.")
    @ApiResponses(value = {
            @ApiResponse (responseCode = "200", description = "Submission updated successfully"),
            @ApiResponse (responseCode = "400", description = "Invalid input data")
    })
    public ResponseEntity<SubmissionResource> updateSubmission(@PathVariable Long submissionId, @RequestBody UpdateSubmissionResource updateSubmissionResource){
        var updateSubmissionCommand= UpdateSubmissionCommandFromResourceAssembler.toCommandFromResource(submissionId, updateSubmissionResource);
        var updatedSubmission= submissionCommandService.handle(updateSubmissionCommand);
        if (updatedSubmission.isEmpty()) {
            return ResponseEntity.badRequest().build(); //400 Bad Request
        }

        var updatedSubmissionEntity= updatedSubmission.get();
        var updatedSubmissionResponse= SubmissionResourceFromEntityAssembler.toResourceFromEntity(updatedSubmissionEntity);
        return ResponseEntity.ok(updatedSubmissionResponse);//200
    }

    @DeleteMapping("/{submissionId}")
    @Operation(summary = "Delete a submission", description = "Deletes a submission by its ID.")
    @ApiResponses(value = {
            @ApiResponse (responseCode = "204", description = "Submission deleted successfully"),
            @ApiResponse (responseCode = "404", description = "Submission not found")
    })
    public ResponseEntity<SubmissionResource> deleteSubmission(@PathVariable Long submissionId){

        var deleteSubmissionCommand = new DeleteSubmissionCommand(submissionId);
        submissionCommandService.handle(deleteSubmissionCommand);

        return ResponseEntity.noContent().build(); //204 No Content
    }

    @GetMapping("/{submissionId}")
    @Operation(summary = "Get a submission by ID", description = "Retrieves a submission by its ID.")
    @ApiResponses(value = {
            @ApiResponse (responseCode = "200", description = "Submission retrieved successfully"),
            @ApiResponse (responseCode = "404", description = "Submission not found")
    })
    public ResponseEntity<SubmissionResource> getSubmissionById(@PathVariable Long submissionId){
        Long authenticatedUserId = getAuthenticatedUserId();
        var getSubmissionByIdQuery= new GetSubmissionByIdQuery(submissionId);
        var submission = submissionQueryService.handle(getSubmissionByIdQuery,authenticatedUserId);
        if (submission.isEmpty()) {
            return ResponseEntity.notFound().build(); //404 Not Found
        }

        var submissionEntity = submission.get();
        var submissionResponse= SubmissionResourceFromEntityAssembler.toResourceFromEntity(submissionEntity);
        return ResponseEntity.ok(submissionResponse); //200 OK

    }

    @GetMapping
    @Operation(summary = "Get all submissions", description = "Retrieves all submissions.")
    @ApiResponses(value = {
            @ApiResponse (responseCode = "200", description = "Submissions retrieved successfully"),
            @ApiResponse (responseCode = "404", description = "No submissions found")
    })
    public ResponseEntity<List<SubmissionResource>> getAllSubmissions(){
        var submissions = submissionQueryService.handle(new GetAllSubmissionsQuery());
        if (submissions.isEmpty()) {
            return ResponseEntity.notFound().build(); //404 Not Found
        }

        var submissionResources = submissions.stream()
                .map(SubmissionResourceFromEntityAssembler::toResourceFromEntity)
                .toList();

        return ResponseEntity.ok(submissionResources);
    }

    @GetMapping("/assignment/{assignmentId}")
    @Operation(summary = "Get submissions by assignmentId", description = "Retrieves submissions by assignmentId.")
    @ApiResponses(value = {
            @ApiResponse (responseCode = "200", description = "Submissions retrieved successfully"),
            @ApiResponse (responseCode = "404", description = "No submissions found")
    })
    public ResponseEntity<List<SubmissionResource>> getSubmissionsByAssignmentIdQuery(@PathVariable Long assignmentId) {
        var getSubmissionsByChallengeIdQuery = new GetSubmissionsByAssignmentIdQuery(assignmentId);
        var submissions = submissionQueryService.handle(getSubmissionsByChallengeIdQuery);
        if (submissions.isEmpty()) {
            return ResponseEntity.notFound().build(); //404 Not Found
        }

        var submissionResources = submissions.stream()
                .map(SubmissionResourceFromEntityAssembler::toResourceFromEntity)
                .toList();

        return ResponseEntity.ok(submissionResources);
    }

    @GetMapping("/student/{studentId}")
    @Operation(summary = "Get submissions by studentId", description = "Retrieves submissions submitted by a specific student.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Submissions retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "No submissions found for the given student")
    })
    public ResponseEntity<List<SubmissionResource>> getSubmissionsByStudentId(@PathVariable Long studentId) {
        //Create the query to get submissions by studentId
        var getSubmissionsByStudentIdQuery = new GetSubmissionsByStudentIdQuery(studentId);

        // Execute the query
        var submissionsOptional = submissionQueryService.handle(getSubmissionsByStudentIdQuery);

        //Verify if submissions were found
        if (submissionsOptional.isEmpty()) {
            return ResponseEntity.notFound().build(); // 404
        }
        //cada entidad de dominio (Submission) en un DTO o recurso (SubmissionResource) que es más adecuado para enviar como respuesta HTTP
        // Convert every Submission entity to SubmissionResource (resource o DTO) to send as HTTP response
        var resources = submissionsOptional.stream()
                .map(SubmissionResourceFromEntityAssembler::toResourceFromEntity)
                .toList();

        return ResponseEntity.ok(resources); // 200
    }

    @GetMapping("/students/{studentId}/assignments/{assignmentId}")
    @Operation(summary = "Get submissions by studentId and assignmentId", description = "Retrieves submissions submitted by a specific student for a specific assignment.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Submissions retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "No submissions found for the given student and assignment")
    })
    public ResponseEntity<List<SubmissionResource>> getSubmissionsByStudentIdAndChallengeId(@PathVariable Long studentId, @PathVariable Long assignmentId) {
        // Create the query
        var getSubmissionsByStudentIdAndChallengeIdQuery = new GetSubmissionsByStudentIdAndAssignmentIdQuery(studentId, assignmentId);

        // Execute the query
        var submissionsOptional = submissionQueryService.handle(getSubmissionsByStudentIdAndChallengeIdQuery);

        // Verify if submissions were found
        if (submissionsOptional.isEmpty()) {
            return ResponseEntity.notFound().build(); // 404 Not Found
        }

        // Convert every Submission entity to SubmissionResource (resource o DTO) to send as HTTP response
        var submissionResources = submissionsOptional.stream()
                .map(SubmissionResourceFromEntityAssembler::toResourceFromEntity)
                .toList();

        return ResponseEntity.ok(submissionResources); // 200 OK
    }

    @GetMapping("/student/{studentId}/group/{courseId}")
    @Operation(summary = "Get submissions by studentId and courseId", description = "Retrieves submissions submitted by a specific student in a specific course.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Submissions retrieved successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "404", description = "No submissions found for the given student and course")
    })
    public ResponseEntity<List<SubmissionResource>> getSubmissionsByStudentIdAndCourseId(
            @PathVariable Long studentId,
            @PathVariable Long courseId) {

        try {
            GetSubmissionsByStudentIdAndCourseIdQuery getSubmissionsByStudentIdAndGroupIdQuery = new GetSubmissionsByStudentIdAndCourseIdQuery(studentId, courseId);
            var submissions = submissionQueryService.handle(getSubmissionsByStudentIdAndGroupIdQuery);
            var resources = submissions.stream()
                    .map(SubmissionResourceFromEntityAssembler::toResourceFromEntity)
                    .toList();
            return ResponseEntity.ok(resources);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(null);
        }
    }

    @GetMapping("/course/{courseId}")
    @Operation(summary = "Get submissions by courseId", description = "Retrieves submissions submitted in a specific course.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Submissions retrieved successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "404", description = "No submissions found for the given student and course")
    })
    public ResponseEntity<List<SubmissionResource>> getSubmissionsByGroupId(@PathVariable Long courseId) {
        try {
            GetSubmissionsByCourseIdQuery query = new GetSubmissionsByCourseIdQuery(courseId);
            var submissions = submissionQueryService.handle(query);
            var resources = submissions.stream()
                    .map(SubmissionResourceFromEntityAssembler::toResourceFromEntity)
                    .toList();
            return ResponseEntity.ok(resources);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(null);
        }
    }

    @PreAuthorize("hasRole('ROLE_TEACHER')")
    @PutMapping("/{submissionId}/grade")
    @Operation(summary = "Grade a submission", description = "Updates the score of a submission.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Submission graded successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    public ResponseEntity<SubmissionResource> gradeSubmission(
            @PathVariable Long submissionId,
            @RequestBody GradeSubmissionResource gradeSubmissionResource) {

        var command = GradeSubmissionCommandFromResourceAssembler.toCommandFromResource(submissionId, gradeSubmissionResource);
        var gradedSubmission = submissionCommandService.handle(command);

        if (gradedSubmission.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        var resource = SubmissionResourceFromEntityAssembler.toResourceFromEntity(gradedSubmission.get());
        return ResponseEntity.ok(resource);
    }




}
