package apx.inc.design_web_services_backend.courses.interfaces.rest;

import apx.inc.design_web_services_backend.courses.domain.model.aggregates.Course;
import apx.inc.design_web_services_backend.courses.domain.model.commands.DeleteCourseCommand;
import apx.inc.design_web_services_backend.courses.domain.model.commands.JoinByJoinCodeCommand;
import apx.inc.design_web_services_backend.courses.domain.model.commands.KickStudentCommand;
import apx.inc.design_web_services_backend.courses.domain.model.queries.GetAllCoursesQuery;
import apx.inc.design_web_services_backend.courses.domain.model.queries.GetCourseByIdQuery;
import apx.inc.design_web_services_backend.courses.domain.model.queries.GetCoursesByUserIdQuery;
import apx.inc.design_web_services_backend.courses.domain.services.CourseCommandService;
import apx.inc.design_web_services_backend.courses.domain.services.CourseQueryService;
import apx.inc.design_web_services_backend.courses.interfaces.rest.resources.CreateCourseResource;
import apx.inc.design_web_services_backend.courses.interfaces.rest.resources.UpdateCourseResource;
import apx.inc.design_web_services_backend.courses.interfaces.rest.transform.CourseResourceFromEntityAssembler;
import apx.inc.design_web_services_backend.courses.interfaces.rest.transform.CreateCourseCommandFromResourceAssembler;
import apx.inc.design_web_services_backend.courses.interfaces.rest.transform.UpdateCourseCommandFromResourceAssembler;
import apx.inc.design_web_services_backend.iam.infrastructure.authorization.sfs.model.UserDetailsImpl;
import apx.inc.design_web_services_backend.shared.interfaces.rest.resources.CourseResource;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping( value = "/api/v1/courses")
@Tag(name = "Courses", description = "Operations related to courses")
public class CoursesController {
    private final CourseCommandService courseCommandService;
    private final CourseQueryService courseQueryService;

