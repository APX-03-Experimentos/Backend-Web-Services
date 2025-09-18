package apx.inc.design_web_services_backend.assigments.interfaces.rest;

import apx.inc.design_web_services_backend.assigments.domain.model.commands.DeleteAssignmentCommand;
import apx.inc.design_web_services_backend.assigments.domain.model.queries.GetAllAssignmentsQuery;
import apx.inc.design_web_services_backend.assigments.domain.model.queries.GetAssignmentByIdQuery;
import apx.inc.design_web_services_backend.assigments.domain.model.queries.GetAssignmentsByGroupIdQuery;
import apx.inc.design_web_services_backend.assigments.domain.services.AssignmentCommandService;
import apx.inc.design_web_services_backend.assigments.domain.services.AssignmentQueryService;
import apx.inc.design_web_services_backend.assigments.interfaces.rest.resource.AssignmentResource;
import apx.inc.design_web_services_backend.assigments.interfaces.rest.resource.CreateAssignmentResource;
import apx.inc.design_web_services_backend.assigments.interfaces.rest.resource.UpdateAssignmentResource;
import apx.inc.design_web_services_backend.assigments.interfaces.rest.transform.AssignmentResourceFromEntityAssembler;
import apx.inc.design_web_services_backend.assigments.interfaces.rest.transform.CreateAssignmentCommandFromResourceAssembler;
import apx.inc.design_web_services_backend.assigments.interfaces.rest.transform.UpdateAssignmentCommandFromResourceAssembler;
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
@RequestMapping(value = "/api/v1/assignments", produces = APPLICATION_JSON_VALUE)
@Tag(name= "Assignments", description = "Operations related to assignments")
public class AssignmentsController {
    private final AssignmentCommandService assignmentCommandService;
    private final AssignmentQueryService assignmentQueryService;


