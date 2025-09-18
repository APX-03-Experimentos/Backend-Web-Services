package apx.inc.design_web_services_backend.assigments.infrastructure.persistence.jpa.repositories;

import apx.inc.design_web_services_backend.assigments.domain.model.aggregates.Submission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubmissionRepository extends JpaRepository<Submission, Long> {

    // Additional query methods can be defined here if needed
    List<Submission> findByAssignmentId(Long assignmentId);

    List<Submission> findByStudentId(Long studentId);

    List<Submission> findByStudentIdAndAssignmentId(Long studentId, Long assignmentId);

}