    public CoursesController(CourseCommandService courseCommandService, CourseQueryService courseQueryService) {
        this.courseCommandService = courseCommandService;
        this.courseQueryService = courseQueryService;
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


    @PostMapping
    @Operation(summary = "Create a Course", description = "Creates a course with the specified parameters")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "201", description = "Course Created Successfully"),
                    @ApiResponse(responseCode = "404", description = "Invalid input data")
            }
    )
    public ResponseEntity<CourseResource> createGroup(@RequestBody CreateCourseResource resource) {
        Long userId = getAuthenticatedUserId();

        // 1️⃣ Convertir el recurso a comando
        var createCommand = CreateCourseCommandFromResourceAssembler.toCommandFromResource(resource,userId);

        //  2️⃣ Ejecutar el servicio
        var createdId = courseCommandService.handle(createCommand);

        // 3️⃣ Validar la creación
        if (createdId == null || createdId <= 0L) {
            return ResponseEntity.badRequest().build();
        }

        // 4️⃣  Recuperar el course creado
        var getCourseByIdQuery = new GetCourseByIdQuery(createdId);
        var course = courseQueryService.handle(getCourseByIdQuery,userId);

        if (course.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        // 5️⃣ Devolver la respuesta
        var courseEntity = course.get();
        var courseResponse = CourseResourceFromEntityAssembler.toResourceFromEntity(courseEntity);
        return new ResponseEntity<>(courseResponse, HttpStatus.CREATED);
    }

    @PutMapping(value = "/{id}")
    @Operation(summary = "Update a course", description = "Update the course with the specified id")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Course updated successfully"),
                    @ApiResponse(responseCode = "404", description = "Course with specified id does not exist")
            }
    )
    public ResponseEntity<CourseResource> updateCourse(@RequestBody UpdateCourseResource resource, @PathVariable("id") Long id) {

        Long userId = getAuthenticatedUserId();

        // 1️⃣ Convertir el recurso a comando

        var updateCommand = UpdateCourseCommandFromResourceAssembler.toCommandFromResource(resource, id);

        //  2️⃣ Ejecutar el servicio

        var updatedCourse = courseCommandService.handle(updateCommand);

        // 3️⃣ Validar la creación
        if (updatedCourse.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        // 4️⃣  Recuperar el course creado
        var getCourseByIdQuery = new GetCourseByIdQuery(id);
        var course = courseQueryService.handle(getCourseByIdQuery,userId);
        if (course.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        var courseEntity = course.get();
        var courseResponse = CourseResourceFromEntityAssembler.toResourceFromEntity(courseEntity);
        return ResponseEntity.ok(courseResponse);
    }

    @DeleteMapping(value = "/{id}")
    @Operation(summary = "Delete course", description = "Delete the course with the specified id")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "204", description = "Course deleted successfully"),
                    @ApiResponse(responseCode = "404", description = "Course with the specified id does not exist")
            }
    )
    public ResponseEntity<Void> deleteCourse(@PathVariable("id") Long id) {
        // 1️⃣ Convertir el recurso a comando
        var deleteCourseCommand = new DeleteCourseCommand(id);
        //  2️⃣ Ejecutar el servicio
        courseCommandService.handle(deleteCourseCommand);

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/join/{key}")
    @Operation(summary = "Join a course via join code")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Joined course successfully"),
            @ApiResponse(responseCode = "404", description = "Course not found")
    })
    public ResponseEntity<CourseResource> joinCourse(
            @PathVariable String key
    ) {

        Long userId = getAuthenticatedUserId();

        var joinByJoinCodeCommand = new JoinByJoinCodeCommand(userId, key);
        var groupOptional = courseCommandService.handle(joinByJoinCodeCommand);

        if (groupOptional.isPresent()) {
            Course course = groupOptional.get();
            CourseResource resource = CourseResourceFromEntityAssembler.toResourceFromEntity(course);
            return ResponseEntity.ok(resource);
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{courseId}/students/{studentId}")
    public ResponseEntity<?> kickStudentFromCourse(
            @PathVariable Long courseId,
            @PathVariable Long studentId) {

        Long teacherId = getAuthenticatedUserId(); // 👈 Id del profe logueado desde el JWT

        KickStudentCommand command = new KickStudentCommand(studentId, courseId);

        courseCommandService.handle(command, teacherId);

        return ResponseEntity.noContent().build(); // 204 No Content ✅
    }

    @GetMapping
    @Operation(summary = "Get all courses", description = "Gets all courses")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Courses retrieved Successfully"),
                    @ApiResponse(responseCode = "404", description = "Could not retrieve courses")
            }
    )
    public ResponseEntity<List<CourseResource>> getAllCourses() {
        var getAllCoursesQuery = new GetAllCoursesQuery();
        var courses = courseQueryService.handle(getAllCoursesQuery);

        if (courses.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        var courseResponse = courses.stream().map(CourseResourceFromEntityAssembler::toResourceFromEntity).toList();
        return ResponseEntity.ok(courseResponse);
    }

    @GetMapping(value = "/{id}")
    @Operation(summary = "Get course by id", description = "Retrieves a course with the specified id")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Course retrieved successfully"),
                    @ApiResponse(responseCode = "404", description = "Course with specified id does not exist")
            }
    )
    public ResponseEntity<CourseResource> getCourseById(@PathVariable("id") Long id) {
        Long userId = getAuthenticatedUserId();

        var getCourseByIdQuery = new GetCourseByIdQuery(id);
        var course = courseQueryService.handle(getCourseByIdQuery,userId);
        if (course.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        var courseEntity = course.get();
        var courseResponse = CourseResourceFromEntityAssembler.toResourceFromEntity(courseEntity);
        return ResponseEntity.ok(courseResponse);
    }

    @GetMapping("/user/{userId}")
    @Operation(summary = "Get groups by user ID", description = "Retrieves all courses that a user belongs to")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Courses retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "User not found or no courses found for user")
    })
    public ResponseEntity<List<CourseResource>> getCoursesByUserId(@PathVariable Long userId) {
        // Create the query to get courses by user ID
        var getCoursesByUserIdQuery = new GetCoursesByUserIdQuery(userId);

        // Execute the query using the courseQueryService
        var courses = courseQueryService.handle(getCoursesByUserIdQuery);

        // Check if courses are found
        if (courses.isEmpty()) {
            return ResponseEntity.ok(List.of());
        }

        // Convert the list of courses to a list of GroupResource
        var groupResponse = courses.stream()
                .map(CourseResourceFromEntityAssembler::toResourceFromEntity)
                .toList();
        return ResponseEntity.ok(groupResponse);
    }

}
