package apx.inc.design_web_services_backend.assigments.application.internal.queryservices;

import apx.inc.design_web_services_backend.assigments.domain.model.aggregates.Assignment;
import apx.inc.design_web_services_backend.assigments.domain.model.queries.GetAllAssignmentsQuery;
import apx.inc.design_web_services_backend.assigments.domain.model.queries.GetAssignmentByIdQuery;
import apx.inc.design_web_services_backend.assigments.domain.model.queries.GetAssignmentsByGroupIdQuery;
import apx.inc.design_web_services_backend.assigments.domain.services.AssignmentQueryService;
import apx.inc.design_web_services_backend.assigments.infrastructure.persistence.jpa.repositories.AssignmentRepository;
import apx.inc.design_web_services_backend.courses.infrastructure.persistence.jpa.repositories.CourseRepository;
import apx.inc.design_web_services_backend.iam.domain.model.valueobjects.Roles;
import apx.inc.design_web_services_backend.iam.infrastructure.persistence.jpa.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AssignmentQueryServiceImpl implements AssignmentQueryService {

    final private AssignmentRepository assignmentRepository;
    final private UserRepository userRepository;
    final private CourseRepository courseRepository;

    public AssignmentQueryServiceImpl(AssignmentRepository assignmentRepository, UserRepository userRepository,CourseRepository courseRepository) {
        this.assignmentRepository = assignmentRepository;
        this.userRepository = userRepository;
        this.courseRepository = courseRepository;
    }


    @Override
    public Optional<Assignment> handle(GetAssignmentByIdQuery query, Long userId) {
        // 1. Buscar el Assignment
        Optional<Assignment> assignmentOpt = assignmentRepository.findById(query.assignmentId());
        if (assignmentOpt.isEmpty()) {
            return Optional.empty();
        }

        // 2. Buscar el Usuario
        var userOpt = userRepository.findById(userId);
        if (userOpt.isEmpty()) {
            throw new IllegalArgumentException("User with ID " + userId + " not found");
        }
        var user = userOpt.get();

        // 3. Obtener el CourseId del Assignment
        Long assignmentCourseId = assignmentOpt.get().getCourseId();

        // 4. Validar pertenencia del usuario al grupo o como teacher
        boolean hasAccess;

        if (user.getUserRoles().stream().anyMatch(r -> r.getName() == Roles.ROLE_TEACHER)) {
            // Usuario es teacher → verificar que sea teacher del curso
            hasAccess = courseRepository.findByTeacherId(userId)
                    .stream()
                    .anyMatch(c -> c.getId().equals(assignmentCourseId));
        } else {
            // Usuario es estudiante → verificar que esté inscrito en el curso
            hasAccess = user.getStudentInCourses()
                    .stream()
                    .anyMatch(c -> c.getId().equals(assignmentCourseId));
        }

        if (!hasAccess) {
            throw new IllegalArgumentException("User with ID " + userId + " does not belong to the group of this challenge");
        }

        return assignmentOpt;
    }

    @Override
    public List<Assignment> handle(GetAllAssignmentsQuery getAllAssignmentsQuery) {
        return assignmentRepository.findAll();
    }

    @Override
    public List<Assignment> handle(GetAssignmentsByGroupIdQuery query, Long userId) {
        Long courseId = query.courseId();

        // 1. Verificar que el usuario existe
        var userOpt = userRepository.findById(userId);
        if (userOpt.isEmpty()) {
            throw new IllegalArgumentException("User with ID " + userId + " not found");
        }

        // 2. Verificar que pertenece al grupo
        boolean belongsToGroup = userOpt.get().getStudentInCourses().stream()
                .anyMatch(c -> c.getId().equals(courseId));

        if (!belongsToGroup) {
            // Usuario no tiene acceso a Challenges de este grupo
            return List.of();
        }

        // 3. Si pertenece, devolver los Challenges
        return assignmentRepository.findByCourseId(courseId);
    }
}