    public AssignmentsController(AssignmentCommandService assignmentCommandService, AssignmentQueryService assignmentQueryService) {
        this.assignmentCommandService = assignmentCommandService;
        this.assignmentQueryService = assignmentQueryService;
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


    @PreAuthorize("hasRole('TEACHER')")
    @PostMapping
    @Operation(summary = "Create a new assignment", description = "Creates a new assignment with the provided details.")
    @ApiResponses( value = {
        @ApiResponse(responseCode = "201", description = "Assignment created successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid input data"),
        @ApiResponse(responseCode = "404", description = "Assignment not found")
    })
    public ResponseEntity<AssignmentResource> createAssignment(@RequestBody CreateAssignmentResource createAssignmentResource){

        Long authenticatedUserId = getAuthenticatedUserId();

        var createdAssignment= CreateAssignmentCommandFromResourceAssembler.toCommandFromResource(createAssignmentResource);
        var assignmentId =assignmentCommandService.handle(createdAssignment, authenticatedUserId);
        if (assignmentId ==null|| assignmentId ==0L){
            return ResponseEntity.badRequest().build(); //da una respuestra 400 y vacia
        }
        var getAssignmentByIdQuery = new GetAssignmentByIdQuery(assignmentId);
        var assignment= assignmentQueryService.handle(getAssignmentByIdQuery,authenticatedUserId);

        if (assignment.isEmpty()){
           return ResponseEntity.notFound().build(); // da una respuesta 404 y vacia
        }
        var assignmentEntity =assignment.get();
        var assignmentResponse= AssignmentResourceFromEntityAssembler.toResourceFromEntity(assignmentEntity);
        return new ResponseEntity<>(assignmentResponse, HttpStatus.CREATED ); //201 es creado
    }

    @PutMapping("/{assignmentId}")
    @Operation(summary = "Update an existing assignment", description = "Updates the details of an existing assignment.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Assignment updated successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid input data"),
        @ApiResponse(responseCode = "404", description = "Assignment not found")
    })
    public ResponseEntity<AssignmentResource> updateAssignment(@PathVariable Long assignmentId, @RequestBody UpdateAssignmentResource updateAssignmentResource){
        Long userId = getAuthenticatedUserId();
        var updateAssignmentCommand = UpdateAssignmentCommandFromResourceAssembler.toCommandFromResource(assignmentId,updateAssignmentResource);
        var updatedAssignment= assignmentCommandService.handle(updateAssignmentCommand,userId);
        if (updatedAssignment.isEmpty()){
            return ResponseEntity.notFound().build(); // da una respuesta 404 y vacia
        }

        var updatedAssignmentEntity = updatedAssignment.get();
        var assignmentResponse = AssignmentResourceFromEntityAssembler.toResourceFromEntity(updatedAssignmentEntity);
        return new ResponseEntity<>(assignmentResponse,HttpStatus.OK); //200 es ok
    }

    @DeleteMapping("/{assignmentId}")
    @Operation(summary = "Delete a assignment", description = "Deletes an existing assignment by its ID.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Assignment deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Assignment not found")
    })
    public ResponseEntity<AssignmentResource> deleteChallenge(@PathVariable Long assignmentId){
        var deleteAssignmentCommand = new DeleteAssignmentCommand(assignmentId);
        assignmentCommandService.handle(deleteAssignmentCommand);

        return ResponseEntity.noContent().build(); //204 es no content, no hay contenido que devolver
    }

    @GetMapping("/{assignmentId}")
    @Operation(summary = "Get a assignment by ID", description = "Retrieves the details of a challenge by its ID.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Challenge retrieved successfully"),
        @ApiResponse(responseCode = "404", description = "Challenge not found")
    })
    public ResponseEntity<AssignmentResource> getChallengeById(@PathVariable Long assignmentId){
        Long authenticatedUserId = getAuthenticatedUserId();

        var getAssignmentByIdQuery = new GetAssignmentByIdQuery(assignmentId);
        var assignment = assignmentQueryService.handle(getAssignmentByIdQuery,authenticatedUserId);
        if (assignment.isEmpty()){
            return ResponseEntity.notFound().build(); // da una respuesta 404 y vacia
        }
        var assignmentEntity = assignment.get();
        var assignmentResponse =AssignmentResourceFromEntityAssembler.toResourceFromEntity(assignmentEntity);
        return ResponseEntity.ok(assignmentResponse); //200 es ok, devuelve el challenge
    }

    @GetMapping
    @Operation(summary = "Get all assignment", description = "Retrieves a list of all assignment.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Assignment retrieved successfully"),
        @ApiResponse(responseCode = "404", description = "No assignment found")
    })
    public ResponseEntity<List<AssignmentResource>> getAllChallenges(){
        var assignments= assignmentQueryService.handle(new GetAllAssignmentsQuery());
        if (assignments.isEmpty()){
            return ResponseEntity.ok(List.of()); // da una respuesta 404 y vacia
        }
        var assignmentResources=assignments.stream()
                .map(AssignmentResourceFromEntityAssembler::toResourceFromEntity)
                .toList();

        return ResponseEntity.ok(assignmentResources); //200 es ok, devuelve la lista de challenges
    }

    @GetMapping("/course/{courseId}")
    @Operation(summary = "Get assignments by course ID", description = "Retrieves a list of challenges associated with a specific course ID.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Challenges retrieved successfully"),
        @ApiResponse(responseCode = "404", description = "No challenges found for the group")
    })
    public ResponseEntity<List<AssignmentResource>> getChallengesByCourseId(@PathVariable Long courseId){

        Long authenticatedUserId = getAuthenticatedUserId();
        var getAssignmentsByGroupIdQuery=new GetAssignmentsByGroupIdQuery(courseId);
        var assignments =assignmentQueryService.handle(getAssignmentsByGroupIdQuery,authenticatedUserId);
        if (assignments.isEmpty()){
            return ResponseEntity.notFound().build(); // da una respuesta 404 y vacia
        }
        var assignmentResources = assignments.stream()
                .map(AssignmentResourceFromEntityAssembler::toResourceFromEntity)
                .toList();

        return ResponseEntity.ok(assignmentResources);
    }




}
