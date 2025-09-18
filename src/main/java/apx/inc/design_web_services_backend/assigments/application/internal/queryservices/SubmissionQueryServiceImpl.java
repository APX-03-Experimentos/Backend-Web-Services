package apx.inc.design_web_services_backend.assigments.application.internal.queryservices;

import apx.inc.design_web_services_backend.assigments.domain.model.aggregates.Submission;
import apx.inc.design_web_services_backend.assigments.domain.model.queries.*;
import apx.inc.design_web_services_backend.assigments.domain.services.SubmissionQueryService;
import apx.inc.design_web_services_backend.assigments.infrastructure.persistence.jpa.repositories.AssignmentRepository;
import apx.inc.design_web_services_backend.assigments.infrastructure.persistence.jpa.repositories.SubmissionRepository;
import apx.inc.design_web_services_backend.iam.infrastructure.persistence.jpa.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SubmissionQueryServiceImpl implements SubmissionQueryService {

    private final SubmissionRepository submissionRepository;
    private final AssignmentRepository assignmentRepository;
    private final UserRepository userRepository;


    public SubmissionQueryServiceImpl(SubmissionRepository submissionRepository, AssignmentRepository assignmentRepository, UserRepository userRepository) {
        this.submissionRepository = submissionRepository;
        this.assignmentRepository = assignmentRepository;
        this.userRepository = userRepository;
    }



    @Override
    public Optional<Submission> handle(GetSubmissionByIdQuery query, Long userId) {
        // 1. Validar Submission
        Optional<Submission> submissionOptional = submissionRepository.findById(query.submissionId());
        if (submissionOptional.isEmpty()) {
            throw new IllegalArgumentException("Submission with ID " + query.submissionId() + " not found");
        }

        // 2. Validar Usuario
        var userOptional = userRepository.findById(userId);
        if (userOptional.isEmpty()) {
            throw new IllegalArgumentException("User with ID " + userId + " not found");
        }

        // 3. Validar Assignment del Submission
        Long submissionAssignmentId = submissionOptional.get().getAssignmentId();
        var assignmentOptional = assignmentRepository.findById(submissionAssignmentId);
        if (assignmentOptional.isEmpty()) {
            throw new IllegalArgumentException("Challenge with ID " + submissionAssignmentId + " not found");
        }

        Long assignmentCourseId = assignmentOptional.get().getCourseId();

        // 4. Validar que el usuario pertenezca al grupo del Challenge
        boolean belongsToGroup = userOptional.get().getStudentInCourses().stream()
                .anyMatch(c -> c.getId().equals(assignmentCourseId));

        if (!belongsToGroup) {
            throw new IllegalArgumentException("User does not belong to the course associated with this Submission's Assignment");
        }

        return submissionOptional;
    }

    @Override
    public List<Submission> handle(GetAllSubmissionsQuery getAllSubmissionsQuery) {
        return submissionRepository.findAll();
    }

    @Override
    public List<Submission> handle(GetSubmissionsByAssignmentIdQuery getSubmissionsByChallengeIdQuery) {
        //es de esta manera porque AssignmentId es un ValueObject
        return submissionRepository.findByAssignmentId(getSubmissionsByChallengeIdQuery.assignmentId());
    }

    @Override
    public List<Submission> handle(GetSubmissionsByStudentIdQuery getSubmissionsByStudentIdQuery) {
        return submissionRepository.findByStudentId(getSubmissionsByStudentIdQuery.studentId());
    }

    @Override
    public List<Submission> handle(GetSubmissionsByStudentIdAndAssignmentIdQuery getSubmissionsByStudentIdAndAssignmentIdQuery) {
        return submissionRepository.findByStudentIdAndAssignmentId(getSubmissionsByStudentIdAndAssignmentIdQuery.studentId(),getSubmissionsByStudentIdAndAssignmentIdQuery.assignmentId());
    }

    @Override
    public List<Submission> handle(GetSubmissionsByStudentIdAndCourseIdQuery query) {
        Long studentId = query.studentId();
        List<Submission> allSubmissions = submissionRepository.findByStudentId(studentId);

        return allSubmissions.stream()
                .filter(submission -> {
                    Long assignmentId = submission.getAssignmentId();
                    return assignmentRepository.findById(assignmentId)
                            .map(assignment -> assignment.getCourseId().equals(query.courseId()))
                            .orElse(false);
                })
                .toList();
    }

    @Override
    public List<Submission> handle(GetSubmissionsByCourseIdQuery query) {
        List<Submission> allSubmissions = submissionRepository.findAll();

        return allSubmissions.stream()
                .filter(submission -> {
                    Long assignmentId = submission.getAssignmentId();
                    return assignmentRepository.findById(assignmentId)
                            .map(challenge -> challenge.getCourseId().equals(query.courseId()))
                            .orElse(false);
                })
                .toList();
    }
}
