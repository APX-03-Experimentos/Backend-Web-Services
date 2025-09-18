package apx.inc.design_web_services_backend.assigments.application.internal.commandservices;


import apx.inc.design_web_services_backend.assigments.domain.model.aggregates.Assignment;
import apx.inc.design_web_services_backend.assigments.domain.model.commands.CreateAssignmentCommand;
import apx.inc.design_web_services_backend.assigments.domain.model.commands.DeleteAssignmentCommand;
import apx.inc.design_web_services_backend.assigments.domain.model.commands.UpdateAssignmentCommand;
import apx.inc.design_web_services_backend.assigments.domain.services.AssignmentCommandService;
import apx.inc.design_web_services_backend.assigments.infrastructure.persistence.jpa.repositories.AssignmentRepository;
import apx.inc.design_web_services_backend.courses.infrastructure.persistence.jpa.repositories.CourseRepository;
import apx.inc.design_web_services_backend.iam.domain.model.valueobjects.Roles;
import apx.inc.design_web_services_backend.iam.infrastructure.persistence.jpa.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AssignmentCommandServiceImpl implements AssignmentCommandService {

    private final AssignmentRepository assignmentRepository;
    private final CourseRepository courseRepository;
    private final UserRepository userRepository;

    public AssignmentCommandServiceImpl(AssignmentRepository assignmentRepository, CourseRepository courseRepository, UserRepository userRepository) {
        this.assignmentRepository = assignmentRepository;
        this.courseRepository = courseRepository;
        this.userRepository=userRepository;
    }

    @Override
    public Long handle(CreateAssignmentCommand createAssignmentCommand, Long userId) {

        // Validación: curso debe existir
        if (!courseRepository.existsById(createAssignmentCommand.courseId())){
            throw new IllegalArgumentException("Course not found: " + createAssignmentCommand.courseId());
        }

        // Validar usuario existe
        var userOptional = userRepository.findById(userId);
        if (userOptional.isEmpty()) {
            throw new IllegalArgumentException("User not found: " + userId);
        }
        var user = userOptional.get();

        boolean isMember;

// Si es teacher, verificar que sea teacher del curso
        if (user.getUserRoles().stream().anyMatch(r -> Roles.ROLE_TEACHER.equals(r.getName()))) {
            // Es teacher → verificar que sea profesor de ese curso
            isMember = courseRepository.findByTeacherId(userId)
                    .stream()
                    .anyMatch(c -> c.getId().equals(createAssignmentCommand.courseId()));
        } else {
            // Si es estudiante, verificar que esté inscrito en el curso
            isMember = user.getStudentInCourses().stream()
                    .anyMatch(c -> c.getId().equals(createAssignmentCommand.courseId()));
        }

        if (!isMember) {
            throw new IllegalStateException("User does not belong to the course");
        }

        // Validar título único en el curso
        if (assignmentRepository.existsByTitleAndCourseId(createAssignmentCommand.title(), createAssignmentCommand.courseId())) {
            throw new IllegalArgumentException("An assignment with this title already exists in the course");
        }

        var assignment = new Assignment(createAssignmentCommand);
        try {
            assignmentRepository.save(assignment);
            return assignment.getId();
        } catch (Exception e) {
            throw new IllegalArgumentException("Failed to create assignment: " + e.getMessage(), e);
        }
    }

    @Override
    public Optional<Assignment> handle(UpdateAssignmentCommand updateAssignmentCommand, Long userId) {
        var challenge = assignmentRepository.findById(updateAssignmentCommand.assigmentId());
        if (challenge.isEmpty()) {
            throw new IllegalArgumentException("assigment not found: " + updateAssignmentCommand.assigmentId());
        }

        // Validación cruzada entre BCs
        if (!courseRepository.existsById(updateAssignmentCommand.courseId())) {
            throw new IllegalArgumentException("Group not found: " + updateAssignmentCommand.courseId());
        }

        // Validar usuario pertenece al grupo
        var userOptional = userRepository.findById(userId);
        if (userOptional.isEmpty()) {
            throw new IllegalArgumentException("User not found: " + userId);
        }
        var user = userOptional.get();

        boolean isMember ;

        if (user.getUserRoles().stream().anyMatch(r -> Roles.ROLE_TEACHER.equals(r.getName()))) {
            // Es teacher → verificar que sea profesor de ese curso
            isMember = courseRepository.findByTeacherId(userId)
                    .stream()
                    .anyMatch(c -> c.getId().equals(updateAssignmentCommand.courseId()));
        } else {
            // Si es estudiante, verificar que esté inscrito en el curso
            isMember = user.getStudentInCourses().stream()
                    .anyMatch(c -> c.getId().equals(updateAssignmentCommand.courseId()));
        }

        if (!isMember) {
            throw new IllegalStateException("User does not belong to the course");
        }


        // Validar que no exista otro challenge en el mismo grupo con el mismo título
        var title = updateAssignmentCommand.title();
        var courseId = updateAssignmentCommand.courseId();
        var existingChallengeWithSameTitle = assignmentRepository.findByTitleAndCourseId(title, courseId);

        if (existingChallengeWithSameTitle.isPresent() &&
                !existingChallengeWithSameTitle.get().getId().equals(updateAssignmentCommand.assigmentId())) {
            throw new IllegalArgumentException("A challenge with this title already exists in the group");
        }

        var assignmentToUpdate = challenge.get();
        try{
            var updatedAssignment= assignmentRepository.save(assignmentToUpdate
                    .updateInformation(
                            updateAssignmentCommand.title(),
                            updateAssignmentCommand.description(),
                            updateAssignmentCommand.courseId(),
                            updateAssignmentCommand.deadline(),
                            updateAssignmentCommand.imageUrl()
                    ));
            return Optional.of(updatedAssignment);
        }catch (Exception e){
            throw new IllegalArgumentException("Failed to update assignment: " + e.getMessage(), e);
        }

    }

    @Override
    public void handle(DeleteAssignmentCommand deleteAssignmentCommand) {
        if (!assignmentRepository.existsById(deleteAssignmentCommand.assigmentId())) {
            throw new IllegalArgumentException("Challenge not found: " + deleteAssignmentCommand.assigmentId());
        }

        try{
            assignmentRepository.deleteById(deleteAssignmentCommand.assigmentId());
        } catch (Exception e) {
            throw new IllegalArgumentException("Failed to delete challenge: " + e.getMessage(), e);
        }

    }
}
