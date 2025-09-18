package apx.inc.design_web_services_backend.assigments.application.internal.commandservices;

import apx.inc.design_web_services_backend.assigments.domain.model.aggregates.Submission;
import apx.inc.design_web_services_backend.assigments.domain.model.commands.CreateSubmissionCommand;
import apx.inc.design_web_services_backend.assigments.domain.model.commands.DeleteSubmissionCommand;
import apx.inc.design_web_services_backend.assigments.domain.model.commands.GradeSubmissionCommand;
import apx.inc.design_web_services_backend.assigments.domain.model.commands.UpdateSubmissionCommand;
import apx.inc.design_web_services_backend.assigments.domain.model.valueobjects.States;
import apx.inc.design_web_services_backend.assigments.domain.services.SubmissionCommandService;
import apx.inc.design_web_services_backend.assigments.infrastructure.persistence.jpa.repositories.AssignmentRepository;
import apx.inc.design_web_services_backend.assigments.infrastructure.persistence.jpa.repositories.SubmissionRepository;
import apx.inc.design_web_services_backend.iam.domain.model.valueobjects.Roles;
import apx.inc.design_web_services_backend.iam.infrastructure.persistence.jpa.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class SubmissionCommandServiceImpl implements SubmissionCommandService {

    private final SubmissionRepository submissionRepository;
    private final AssignmentRepository assignmentRepository;
    private final UserRepository userRepository;

    public SubmissionCommandServiceImpl(SubmissionRepository submissionRepository, AssignmentRepository assignmentRepository, UserRepository userRepository) {
        this.submissionRepository = submissionRepository;
        this.assignmentRepository = assignmentRepository;
        this.userRepository= userRepository;
    }

    @Override
    public Long handle(CreateSubmissionCommand createSubmissionCommand) {

        // 1. Verificar que el Assignment existe
        var assignmentOptional = assignmentRepository.findById(createSubmissionCommand.assignmentId());
        if (assignmentOptional.isEmpty()) {
            throw new IllegalArgumentException("Challenge with ID " + createSubmissionCommand.assignmentId() + " not found");
        }
        var assignment = assignmentOptional.get();

        // 2. Verificar que el usuario existe y sea estudiante
        var optionalUser = userRepository.findById(createSubmissionCommand.studentId());
        if (optionalUser.isEmpty()) {
            throw new IllegalArgumentException("Student no encontrado");
        }
        var user = optionalUser.get();
        if (user.getUserRoles().stream().noneMatch(role -> role.getName().equals(Roles.ROLE_STUDENT))) {
            throw new IllegalStateException("Solo un usuario con rol STUDENT puede crear un submission");
        }

        // 3. Verificar que el estudiante pertenece al course del assignment
        Long assignmentCourseId = assignment.getCourseId();
        boolean belongsToCourse = user.getStudentInCourses().stream()
                .anyMatch(c -> c.getId().equals(assignmentCourseId));

        if (!belongsToCourse) {
            throw new IllegalArgumentException("Student does not belong to the course of this assignment");
        }

        // 4. Crear la Submission
        var submission= new Submission(createSubmissionCommand);
        try {
            submissionRepository.save(submission);
            return submission.getId();

        } catch (Exception e) {
            // Aquí podrías manejar excepciones específicas o registrar el error
            throw new RuntimeException("Error creating submission: " + e.getMessage(), e);
        }
    }

    @Override
    public Optional<Submission> handle(UpdateSubmissionCommand updateSubmissionCommand) {
        // 1. Validar que el assignment exista
        var assignmentOptional = assignmentRepository.findById(updateSubmissionCommand.assignmentId());
        if (assignmentOptional.isEmpty()) {
            throw new IllegalArgumentException("Assignment with ID " + updateSubmissionCommand.assignmentId() + " not found");
        }
        var assignment = assignmentOptional.get();

        // 2. Validar que el estudiante exista y sea de rol STUDENT
        var userOptional = userRepository.findById(updateSubmissionCommand.studentId());
        if (userOptional.isEmpty()) {
            throw new IllegalArgumentException("Student with ID " + updateSubmissionCommand.studentId() + " not found");
        }

        var student = userOptional.get();
        if (student.getUserRoles().stream().anyMatch(role -> role.getName().equals(Roles.ROLE_TEACHER))) {
            throw new IllegalArgumentException("Only teachers can update submissions");
        }

        // 3. Validar que el estudiante pertenece al grupo del assignment
        Long assignmentCourseId = assignment.getCourseId(); // es value object
        boolean belongsToGroup = student.getStudentInCourses().stream()
                .anyMatch(c -> c.getId().equals(assignmentCourseId));

        if (!belongsToGroup) {
            throw new IllegalArgumentException("Student does not belong to the course of this assignment");
        }

        // 4. Verificar que el submission exista
        var submissionOptional = submissionRepository.findById(updateSubmissionCommand.submissionId());
        if (submissionOptional.isEmpty()) {
            throw new IllegalArgumentException("Submission with ID " + updateSubmissionCommand.submissionId() + " not found");
        }

        var submissionToUpdate = submissionOptional.get();

        // 5. Actualizar y guardar
        try {
            var updatedSubmission = submissionRepository.save(submissionToUpdate
                    .updateSubmission(
                            updateSubmissionCommand.assignmentId(),
                            updateSubmissionCommand.studentId(),
                            updateSubmissionCommand.content(),
                            updateSubmissionCommand.score(),
                            updateSubmissionCommand.imageUrl()
                    ));
            return Optional.of(updatedSubmission);
        } catch (Exception e) {
            throw new RuntimeException("Error updating submission: " + e.getMessage(), e);
        }
    }

    @Override
    public void handle(DeleteSubmissionCommand deleteSubmissionCommand) {
        if (!submissionRepository.existsById(deleteSubmissionCommand.submissionId())) {
            throw new IllegalArgumentException("Submission with ID " + deleteSubmissionCommand.submissionId() + " not found");
        }
        try{
            submissionRepository.deleteById(deleteSubmissionCommand.submissionId());
        }catch (Exception e) {
            throw new RuntimeException("Error deleting submission: " + e.getMessage(), e);
        }
    }

    @Override
    public Optional<Submission> handle(GradeSubmissionCommand command) {
        var submissionOptional = submissionRepository.findById(command.submissionId());
        if (submissionOptional.isEmpty()) {
            throw new IllegalArgumentException("Submission not found");
        }

        var submission = submissionOptional.get();

        // Aquí podrías validar reglas de negocio:
        // Por ejemplo: verificar que quien califica sea teacher (ya lo hace @PreAuthorize)
        submission.gradeSubmission(command.score());
        submission.changeState(States.GRADED);

        try {
            submissionRepository.save(submission);
            return Optional.of(submission);
        } catch (Exception e) {
            throw new RuntimeException("Error grading submission: " + e.getMessage(), e);
        }
    }


}
